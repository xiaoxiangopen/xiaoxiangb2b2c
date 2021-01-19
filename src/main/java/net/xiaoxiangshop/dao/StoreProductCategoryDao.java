package net.xiaoxiangshop.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StoreProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Dao - 店铺商品分类
 * 
 */
public interface StoreProductCategoryDao extends BaseDao<StoreProductCategory> {

	/**
	 * 查找顶级店铺商品分类
	 * 
	 * @param store
	 *            店铺
	 * @param count
	 *            数量
	 * @return 顶级店铺商品分类
	 */
	List<StoreProductCategory> findRoots(@Param("store")Store store, @Param("count")Integer count);

	/**
	 * 查找上级店铺商品分类
	 * 
	 * @param storeProductCategory
	 *            店铺商品分类
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 上级店铺商品分类
	 */
	List<StoreProductCategory> findParents(@Param("storeProductCategory")StoreProductCategory storeProductCategory, @Param("recursive")boolean recursive, @Param("count")Integer count);

	/**
	 * 查找下级店铺商品分类
	 * 
	 * @param storeProductCategory
	 *            店铺商品分类
	 * @param store
	 *            店铺
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 下级店铺商品分类
	 */
	List<StoreProductCategory> findChildren(@Param("storeProductCategory")StoreProductCategory storeProductCategory, @Param("store")Store store, @Param("recursive")boolean recursive, @Param("count")Integer count);

	/**
	 * 查找店铺商品分类
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 店铺商品分类
	 */
	List<StoreProductCategory> findPage(IPage<StoreProductCategory> iPage, @Param("ew")QueryWrapper<StoreProductCategory> queryWrapper, @Param("store")Store store);
}