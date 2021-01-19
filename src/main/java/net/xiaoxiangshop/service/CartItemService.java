package net.xiaoxiangshop.service;

import net.xiaoxiangshop.entity.CartItem;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

/**
 * Service - 购物车项
 * 
 */
public interface CartItemService extends BaseService<CartItem> {

    Set<CartItem> fingItemByCartId(Long  cartId);

    CartItem fingItemBySkuId(Long skuId,Long cartId);
}