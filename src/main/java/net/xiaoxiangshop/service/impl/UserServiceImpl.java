package net.xiaoxiangshop.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.xiaoxiangshop.exception.MobileSmsCodeException;
import net.xiaoxiangshop.security.UserMobileAuthenticationToken;
import net.xiaoxiangshop.service.SmsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.wf.jwtp.provider.Token;
import org.wf.jwtp.util.SubjectUtil;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.dao.BusinessDao;
import net.xiaoxiangshop.dao.MemberDao;
import net.xiaoxiangshop.dao.UserDao;
import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.User;
import net.xiaoxiangshop.event.UserLoggedInEvent;
import net.xiaoxiangshop.event.UserLoggedOutEvent;
import net.xiaoxiangshop.event.UserRegisteredEvent;
import net.xiaoxiangshop.security.AuthenticationProvider;
import net.xiaoxiangshop.security.SocialUserAuthenticationToken;
import net.xiaoxiangshop.security.UserAuthenticationToken;
import net.xiaoxiangshop.service.UserService;
import net.xiaoxiangshop.util.SystemUtils;
import net.xiaoxiangshop.util.WebUtils;

/**
 * Service - 用户
 * 
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	/**
	 * 认证Provider缓存
	 */
	private static final Map<Class<?>, AuthenticationProvider> AUTHENTICATION_PROVIDER_CACHE = new ConcurrentHashMap<>();

	@Inject
	private ApplicationEventPublisher applicationEventPublisher;
	@Inject
	private List<AuthenticationProvider> authenticationProviders;
	@Inject
	private CacheManager cacheManager;
	@Inject
	private MemberDao memberDao;
	@Inject
	private BusinessDao businessDao;
	@Inject
	private UserDao userDao;
	@Inject
	private SmsService smsService;
	@Override
	public User findByUsername(String username) {
		return userDao.findByAttribute("username", StringUtils.lowerCase(username));
	}
	
	@Override
	@Transactional(readOnly = true)
	public User getCurrentAuditor() {
		return getCurrent();
	}

	@Override
	@Transactional(noRollbackFor = AuthenticationException.class)
	public User getUser(AuthenticationToken authenticationToken) {
		Assert.notNull(authenticationToken, "[Assertion failed] - authenticationToken is required; it must not be null");
		Assert.state(authenticationToken instanceof UserAuthenticationToken || authenticationToken instanceof SocialUserAuthenticationToken||authenticationToken instanceof UserMobileAuthenticationToken, "[Assertion failed] - authenticationToken must be instanceof UserAuthenticationToken or SocialUserAuthenticationToken");

		User user = null;
		if (authenticationToken instanceof UserAuthenticationToken) {
			UserAuthenticationToken userAuthenticationToken = (UserAuthenticationToken) authenticationToken;
			Class<?> userClass = userAuthenticationToken.getUserClass();
			Object principal = userAuthenticationToken.getPrincipal();

			if (userClass == null || principal == null) {
				throw new UnknownAccountException();
			}
			AuthenticationProvider authenticationProvider = getAuthenticationProvider(userClass);
			user = authenticationProvider != null ? authenticationProvider.getUser(principal) : null;
		} else if (authenticationToken instanceof SocialUserAuthenticationToken) {
			SocialUserAuthenticationToken socialUserAuthenticationToken = (SocialUserAuthenticationToken) authenticationToken;
			user = socialUserAuthenticationToken.getSocialUser() != null ? socialUserAuthenticationToken.getSocialUser().getUser() : null;
		}else if(authenticationToken instanceof UserMobileAuthenticationToken){	/**新增2020-10-24新增短信验证码开始******/
			UserMobileAuthenticationToken userAuthenticationToken = (UserMobileAuthenticationToken) authenticationToken;
			Class<?> userClass = userAuthenticationToken.getUserClass();
			Object principal = userAuthenticationToken.getPrincipal();
			if (userClass == null || principal == null) {
				throw new UnknownAccountException();
			}
			AuthenticationProvider authenticationProvider = getAuthenticationProvider(userClass);
			user = authenticationProvider != null ? authenticationProvider.getUser(principal) : null;
		}

		if (user == null) {
			throw new UnknownAccountException();
		}
		if (BooleanUtils.isNotTrue(user.getIsEnabled())) {
			throw new DisabledAccountException();
		}
		if (BooleanUtils.isTrue(user.getIsLocked()) && !tryUnlock(user)) {
			throw new LockedAccountException();
		}
		if (authenticationToken instanceof UserAuthenticationToken) {
			Object credentials = authenticationToken.getCredentials();
			if (!user.isValidCredentials(credentials)) {
				addFailedLoginAttempt(user);
				tryLock(user);
				throw new IncorrectCredentialsException();
			}
		}
		/**新增2020-10-24新增短信验证码校验开始******/
//		if (authenticationToken instanceof UserMobileAuthenticationToken) {
//			Object credentials = authenticationToken.getCredentials();
//			Object principal=authenticationToken.getPrincipal();
//			Boolean validFlag=true;
//			if(credentials!=null&&principal!=null){
//				ResponseEntity<?> responseEntity= smsService.checkSmsCode("1",principal.toString(),credentials.toString());
//				int code=responseEntity.getStatusCodeValue();
//				if(code!=200){
//					validFlag=false;
//				}
//			}else{
//				validFlag=false;
//			}
//			if (!validFlag) {
//				addFailedLoginAttempt(user);
//				tryLock(user);
//				throw new MobileSmsCodeException();
//			}
//		}
		/**********结束***************/
		if (authenticationToken instanceof HostAuthenticationToken) {
			HostAuthenticationToken hostAuthenticationToken = (HostAuthenticationToken) authenticationToken;
			user.setLastLoginIp(hostAuthenticationToken.getHost());
		}
		user.setLastLoginDate(new Date());
		resetFailedLoginAttempts(user);
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<String> getPermissions(User user) {
		Assert.notNull(user, "[Assertion failed] - user is required; it must not be null");

		AuthenticationProvider authenticationProvider = getAuthenticationProvider(user.getClass());
		return authenticationProvider != null ? authenticationProvider.getPermissions(user) : null;
	}

	@Override
	public void register(User user) {
		Assert.notNull(user, "[Assertion failed] - user is required; it must not be null");
		Assert.isTrue(user.isNew(), "[Assertion failed] - user must be new");

		user.setId(IdWorker.getId());
		user.setCreatedDate(new Date());
		user.setVersion(0L);
		
		if (user instanceof Member) {
			memberDao.save((Member)user);
		} else if (user instanceof Business) {
			businessDao.save((Business)user);
		}
		applicationEventPublisher.publishEvent(new UserRegisteredEvent(this, user));
	}

	@Override
	public void login(AuthenticationToken authenticationToken) {
		Assert.notNull(authenticationToken, "[Assertion failed] - authenticationToken is required; it must not be null");

		Subject subject = SecurityUtils.getSubject();
		ThreadContext.remove(ThreadContext.SUBJECT_KEY);
		subject.login(authenticationToken);

		applicationEventPublisher.publishEvent(new UserLoggedInEvent(this, getCurrent()));
	}

	@Override
	public void logout() {
		applicationEventPublisher.publishEvent(new UserLoggedOutEvent(this, getCurrent()));

		Subject subject = SecurityUtils.getSubject();
		subject.logout();
	}

	@Override
	@Transactional(readOnly = true)
	public User getCurrent() {
		return getCurrent(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public <T extends User> T getCurrent(Class<T> userClass) {
		Subject subject = SecurityUtils.getSubject();
		User principal = subject != null && subject.getPrincipal() instanceof User ? (User) subject.getPrincipal() : null;
		if (principal != null) {
			User user = userDao.find(principal.getId());
			if (userClass == null || (user != null && userClass.isAssignableFrom(user.getClass()))) {
				return (T) user;
			}
		} 
		// 处理api当前用户
		HttpServletRequest request = WebUtils.getRequest();
		Token token = SubjectUtil.getToken(request);
		if (token != null) {
			User user = userDao.find(Long.valueOf(token.getUserId()));
			if (userClass == null || (user != null && userClass.isAssignableFrom(user.getClass()))) {
				return (T) user;
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public int getFailedLoginAttempts(User user) {
		Assert.notNull(user, "[Assertion failed] - user is required; it must not be null");
		Assert.isTrue(!user.isNew(), "[Assertion failed] - user must not be new");

		Ehcache cache = cacheManager.getEhcache(User.FAILED_LOGIN_ATTEMPTS_CACHE_NAME);
		Element element = cache.get(user.getId());
		AtomicInteger failedLoginAttempts = element != null ? (AtomicInteger) element.getObjectValue() : null;
		return failedLoginAttempts != null ? failedLoginAttempts.get() : 0;
	}

	@Override
	@Transactional(readOnly = true)
	public void addFailedLoginAttempt(User user) {
		Assert.notNull(user, "[Assertion failed] - user is required; it must not be null");
		Assert.isTrue(!user.isNew(), "[Assertion failed] - user must not be new");

		Long userId = user.getId();
		Ehcache cache = cacheManager.getEhcache(User.FAILED_LOGIN_ATTEMPTS_CACHE_NAME);
		cache.acquireWriteLockOnKey(userId);
		try {
			Element element = cache.get(userId);
			AtomicInteger failedLoginAttempts = element != null ? (AtomicInteger) element.getObjectValue() : null;
			if (failedLoginAttempts != null) {
				failedLoginAttempts.incrementAndGet();
			} else {
				cache.put(new Element(userId, new AtomicInteger(1)));
			}
		} finally {
			cache.releaseWriteLockOnKey(userId);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public void resetFailedLoginAttempts(User user) {
		Assert.notNull(user, "[Assertion failed] - user is required; it must not be null");
		Assert.isTrue(!user.isNew(), "[Assertion failed] - user must not be new");

		Ehcache cache = cacheManager.getEhcache(User.FAILED_LOGIN_ATTEMPTS_CACHE_NAME);
		cache.remove(user.getId());
	}

	@Override
	public boolean tryLock(User user) {
		Assert.notNull(user, "[Assertion failed] - user is required; it must not be null");
		Assert.isTrue(!user.isNew(), "[Assertion failed] - user must not be new");

		if (BooleanUtils.isTrue(user.getIsLocked())) {
			return true;
		}

		Setting setting = SystemUtils.getSetting();
		if (setting.getMaxFailedLoginAttempts() != null) {
			int failedLoginAttempts = getFailedLoginAttempts(user);
			if (failedLoginAttempts >= setting.getMaxFailedLoginAttempts()) {
				user.setIsLocked(true);
				user.setLockDate(new Date());
				super.update(user);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean tryUnlock(User user) {
		Assert.notNull(user, "[Assertion failed] - user is required; it must not be null");
		Assert.isTrue(!user.isNew(), "[Assertion failed] - user must not be new");

		if (BooleanUtils.isFalse(user.getIsLocked())) {
			return true;
		}

		Setting setting = SystemUtils.getSetting();
		if (setting.getPasswordLockTime() != null) {
			Date lockDate = user.getLockDate();
			Date unlockDate = DateUtils.addMinutes(lockDate, setting.getPasswordLockTime());
			if (new Date().after(unlockDate)) {
				unlock(user);
				return true;
			}
		}
		return false;
	}

	@Override
	public void unlock(User user) {
		Assert.notNull(user, "[Assertion failed] - user is required; it must not be null");
		Assert.isTrue(!user.isNew(), "[Assertion failed] - user must not be new");

		if (BooleanUtils.isFalse(user.getIsLocked())) {
			return;
		}

		user.setIsLocked(false);
		user.setLockDate(null);
		resetFailedLoginAttempts(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public boolean save(User user) {
		return super.save(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public User update(User user) {
		return super.update(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public User update(User user, String... ignoreProperties) {
		return super.update(user, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(User user) {
		super.delete(user);
	}

	/**
	 * 获取认证Provider
	 * 
	 * @param userClass
	 *            用户类型
	 * @return 认证Provider，若不存在则返回null
	 */
	private AuthenticationProvider getAuthenticationProvider(Class<?> userClass) {
		Assert.notNull(userClass, "[Assertion failed] - userClass is required; it must not be null");

		if (AUTHENTICATION_PROVIDER_CACHE.containsKey(userClass)) {
			return AUTHENTICATION_PROVIDER_CACHE.get(userClass);
		}

		if (CollectionUtils.isNotEmpty(authenticationProviders)) {
			for (AuthenticationProvider authenticationProvider : authenticationProviders) {
				if (authenticationProvider != null && authenticationProvider.supports(userClass)) {
					AUTHENTICATION_PROVIDER_CACHE.put(userClass, authenticationProvider);
					return authenticationProvider;
				}
			}
		}
		return null;
	}


}