package net.xiaoxiangshop.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * Entity - 支付项
 * 
 */
public class PaymentItem implements Serializable {

	private static final long serialVersionUID = -4913487735837005177L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum<Integer> {

		/**
		 * 订单支付
		 */
		ORDER_PAYMENT(0),

		/**
		 * 服务支付
		 */
		SVC_PAYMENT(1),

		/**
		 * 预存款充值
		 */
		DEPOSIT_RECHARGE(2),

		/**
		 * 保证金支付
		 */
		BAIL_PAYMENT(3);
		
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
	@EnumValue
	private PaymentItem.Type type;

	/**
	 * 支付金额
	 */
	private BigDecimal amount;

	/**
	 * 订单编号
	 */
	private String orderSn;

	/**
	 * 服务编号
	 */
	private String svcSn;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public PaymentItem.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(PaymentItem.Type type) {
		this.type = type;
	}

	/**
	 * 获取支付金额
	 * 
	 * @return 支付金额
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置支付金额
	 * 
	 * @param amount
	 *            支付金额
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获取订单编号
	 * 
	 * @return 订单编号
	 */
	public String getOrderSn() {
		return orderSn;
	}

	/**
	 * 设置订单编号
	 * 
	 * @param orderSn
	 *            订单编号
	 */
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	/**
	 * 获取服务编号
	 * 
	 * @return 服务编号
	 */
	public String getSvcSn() {
		return svcSn;
	}

	/**
	 * 设置服务编号
	 * 
	 * @param svcSn
	 *            服务编号
	 */
	public void setSvcSn(String svcSn) {
		this.svcSn = svcSn;
	}

}