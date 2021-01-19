package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.ProductNotify;
import net.xiaoxiangshop.entity.Sku;
import net.xiaoxiangshop.entity.Store;

/**
 * Dao - 到货通知
 * 
 */
public interface ProductNotifyDao extends BaseDao<ProductNotify> {

	/**
	 * 判断到货通知是否存在
	 * 
	 * @param sku
	 *            SKU
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return 到货通知是否存在
	 */
	boolean exists(@Param("sku")Sku sku, @Param("email")String email);

	/**
	 * 查找到货通知分页
	 * 
	 * @param store
	 *            店铺
	 * @param member
	 *            会员
	 * @param isMarketable
	 *            是否上架
	 * @param isOutOfStock
	 *            SKU是否缺货
	 * @param hasSent
	 *            是否已发送.
	 * @param pageable
	 *            分页信息
	 * @return 到货通知分页
	 */
	List<ProductNotify> findPage(IPage<ProductNotify> iPage, @Param("ew")QueryWrapper<ProductNotify> queryWrapper, @Param("store")Store store, @Param("member")Member member, @Param("isMarketable")Boolean isMarketable, @Param("isOutOfStock")Boolean isOutOfStock, @Param("hasSent")Boolean hasSent);

	/**
	 * 查找到货通知数量
	 * 
	 * @param member
	 *            会员
	 * @param isMarketable
	 *            是否上架
	 * @param isOutOfStock
	 *            SKU是否缺货
	 * @param hasSent
	 *            是否已发送.
	 * @return 到货通知数量
	 */
	Long count(@Param("member")Member member, @Param("isMarketable")Boolean isMarketable, @Param("isOutOfStock")Boolean isOutOfStock, @Param("hasSent")Boolean hasSent);

}