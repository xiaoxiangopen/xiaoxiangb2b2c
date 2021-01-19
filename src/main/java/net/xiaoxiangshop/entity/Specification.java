package net.xiaoxiangshop.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 规格
 * 
 */
@Entity
public class Specification extends OrderedEntity<Specification> {

	private static final long serialVersionUID = -6346775052811140926L;

	/**
	 * 最大可选项数量
	 */
	public static final int MAX_OPTION_SIZE = 100;

	/**
	 * 名称
	 */
	@NotEmpty
	private String name;

	/**
	 * 绑定分类
	 */
	@NotNull(groups = Save.class)
	@TableField(exist = false)
	private ProductCategory productCategory;

	/**
	 * 可选项
	 */
	@Size(max = MAX_OPTION_SIZE)
	@TableField(exist = false)
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