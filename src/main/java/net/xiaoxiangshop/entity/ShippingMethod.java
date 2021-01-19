package net.xiaoxiangshop.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.PreRemove;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.collections.CollectionUtils;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 配送方式
 * 
 */
@Entity
public class ShippingMethod extends OrderedEntity<ShippingMethod> {

	private static final long serialVersionUID = 5873163245980853245L;

	/**
	 * 名称
	 */
	@NotEmpty
	private String name;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 介绍
	 */
	private String description;

	/**
	 * 默认物流公司
	 */
	@TableField(exist = false)
	private DeliveryCorp defaultDeliveryCorp;

	/**
	 * 支持支付方式
	 */
	@TableField(exist = false)
	private Set<PaymentMethod> paymentMethods = new HashSet<>();

	/**
	 * 默认运费配置
	 */
	@TableField(exist = false)
	private Set<DefaultFreightConfig> defaultFreightConfigs = new HashSet<>();

	/**
	 * 地区运费配置
	 */
	@TableField(exist = false)
	private Set<AreaFreightConfig> areaFreightConfigs = new HashSet<>();

	/**
	 * 订单
	 */
	@TableField(exist = false)
	private Set<Order> orders = new HashSet<>();

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
	 * 获取图标
	 * 
	 * @return 图标
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * 设置图标
	 * 
	 * @param icon
	 *            图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * 获取介绍
	 * 
	 * @return 介绍
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置介绍
	 * 
	 * @param description
	 *            介绍
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取默认物流公司
	 * 
	 * @return 默认物流公司
	 */
	public DeliveryCorp getDefaultDeliveryCorp() {
		return defaultDeliveryCorp;
	}

	/**
	 * 设置默认物流公司
	 * 
	 * @param defaultDeliveryCorp
	 *            默认物流公司
	 */
	public void setDefaultDeliveryCorp(DeliveryCorp defaultDeliveryCorp) {
		this.defaultDeliveryCorp = defaultDeliveryCorp;
	}

	/**
	 * 获取支持支付方式
	 * 
	 * @return 支持支付方式
	 */
	public Set<PaymentMethod> getPaymentMethods() {
		return paymentMethods;
	}

	/**
	 * 设置支持支付方式
	 * 
	 * @param paymentMethods
	 *            支持支付方式
	 */
	public void setPaymentMethods(Set<PaymentMethod> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	/**
	 * 获取默认运费配置
	 * 
	 * @return 默认运费配置
	 */
	public Set<DefaultFreightConfig> getDefaultFreightConfigs() {
		return defaultFreightConfigs;
	}

	/**
	 * 设置默认运费配置
	 * 
	 * @param defaultFreightConfigs
	 *            默认运费配置
	 */
	public void setDefaultFreightConfigs(Set<DefaultFreightConfig> defaultFreightConfigs) {
		this.defaultFreightConfigs = defaultFreightConfigs;
	}

	/**
	 * 获取地区运费配置
	 * 
	 * @return 地区运费配置
	 */
	public Set<AreaFreightConfig> getAreaFreightConfigs() {
		return areaFreightConfigs;
	}

	/**
	 * 设置地区运费配置
	 * 
	 * @param areaFreightConfigs
	 *            地区运费配置
	 */
	public void setAreaFreightConfigs(Set<AreaFreightConfig> areaFreightConfigs) {
		this.areaFreightConfigs = areaFreightConfigs;
	}

	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	public Set<Order> getOrders() {
		return orders;
	}

	/**
	 * 设置订单
	 * 
	 * @param orders
	 *            订单
	 */
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 判断是否支持支付方式
	 * 
	 * @param paymentMethod
	 *            支付方式
	 * @return 是否支持支付方式
	 */
	public boolean isSupported(PaymentMethod paymentMethod) {
		return paymentMethod == null || (getPaymentMethods() != null && getPaymentMethods().contains(paymentMethod));
	}

	/**
	 * 获取地区运费配置
	 * 
	 * @param store
	 *            店铺
	 * @param area
	 *            地区
	 * @return 地区运费配置
	 */
	@Transient
	public AreaFreightConfig getAreaFreightConfig(Store store, Area area) {
		if (area == null || store == null || CollectionUtils.isEmpty(getAreaFreightConfigs())) {
			return null;
		}
		for (AreaFreightConfig areaFreightConfig : getAreaFreightConfigs()) {
			if (areaFreightConfig.getArea() != null && store.equals(areaFreightConfig.getStore()) && areaFreightConfig.getArea().equals(area)) {
				return areaFreightConfig;
			}
		}
		return null;
	}

	/**
	 * 获取默认运费配置
	 * 
	 * @param store
	 *            店铺
	 * @return 默认运费配置
	 */
	@Transient
	public DefaultFreightConfig getDefaultFreightConfig(final Store store) {
		DefaultFreightConfig defaultFreightConfig = new DefaultFreightConfig();
		if (store == null || CollectionUtils.isEmpty(getDefaultFreightConfigs())) {
			return defaultFreightConfig;
		}
		for (DefaultFreightConfig pDefaultFreightConfig : getDefaultFreightConfigs()) {
			if (pDefaultFreightConfig.getStore() != null && pDefaultFreightConfig.getStore().equals(store)) {
				return pDefaultFreightConfig;
			}
		}
		return defaultFreightConfig;
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
		Set<Order> orders = getOrders();
		if (orders != null) {
			for (Order order : orders) {
				order.setShippingMethod(null);
			}
		}
	}

}