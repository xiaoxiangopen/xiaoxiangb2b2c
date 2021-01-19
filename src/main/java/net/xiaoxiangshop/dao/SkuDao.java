package net.xiaoxiangshop.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.entity.Sku;
import net.xiaoxiangshop.entity.Store;

/**
 * Dao - SKU
 * 
 */
public interface SkuDao extends BaseDao<Sku> {

	/**
	 * 通过编号、名称查找SKU
	 * 
	 * @param store
	 *            店铺
	 * @param type
	 *            类型
	 * @param keyword
	 *            关键词
	 * @param excludes
	 *            排除SKU
	 * @param count
	 *            数量
	 * @return SKU
	 */
	List<Sku> search(@Param("store")Store store, @Param("type")Product.Type type, @Param("keyword")String keyword, @Param("excludes")Set<Sku> excludes, @Param("count")Integer count);

	/**
	 * 查找SKU
	 * 
	 * @param store
	 *            店铺
	 * @param type
	 *            类型
	 * @param matchs
	 *            匹配SKU
	 * @param count
	 *            数量
	 * @return SKU
	 */
	List<Sku> findList(@Param("store")Store store, @Param("type")Product.Type type, @Param("matchs")Set<Sku> matchs, @Param("count")Integer count);

	Set<Sku> findErpSet(@Param("internal_number")String internal_number, @Param("mddm")String mddm);

}