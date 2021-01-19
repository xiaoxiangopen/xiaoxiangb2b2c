package net.xiaoxiangshop.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 导航组
 * 
 */
@Entity
public class NavigationGroup extends BaseEntity<NavigationGroup> {

	private static final long serialVersionUID = -7911500541698399102L;

	/**
	 * 名称
	 */
	@NotEmpty
	private String name;

	/**
	 * 导航
	 */
	@TableField(exist = false)
	private Set<Navigation> navigations = new HashSet<>();

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
	 * 获取导航
	 * 
	 * @return 导航
	 */
	public Set<Navigation> getNavigations() {
		return navigations;
	}

	/**
	 * 设置导航
	 * 
	 * @param navigations
	 *            导航
	 */
	public void setNavigations(Set<Navigation> navigations) {
		this.navigations = navigations;
	}

}