package net.xiaoxiangshop.security;

import net.xiaoxiangshop.entity.User;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Security - 用户认证令牌
 * 
 */
public class UserMobileAuthenticationToken extends MobileToken {

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
	 * @param mobile
	 *            手机号
	 * @param smsCode
	 *            验证码
	 * @param rememberMe
	 *            记住我
	 * @param host
	 *            主机
	 */
	public UserMobileAuthenticationToken(Class<? extends User> userClass, String mobile, String smsCode, boolean rememberMe, String host) {
		super(mobile, smsCode, rememberMe, host);
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