package net.xiaoxiangshop.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;

import net.xiaoxiangshop.util.JsonUtils;
import net.xiaoxiangshop.util.WebUtils;

/**
 * Plugin - 新浪微博登录
 * 
 */
@Component("weiboLoginPlugin")
public class WeiboLoginPlugin extends LoginPlugin {

	/**
	 * code请求URL
	 */
	private static final String CODE_REQUEST_URL = "https://api.weibo.com/oauth2/authorize";

	/**
	 * uid请求URL
	 */
	private static final String UID_REQUEST_URL = "https://api.weibo.com/oauth2/access_token";

	/**
	 * "状态"属性名称
	 */
	private static final String STATE_ATTRIBUTE_NAME = WeiboLoginPlugin.class.getName() + ".STATE";

	@Override
	public String getName() {
		return "新浪微博登录";
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
		return "/admin/plugin/weibo_login/install";
	}

	@Override
	public String getUninstallUrl() {
		return "/admin/plugin/weibo_login/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/weibo_login/setting";
	}

	@Override
	public void signInHandle(LoginPlugin loginPlugin, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		HttpSession session = request.getSession();

		String state = DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30));
		session.setAttribute(STATE_ATTRIBUTE_NAME, state);

		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("response_type", "code");
		parameterMap.put("client_id", getClientId());
		parameterMap.put("redirect_uri", getPostSignInUrl(loginPlugin));
		parameterMap.put("state", state);

		modelAndView.addObject("requestUrl", CODE_REQUEST_URL);
		modelAndView.addObject("parameterMap", parameterMap);
		modelAndView.setViewName(LoginPlugin.DEFAULT_SIGN_IN_VIEW_NAME);
	}

	@Override
	public boolean isSignInSuccess(LoginPlugin loginPlugin, String extra, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		String state = (String) session.getAttribute(STATE_ATTRIBUTE_NAME);
		String code = request.getParameter("code");
		if (StringUtils.isNotEmpty(state) && StringUtils.equals(state, request.getParameter("state")) && StringUtils.isNotEmpty(code)) {
			session.removeAttribute(STATE_ATTRIBUTE_NAME);
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("grant_type", "authorization_code");
			parameterMap.put("client_id", getClientId());
			parameterMap.put("client_secret", getClientSecret());
			parameterMap.put("redirect_uri", getPostSignInUrl(loginPlugin));
			parameterMap.put("code", code);
			String content = WebUtils.post(UID_REQUEST_URL, parameterMap);
			JsonNode tree = JsonUtils.toTree(content);
			String uid = tree.get("uid").textValue();
			if (StringUtils.isNotEmpty(uid)) {
				request.setAttribute("uid", uid);
				return true;
			}
		}
		return false;
	}

	@Override
	public String getUniqueId(HttpServletRequest request) {
		return (String) request.getAttribute("uid");
	}

	/**
	 * 获取ClientId
	 * 
	 * @return ClientId
	 */
	private String getClientId() {
		return getAttribute("oauthKey");
	}

	/**
	 * 获取ClientSecret
	 * 
	 * @return ClientSecret
	 */
	private String getClientSecret() {
		return getAttribute("oauthSecret");
	}

}