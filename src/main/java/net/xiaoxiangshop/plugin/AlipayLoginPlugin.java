package net.xiaoxiangshop.plugin;

import java.util.Collections;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayUserInfoAuthModel;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoAuthRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;

/**
 * Plugin - 支付宝快捷登录
 * 
 */
@Component("alipayLoginPlugin")
public class AlipayLoginPlugin extends LoginPlugin {

	/**
	 * 网关URL
	 */
	private static final String SERVER_URL = "https://openapi.alipay.com/gateway.do";

	/**
	 * "状态"属性名称
	 */
	private static final String STATE_ATTRIBUTE_NAME = AlipayLoginPlugin.class.getName() + ".STATE";

	@Override
	public String getName() {
		return "支付宝快捷登录";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "SHOP";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.xiaoxiangai.com.cn";
	}

	@Override
	public String getInstallUrl() {
		return "/admin/plugin/alipay_login/install";
	}

	@Override
	public String getUninstallUrl() {
		return "/admin/plugin/alipay_login/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/alipay_login/setting";
	}

	@Override
	public boolean supports(HttpServletRequest request) {
		Device device = DeviceUtils.getCurrentDevice(request);
		String userAgent = request.getHeader("USER-AGENT");
		return (device != null && (device.isNormal() || device.isTablet())) || StringUtils.containsIgnoreCase(userAgent, "AliApp");
	}

	@Override
	public void signInHandle(LoginPlugin loginPlugin, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		HttpSession session = request.getSession();

		String state = DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30));
		session.setAttribute(STATE_ATTRIBUTE_NAME, state);

		AlipayUserInfoAuthModel alipayUserInfoAuthModel = new AlipayUserInfoAuthModel();
		alipayUserInfoAuthModel.setScopes(Collections.singletonList("auth_user"));
		alipayUserInfoAuthModel.setState(state);

		AlipayUserInfoAuthRequest alipayUserInfoAuthRequest = new AlipayUserInfoAuthRequest();
		alipayUserInfoAuthRequest.setReturnUrl(getPostSignInUrl(loginPlugin));
		alipayUserInfoAuthRequest.setBizModel(alipayUserInfoAuthModel);
		try {
			modelAndView.addObject("body", getAlipayClient().pageExecute(alipayUserInfoAuthRequest).getBody());
			modelAndView.setViewName("shop/plugin/alipay_login/sign_in");
		} catch (AlipayApiException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public boolean isSignInSuccess(LoginPlugin loginPlugin, String extra, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		String state = (String) session.getAttribute(STATE_ATTRIBUTE_NAME);
		String code = request.getParameter("auth_code");
		if (StringUtils.isNotEmpty(state) && StringUtils.equals(state, request.getParameter("state")) && StringUtils.isNotEmpty(code)) {
			session.removeAttribute(STATE_ATTRIBUTE_NAME);
			AlipaySystemOauthTokenRequest alipaySystemOauthTokenRequest = new AlipaySystemOauthTokenRequest();
			alipaySystemOauthTokenRequest.setCode(code);
			alipaySystemOauthTokenRequest.setGrantType("authorization_code");
			try {
				AlipaySystemOauthTokenResponse alipaySystemOauthTokenResponse = getAlipayClient().execute(alipaySystemOauthTokenRequest);
				String userId = alipaySystemOauthTokenResponse.getUserId();
				if (StringUtils.isNotEmpty(userId)) {
					request.setAttribute("userId", userId);
					return true;
				}
			} catch (AlipayApiException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return false;
	}

	@Override
	public String getUniqueId(HttpServletRequest request) {
		return (String) request.getAttribute("userId");
	}

	/**
	 * 获取AppID
	 * 
	 * @return AppID
	 */
	private String getAppId() {
		return getAttribute("appId");
	}

	/**
	 * 获取开发者应用私钥
	 * 
	 * @return 开发者应用私钥
	 */
	private String getAppPrivateKey() {
		return getAttribute("appPrivateKey");
	}

	/**
	 * 获取支付宝公钥
	 * 
	 * @return 支付宝公钥
	 */
	private String getAlipayPublicKey() {
		return getAttribute("alipayPublicKey");
	}

	/**
	 * 获取AlipayClient
	 * 
	 * @return AlipayClient
	 */
	private AlipayClient getAlipayClient() {
		return new DefaultAlipayClient(SERVER_URL, getAppId(), getAppPrivateKey(), "json", "UTF-8", getAlipayPublicKey(), "RSA2");
	}

}