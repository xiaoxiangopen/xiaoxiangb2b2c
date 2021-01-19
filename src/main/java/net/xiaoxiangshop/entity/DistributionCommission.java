package net.xiaoxiangshop.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 分销佣金
 * 
 */
@Entity
public class DistributionCommission extends BaseEntity<DistributionCommission> {

	private static final long serialVersionUID = -4341994379291310792L;

	/**
	 * 金额
	 */
	@JsonView(BaseView.class)
	private BigDecimal amount;

	/**
	 * 订单
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private Order order;

	/**
	 * 分销员
	 */
	@TableField(exist = false)
	private Distributor distributor;

	/**
	 * 获取金额
	 * 
	 * @return 金额
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置金额
	 * 
	 * @param amount
	 *            金额
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	/**
	 * 获取分销员
	 * 
	 * @return 分销员
	 */
	public Distributor getDistributor() {
		return distributor;
	}

	/**
	 * 设置分销员
	 * 
	 * @param distributor
	 *            分销员
	 */
	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}

}