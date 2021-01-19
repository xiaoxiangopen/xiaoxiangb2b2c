package net.xiaoxiangshop.entity;

import javax.persistence.Entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 积分记录
 * 
 */
@Entity
public class PointLog extends BaseEntity<PointLog> {

	private static final long serialVersionUID = -1758056800285585097L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum<Integer> {

		/**
		 * 积分赠送
		 */
		REWARD(0),

		/**
		 * 积分兑换
		 */
		EXCHANGE(1),

		/**
		 * 积分兑换撤销
		 */
		UNDO_EXCHANGE(2),

		/**
		 * 积分调整
		 */
		ADJUSTMENT(3);
		
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
	private PointLog.Type type;

	/**
	 * 获取积分
	 */
	@JsonView(BaseView.class)
	private Long credit;

	/**
	 * 扣除积分
	 */
	@JsonView(BaseView.class)
	private Long debit;

	/**
	 * 当前积分
	 */
	@JsonView(BaseView.class)
	private Long balance;

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

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public PointLog.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(PointLog.Type type) {
		this.type = type;
	}

	/**
	 * 获取获取积分
	 * 
	 * @return 获取积分
	 */
	public Long getCredit() {
		return credit;
	}

	/**
	 * 设置获取积分
	 * 
	 * @param credit
	 *            获取积分
	 */
	public void setCredit(Long credit) {
		this.credit = credit;
	}

	/**
	 * 获取扣除积分
	 * 
	 * @return 扣除积分
	 */
	public Long getDebit() {
		return debit;
	}

	/**
	 * 设置扣除积分
	 * 
	 * @param debit
	 *            扣除积分
	 */
	public void setDebit(Long debit) {
		this.debit = debit;
	}

	/**
	 * 获取当前积分
	 * 
	 * @return 当前积分
	 */
	public Long getBalance() {
		return balance;
	}

	/**
	 * 设置当前积分
	 * 
	 * @param balance
	 *            当前积分
	 */
	public void setBalance(Long balance) {
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

}