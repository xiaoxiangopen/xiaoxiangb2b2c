package net.xiaoxiangshop.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 店铺分类
 * 
 */
@Entity
public class StoreCategory extends OrderedEntity<StoreCategory> {

	private static final long serialVersionUID = -7932873038066360102L;

	/**
	 * 名称
	 */
	@NotEmpty
	private String name;

	/**
	 * 保证金
	 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	private BigDecimal bail;

	/**
	 * 店铺
	 */
	@TableField(exist = false)
	private Set<Store> stores = new HashSet<>();

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
	 * 获取保证金
	 * 
	 * @return 保证金
	 */
	public BigDecimal getBail() {
		return bail;
	}

	/**
	 * 设置保证金
	 * 
	 * @param bail
	 *            保证金
	 */
	public void setBail(BigDecimal bail) {
		this.bail = bail;
	}

	/**
	 * 获取店铺
	 * 
	 * @return 店铺
	 */
	public Set<Store> getStores() {
		return stores;
	}

	/**
	 * 设置店铺
	 * 
	 * @param stores
	 *            店铺
	 */
	public void setStores(Set<Store> stores) {
		this.stores = stores;
	}

}