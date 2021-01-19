package net.xiaoxiangshop.security;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import net.xiaoxiangshop.service.StoreService;

/**
 * Security - 当前店铺拦截器
 * 
 */
public class CurrentStoreHandlerInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 默认"当前店铺"属性名称
	 */
	public static final String DEFAULT_CURRENT_STORE_ATTRIBUTE_NAME = "currentStore";

	/**
	 * "当前店铺"属性名称
	 */
	private String currentStoreAttributeName = DEFAULT_CURRENT_STORE_ATTRIBUTE_NAME;

	@Inject
	private StoreService storeService;

	/**
	 * 请求后处理
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param handler
	 *            处理器
	 * @param modelAndView
	 *            数据视图
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		request.setAttribute(getCurrentStoreAttributeName(), storeService.getCurrent());
	}

	/**
	 * 获取"当前店铺"属性名称
	 * 
	 * @return "当前店铺"属性名称
	 */
	public String getCurrentStoreAttributeName() {
		return currentStoreAttributeName;
	}

	/**
	 * 设置"当前店铺"属性名称
	 * 
	 * @param currentStoreAttributeName
	 *            "当前店铺"属性名称
	 */
	public void setCurrentStoreAttributeName(String currentStoreAttributeName) {
		this.currentStoreAttributeName = currentStoreAttributeName;
	}

}