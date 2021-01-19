package net.xiaoxiangshop.service;

import java.util.List;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.entity.DeliveryCenter;
import net.xiaoxiangshop.entity.DeliveryTemplate;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.entity.Store;

/**
 * Service - 快递单模板
 * 
 */
public interface DeliveryTemplateService extends BaseService<DeliveryTemplate> {

	/**
	 * 查找默认快递单模板
	 * 
	 * @param store
	 *            店铺
	 * @return 默认快递单模板，若不存在则返回null
	 */
	DeliveryTemplate findDefault(Store store);

	/**
	 * 查找快递单模板
	 * 
	 * @param store
	 *            店铺
	 * @return 快递单模板
	 */
	List<DeliveryTemplate> findList(Store store);

	/**
	 * 查找快递单模板分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页信息
	 * @return 快递单模板分页
	 */
	Page<DeliveryTemplate> findPage(Store store, Pageable pageable);

	/**
	 * 解析内容
	 * 
	 * @param deliveryTemplate
	 *            快递单模板
	 * @param store
	 *            店铺
	 * @param deliveryCenter
	 *            发货点
	 * @param order
	 *            订单
	 * @return 内容
	 */
	String resolveContent(DeliveryTemplate deliveryTemplate, Store store, DeliveryCenter deliveryCenter, Order order);

}