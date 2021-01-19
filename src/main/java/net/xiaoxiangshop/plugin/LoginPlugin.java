package net.xiaoxiangshop.plugin;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.shiro.web.util.SavedRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;

import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.entity.PluginConfig;
import net.xiaoxiangshop.entity.SocialUser;
import net.xiaoxiangshop.service.PluginConfigService;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * Plugin - 登录
 * 
 */
public abstract class LoginPlugin implements Comparable<LoginPlugin> {

	/**
	 * "显示名称"属性名称
	 */
	public static final String DISPLAY_NAME_ATTRIBUTE_NAME = "displayName";

	/**
	 * "logo"属性名称
	 */
	public static final String LOGO_ATTRIBUTE_NAME = "logo";

	/**
	 * "描述"属性名称
	 */
	public static final String DESCRIPTION_ATTRIBUTE_NAME = "description";

	/**
	 * 默认登录视图名称
	 */
	public static final String DEFAULT_SIGN_IN_VIEW_NAME = "/shop/social_user_login/sign_in";

	/**
	 * 默认登录结果视图名称
	 */
	public static final String DEFAULT_SIGN_IN_RESULT_VIEW_NAME = "/shop/social_user_login/sign_in_result";

	@Value("${security.member_login_url}")
	private String memberLoginUrl;
	@Value("${security.member_login_success_url}")
	private String memberLoginSuccessUrl;

	@Inject
	private PluginConfigService pluginConfigService;

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	public String getId() {
		return getClass().getAnnotation(Component.class).value();
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public abstract String getName();

	/**
	 * 获取版本
	 * 
	 * @return 版本
	 */
	public abstract String getVersion();

	/**
	 * 获取作者
	 * 
	 * @return 作者
	 */
	public abstract String getAuthor();

	/**
	 * 获取网址
	 * 
	 * @return 网址
	 */
	public abstract String getSiteUrl();

	/**
	 * 获取安装URL
	 * 
	 * @return 安装URL
	 */
	public abstract String getInstallUrl();

	/**
	 * 获取卸载URL
	 * 
	 * @return 卸载URL
	 */
	public abstract String getUninstallUrl();

	/**
	 * 获取设置URL
	 * 
	 * @return 设置URL
	 */
	public abstract String getSettingUrl();

	/**
	 * 获取是否已安装
	 * 
	 * @return 是否已安装
	 */
	public boolean getIsInstalled() {
		return pluginConfigService.pluginIdExists(getId());
	}

	/**
	 * 获取插件配置
	 * 
	 * @return 插件配置
	 */
	public PluginConfig getPluginConfig() {
		return pluginConfigService.findByPluginId(getId());
	}

	/**
	 * 获取是否启用
	 * 
	 * @return 是否启用
	 */
	public boolean getIsEnabled() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getIsEnabled() : false;
	}

	/**
	 * 获取属性值
	 * 
	 * @param name
	 *            属性名称
	 * @return 属性值
	 */
	public String getAttribute(String name) {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(name) : null;
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public Integer getOrder() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getOrder() : null;
	}

