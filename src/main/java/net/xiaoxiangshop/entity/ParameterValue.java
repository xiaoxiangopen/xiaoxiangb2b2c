package net.xiaoxiangshop.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Entity - 参数值
 * 
 */
public class ParameterValue implements Serializable {

	private static final long serialVersionUID = 1915986624257267840L;

	/**
	 * 最大条目数量
	 */
	public static final int MAX_ENTRY_SIZE = 100;

	/**
	 * 参数组
	 */
	@NotEmpty
	private String group;

	/**
	 * 条目
	 */
	@Size(max = MAX_ENTRY_SIZE)
	@NotEmpty
	private List<ParameterValue.Entry> entries = new ArrayList<>();

	/**
	 * 获取参数组
	 * 
	 * @return 参数组
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * 设置参数组
	 * 
	 * @param group
	 *            参数组
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * 获取条目
	 * 
	 * @return 条目
	 */
	public List<ParameterValue.Entry> getEntries() {
		return entries;
	}

	/**
	 * 设置条目
	 * 
	 * @param entries
	 *            条目
	 */
	public void setEntries(List<ParameterValue.Entry> entries) {
		this.entries = entries;
	}

	/**
	 * 重写equals方法
	 * 
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * Entity - 条目
	 * 
	 */
	public static class Entry implements Serializable {

		private static final long serialVersionUID = 4931882586949537777L;

		/**
		 * 名称
		 */
		@NotEmpty
		private String name;

		/**
		 * 值
		 */
		@NotEmpty
		private String value;

		/**
		 * 获取名称
		 * 
		 * @return 名称
		 */
		public String getName() {
			return name;
		}

		/**
		 * 设置名称
		 * 
		 * @param name
		 *            名称
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * 获取值
		 * 
		 * @return 值
		 */
		public String getValue() {
			return value;
		}

		/**
		 * 设置值
		 * 
		 * @param value
		 *            值
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * 重写equals方法
		 * 
		 * @param obj
		 *            对象
		 * @return 是否相等
		 */
		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}

		/**
		 * 重写hashCode方法
		 * 
		 * @return HashCode
		 */
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}
	}

}