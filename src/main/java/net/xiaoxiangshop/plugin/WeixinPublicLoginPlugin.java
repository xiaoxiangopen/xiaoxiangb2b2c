package net.xiaoxiangshop.plugin;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.TokenRequestBuilder;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 * Plugin - 微信登录(公众号登录)
 * 
 */
@Component("weixinPublicLoginPlugin")
public class WeixinPublicLoginPlugin extends LoginPlugin {

	/**
	 * code请求URL
	 */
	private static final String CODE_REQUEST_URL = "https://open.weixin.qq.com/connect/oauth2/authorize#wechat_redirect";

	/**
	 * openId请求URL
	 */
	private static final String OPEN_ID_REQUEST_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

	@Override
	public String getName() {
		return "微信登录(公众号登录)";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "xiaoxiangAI";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.xiaoxiangshop.net";
	}

	@Override
	public String getInstallUrl() {
		return "/admin/plugin/weixin_public_login/install";
	}

	@Override
	public String getUninstallUrl() {
		return "/admin/plugin/weixin_public_login/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/weixin_public_login/setting";
	}

	@Override
	public boolean supports(HttpServletRequest request) {
		String userAgent = request.getHeader("USER-AGENT");
		return StringUtils.containsIgnoreCase(userAgent, "micromessenger");
	}

	@Override
	public void signInHandle(LoginPlugin loginPlugin, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		Map<String, Object> parameterMap = new TreeMap<>();
		parameterMap.put("appid", getAppId());
		parameterMap.put("redirect_uri", getPostSignInUrl(loginPlugin));
		parameterMap.put("response_type", "code");
		parameterMap.put("scope", "snsapi_userinfo");

		modelAndView.addObject("requestUrl", CODE_REQUEST_URL);
		modelAndView.addObject("parameterMap", parameterMap);
		modelAndView.setViewName(LoginPlugin.DEFAULT_SIGN_IN_VIEW_NAME);
	}

	@Override
	public boolean isSignInSuccess(LoginPlugin loginPlugin, String extra, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String code = request.getParameter("code");
		if (StringUtils.isEmpty(code)) {
			return false;
		}

		try {
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
			TokenRequestBuilder tokenRequestBuilder = OAuthClientRequest.tokenLocation(OPEN_ID_REQUEST_URL);
			tokenRequestBuilder.setParameter("appid", getAppId());
			tokenRequestBuilder.setParameter("secret", getAppSecret());
			tokenRequestBuilder.setParameter("connect_redirect", "1");
			tokenRequestBuilder.setCode(code);
			tokenRequestBuilder.setGrantType(GrantType.AUTHORIZATION_CODE);
			OAuthClientRequest accessTokenRequest = tokenRequestBuilder.buildQueryMessage();
			OAuthJSONAccessTokenResponse authJSONAccessTokenResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.GET);
			String openId = authJSONAccessTokenResponse.getParam("openid");
			if (StringUtils.isNotEmpty(openId)) {
				request.setAttribute("openid", openId);
				return true;
			}
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (OAuthProblemException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public String getUniqueId(HttpServletRequest request) {
		return (String) request.getAttribute("openid");
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
	 * 获取AppSecret
	 * 
	 * @return AppSecret
	 */
	private String getAppSecret() {
		return getAttribute("appSecret");
	}

}