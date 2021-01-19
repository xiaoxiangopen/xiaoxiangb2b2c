package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StoreAdImage;

/**
 * Dao - 店铺广告图片
 * 
 */
public interface StoreAdImageDao extends BaseDao<StoreAdImage> {

	/**
	 * 查找店铺广告图片
	 * 
	 * @param store
	 *            店铺
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 店铺广告图片
	 */
	List<StoreAdImage> findList(@Param("ew")QueryWrapper<StoreAdImage> queryWrapper, @Param("store")Store store);

	/**
	 * 查找店铺广告图片分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页信息
	 * @return 店铺广告图片分页
	 */
	List<StoreAdImage> findPage(IPage<StoreAdImage> iPage, @Param("ew")QueryWrapper<StoreAdImage> queryWrapper, Store store);

}