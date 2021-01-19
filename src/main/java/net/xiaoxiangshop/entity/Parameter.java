package net.xiaoxiangshop.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 参数
 * 
 */
@Entity
public class Parameter extends OrderedEntity<Parameter> {

	private static final long serialVersionUID = -6159626519016913987L;

	/**
	 * 最大参数名称数量
	 */
	public static final int MAX_NAME_SIZE = 100;

	/**
	 * 参数组
	 */
	@NotEmpty
	@TableField(value = "parameter_group")
	private String group;

	/**
	 * 绑定分类
	 */
	@NotNull(groups = Save.class)
	@TableField(exist = false)
	private ProductCategory productCategory;

	/**
	 * 参数名称
	 */
	@NotEmpty
	@TableField(exist = false)
	private List<String> names = new ArrayList<>();

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
	 * 获取绑定分类
	 * 
	 * @return 绑定分类
	 */
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	/**
	 * 设置绑定分类
	 * 
	 * @param productCategory
	 *            绑定分类
	 */
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	/**
	 * 获取参数名称
	 * 
	 * @return 参数名称
	 */
	public List<String> getNames() {
		return names;
	}

	/**
	 * 设置参数名称
	 * 
	 * @param names
	 *            参数名称
	 */
	public void setNames(List<String> names) {
		this.names = names;
	}

}