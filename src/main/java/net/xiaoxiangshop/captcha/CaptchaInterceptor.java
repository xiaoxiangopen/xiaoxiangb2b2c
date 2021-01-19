package net.xiaoxiangshop.captcha;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.service.CaptchaService;
import net.xiaoxiangshop.util.SystemUtils;
import net.xiaoxiangshop.util.WebUtils;

/**
 * Captcha - 验证码拦截器
 * 
 */
public class CaptchaInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 默认无需防护的请求方法
	 */
	private static final String[] DEFAULT_NOT_REQUIRE_PROTECTION_REQUEST_METHODS = new String[] { "GET", "HEAD", "TRACE", "OPTIONS" };

	/**
	 * 默认验证码错误页URL
	 */
	private static final String DEFAULT_NCORRECT_CAPTCHA_URL = "/common/error/ncorrect_captcha";

	/**
	 * "验证码ID"参数名称
	 */
	private static final String CAPTCHA_ID_PARAMETER_NAME = "captchaId";

	/**
	 * "验证码"参数名称
	 */
	private static final String CAPTCHA_PARAMETER_NAME = "captcha";

	/**
	 * 验证码类型
	 */
	private Setting.CaptchaType captchaType;

	/**
	 * 无需防护的请求方法
	 */
	private String[] notRequireProtectionRequestMethods = DEFAULT_NOT_REQUIRE_PROTECTION_REQUEST_METHODS;

	/**
	 * 验证码错误页URL
	 */
	private String ncorrectCaptchaUrl = DEFAULT_NCORRECT_CAPTCHA_URL;

	@Inject
	private CaptchaService captchaService;

	/**
	 * 请求前处理
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param handler
	 *            处理器
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Setting setting = SystemUtils.getSetting();
		String captchaId = request.getParameter(CAPTCHA_ID_PARAMETER_NAME);
		String captcha = request.getParameter(CAPTCHA_PARAMETER_NAME);
		if (ArrayUtils.contains(setting.getCaptchaTypes(), getCaptchaType()) && !containsIgnoreCase(getNotRequireProtectionRequestMethods(), request.getMethod()) && !captchaService.isValid(captchaId, captcha)) {
			if (WebUtils.isAjaxRequest(request)) {
				Results.unprocessableEntity(response, "common.message.ncorrectCaptcha");
			} else {
				WebUtils.sendRedirect(request, response, getNcorrectCaptchaUrl());
			}
			return false;
		}
		return super.preHandle(request, response, handler);
	}

	/**
	 * 判断数组是否包含字符串
	 * 
	 * @param array
	 *            数组
	 * @param searchStr
	 *            查找字符串(忽略大小写)
	 * @return 是否包含字符串
	 */
	private boolean containsIgnoreCase(String[] array, String searchStr) {
		if (ArrayUtils.isNotEmpty(array) && searchStr != null) {
			for (String str : array) {
				if (StringUtils.equalsIgnoreCase(str, searchStr)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取验证码类型
	 * 
	 * @return 验证码类型
	 */
	public Setting.CaptchaType getCaptchaType() {
		return captchaType;
	}

	/**
	 * 设置验证码类型
	 * 
	 * @param captchaType
	 *            验证码类型
	 */
	public void setCaptchaType(Setting.CaptchaType captchaType) {
		this.captchaType = captchaType;
	}

	/**
	 * 获取无需防护的请求方法
	 * 
	 * @return 无需防护的请求方法
	 */
	public String[] getNotRequireProtectionRequestMethods() {
		return notRequireProtectionRequestMethods;
	}

	/**
	 * 设置无需防护的请求方法
	 * 
	 * @param notRequireProtectionRequestMethods
	 *            无需防护的请求方法
	 */
	public void setNotRequireProtectionRequestMethods(String[] notRequireProtectionRequestMethods) {
		this.notRequireProtectionRequestMethods = notRequireProtectionRequestMethods;
	}

	/**
	 * 获取验证码错误页URL
	 * 
	 * @return 验证码错误页URL
	 */
	public String getNcorrectCaptchaUrl() {
		return ncorrectCaptchaUrl;
	}

	/**
	 * 设置验证码错误页URL
	 * 
	 * @param ncorrectCaptchaUrl
	 *            验证码错误页URL
	 */
	public void setNcorrectCaptchaUrl(String ncorrectCaptchaUrl) {
		this.ncorrectCaptchaUrl = ncorrectCaptchaUrl;
	}

}