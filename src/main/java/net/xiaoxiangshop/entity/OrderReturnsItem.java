package net.xiaoxiangshop.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 退货项
 * 
 */
@Entity
public class OrderReturnsItem extends BaseEntity<OrderReturnsItem> {

	private static final long serialVersionUID = -4112374596087084162L;

	/**
	 * 编号
	 */
	private String sn;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 数量
	 */
	@NotNull
	@Min(1)
	private Integer quantity;

	/**
	 * 订单退货
	 */
	@TableField(exist = false)
	private OrderReturns orderReturns;

	/**
	 * 规格
	 */
	@TableField(exist = false)
	private List<String> specifications = new ArrayList<>();

	/**
	 * 获取编号
	 * 
	 * @return 编号
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * 设置编号
	 * 
	 * @param sn
	 *            编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
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
	 * 获取订单退货
	 * 
	 * @return 订单退货
	 */
	public OrderReturns getOrderReturns() {
		return orderReturns;
	}

	/**
	 * 设置订单退货
	 * 
	 * @param orderReturns
	 *            订单退货
	 */
	public void setOrderReturns(OrderReturns orderReturns) {
		this.orderReturns = orderReturns;
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

	/**
	 * 类型转换 - 规格
	 * 
	 */
//	@Converter
//	public static class SpecificationConverter extends BaseAttributeConverter<List<String>> {
//	}

}