package net.xiaoxiangshop.security;

import org.apache.shiro.authc.UsernamePasswordToken;

import net.xiaoxiangshop.entity.User;

/**
 * Security - 用户认证令牌
 * 
 */
public class UserAuthenticationToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 3568114506450481789L;

	/**
	 * 用户类型
	 */
	private Class<? extends User> userClass;

	/**
	 * 构造方法
	 * 
	 * @param userClass
	 *            用户类型
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param rememberMe
	 *            记住我
	 * @param host
	 *            主机
	 */
	public UserAuthenticationToken(Class<? extends User> userClass, String username, String password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
		this.userClass = userClass;
	}

	/**
	 * 构造方法
	 * 
	 * @param userClass
	 *            用户类型
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param rememberMe
	 *            记住我
	 * @param host
	 *            主机
	 */
	public UserAuthenticationToken(Class<? extends User> userClass, String username, char[] password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
		this.userClass = userClass;
	}

	/**
	 * 获取用户类型
	 * 
	 * @return 用户类型
	 */
	public Class<? extends User> getUserClass() {
		return userClass;
	}

	/**
	 * 设置用户类型
	 * 
	 * @param userClass
	 *            用户类型
	 */
	public void setUserClass(Class<? extends User> userClass) {
		this.userClass = userClass;
	}

}