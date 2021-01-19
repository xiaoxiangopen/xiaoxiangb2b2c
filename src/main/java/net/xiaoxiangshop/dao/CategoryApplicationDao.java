package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.CategoryApplication;
import net.xiaoxiangshop.entity.ProductCategory;
import net.xiaoxiangshop.entity.Store;

/**
 * Dao - 经营分类申请
 * 
 */
public interface CategoryApplicationDao extends BaseDao<CategoryApplication> {

	/**
	 * 查找经营分类申请
	 * 
	 * @param store
	 *            店铺
	 * @param productCategory
	 *            经营分类
	 * @param status
	 *            状态
	 * @return 经营分类申请
	 */
	List<CategoryApplication> findList(@Param("store")Store store, @Param("productCategory")ProductCategory productCategory, @Param("status")CategoryApplication.Status status);


	/**
	 * 查找经营分类申请分页
	 * 
	 * @param status
	 *            状态
	 * @param store
	 *            店铺
	 * @param productCategory
	 *            经营分类
	 * @param pageable
	 *            分页
	 * @return 经营分类申请分页
	 */
	List<CategoryApplication> findPage(IPage<CategoryApplication> iPage, @Param("ew")QueryWrapper<CategoryApplication> queryWrapper, @Param("status")CategoryApplication.Status status, @Param("store")Store store, @Param("productCategory")ProductCategory productCategory);

	/**
	 * 查找经营分类申请数量
	 * 
	 * @param status
	 *            状态
	 * @param store
	 *            店铺
	 * @param productCategory
	 *            经营分类
	 * @return 经营分类申请数量
	 */
	Long count(@Param("status")CategoryApplication.Status status, @Param("store")Store store, @Param("productCategory")ProductCategory productCategory);

}