	/**
	 * 获取显示名称
	 * 
	 * @return 显示名称
	 */
	public String getDisplayName() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(DISPLAY_NAME_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取logo
	 * 
	 * @return logo
	 */
	public String getLogo() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(LOGO_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取描述
	 * 
	 * @return 描述
	 */
	public String getDescription() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(DESCRIPTION_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 是否支持
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 是否支持
	 */
	public boolean supports(HttpServletRequest request) {
		return true;
	}

	/**
	 * 登录前处理
	 * 
	 * @param loginPlugin
	 *            登录插件
	 * @param extra
	 *            附加内容
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param modelAndView
	 *            ModelAndView
	 * @throws Exception
	 */
	public void preSignInHandle(LoginPlugin loginPlugin, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		modelAndView.setViewName("redirect:" + loginPlugin.getSignInUrl(loginPlugin, extra));
	}

	/**
	 * 登录处理
	 * 
	 * @param loginPlugin
	 *            登录插件
	 * @param extra
	 *            附加内容
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param modelAndView
	 *            ModelAndView
	 * @throws Exception
	 */
	public abstract void signInHandle(LoginPlugin loginPlugin, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception;

	/**
	 * 登录后处理
	 * 
	 * @param loginPlugin
	 *            登录插件
	 * @param socialUser
	 *            社会化用户
	 * @param extra
	 *            附加内容
	 * @param isSignInSuccess
	 *            是否登录成功
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param modelAndView
	 *            ModelAndView
	 * @throws Exception
	 */
	public void postSignInHandle(LoginPlugin loginPlugin, SocialUser socialUser, String extra, boolean isSignInSuccess, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		String redirectUrl = null;
		SavedRequest savedRequest = org.apache.shiro.web.util.WebUtils.getAndClearSavedRequest(request);
		if (savedRequest != null) {
			redirectUrl = savedRequest.getRequestUrl();
		} else {
			HttpSession session = request.getSession();
			redirectUrl = (String) session.getAttribute("redirectUrl");
			if (redirectUrl != null) {
				session.removeAttribute("redirectUrl");
			}
		}
		modelAndView.addObject("socialUser", socialUser);
		modelAndView.addObject("redirectUrl", redirectUrl);
		modelAndView.addObject("memberLoginUrl", memberLoginUrl);
		modelAndView.addObject("memberLoginSuccessUrl", memberLoginSuccessUrl);
		modelAndView.setViewName(DEFAULT_SIGN_IN_RESULT_VIEW_NAME);
	}

	/**
	 * 判断是否登录成功
	 * 
	 * @param loginPlugin
	 *            登录插件
	 * @param extra
	 *            附加内容
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return 是否登录成功
	 * @throws Exception
	 */
	public abstract boolean isSignInSuccess(LoginPlugin loginPlugin, String extra, HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * 获取登录前处理URL
	 * 
	 * @param loginPlugin
	 *            登录插件
	 * @return 登录前处理URL
	 */
	public String getPreSignInUrl(LoginPlugin loginPlugin) {
		return getPreSignInUrl(loginPlugin, null);
	}

	/**
	 * 获取登录前处理URL
	 * 
	 * @param loginPlugin
	 *            登录插件
	 * @param extra
	 *            附加内容
	 * @return 登录前处理URL
	 */
	public String getPreSignInUrl(LoginPlugin loginPlugin, String extra) {
		Assert.notNull(loginPlugin, "[Assertion failed] - loginPlugin is required; it must not be null");
		Assert.hasText(loginPlugin.getId(), "[Assertion failed] - loginPlugin id must have text; it must not be null, empty, or blank");

		Setting setting = SystemUtils.getSetting();
		return setting.getSiteUrl() + "/social_user_login/pre_sign_in_" + loginPlugin.getId() + (StringUtils.isNotEmpty(extra) ? "_" + extra : StringUtils.EMPTY);
	}

	/**
	 * 获取登录处理URL
	 * 
	 * @param loginPlugin
	 *            登录插件
	 * @return 登录处理URL
	 */
	public String getSignInUrl(LoginPlugin loginPlugin) {
		return getSignInUrl(loginPlugin, null);
	}

	/**
	 * 获取登录处理URL
	 * 
	 * @param loginPlugin
	 *            登录插件
	 * @param extra
	 *            附加内容
	 * @return 登录处理URL
	 */
	public String getSignInUrl(LoginPlugin loginPlugin, String extra) {
		Assert.notNull(loginPlugin, "[Assertion failed] - loginPlugin is required; it must not be null");
		Assert.hasText(loginPlugin.getId(), "[Assertion failed] - loginPlugin id must have text; it must not be null, empty, or blank");

		Setting setting = SystemUtils.getSetting();
		return setting.getSiteUrl() + "/social_user_login/sign_in_" + loginPlugin.getId() + (StringUtils.isNotEmpty(extra) ? "_" + extra : StringUtils.EMPTY);
	}

	/**
	 * 获取登录后处理URL
	 * 
	 * @param loginPlugin
	 *            登录插件
	 * @return 登录后处理URL
	 */
	public String getPostSignInUrl(LoginPlugin loginPlugin) {
		return getPostSignInUrl(loginPlugin, null);
	}

	/**
	 * 获取登录后处理URL
	 * 
	 * @param loginPlugin
	 *            登录插件
	 * @param extra
	 *            附加内容
	 * @return 登录后处理URL
	 */
	public String getPostSignInUrl(LoginPlugin loginPlugin, String extra) {
		Assert.notNull(loginPlugin, "[Assertion failed] - loginPlugin is required; it must not be null");
		Assert.hasText(loginPlugin.getId(), "[Assertion failed] - loginPlugin id must have text; it must not be null, empty, or blank");

		Setting setting = SystemUtils.getSetting();
		return setting.getSiteUrl() + "/social_user_login/post_sign_in_" + loginPlugin.getId() + (StringUtils.isNotEmpty(extra) ? "_" + extra : StringUtils.EMPTY);
	}

	/**
	 * 获取唯一ID
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 唯一ID
	 */
	public abstract String getUniqueId(HttpServletRequest request);

	/**
	 * 实现compareTo方法
	 * 
	 * @param loginPlugin
	 *            登录插件
	 * @return 比较结果
	 */
	@Override
	public int compareTo(LoginPlugin loginPlugin) {
		if (loginPlugin == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), loginPlugin.getOrder()).append(getId(), loginPlugin.getId()).toComparison();
	}

	/**
	 * 重写equals方法
	 * 
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		LoginPlugin other = (LoginPlugin) obj;
		return new EqualsBuilder().append(getId(), other.getId()).isEquals();
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

}