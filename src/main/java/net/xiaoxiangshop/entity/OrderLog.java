package net.xiaoxiangshop.entity;

import javax.persistence.Entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * Entity - 订单记录
 * 
 */
@Entity
public class OrderLog extends BaseEntity<OrderLog> {

	private static final long serialVersionUID = -2704154761295319939L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum<Integer> {

		/**
		 * 订单创建
		 */
		CREATE(0),

		/**
		 * 订单修改
		 */
		MODIFY(1),

		/**
		 * 订单取消
		 */
		CANCEL(2),

		/**
		 * 订单审核
		 */
		REVIEW(3),

		/**
		 * 订单收款
		 */
		PAYMENT(4),

		/**
		 * 订单退款
		 */
		REFUNDS(5),

		/**
		 * 订单发货
		 */
		SHIPPING(6),

		/**
		 * 订单退货
		 */
		RETURNS(7),

		/**
		 * 订单收货
		 */
		RECEIVE(8),

		/**
		 * 订单完成
		 */
		COMPLETE(9),


		/**
		 * 订单失败
		 */
		FAIL(10);
		
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
	private OrderLog.Type type;

	/**
	 * 详情
	 */
	private String detail;

	/**
	 * 订单
	 */
	@TableField(exist = false)
	private Order order;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public OrderLog.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(OrderLog.Type type) {
		this.type = type;
	}

	/**
	 * 获取详情
	 * 
	 * @return 详情
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * 设置详情
	 * 
	 * @param detail
	 *            详情
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * 设置订单
	 * 
	 * @param order
	 *            订单
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

}