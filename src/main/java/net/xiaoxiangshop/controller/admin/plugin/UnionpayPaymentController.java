package net.xiaoxiangshop.controller.admin.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.controller.admin.BaseController;
import net.xiaoxiangshop.entity.PluginConfig;
import net.xiaoxiangshop.plugin.PaymentPlugin;
import net.xiaoxiangshop.plugin.UnionpayPaymentPlugin;
import net.xiaoxiangshop.service.PluginConfigService;
import net.xiaoxiangshop.util.SecurityUtils;

/**
 * Controller - 银联在线支付
 * 
 */
@Controller("adminPluginUnionpayPaymentController")
@RequestMapping("/admin/plugin/unionpay_payment")
public class UnionpayPaymentController extends BaseController {

	@Inject
	private UnionpayPaymentPlugin unionpayPaymentPlugin;
	@Inject
	private PluginConfigService pluginConfigService;

	/**
	 * 安装
	 */
	@PostMapping("/install")
	public ResponseEntity<?> install() {
		if (!unionpayPaymentPlugin.getIsInstalled()) {
			PluginConfig pluginConfig = new PluginConfig();
			pluginConfig.setPluginId(unionpayPaymentPlugin.getId());
			pluginConfig.setIsEnabled(false);
			pluginConfig.setAttributes(null);
			pluginConfigService.save(pluginConfig);
		}
		return Results.OK;
	}

	/**
	 * 卸载
	 */
	@PostMapping("/uninstall")
	public ResponseEntity<?> uninstall() {
		if (unionpayPaymentPlugin.getIsInstalled()) {
			pluginConfigService.deleteByPluginId(unionpayPaymentPlugin.getId());
		}
		return Results.OK;
	}

	/**
	 * 设置
	 */
	@GetMapping("/setting")
	public String setting(ModelMap model) {
		PluginConfig pluginConfig = unionpayPaymentPlugin.getPluginConfig();
		model.addAttribute("feeTypes", PaymentPlugin.FeeType.values());
		model.addAttribute("pluginConfig", pluginConfig);
		return "/admin/plugin/unionpay_payment/setting";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(String displayName, String merchantId, MultipartFile keyFile, String keyPassword, PaymentPlugin.FeeType feeType, BigDecimal fee, String logo, String description, @RequestParam(defaultValue = "false") Boolean isEnabled, Integer order) {
		PluginConfig pluginConfig = unionpayPaymentPlugin.getPluginConfig();
		Map<String, String> attributes = new HashMap<>();
		attributes.put(PaymentPlugin.DISPLAY_NAME_ATTRIBUTE_NAME, displayName);
		attributes.put("merchantId", merchantId);
		if (keyFile != null && !keyFile.isEmpty()) {
			try (InputStream inputStream = keyFile.getInputStream()){
				KeyStore keyStore = SecurityUtils.getKeyStore("PKCS12", inputStream, keyPassword);
				String alias = keyStore.aliases().hasMoreElements() ? keyStore.aliases().nextElement() : null;
				X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
				attributes.put("certId", String.valueOf(x509Certificate.getSerialNumber()));
				attributes.put("key", SecurityUtils.getKeyString(keyStore.getKey(alias, keyPassword != null ? keyPassword.toCharArray() : null)));
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (UnrecoverableKeyException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (KeyStoreException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (RuntimeException e) {
				return Results.unprocessableEntity("admin.plugin.unionpayPayment.keyInvalid");
			}
		} else {
			attributes.put("key", pluginConfig.getAttribute("key"));
			attributes.put("certId", pluginConfig.getAttribute("certId"));
		}
		attributes.put(PaymentPlugin.FEE_TYPE_ATTRIBUTE_NAME, String.valueOf(feeType));
		attributes.put(PaymentPlugin.FEE_ATTRIBUTE_NAME, String.valueOf(fee));
		attributes.put(PaymentPlugin.LOGO_ATTRIBUTE_NAME, logo);
		attributes.put(PaymentPlugin.DESCRIPTION_ATTRIBUTE_NAME, description);
		pluginConfig.setAttributes(attributes);
		pluginConfig.setIsEnabled(isEnabled);
		pluginConfig.setOrder(order);
		pluginConfigService.update(pluginConfig);
		return Results.OK;
	}

}