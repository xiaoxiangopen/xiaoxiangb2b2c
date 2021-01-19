package net.xiaoxiangshop.entity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity - 统计
 * 
 */
@Entity
public class Statistic extends BaseEntity<Statistic> {

	private static final long serialVersionUID = 2022131337300482638L;

	/**
	 * 组
	 */
	public enum Group {

		/**
		 * 注册组
		 */
		REGISTER,

		/**
		 * 订单组
		 */
		ORDER,

		/**
		 * 资金组
		 */
		FUND,

		/**
		 * 商家资金组
		 */
		BUSINESS_FUND,

		/**
		 * 商家订单组
		 */
		BUSINESS_ORDER

	}

	/**
	 * 类型
	 */
	public enum Type {

		/**
		 * 会员注册数
		 */
		REGISTER_MEMBER_COUNT(Statistic.Group.REGISTER),

		/**
		 * 商家注册数
		 */
		REGISTER_BUSINESS_COUNT(Statistic.Group.REGISTER),

		/**
		 * 订单创建数
		 */
		CREATE_ORDER_COUNT(Statistic.Group.ORDER, Statistic.Group.BUSINESS_ORDER),

		/**
		 * 订单完成数
		 */
		COMPLETE_ORDER_COUNT(Statistic.Group.ORDER, Statistic.Group.BUSINESS_ORDER),

		/**
		 * 订单创建金额
		 */
		CREATE_ORDER_AMOUNT(Statistic.Group.ORDER, Statistic.Group.BUSINESS_ORDER),

		/**
		 * 订单完成金额
		 */
		COMPLETE_ORDER_AMOUNT(Statistic.Group.ORDER, Statistic.Group.BUSINESS_ORDER),

		/**
		 * 会员余额
		 */
		MEMBER_BALANCE(Statistic.Group.FUND),

		/**
		 * 会员冻结金额
		 */
		MEMBER_FROZEN_AMOUNT(Statistic.Group.FUND),

		/**
		 * 会员提现金额
		 */
		MEMBER_CASH(Statistic.Group.FUND),

		/**
		 * 新增会员提现金额
		 */
		ADDED_MEMBER_CASH(Statistic.Group.FUND),

		/**
		 * 商家余额
		 */
		BUSINESS_BALANCE(Statistic.Group.FUND),

		/**
		 * 商家冻结金额
		 */
		BUSINESS_FROZEN_AMOUNT(Statistic.Group.FUND),

		/**
		 * 商家提现金额
		 */
		BUSINESS_CASH(Statistic.Group.FUND),

		/**
		 * 新增商家提现金额
		 */
		ADDED_BUSINESS_CASH(Statistic.Group.FUND),

		/**
		 * 保证金
		 */
		BAIL(Statistic.Group.FUND),

		/**
		 * 新增保证金
		 */
		ADDED_BAIL(Statistic.Group.FUND),

		/**
		 * 平台佣金
		 */
		PLATFORM_COMMISSION(Statistic.Group.FUND, Statistic.Group.BUSINESS_FUND),

		/**
		 * 新增平台佣金
		 */
		ADDED_PLATFORM_COMMISSION(Statistic.Group.FUND, Statistic.Group.BUSINESS_FUND),

		/**
		 * 分销佣金
		 */
		DISTRIBUTION_COMMISSION(Statistic.Group.FUND, Statistic.Group.BUSINESS_FUND),

		/**
		 * 新增分销佣金
		 */
		ADDED_DISTRIBUTION_COMMISSION(Statistic.Group.FUND, Statistic.Group.BUSINESS_FUND);

		/**
		 * 组
		 */
		private Statistic.Group[] groups;

		/**
		 * 构造方法
		 * 
		 * @param groups
		 *            组
		 */
		Type(Statistic.Group... groups) {
			this.groups = groups;
		}

		/**
		 * 获取组
		 * 
		 * @return 组
		 */
		public Statistic.Group[] getGroups() {
			return groups;
		}

		/**
		 * 获取类型
		 * 
		 * @param group
		 *            组
		 * @return 类型
		 */
		@SuppressWarnings("unchecked")
		public static List<Statistic.Type> getTypes(final Statistic.Group group) {
			Assert.notNull(group, "[Assertion failed] - group is required; it must not be null");

			return (List<Statistic.Type>) CollectionUtils.select(Arrays.asList(Statistic.Type.values()), new Predicate() {

				@Override
				public boolean evaluate(Object object) {
					Statistic.Type type = (Statistic.Type) object;
					return ArrayUtils.contains(type.getGroups(), group);
				}

			});
		}

	}

	/**
	 * 周期
	 */
	public enum Period {

		/**
		 * 年
		 */
		YEAR,

		/**
		 * 月
		 */
		MONTH,

		/**
		 * 日
		 */
		DAY
	}

	/**
	 * 类型
	 */
	private Statistic.Type type;

	/**
	 * 年
	 */
	private Integer year;

	/**
	 * 月
	 */
	private Integer month;

	/**
	 * 日
	 */
	private Integer day;

	/**
	 * 值
	 */
	private BigDecimal value;

	/**
	 * 店铺
	 */
	@JsonIgnore
	@TableField(exist = false)
	private Store store;

	/**
	 * 构造方法
	 */
	public Statistic() {
	}

	/**
	 * 构造方法
	 * 
	 * @param type
	 *            类型
	 * @param year
	 *            年
	 * @param value
	 *            值
	 */
	public Statistic(Statistic.Type type, Integer year, BigDecimal value) {
		this.type = type;
		this.year = year;
		this.value = value;
	}

	/**
	 * 构造方法
	 * 
	 * @param type
	 *            类型
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param value
	 *            值
	 */
	public Statistic(Statistic.Type type, Integer year, Integer month, BigDecimal value) {
		this.type = type;
		this.year = year;
		this.month = month;
		this.value = value;
	}

	/**
	 * 构造方法
	 * 
	 * @param type
	 *            类型
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @param value
	 *            值
	 */
	public Statistic(Statistic.Type type, Integer year, Integer month, Integer day, BigDecimal value) {
		this.type = type;
		this.year = year;
		this.month = month;
		this.day = day;
		this.value = value;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public Statistic.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Statistic.Type type) {
		this.type = type;
	}

	/**
	 * 获取年
	 * 
	 * @return 年
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * 设置年
	 * 
	 * @param year
	 *            年
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * 获取月
	 * 
	 * @return 月
	 */
	public Integer getMonth() {
		return month;
	}

	/**
	 * 设置月
	 * 
	 * @param month
	 *            月
	 */
	public void setMonth(Integer month) {
		this.month = month;
	}

	/**
	 * 获取日
	 * 
	 * @return 日
	 */
	public Integer getDay() {
		return day;
	}

	/**
	 * 设置日
	 * 
	 * @param day
	 *            日
	 */
	public void setDay(Integer day) {
		this.day = day;
	}

	/**
	 * 获取值
	 * 
	 * @return 值
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * 设置值
	 * 
	 * @param value
	 *            值
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
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
	 * 获取日期
	 * 
	 * @return 日期
	 */
	@Transient
	public Date getDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, getYear() != null ? getYear() : 0);
		calendar.set(Calendar.MONTH, getMonth() != null ? getMonth() : 0);
		calendar.set(Calendar.DAY_OF_MONTH, getDay() != null ? getDay() : 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

}