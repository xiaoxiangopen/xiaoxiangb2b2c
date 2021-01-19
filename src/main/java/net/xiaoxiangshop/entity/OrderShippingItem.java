package net.xiaoxiangshop.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 发货项
 * 
 */
@Entity
public class OrderShippingItem extends BaseEntity<OrderShippingItem> {

	private static final long serialVersionUID = 2756395514949325790L;

	/**
	 * SKU编号
	 */
	@JsonView(BaseView.class)
	private String sn;

	/**
	 * SKU名称
	 */
	@JsonView(BaseView.class)
	private String name;

	/**
	 * 数量
	 */
	@JsonView(BaseView.class)
	@NotNull
	@Min(1)
	private Integer quantity;

	/**
	 * 是否需要物流
	 */
	private Boolean isDelivery;

	/**
	 * SKU
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private Sku sku;

	/**
	 * 订单发货
	 */
	@TableField(exist = false)
	private OrderShipping orderShipping;

	/**
	 * 规格
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private List<String> specifications = new ArrayList<>();

	/**
	 * 获取SKU编号
	 * 
	 * @return SKU编号
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * 设置SKU编号
	 * 
	 * @param sn
	 *            SKU编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取SKU名称
	 * 
	 * @return SKU名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置SKU名称
	 * 
	 * @param name
	 *            SKU名称
	 */
	public void setName(String name) {
		this.name = name;
	}

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
	 * 获取是否需要物流
	 * 
	 * @return 是否需要物流
	 */
	public Boolean getIsDelivery() {
		return isDelivery;
	}

	/**
	 * 设置是否需要物流
	 * 
	 * @param isDelivery
	 *            是否需要物流
	 */
	public void setIsDelivery(Boolean isDelivery) {
		this.isDelivery = isDelivery;
	}

	/**
	 * 获取SKU
	 * 
	 * @return SKU
	 */
	public Sku getSku() {
		return sku;
	}

	/**
	 * 设置SKU
	 * 
	 * @param sku
	 *            SKU
	 */
	public void setSku(Sku sku) {
		this.sku = sku;
	}

	/**
	 * 获取订单发货
	 * 
	 * @return 订单发货
	 */
	public OrderShipping getOrderShipping() {
		return orderShipping;
	}

	/**
	 * 设置订单发货
	 * 
	 * @param orderShipping
	 *            订单发货
	 */
	public void setOrderShipping(OrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}

	/**
	 * 获取规格
	 * 
	 * @return 规格
	 */
	public List<String> getSpecifications() {
		return specifications;
	}

	/**
	 * 设置规格
	 * 
	 * @param specifications
	 *            规格
	 */
	public void setSpecifications(List<String> specifications) {
		this.specifications = specifications;
	}

}