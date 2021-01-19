package net.xiaoxiangshop.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.PreRemove;
import javax.validation.constraints.NotEmpty;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 物流公司
 * 
 */
@Entity
public class DeliveryCorp extends OrderedEntity<DeliveryCorp> {

	private static final long serialVersionUID = 10595703086045998L;

	/**
	 * 名称
	 */
	@NotEmpty
	private String name;

	/**
	 * 网址
	 */
	private String url;

	/**
	 * 代码
	 */
	private String code;

	/**
	 * 配送方式
	 */
	@TableField(exist = false)
	private Set<ShippingMethod> shippingMethods = new HashSet<>();

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
	 * 获取网址
	 * 
	 * @return 网址
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置网址
	 * 
	 * @param url
	 *            网址
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取代码
	 * 
	 * @return 代码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置代码
	 * 
	 * @param code
	 *            代码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取配送方式
	 * 
	 * @return 配送方式
	 */
	public Set<ShippingMethod> getShippingMethods() {
		return shippingMethods;
	}

	/**
	 * 设置配送方式
	 * 
	 * @param shippingMethods
	 *            配送方式
	 */
	public void setShippingMethods(Set<ShippingMethod> shippingMethods) {
		this.shippingMethods = shippingMethods;
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
		Set<ShippingMethod> shippingMethods = getShippingMethods();
		if (shippingMethods != null) {
			for (ShippingMethod shippingMethod : shippingMethods) {
				shippingMethod.setDefaultDeliveryCorp(null);
			}
		}
	}

}