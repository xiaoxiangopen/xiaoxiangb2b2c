package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StoreProductTag;

/**
 * Dao - 店铺商品标签
 * 
 */
public interface StoreProductTagDao extends BaseDao<StoreProductTag> {

	/**
	 * 查找店铺商品标签
	 * 
	 * @param store
	 *            店铺
	 * @param isEnabled
	 *            是否启用
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 店铺商品标签
	 */
	List<StoreProductTag> findList(@Param("ew")QueryWrapper<StoreProductTag> queryWrapper, @Param("store")Store store, @Param("isEnabled")Boolean isEnabled);

	/**
	 * 查找店铺商品标签分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 店铺商品标签分页
	 */
	List<StoreProductTag> findPage(IPage<StoreProductTag> iPage, @Param("ew")QueryWrapper<StoreProductTag> queryWrapper, Store store);

}