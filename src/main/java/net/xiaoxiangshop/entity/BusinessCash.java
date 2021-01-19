package net.xiaoxiangshop.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;


/**
 * Entity - 商家提现
 * 
 */
@Entity
public class BusinessCash extends BaseEntity<BusinessCash> {

	private static final long serialVersionUID = -1129619429301847081L;

	/**
	 * 状态
	 */
	public enum Status implements IEnum<Integer> {

		/**
		 * 等待审核
		 */
		PENDING(0),

		/**
		 * 审核通过
		 */
		APPROVED(1),

		/**
		 * 审核失败
		 */
		FAILED(2);
		
		private int value;

		Status(final int value) {
			this.value = value;
		}
		
		@Override
		public Integer getValue() {
			return this.value;
		}
	}

	/**
	 * 状态
	 */
	@NotNull(groups = Save.class)
	@EnumValue
	private BusinessCash.Status status;

	/**
	 * 金额
	 */
	@NotNull
	private BigDecimal amount;

	/**
	 * 收款银行
	 */
	@NotNull
	private String bank;

	/**
	 * 收款账号
	 */
	@NotNull
	private String account;

	/**
	 * 商家
	 */
	private Business business;

	/**
	 * 获取状态
	 * 
	 * @return 状态
	 */
	public BusinessCash.Status getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * 
	 * @param status
	 *            状态
	 */
	public void setStatus(BusinessCash.Status status) {
		this.status = status;
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
	 * 获取收款银行
	 * 
	 * @return 收款银行
	 */
	public String getBank() {
		return bank;
	}

	/**
	 * 设置收款银行
	 * 
	 * @param bank
	 *            收款银行
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}

	/**
	 * 获取收款账号
	 * 
	 * @return 收款账号
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * 设置收款账号
	 * 
	 * @param account
	 *            收款账号
	 */
	public void setAccount(String account) {
		this.account = account;
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