package net.xiaoxiangshop.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Entity - 订单统计
 * 
 */
@Entity
public class OrderStatistic extends BaseEntity<OrderStatistic> {

	private static final long serialVersionUID = 2022131337300482638L;
	/**
	 * 创建日期
	 */
	private Date createdDate;

	/**
	 * 版本
	 */
	private Long version;

	/**
	 * 统计日期
	 */
	private Date statisticDate;

	/**
	 * PC订单数量
	 */
	private Long pcOPrderNum;
	/**
	 * PC订单总金额
	 */
	private BigDecimal pcOrderAmount;
	/**
	 * 小程序订单数量
	 */
	private Long appletsOrderNum;
	/**
	 * 小程序订单总金额
	 */
	private BigDecimal appletsOrderAmount;
	/**
	 * app订单数量
	 */
	private Long appOrderNum;
	/**
	 * app订单总金额
	 */
	private BigDecimal appOrderAmount;

	/**
	 * 订单数量
	 */
	private Long orderNum;
	/**
	 * 订单总金额
	 */
	private BigDecimal totalOrderAmount;
	/**
	 * 平均订单总金额
	 */
	@JsonIgnore
	@TableField(exist = false)
	private BigDecimal averageOrderAmount;

	/**
	 * 店铺
	 */
	@JsonIgnore
	@TableField(exist = false)
	private Store store;

	@Override
	public Date getCreatedDate() {
		return createdDate;
	}

	@Override
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public Long getVersion() {
		return version;
	}

	@Override
	public void setVersion(Long version) {
		this.version = version;
	}

	public Date getStatisticDate() {
		return statisticDate;
	}

	public void setStatisticDate(Date statisticDate) {
		this.statisticDate = statisticDate;
	}

	public Long getPcOPrderNum() {
		return pcOPrderNum;
	}

	public void setPcOPrderNum(Long pcOPrderNum) {
		this.pcOPrderNum = pcOPrderNum;
	}

	public BigDecimal getPcOrderAmount() {
		return pcOrderAmount;
	}

	public void setPcOrderAmount(BigDecimal pcOrderAmount) {
		this.pcOrderAmount = pcOrderAmount;
	}

	public Long getAppletsOrderNum() {
		return appletsOrderNum;
	}

	public void setAppletsOrderNum(Long appletsOrderNum) {
		this.appletsOrderNum = appletsOrderNum;
	}

	public BigDecimal getAppletsOrderAmount() {
		return appletsOrderAmount;
	}

	public void setAppletsOrderAmount(BigDecimal appletsOrderAmount) {
		this.appletsOrderAmount = appletsOrderAmount;
	}

	public Long getAppOrderNum() {
		return appOrderNum;
	}

	public void setAppOrderNum(Long appOrderNum) {
		this.appOrderNum = appOrderNum;
	}

	public BigDecimal getAppOrderAmount() {
		return appOrderAmount;
	}

	public void setAppOrderAmount(BigDecimal appOrderAmount) {
		this.appOrderAmount = appOrderAmount;
	}

	public Long getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Long orderNum) {
		this.orderNum = orderNum;
	}

	public BigDecimal getTotalOrderAmount() {
		return totalOrderAmount;
	}

	public void setTotalOrderAmount(BigDecimal totalOrderAmount) {
		this.totalOrderAmount = totalOrderAmount;
	}

	public BigDecimal getAverageOrderAmount() {
		return averageOrderAmount;
	}

	public void setAverageOrderAmount(BigDecimal averageOrderAmount) {
		this.averageOrderAmount = averageOrderAmount;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
}