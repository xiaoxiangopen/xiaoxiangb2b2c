package net.xiaoxiangshop.entity;

import javax.persistence.Entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * Entity - 序列号
 * 
 */
@Entity
public class Sn extends BaseEntity<Sn> {

	private static final long serialVersionUID = -2330598144835706164L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum<Integer> {

		/**
		 * 商品
		 */
		PRODUCT(0),

		/**
		 * 订单
		 */
		ORDER(1),

		/**
		 * 订单支付
		 */
		ORDER_PAYMENT(2),

		/**
		 * 订单退款
		 */
		ORDER_REFUNDS(3),

		/**
		 * 订单发货
		 */
		ORDER_SHIPPING(4),

		/**
		 * 订单退货
		 */
		ORDER_RETURNS(5),

		/**
		 * 支付事务
		 */
		PAYMENT_TRANSACTION(6),

		/**
		 * 平台服务
		 */
		PLATFORM_SERVICE(7);
		
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
	private Sn.Type type;

	/**
	 * 末值
	 */
	private Long lastValue;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public Sn.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Sn.Type type) {
		this.type = type;
	}

	/**
	 * 获取末值
	 * 
	 * @return 末值
	 */
	public Long getLastValue() {
		return lastValue;
	}

	/**
	 * 设置末值
	 * 
	 * @param lastValue
	 *            末值
	 */
	public void setLastValue(Long lastValue) {
		this.lastValue = lastValue;
	}

}