package net.xiaoxiangshop.service.impl;

import net.xiaoxiangshop.dao.CartItemDao;
import net.xiaoxiangshop.entity.CartItem;
import org.springframework.stereotype.Service;

import net.xiaoxiangshop.entity.OrderItem;
import net.xiaoxiangshop.service.OrderItemService;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Service - 订单项
 * 
 */
@Service
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem> implements OrderItemService {

    @Inject
    private CartItemDao cartItemDao;
    @Override
    public List<CartItem>  findItemList(Date beginDate, Date endDate,Long MemberId, Long skuId) {
        return cartItemDao.findItemList(beginDate,endDate,MemberId,skuId);

    }
}