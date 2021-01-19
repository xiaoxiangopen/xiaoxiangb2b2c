package net.xiaoxiangshop.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * Entity - 订单退款
 * 
 */
@Entity
public class OrderRefunds extends BaseEntity<OrderRefunds> {

	private static final long serialVersionUID = 354885216604823632L;

	/**
	 * 方式
	 */
	public enum Method implements IEnum<Integer> {

		/**
		 * 在线支付
		 */
		ONLINE(0),

		/**
		 * 线下支付
		 */
		OFFLINE(1),

		/**
		 * 预存款支付
		 */
		DEPOSIT(3);
		
		private int value;

		Method(final int value) {
			this.value = value;
		}
		
		@Override
		public Integer getValue() {
			return this.value;
		}
	}

	/**
	 * 编号
	 */
	private String sn;

	/**
	 * 方式
	 */
	@NotNull
	@EnumValue
	private OrderRefunds.Method method;

	/**
	 * 支付方式
	 */
	private String paymentMethod;

	/**
	 * 退款银行
	 */
	@Length(max = 200)
	private String bank;

	/**
	 * 退款账号
	 */
	@Length(max = 200)
	private String account;

	/**
	 * 退款金额
	 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	private BigDecimal amount;

	/**
	 * 收款人
	 */
	@Length(max = 200)
	private String payee;

	/**
	 * 备注
	 */
	@Length(max = 200)
	private String memo;

	/**
	 * 订单
	 */
	@NotNull
	@TableField(exist = false)
	private Order order;

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
	 * 获取方式
	 * 
	 * @return 方式
	 */
	public OrderRefunds.Method getMethod() {
		return method;
	}

	/**
	 * 设置方式
	 * 
	 * @param method
	 *            方式
	 */
	public void setMethod(OrderRefunds.Method method) {
		this.method = method;
	}

	/**
	 * 获取支付方式
	 * 
	 * @return 支付方式
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * 设置支付方式
	 * 
	 * @param paymentMethod
	 *            支付方式
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * 获取退款银行
	 * 
	 * @return 退款银行
	 */
	public String getBank() {
		return bank;
	}

	/**
	 * 设置退款银行
	 * 
	 * @param bank
	 *            退款银行
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}

	/**
	 * 获取退款账号
	 * 
	 * @return 退款账号
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * 设置退款账号
	 * 
	 * @param account
	 *            退款账号
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * 获取退款金额
	 * 
	 * @return 退款金额
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置退款金额
	 * 
	 * @param amount
	 *            退款金额
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获取收款人
	 * 
	 * @return 收款人
	 */
	public String getPayee() {
		return payee;
	}

	/**
	 * 设置收款人
	 * 
	 * @param payee
	 *            收款人
	 */
	public void setPayee(String payee) {
		this.payee = payee;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 * 
	 * @param memo
	 *            备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
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
	 * 设置支付方式
	 * 
	 * @param paymentMethod
	 *            支付方式
	 */
	@Transient
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		setPaymentMethod(paymentMethod != null ? paymentMethod.getName() : null);
	}

	/**
	 * 持久化前处理
	 */
	@PrePersist
	public void prePersist() {
		setSn(StringUtils.lowerCase(getSn()));
	}

}