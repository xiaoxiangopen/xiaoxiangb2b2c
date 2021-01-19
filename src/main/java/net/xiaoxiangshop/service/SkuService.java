package net.xiaoxiangshop.service;

import java.util.List;
import java.util.Set;

import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.entity.Sku;
import net.xiaoxiangshop.entity.StockLog;
import net.xiaoxiangshop.entity.Store;

/**
 * Service - SKU
 * 
 */
public interface SkuService extends BaseService<Sku> {

	/**
	 * 判断编号是否存在
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 编号是否存在
	 */
	boolean snExists(String sn);

	/**
	 * 根据编号查找SKU
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return SKU，若不存在则返回null
	 */
	Sku findBySn(String sn);

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
	List<Sku> search(Store store, Product.Type type, String keyword, Set<Sku> excludes, Integer count);

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
	List<Sku> findList(Store store, Product.Type type, Set<Sku> matchs, Integer count);

	/**
	 * 增加库存
	 * 
	 * @param sku
	 *            SKU
	 * @param amount
	 *            值
	 * @param type
	 *            类型
	 * @param memo
	 *            备注
	 */
	void addStock(Sku sku, int amount, StockLog.Type type, String memo);

	/**
	 * 增加已分配库存
	 * 
	 * @param sku
	 *            SKU
	 * @param amount
	 *            值
	 */
	void addAllocatedStock(Sku sku, int amount);

	/**
	 * SKU过滤
	 * 
	 * @param skus
	 *            SKU
	 */
	void filter(List<Sku> skus);

}