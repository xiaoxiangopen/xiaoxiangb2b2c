package net.xiaoxiangshop.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 消息组
 * 
 */
@Entity
public class MessageGroup extends BaseEntity<MessageGroup> {

	private static final long serialVersionUID = -7049435815884077484L;

	/**
	 * 用户1
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private User user1;

	/**
	 * 用户2
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private User user2;

	/**
	 * 用户1消息状态
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private MessageStatus user1MessageStatus;

	/**
	 * 用户2消息状态
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private MessageStatus user2MessageStatus;

	/**
	 * 消息
	 */
	@TableField(exist = false)
	private Set<Message> messages = new HashSet<>();

	/**
	 * 获取用户1
	 * 
	 * @return 用户1
	 */
	public User getUser1() {
		return user1;
	}

	/**
	 * 设置用户1
	 * 
	 * @param user1
	 *            用户1
	 */
	public void setUser1(User user1) {
		this.user1 = user1;
	}

	/**
	 * 获取用户2
	 * 
	 * @return 用户2
	 */
	public User getUser2() {
		return user2;
	}

	/**
	 * 设置用户2
	 * 
	 * @param user2
	 *            用户2
	 */
	public void setUser2(User user2) {
		this.user2 = user2;
	}

	/**
	 * 获取用户1消息状态
	 * 
	 * @return 用户1消息状态
	 */
	public MessageStatus getUser1MessageStatus() {
		return user1MessageStatus;
	}

	/**
	 * 设置用户1消息状态
	 * 
	 * @param user1MessageStatus
	 *            用户1消息状态
	 */
	public void setUser1MessageStatus(MessageStatus user1MessageStatus) {
		this.user1MessageStatus = user1MessageStatus;
	}

	/**
	 * 获取用户2消息状态
	 * 
	 * @return 用户2消息状态
	 */
	public MessageStatus getUser2MessageStatus() {
		return user2MessageStatus;
	}

	/**
	 * 设置用户2消息状态
	 * 
	 * @param user2MessageStatus
	 *            用户2消息状态
	 */
	public void setUser2MessageStatus(MessageStatus user2MessageStatus) {
		this.user2MessageStatus = user2MessageStatus;
	}

	/**
	 * 获取消息
	 * 
	 * @return 消息
	 */
	public Set<Message> getMessages() {
		return messages;
	}

	/**
	 * 设置消息
	 * 
	 * @param messages
	 *            消息
	 */
	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}

}