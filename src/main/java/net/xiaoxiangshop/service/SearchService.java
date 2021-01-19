package net.xiaoxiangshop.service;

import com.baomidou.mybatisplus.extension.api.R;
import net.xiaoxiangshop.entity.Product;

/**
 * Service - 搜索
 * 
 */
public interface SearchService {

	/**
	 * 创建索引
	 * 
	 * @param type
	 *            索引类型
	 */
	void index(Class<?> type);

	/**
	 * 创建索引
	 * 
	 * @param type
	 *            索引类型
	 * @param purgeAll
	 *            是否清空已存在索引
	 */
	void index(Class<?> type, boolean purgeAll);

	/**
	 * 创建单一商品索引
	 *
	 * @param product
	 *            商品
	 */
	void add(Product product);

	/**
	 * 删除单一商品索引
	 *
	 * @param productId
	 *            商品ID
	 */
	void del(Long productId);

	/**
	 * 上架单一商品索引
	 *
	 * @param product
	 *            商品
	 */
	void shelves(Product product);

	/**
	 * 下架单一商品索引
	 *
	 * @param product
	 *            商品
	 */
	void shelf(Product product);

}