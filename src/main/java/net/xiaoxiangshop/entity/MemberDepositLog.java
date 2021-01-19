package net.xiaoxiangshop.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 会员预存款记录
 * 
 */
@Entity
public class MemberDepositLog extends BaseEntity<MemberDepositLog> {

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
		 * 订单退款
		 */
		ORDER_REFUNDS(3),

		/**
		 * 分销提成
		 */
		DISTRIBUTION_COMMISSION(4),

		/**
		 * 分销提现
		 */
		DISTRIBUTION_CASH(5),

		//充值卡充值
		CHARGE_CARD(6);

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
	private MemberDepositLog.Type type;

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
	 * 会员
	 */
	@TableField(exist = false)
	private Member member;


	//卡号
	@JsonView(BaseView.class)
	private  String cardNo;
	@JsonView(BaseView.class)
	private Long orders;
	/**
	 * 订单
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private Order order;

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Long getOrders() {
		return orders;
	}

	public void setOrders(Long orders) {
		this.orders = orders;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public MemberDepositLog.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(MemberDepositLog.Type type) {
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
	 * 获取会员
	 * 
	 * @return 会员
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * 设置会员
	 * 
	 * @param member
	 *            会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}