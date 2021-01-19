package net.xiaoxiangshop.security;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.util.WebUtils;

/**
 * Security - CSRF拦截器
 * 
 */
public class CsrfInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 默认无需防护的请求方法
	 */
	private static final String[] DEFAULT_NOT_REQUIRE_PROTECTION_REQUEST_METHODS = new String[] { "GET", "HEAD", "TRACE", "OPTIONS" };

	/**
	 * 默认CSRF令牌错误页URL
	 */
	private static final String DEFAULT_INVALID_CSRF_TOKEN_URL = "/common/error/invalid_csrf_token";

	/**
	 * "CSRF令牌"Cookie名称
	 */
	private static final String CSRF_TOKEN_COOKIE_NAME = "csrfToken";

	/**
	 * "CSRF令牌"参数名称
	 */
	private static final String CSRF_TOKEN_PARAMETER_NAME = "csrfToken";

	/**
	 * "CSRF令牌"Header名称
	 */
	private static final String CSRF_TOKEN_HEADER_NAME = "X-Csrf-Token";

	/**
	 * "CSRF令牌"属性名称
	 */
	private static final String CSRF_TOKEN_ATTRIBUTE_NAME = "csrfToken";

	/**
	 * 无需防护的请求方法
	 */
	private String[] notRequireProtectionRequestMethods = DEFAULT_NOT_REQUIRE_PROTECTION_REQUEST_METHODS;

	/**
	 * CSRF令牌错误页URL
	 */
	private String invalidCsrfTokenUrl = DEFAULT_INVALID_CSRF_TOKEN_URL;

	/**
	 * 请求前处理
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param handler
	 *            处理器
	 * @return 是否继续执行
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String csrfToken = WebUtils.getCookie(request, CSRF_TOKEN_COOKIE_NAME);
		if (StringUtils.isEmpty(csrfToken)) {
			csrfToken = DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30));
			WebUtils.addCookie(request, response, CSRF_TOKEN_COOKIE_NAME, csrfToken);
		}
		if (!containsIgnoreCase(getNotRequireProtectionRequestMethods(), request.getMethod())) {
			String actualCsrfToken = request.getParameter(CSRF_TOKEN_PARAMETER_NAME);
			if (actualCsrfToken == null) {
				actualCsrfToken = request.getHeader(CSRF_TOKEN_HEADER_NAME);
			}
			if (!StringUtils.equals(csrfToken, actualCsrfToken)) {
				if (WebUtils.isAjaxRequest(request)) {
					Results.forbidden(response, "common.message.invalidCsrfToken");
				} else {
					WebUtils.sendRedirect(request, response, getInvalidCsrfTokenUrl());
				}
				return false;
			}
		}
		request.setAttribute(CSRF_TOKEN_ATTRIBUTE_NAME, csrfToken);
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
	 * 获取CSRF令牌错误页URL
	 * 
	 * @return CSRF令牌错误页URL
	 */
	public String getInvalidCsrfTokenUrl() {
		return invalidCsrfTokenUrl;
	}

	/**
	 * 设置CSRF令牌错误页URL
	 * 
	 * @param invalidCsrfTokenUrl
	 *            CSRF令牌错误页URL
	 */
	public void setInvalidCsrfTokenUrl(String invalidCsrfTokenUrl) {
		this.invalidCsrfTokenUrl = invalidCsrfTokenUrl;
	}

}