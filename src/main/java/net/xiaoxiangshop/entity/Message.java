package net.xiaoxiangshop.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 消息
 * 
 */
@Entity
public class Message extends BaseEntity<Message> {

	private static final long serialVersionUID = 4592467651538360915L;

	/**
	 * 内容
	 */
	@JsonView(BaseView.class)
	@NotEmpty
	private String content;

	/**
	 * IP
	 */
	private String ip;

	/**
	 * 发送人
	 */
	@TableField(exist = false)
	private User fromUser;

	/**
	 * 接收人
	 */
	@TableField(exist = false)
	private User toUser;

	/**
	 * 发送人消息状态
	 */
	@TableField(exist = false)
	private MessageStatus fromUserMessageStatus;

	/**
	 * 接收人消息状态
	 */
	@TableField(exist = false)
	private MessageStatus toUserMessageStatus;

	/**
	 * 消息组
	 */
	@TableField(exist = false)
	private MessageGroup messageGroup;

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取IP
	 * 
	 * @return IP
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 设置IP
	 * 
	 * @param ip
	 *            IP
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 获取发送人
	 * 
	 * @return 发送人
	 */
	public User getFromUser() {
		return fromUser;
	}

	/**
	 * 设置发送人
	 * 
	 * @param fromUser
	 *            发送人
	 */
	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	/**
	 * 获取接收人
	 * 
	 * @return 接收人
	 */
	public User getToUser() {
		return toUser;
	}

	/**
	 * 设置接收人
	 * 
	 * @param toUser
	 *            接收人
	 */
	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	/**
	 * 获取发送人消息状态
	 * 
	 * @return 发送人消息状态
	 */
	public MessageStatus getFromUserMessageStatus() {
		return fromUserMessageStatus;
	}

	/**
	 * 设置发送人消息状态
	 * 
	 * @param fromUserMessageStatus
	 *            发送人消息状态
	 */
	public void setFromUserMessageStatus(MessageStatus fromUserMessageStatus) {
		this.fromUserMessageStatus = fromUserMessageStatus;
	}

	/**
	 * 获取接收人消息状态
	 * 
	 * @return 接收人消息状态
	 */
	public MessageStatus getToUserMessageStatus() {
		return toUserMessageStatus;
	}

	/**
	 * 设置接收人消息状态
	 * 
	 * @param toUserMessageStatus
	 *            接收人消息状态
	 */
	public void setToUserMessageStatus(MessageStatus toUserMessageStatus) {
		this.toUserMessageStatus = toUserMessageStatus;
	}

	/**
	 * 获取消息组
	 * 
	 * @return 消息组
	 */
	public MessageGroup getMessageGroup() {
		return messageGroup;
	}

	/**
	 * 设置消息组
	 * 
	 * @param messageGroup
	 *            消息组
	 */
	public void setMessageGroup(MessageGroup messageGroup) {
		this.messageGroup = messageGroup;
	}

}