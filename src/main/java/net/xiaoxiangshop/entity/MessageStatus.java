package net.xiaoxiangshop.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonView;

import net.xiaoxiangshop.entity.BaseEntity.BaseView;

/**
 * Entity - 消息状态
 * 
 */
public class MessageStatus implements Serializable {

	private static final long serialVersionUID = 2942316162274502962L;

	/**
	 * 构造方法
	 */
	public MessageStatus() {
	}

	/**
	 * 构造方法
	 * 
	 * @param isRead
	 *            是否已读
	 * @param isDeleted
	 *            是否已删除
	 */
	public MessageStatus(Boolean isRead, Boolean isDeleted) {
		this.isRead = isRead;
		this.isDeleted = isDeleted;
	}

	/**
	 * 是否已读
	 */
	@JsonView(BaseView.class)
	private Boolean isRead;

	/**
	 * 是否已删除
	 */
	@JsonView(BaseView.class)
	private Boolean isDeleted;

	/**
	 * 获取是否已读
	 * 
	 * @return 是否已读
	 */
	public Boolean getIsRead() {
		return isRead;
	}

	/**
	 * 设置是否已读
	 * 
	 * @param isRead
	 *            是否已读
	 */
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	/**
	 * 获取是否已删除
	 * 
	 * @return 是否已删除
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * 设置是否已删除
	 * 
	 * @param isDeleted
	 *            是否已删除
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * 重写equals方法
	 * 
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}