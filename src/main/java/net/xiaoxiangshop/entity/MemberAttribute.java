package net.xiaoxiangshop.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * Entity - 会员注册项
 * 
 */
@Entity
public class MemberAttribute extends OrderedEntity<MemberAttribute> {

	private static final long serialVersionUID = 4513705276569738136L;

	/**
	 * 最大可选项数量
	 */
	public static final int MAX_OPTION_SIZE = 100;

	/**
	 * 类型
	 */
	public enum Type implements IEnum<Integer> {

		/**
		 * 姓名
		 */
		NAME(0),

		/**
		 * 性别
		 */
		GENDER(1),

		/**
		 * 出生日期
		 */
		BIRTH(2),

		/**
		 * 地区
		 */
		AREA(3),

		/**
		 * 地址
		 */
		ADDRESS(4),

		/**
		 * 邮编
		 */
		ZIP_CODE(5),

		/**
		 * 电话
		 */
		PHONE(6),

		/**
		 * 文本
		 */
		TEXT(7),

		/**
		 * 单选项
		 */
		SELECT(8),

		/**
		 * 多选项
		 */
		CHECKBOX(9);
		
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
	 * 名称
	 */
	@NotEmpty
	private String name;

	/**
	 * 类型
	 */
	@NotNull(groups = Save.class)
	private MemberAttribute.Type type;

	/**
	 * 配比
	 */
	private String pattern;

	/**
	 * 是否启用
	 */
	@NotNull
	private Boolean isEnabled;

	/**
	 * 是否必填
	 */
	@NotNull
	private Boolean isRequired;

	/**
	 * 属性序号
	 */
	private Integer propertyIndex;

	/**
	 * 可选项
	 */
	@Size(max = MAX_OPTION_SIZE)
	private List<String> options = new ArrayList<>();

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
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public MemberAttribute.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(MemberAttribute.Type type) {
		this.type = type;
	}

	/**
	 * 获取配比
	 * 
	 * @return 配比
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * 设置配比
	 * 
	 * @param pattern
	 *            配比
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * 获取是否启用
	 * 
	 * @return 是否启用
	 */
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	/**
	 * 设置是否启用
	 * 
	 * @param isEnabled
	 *            是否启用
	 */
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * 获取是否必填
	 * 
	 * @return 是否必填
	 */
	public Boolean getIsRequired() {
		return isRequired;
	}

	/**
	 * 设置是否必填
	 * 
	 * @param isRequired
	 *            是否必填
	 */
	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	/**
	 * 获取属性序号
	 * 
	 * @return 属性序号
	 */
	public Integer getPropertyIndex() {
		return propertyIndex;
	}

	/**
	 * 设置属性序号
	 * 
	 * @param propertyIndex
	 *            属性序号
	 */
	public void setPropertyIndex(Integer propertyIndex) {
		this.propertyIndex = propertyIndex;
	}

	/**
	 * 获取可选项
	 * 
	 * @return 可选项
	 */
	public List<String> getOptions() {
		return options;
	}

	/**
	 * 设置可选项
	 * 
	 * @param options
	 *            可选项
	 */
	public void setOptions(List<String> options) {
		this.options = options;
	}


}