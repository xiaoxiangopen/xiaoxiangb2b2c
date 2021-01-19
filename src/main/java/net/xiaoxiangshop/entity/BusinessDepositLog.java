package net.xiaoxiangshop.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 商家预存款记录
 * 
 */
@Entity
public class BusinessDepositLog extends BaseEntity<BusinessDepositLog> {

	private static final long serialVersionUID = -8323452873046981882L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum<Integer> {

		/**
		 * 预存款充值
		 */
		RECHARGE(0),

		/**
		 * 预存款调整
		 */
		ADJUSTMENT(1),

		/**
		 * 订单支付
		 */
		ORDER_PAYMENT(2),

		/**
		 * 服务支付
		 */
		SVC_PAYMENT(3),

		/**
		 * 订单退款
		 */
		ORDER_REFUNDS(4),

		/**
		 * 订单结算
		 */
		ORDER_SETTLEMENT(5),

		/**
		 * 提现
		 */
		CASH(6);
		
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
	@JsonView(BaseView.class)
	@EnumValue
	private BusinessDepositLog.Type type;

	/**
	 * 收入金额
	 */
	@JsonView(BaseView.class)
	private BigDecimal credit;

	/**
	 * 支出金额
	 */
	@JsonView(BaseView.class)
	private BigDecimal debit;

	/**
	 * 当前余额
	 */
	@JsonView(BaseView.class)
	private BigDecimal balance;

	/**
	 * 备注
	 */
	@JsonView(BaseView.class)
	private String memo;

	/**
	 * 商家
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Business business;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public BusinessDepositLog.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(BusinessDepositLog.Type type) {
		this.type = type;
	}

	/**
	 * 获取收入金额
	 * 
	 * @return 收入金额
	 */
	public BigDecimal getCredit() {
		return credit;
	}

	/**
	 * 设置收入金额
	 * 
	 * @param credit
	 *            收入金额
	 */
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	/**
	 * 获取支出金额
	 * 
	 * @return 支出金额
	 */
	public BigDecimal getDebit() {
		return debit;
	}

	/**
	 * 设置支出金额
	 * 
	 * @param debit
	 *            支出金额
	 */
	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	/**
	 * 获取当前余额
	 * 
	 * @return 当前余额
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * 设置当前余额
	 * 
	 * @param balance
	 *            当前余额
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
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
	 * 获取商家
	 * 
	 * @return 商家
	 */
	public Business getBusiness() {
		return business;
	}

	/**
	 * 设置商家
	 * 
	 * @param business
	 *            商家
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}

}