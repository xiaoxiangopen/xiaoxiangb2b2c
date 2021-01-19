package net.xiaoxiangshop.service.impl;

import net.xiaoxiangshop.dao.CartItemDao;
import org.springframework.stereotype.Service;

import net.xiaoxiangshop.entity.CartItem;
import net.xiaoxiangshop.service.CartItemService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Service - 购物车项
 * 
 */
@Service
public class CartItemServiceImpl extends BaseServiceImpl<CartItem> implements CartItemService {

    @Inject
    private CartItemDao cartItemDao;
    @Override
    public Set<CartItem> fingItemByCartId(Long cartId) {
        return cartItemDao.findSet("cart_id",cartId);
    }

    @Override
    public CartItem fingItemBySkuId(Long skuId,Long cartId) {
        return cartItemDao.fingItemBySkuId(skuId,cartId);
    }
}