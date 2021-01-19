package net.xiaoxiangshop.service;

import java.util.List;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.entity.ProductCategory;
import net.xiaoxiangshop.entity.Store;

/**
 * Service - 商品分类
 * 
 */
public interface ProductCategoryService extends BaseService<ProductCategory> {

	/**
	 * 查找商品分类
	 * 
	 * @param store
	 *            店铺
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 商品分类
	 */
	List<ProductCategory> findList(Store store, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找顶级商品分类
	 * 
	 * @return 顶级商品分类
	 */
	List<ProductCategory> findRoots();

	/**
	 * 查找顶级商品分类
	 * 
	 * @param count
	 *            数量
	 * @return 顶级商品分类
	 */
	List<ProductCategory> findRoots(Integer count);

	/**
	 * 查找顶级商品分类
	 * 
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 顶级商品分类
	 */
	List<ProductCategory> findRoots(Integer count, boolean useCache);

	/**
	 * 查找上级商品分类
	 * 
	 * @param productCategory
	 *            商品分类
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 上级商品分类
	 */
	List<ProductCategory> findParents(ProductCategory productCategory, boolean recursive, Integer count);

	/**
	 * 查找上级商品分类
	 * 
	 * @param productCategoryId
	 *            商品分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 上级商品分类
	 */
	List<ProductCategory> findParents(Long productCategoryId, boolean recursive, Integer count, boolean useCache);

	/**
	 * 查找商品分类树
	 * 
	 * @return 商品分类树
	 */
	List<ProductCategory> findTree();

	/**
	 * 查找下级商品分类
	 * 
	 * @param productCategory
	 *            商品分类
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 下级商品分类
	 */
	List<ProductCategory> findChildren(ProductCategory productCategory, boolean recursive, Integer count);

	/**
	 * 查找下级商品分类
	 * 
	 * @param productCategoryId
	 *            商品分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 下级商品分类
	 */
	List<ProductCategory> findChildren(Long productCategoryId, boolean recursive, Integer count, boolean useCache);

    List<ProductCategory> findCategoryByPtId(Long id);

	void updateState(ProductCategory p);
}