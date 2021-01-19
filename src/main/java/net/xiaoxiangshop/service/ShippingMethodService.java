package net.xiaoxiangshop.service;

import java.math.BigDecimal;

import net.xiaoxiangshop.entity.Area;
import net.xiaoxiangshop.entity.Receiver;
import net.xiaoxiangshop.entity.ShippingMethod;
import net.xiaoxiangshop.entity.Store;

/**
 * Service - 配送方式
 * 
 */
public interface ShippingMethodService extends BaseService<ShippingMethod> {

	/**
	 * 计算运费
	 * 
	 * @param shippingMethod
	 *            配送方式
	 * @param store
	 *            店铺
	 * @param area
	 *            地区
	 * @param weight
	 *            重量
	 * @return 运费
	 */
	BigDecimal calculateFreight(ShippingMethod shippingMethod, Store store, Area area, Integer weight);

	/**
	 * 计算运费
	 * 
	 * @param shippingMethod
	 *            配送方式
	 * @param store
	 *            店铺
	 * @param receiver
	 *            收货地址
	 * @param weight
	 *            重量
	 * @return 运费
	 */
	BigDecimal calculateFreight(ShippingMethod shippingMethod, Store store, Receiver receiver, Integer weight);
}