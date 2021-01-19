package net.xiaoxiangshop.plugin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 * Plugin - 微信登录(小程序登录)
 * 
 */
@Component("weixinMiniLoginPlugin")
public class WeixinMiniLoginPlugin extends LoginPlugin {

	@Override
	public String getName() {
		return "微信登录(小程序登录)";
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
		return "/admin/plugin/weixin_mini_login/install";
	}

	@Override
	public String getUninstallUrl() {
		return "/admin/plugin/weixin_mini_login/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/weixin_mini_login/setting";
	}

	@Override
	public void signInHandle(LoginPlugin loginPlugin, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public boolean isSignInSuccess(LoginPlugin loginPlugin, String extra, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getUniqueId(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
