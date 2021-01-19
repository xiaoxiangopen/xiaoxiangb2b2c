package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import net.xiaoxiangshop.entity.Attribute;
import net.xiaoxiangshop.entity.ProductCategory;

/**
 * Dao - 属性
 * 
 */
public interface AttributeDao extends BaseDao<Attribute> {

	/**
	 * 查找未使用的属性序号
	 * 
	 * @param productCategory
	 *            商品分类
	 * @return 未使用的属性序号，若不存在则返回null
	 */
	Integer findUnusedPropertyIndex(@Param("productCategory")ProductCategory productCategory, @Param("index")Integer index);

	/**
	 * 查找属性
	 * 
	 * @param productCategory
	 *            商品分类
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 属性
	 */
	List<Attribute> findList(@Param("ew")QueryWrapper<Attribute> queryWrapper, @Param("productCategory")ProductCategory productCategory);

}