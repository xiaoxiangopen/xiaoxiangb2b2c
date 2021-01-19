package net.xiaoxiangshop.listener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.Cart;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.PointLog;
import net.xiaoxiangshop.entity.SocialUser;
import net.xiaoxiangshop.entity.User;
import net.xiaoxiangshop.event.UserLoggedInEvent;
import net.xiaoxiangshop.event.UserRegisteredEvent;
import net.xiaoxiangshop.service.CartService;
import net.xiaoxiangshop.service.MemberService;
import net.xiaoxiangshop.service.SocialUserService;
import net.xiaoxiangshop.util.SystemUtils;
import net.xiaoxiangshop.util.WebUtils;

/**
 * Listener - 用户事件
 * 
 */
@Component
public class UserEventListener {

	@Inject
	private MemberService memberService;
	@Inject
	private CartService cartService;
	@Inject
	private SocialUserService socialUserService;

	/**
	 * 事件处理
	 * 
	 * @param userRegisteredEvent
	 *            用户注册事件
	 */
	@EventListener
	public void handle(UserRegisteredEvent userRegisteredEvent) {
		User user = userRegisteredEvent.getUser();
		HttpServletRequest request = WebUtils.getRequest();

		if (user instanceof Member) {
			String socialUserId = request.getParameter("socialUserId");
			String uniqueId = request.getParameter("uniqueId");
			if (StringUtils.isNotEmpty(socialUserId) && StringUtils.isNotEmpty(uniqueId)) {
				SocialUser socialUser = socialUserService.find(Long.parseLong(socialUserId));
				if (socialUser != null && socialUser.getUser() == null) {
					socialUserService.bindUser(user, socialUser, uniqueId);
				}
			}

			Member member = (Member) user;
			Setting setting = SystemUtils.getSetting();
			if (setting.getRegisterPoint() > 0) {
				memberService.addPoint(member, setting.getRegisterPoint(), PointLog.Type.REWARD, null);
			}
		}
	}

	/**
	 * 事件处理
	 * 
	 * @param userLoggedInEvent
	 *            用户登录事件
	 */
	@EventListener
	public void handle(UserLoggedInEvent userLoggedInEvent) {
		User user = userLoggedInEvent.getUser();
		HttpServletRequest request = WebUtils.getRequest();

		if (user instanceof Member) {
			String socialUserId = request.getParameter("socialUserId");
			String uniqueId = request.getParameter("uniqueId");
			if (StringUtils.isNotEmpty(socialUserId) && StringUtils.isNotEmpty(uniqueId)) {
				SocialUser socialUser = socialUserService.find(Long.parseLong(socialUserId));
				if (socialUser != null && socialUser.getUser() == null) {
					socialUserService.bindUser(user, socialUser, uniqueId);
				}
			}

			Member member = (Member) user;
			Subject subject = SecurityUtils.getSubject();
			sessionFixationProtection(subject);

			Cart cart = member.getCart();
			cartService.merge(cart != null ? cart : cartService.create());
		} else if (user instanceof Business) {
			Subject subject = SecurityUtils.getSubject();
			sessionFixationProtection(subject);
		}

	}

	/**
	 * Session固定攻击防护
	 * 
	 * @param subject
	 *            Subject
	 */
	private void sessionFixationProtection(Subject subject) {
		Assert.notNull(subject, "[Assertion failed] - subject is required; it must not be null");

		Session session = subject.getSession();
		Map<Object, Object> attributes = new HashMap<>();
		Collection<Object> attributeKeys = session.getAttributeKeys();
		for (Object attributeKey : attributeKeys) {
			attributes.put(attributeKey, session.getAttribute(attributeKey));
		}
		session.stop();
		session = subject.getSession(true);
		for (Map.Entry<Object, Object> entry : attributes.entrySet()) {
			session.setAttribute(entry.getKey(), entry.getValue());
		}
	}

}