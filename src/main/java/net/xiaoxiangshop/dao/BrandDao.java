package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import net.xiaoxiangshop.entity.Brand;
import net.xiaoxiangshop.entity.ProductCategory;

/**
 * Dao - 品牌
 * 
 */
public interface BrandDao extends BaseDao<Brand> {

	/**
	 * 查找品牌
	 * 
	 * @param productCategory
	 *            商品分类
	 * @return 品牌
	 */
	List<Brand> findList(@Param("ew")QueryWrapper<Brand> queryWrapper, @Param("productCategory")ProductCategory productCategory);

}