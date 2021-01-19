package net.xiaoxiangshop.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 分销提现
 * 
 */
@Entity
public class DistributionCash extends BaseEntity<DistributionCash> {

	private static final long serialVersionUID = 8900377558490309014L;

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
	@JsonView(BaseView.class)
	@NotNull(groups = Save.class)
	@EnumValue
	private DistributionCash.Status status;

	/**
	 * 金额
	 */
	@JsonView(BaseView.class)
	@NotNull
	private BigDecimal amount;

	/**
	 * 收款银行
	 */
	@JsonView(BaseView.class)
	@NotNull
	private String bank;

	/**
	 * 收款账号
	 */
	@JsonView(BaseView.class)
	@NotNull
	private String account;

	/**
	 * 开户人
	 */
	@JsonView(BaseView.class)
	@NotNull
	private String accountHolder;

	/**
	 * 分销员
	 */
	@TableField(exist = false)
	private Distributor distributor;

	/**
	 * 获取状态
	 * 
	 * @return 状态
	 */
	public DistributionCash.Status getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * 
	 * @param status
	 *            状态
	 */
	public void setStatus(DistributionCash.Status status) {
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
	 * 获取开户人
	 * 
	 * @return 开户人
	 */
	public String getAccountHolder() {
		return accountHolder;
	}

	/**
	 * 设置开户人
	 * 
	 * @param accountHolder
	 *            开户人
	 */
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
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