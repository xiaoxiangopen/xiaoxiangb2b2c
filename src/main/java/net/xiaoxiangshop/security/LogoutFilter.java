package net.xiaoxiangshop.security;

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.context.ApplicationEventPublisher;

import net.xiaoxiangshop.entity.User;
import net.xiaoxiangshop.event.UserLoggedOutEvent;
import net.xiaoxiangshop.service.UserService;

/**
 * Security - 注销过滤器
 * 
 */
public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {

	@Inject
	private ApplicationEventPublisher applicationEventPublisher;
	@Inject
	private UserService userService;

	/**
	 * 请求前处理
	 * 
	 * @param servletRequest
	 *            ServletRequest
	 * @param servletResponse
	 *            ServletResponse
	 * @return 是否继续执行
	 */
	@Override
	protected boolean preHandle(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		User currentUser = userService.getCurrent();
		applicationEventPublisher.publishEvent(new UserLoggedOutEvent(this, currentUser));

		return super.preHandle(servletRequest, servletResponse);
	}

}