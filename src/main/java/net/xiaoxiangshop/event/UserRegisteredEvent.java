package net.xiaoxiangshop.event;

import net.xiaoxiangshop.entity.User;

/**
 * Event - 用户注册
 * 
 */
public class UserRegisteredEvent extends UserEvent {

	private static final long serialVersionUID = 3930447081075693577L;

	/**
	 * 构造方法
	 * 
	 * @param source
	 *            事件源
	 * @param user
	 *            用户
	 */
	public UserRegisteredEvent(Object source, User user) {
		super(source, user);
	}

}