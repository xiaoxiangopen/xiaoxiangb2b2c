package net.xiaoxiangshop.service;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationToken;

import net.xiaoxiangshop.audit.AuditorProvider;
import net.xiaoxiangshop.entity.User;

/**
 * Service - 用户
 * 
 */
public interface UserService extends BaseService<User>, AuditorProvider<User> {

	/**
	 * 根据用户名查找用户
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 用户，若不存在则返回null
	 */
	User findByUsername(String username);
	
	/**
	 * 获取用户
	 * 
	 * @param authenticationToken
	 *            认证令牌
	 * @return 用户
	 */
	User getUser(AuthenticationToken authenticationToken);

	/**
	 * 获取权限
	 * 
	 * @param user
	 *            用户
	 * @return 权限
	 */
	Set<String> getPermissions(User user);

	/**
	 * 用户注册
	 * 
	 * @param user
	 *            用户
	 */
	void register(User user);

	/**
	 * 用户登录
	 * 
	 * @param authenticationToken
	 *            认证令牌
	 */
	void login(AuthenticationToken authenticationToken);

	/**
	 * 用户注销
	 */
	void logout();

	/**
	 * 获取当前登录用户
	 * 
	 * @return 当前登录用户，若不存在则返回null
	 */
	User getCurrent();

	/**
	 * 获取当前登录用户
	 * 
	 * @param userClass
	 *            用户类型
	 * @return 当前登录用户，若不存在则返回null
	 */
	<T extends User> T getCurrent(Class<T> userClass);

	/**
	 * 获取登录失败尝试次数
	 * 
	 * @param user
	 *            用户
	 * @return 登录失败尝试次数
	 */
	int getFailedLoginAttempts(User user);

	/**
	 * 增加登录失败尝试次数
	 * 
	 * @param user
	 *            用户
	 */
	void addFailedLoginAttempt(User user);

	/**
	 * 重置登录失败尝试次数
	 * 
	 * @param user
	 *            用户
	 */
	void resetFailedLoginAttempts(User user);

	/**
	 * 尝试用户锁定
	 * 
	 * @param user
	 *            用户
	 * @return 是否锁定
	 */
	boolean tryLock(User user);

	/**
	 * 尝试用户解锁
	 * 
	 * @param user
	 *            用户
	 * @return 是否解锁
	 */
	boolean tryUnlock(User user);

	/**
	 * 用户解锁
	 * 
	 * @param user
	 *            用户
	 */
	void unlock(User user);

}