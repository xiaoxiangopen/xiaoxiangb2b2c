package net.xiaoxiangshop.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * Entity - 消息配置
 * 
 */
@Entity
public class MessageConfig extends BaseEntity<MessageConfig> {

	private static final long serialVersionUID = -5214678967755261831L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum<Integer> {

		/**
		 * 会员注册
		 */
		REGISTER_MEMBER(0),

		/**
		 * 订单创建
		 */
		CREATE_ORDER(1),

		/**
		 * 订单更新
		 */
		UPDATE_ORDER(2),

		/**
		 * 订单取消
		 */
		CANCEL_ORDER(3),

		/**
		 * 订单审核
		 */
		REVIEW_ORDER(4),

		/**
		 * 订单收款
		 */
		PAYMENT_ORDER(5),

		/**
		 * 订单退款
		 */
		REFUNDS_ORDER(6),

		/**
		 * 订单发货
		 */
		SHIPPING_ORDER(7),

		/**
		 * 订单退货
		 */
		RETURNS_ORDER(8),

		/**
		 * 订单收货
		 */
		RECEIVE_ORDER(9),

		/**
		 * 订单完成
		 */
		COMPLETE_ORDER(10),

		/**
		 * 订单失败
		 */
		FAIL_ORDER(11),

		/**
		 * 商家注册
		 */
		REGISTER_BUSINESS(12),

		/**
		 * 店铺审核成功
		 */
		APPROVAL_STORE(13),

		/**
		 * 店铺审核失败
		 */
		FAIL_STORE(14);
		
		private int value;

		Type(final int value) {
			this.value = value;
		}
		
		@Override
		public Integer getValue() {
			return this.value;
		}
	}

	/**
	 * 类型
	 */
	private MessageConfig.Type type;

	/**
	 * 是否启用邮件
	 */
	@NotNull
	private Boolean isMailEnabled;

	/**
	 * 是否启用短信
	 */
	@NotNull
	private Boolean isSmsEnabled;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public MessageConfig.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(MessageConfig.Type type) {
		this.type = type;
	}

	/**
	 * 获取是否启用邮件
	 * 
	 * @return 是否启用邮件
	 */
	public Boolean getIsMailEnabled() {
		return isMailEnabled;
	}

	/**
	 * 设置是否启用邮件
	 * 
	 * @param isMailEnabled
	 *            是否启用邮件
	 */
	public void setIsMailEnabled(Boolean isMailEnabled) {
		this.isMailEnabled = isMailEnabled;
	}

	/**
	 * 获取是否启用短信
	 * 
	 * @return 是否启用短信
	 */
	public Boolean getIsSmsEnabled() {
		return isSmsEnabled;
	}

	/**
	 * 设置是否启用短信
	 * 
	 * @param isSmsEnabled
	 *            是否启用短信
	 */
	public void setIsSmsEnabled(Boolean isSmsEnabled) {
		this.isSmsEnabled = isSmsEnabled;
	}

}