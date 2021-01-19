package net.xiaoxiangshop.dao;

import net.xiaoxiangshop.entity.CartItem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Dao - 购物车项
 * 
 */
public interface CartItemDao extends BaseDao<CartItem> {

    CartItem fingItemBySkuId(@Param("skuId")Long skuId,@Param("cartId")Long cartId);

    List<CartItem> findItemList(@Param("beginDate")Date beginDate, @Param("endDate")Date endDate,  @Param("memberId")Long memberId,@Param("skuId")Long skuId);
}