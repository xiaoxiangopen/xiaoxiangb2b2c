package net.xiaoxiangshop.service;

import net.xiaoxiangshop.entity.CartItem;
import net.xiaoxiangshop.entity.OrderItem;

import java.util.Date;
import java.util.List;

/**
 * Service - 订单项
 * 
 */
public interface OrderItemService extends BaseService<OrderItem> {

    List<CartItem> findItemList(Date beginDate, Date endDate, Long MemberId, Long skuId);
}