package net.xiaoxiangshop.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 服务
 * 
 */
@Entity
public abstract class Svc extends BaseEntity<Svc> {

	private static final long serialVersionUID = -7367901462418664073L;

	/**
	 * 编号
	 */
	private String sn;

	/**
	 * 金额
	 */
	private BigDecimal amount;

	/**
	 * 有效天数
	 */
	private Integer durationDays;

	/**
	 * 店铺
	 */
	@TableField(exist = false)
	private Store store;

	/**
	 * 支付事务
	 */
	@TableField(exist = false)
	private Set<PaymentTransaction> paymentTransactions = new HashSet<>();

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
	 * 获取有效天数
	 * 
	 * @return 有效天数
	 */
	public Integer getDurationDays() {
		return durationDays;
	}

	/**
	 * 设置有效天数
	 * 
	 * @param durationDays
	 *            有效天数
	 */
	public void setDurationDays(Integer durationDays) {
		this.durationDays = durationDays;
	}

	/**
	 * 获取店铺
	 * 
	 * @return 店铺
	 */
	public Store getStore() {
		return store;
	}

	/**
	 * 设置店铺
	 * 
	 * @param store
	 *            店铺
	 */
	public void setStore(Store store) {
		this.store = store;
	}

	/**
	 * 获取支付事务
	 * 
	 * @return 支付事务
	 */
	public Set<PaymentTransaction> getPaymentTransactions() {
		return paymentTransactions;
	}

	/**
	 * 设置支付事务
	 * 
	 * @param paymentTransactions
	 *            支付事务
	 */
	public void setPaymentTransactions(Set<PaymentTransaction> paymentTransactions) {
		this.paymentTransactions = paymentTransactions;
	}

}