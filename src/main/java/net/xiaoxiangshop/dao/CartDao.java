package net.xiaoxiangshop.dao;

import net.xiaoxiangshop.entity.Cart;
import net.xiaoxiangshop.entity.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Dao - 购物车
 * 
 */
public interface CartDao extends BaseDao<Cart> {

	/**
	 * 删除过期购物车
	 */
	void deleteExpired();

    List<Cart> findCartByUserId(@Param("member")Member member);
}