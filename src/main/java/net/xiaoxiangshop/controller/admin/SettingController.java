package net.xiaoxiangshop.controller.admin;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.AuthenticationFailedException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.sun.mail.smtp.SMTPSenderFailedException;

import net.xiaoxiangshop.FileType;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.audit.Audit;
import net.xiaoxiangshop.service.CacheService;
import net.xiaoxiangshop.service.FileService;
import net.xiaoxiangshop.service.MailService;
import net.xiaoxiangshop.service.SmsService;
import net.xiaoxiangshop.util.SpringUtils;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * Controller - 系统设置
 * 
 */
@Controller("adminstingController")
@RequestMapping("/admin/setting")
public class SettingController extends BaseController {

	@Inject
	private FileService fileService;
	@Inject
	private MailService mailService;
	@Inject
	private SmsService smsService;
	@Inject
	private CacheService cacheService;

	/**
	 * SMTP测试
	 */
	@PostMapping("/test_smtp")
	public ResponseEntity<?> testSmtp(String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, Boolean smtpSSLEnabled, String smtpFromMail, String toMail) {
		if (StringUtils.isEmpty(toMail)) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		Setting setting = SystemUtils.getSetting();
		try {
			Map<String, Object> properties = new HashMap<>();
			properties.put("smtpHost", smtpHost);
			properties.put("smtpPort", smtpPort);
			properties.put("smtpUsername", smtpUsername);
			properties.put("smtpSSLEnabled", smtpSSLEnabled);
			properties.put("smtpFromMail", smtpFromMail);
			if (!isValid(Setting.class, properties)) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			mailService.sendTestSmtpMail(smtpHost, smtpPort, smtpUsername, StringUtils.isNotEmpty(smtpPassword) ? smtpPassword : setting.getSmtpPassword(), smtpSSLEnabled, smtpFromMail, toMail);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			if (rootCause != null) {
				if (rootCause instanceof UnknownHostException) {
					return Results.unprocessableEntity("admin.setting.testSmtpUnknownHost");
				} else if (rootCause instanceof ConnectException || rootCause instanceof SocketTimeoutException) {
					return Results.unprocessableEntity("admin.setting.testSmtpConnectFailed");
				} else if (rootCause instanceof AuthenticationFailedException) {
					return Results.unprocessableEntity("admin.setting.testSmtpAuthenticationFailed");
				} else if (rootCause instanceof SMTPSenderFailedException) {
					return Results.unprocessableEntity("admin.setting.testSmtpSenderFailed");
				}
			}
			return Results.unprocessableEntity("admin.setting.testSmtpFailed");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("mesaage", SpringUtils.getMessage("admin.setting.testSmtpSuccess"));
		return ResponseEntity.ok(map);
	}

	/**
	 * 短信余额查询
	 */
	@GetMapping("/sms_balance")
	public ResponseEntity<?> smsBalance() {
		Setting setting = SystemUtils.getSetting();
		String smsAppId = setting.getSmsAppId();
		String smsSecretKey = setting.getSmsSecretKey();

		if (StringUtils.isEmpty(smsAppId) || StringUtils.isEmpty(smsSecretKey)) {
			return Results.unprocessableEntity("admin.setting.smsInvalid");
		}
		try {
			long balance = smsService.getBalance();
			Map<String, Object> map = new HashMap<>();
			map.put("mesaage", SpringUtils.getMessage("admin.setting.smsBalanceResult", balance));
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			if (rootCause != null) {
				if (rootCause instanceof ConnectException) {
					return Results.unprocessableEntity("admin.setting.smsResponseFailed");
				} else if (rootCause instanceof SocketException) {
					return Results.unprocessableEntity("admin.setting.smsConnectFailed");
				} else if (rootCause instanceof GeneralSecurityException) {
					return Results.unprocessableEntity("admin.setting.smsSecretKeyInvalid");
				}
			}
			return Results.unprocessableEntity("admin.setting.smsInvalid");
		}

	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(ModelMap model) {
		model.addAttribute("setting", SystemUtils.getSetting());
		model.addAttribute("locales", Setting.Locale.values());
		model.addAttribute("watermarkPositions", Setting.WatermarkPosition.values());
		model.addAttribute("roundTypes", Setting.RoundType.values());
		model.addAttribute("registerTypes", Setting.RegisterType.values());
		model.addAttribute("captchaTypes", Setting.CaptchaType.values());
		model.addAttribute("stockAllocationTimes", Setting.StockAllocationTime.values());
		return "admin/setting/edit";
	}

	/**
	 * 更新
	 */
	@Audit(action = "auditLog.action.admin.setting.update")
	@PostMapping("/update")
	public ResponseEntity<?> update(Setting setting, MultipartFile watermarkImageFile) {
		if (!isValid(setting)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (setting.getDefaultPointScale() > setting.getMaxPointScale()) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (ArrayUtils.isNotEmpty(setting.getDistributionCommissionRates())) {
			double distributionCommissionRateSum = 0;
			for (Double distributionCommissionRate : setting.getDistributionCommissionRates()) {
				if (distributionCommissionRate == null || distributionCommissionRate < 0) {
					return Results.UNPROCESSABLE_ENTITY;
				}
				distributionCommissionRateSum += distributionCommissionRate;
			}
			if (distributionCommissionRateSum > 100) {
				return Results.UNPROCESSABLE_ENTITY;
			}
		}
		Setting srcSetting = SystemUtils.getSetting();
		if (watermarkImageFile != null && !watermarkImageFile.isEmpty()) {
			if (!fileService.isValid(FileType.IMAGE, watermarkImageFile)) {
				return Results.unprocessableEntity("common.upload.invalid");
			}
			String watermarkImage = fileService.uploadLocal(FileType.IMAGE, watermarkImageFile);
			setting.setWatermarkImage(watermarkImage);
		}
		if (StringUtils.isEmpty(setting.getSmtpPassword())) {
			setting.setSmtpPassword(srcSetting.getSmtpPassword());
		}
		if (StringUtils.isEmpty(setting.getSmsAppId()) || StringUtils.isEmpty(setting.getSmsSecretKey())) {
			setting.setSmsAppId(null);
			setting.setSmsSecretKey(null);
		}
		setting.setIsCnzzEnabled(srcSetting.getIsCnzzEnabled());
		setting.setCnzzSiteId(srcSetting.getCnzzSiteId());
		setting.setCnzzPassword(srcSetting.getCnzzPassword());

		SystemUtils.setSetting(setting);
		cacheService.clear();

		return Results.OK;
	}

}