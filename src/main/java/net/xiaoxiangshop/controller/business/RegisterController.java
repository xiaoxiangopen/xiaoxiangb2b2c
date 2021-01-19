package net.xiaoxiangshop.controller.business;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.xiaoxiangshop.util.XmlUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.entity.BaseEntity;
import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.BusinessAttribute;
import net.xiaoxiangshop.security.UserAuthenticationToken;
import net.xiaoxiangshop.service.BusinessAttributeService;
import net.xiaoxiangshop.service.BusinessService;
import net.xiaoxiangshop.service.UserService;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * Controller - 商家注册
 * 
 */
@Controller("businessRegisterController")
@RequestMapping("/business/register")
public class RegisterController extends BaseController {

	@Inject
	private UserService userService;
	@Inject
	private BusinessService businessService;
	@Inject
	private BusinessAttributeService businessAttributeService;

	/**
	 * 检查用户名是否存在
	 */
	@GetMapping("/check_username")
	public @ResponseBody boolean checkUsername(String username) {
		return StringUtils.isNotEmpty(username) && !businessService.usernameExists(username);
	}

	/**
	 * 检查E-mail是否存在
	 */
	@GetMapping("/check_email")
	public @ResponseBody boolean checkEmail(String email) {
		return StringUtils.isNotEmpty(email) && !businessService.emailExists(email);
	}

	/**
	 * 检查手机是否存在
	 */
	@GetMapping("/check_mobile")
	public @ResponseBody boolean checkMobile(String mobile) {
		return StringUtils.isNotEmpty(mobile) && !businessService.mobileExists(mobile);
	}

	/**
	 * 注册页面
	 */
	@GetMapping
	public String index(ModelMap model) {
		return "business/register/index";
	}

	/**
	 * 注册提交
	 */
	@PostMapping("/submit")
	public ResponseEntity<?> submit(String username, String password, String email, String mobile, HttpServletRequest request) {
		Setting setting = SystemUtils.getSetting();
		if (!ArrayUtils.contains(setting.getAllowedRegisterTypes(), Setting.RegisterType.BUSINESS)) {
			return Results.unprocessableEntity("business.register.disabled");
		}
		if (!isValid(Business.class, "username", username, BaseEntity.Save.class) || !isValid(Business.class, "password", password, BaseEntity.Save.class) || !isValid(Business.class, "email", email, BaseEntity.Save.class) || !isValid(Business.class, "mobile", mobile, BaseEntity.Save.class)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (businessService.usernameExists(username)) {
			return Results.unprocessableEntity("business.register.usernameExist");
		}
		if (businessService.emailExists(email)) {
			return Results.unprocessableEntity("business.register.emailExist");
		}
		if (businessService.mobileExists(mobile)) {
			return Results.unprocessableEntity("business.register.mobileExist");
		}

		Business business = new Business();
		business.removeAttributeValue();
		for (BusinessAttribute businessAttribute : businessAttributeService.findList(true, true)) {
			String[] values = request.getParameterValues("businessAttribute_" + businessAttribute.getId());
			if (!businessAttributeService.isValid(businessAttribute, values)) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			Object memberAttributeValue = businessAttributeService.toBusinessAttributeValue(businessAttribute, values);
			business.setAttributeValue(businessAttribute, memberAttributeValue);
		}

		business.setUsername(username);
		business.setPassword(password);
		business.setEmail(email);
		business.setMobile(mobile);
		business.setBalance(BigDecimal.ZERO);
		business.setFrozenAmount(BigDecimal.ZERO);
		business.setStore(null);
		business.setBusinessCashs(null);
		business.setBusinessDepositLogs(null);
		business.setIsEnabled(true);
		business.setIsLocked(false);
		business.setLockDate(null);
		business.setLastLoginIp(XmlUtils.getIp(request));
		business.setLastLoginDate(new Date());

		userService.register(business);
		userService.login(new UserAuthenticationToken(Business.class, username, password, false, request.getRemoteAddr()));
		return Results.OK;
	}

}