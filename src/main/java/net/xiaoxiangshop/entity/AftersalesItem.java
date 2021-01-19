package net.xiaoxiangshop.entity;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 售后项
 * 
 */
@Entity
public class AftersalesItem extends BaseEntity<AftersalesItem> {

	private static final long serialVersionUID = -3285489579368368057L;

	/**
	 * 数量
	 */
	@JsonView(BaseView.class)
	@NotNull
	@Min(1)
	private Integer quantity;

	/**
	 * 订单项
	 */
	@JsonIgnore
	//@JsonView(BaseView.class)
	private OrderItem orderItem;

	/**
	 * 售后
	 */
	@JsonIgnore
	private Aftersales aftersales;

	/**
	 * 获取数量
	 * 
	 * @return 数量
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * 设置数量
	 * 
	 * @param quantity
	 *            数量
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * 获取订单项
	 * 
	 * @return 订单项
	 */
	public OrderItem getOrderItem() {
		return orderItem;
	}

	/**
	 * 设置订单项
	 * 
	 * @param orderItem
	 *            订单项
	 */
	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	/**
	 * 获取售后
	 * 
	 * @return 售后
	 */
	public Aftersales getAftersales() {
		return aftersales;
	}

	/**
	 * 设置售后
	 * 
	 * @param aftersales
	 *            售后
	 */
	public void setAftersales(Aftersales aftersales) {
		this.aftersales = aftersales;
	}

}