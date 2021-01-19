package net.xiaoxiangshop.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * Entity - 退货
 * 
 */
@Entity
public class AftersalesReturns extends Aftersales {

	private static final long serialVersionUID = -1169192297186515973L;

	/**
	 * 退款方式
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
		DEPOSIT(2);
		
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
	 * 退款方式
	 */
	@NotNull
	private AftersalesReturns.Method method;

	/**
	 * 收款银行
	 */
	@Length(max = 200)
	private String bank;

	/**
	 * 收款账号
	 */
	@Length(max = 200)
	private String account;

	/**
	 * 获取退款方式
	 * 
	 * @return 退款方式
	 */
	public AftersalesReturns.Method getMethod() {
		return method;
	}

	/**
	 * 设置退款方式
	 * 
	 * @param method
	 *            退款方式
	 */
	public void setMethod(AftersalesReturns.Method method) {
		this.method = method;
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

}