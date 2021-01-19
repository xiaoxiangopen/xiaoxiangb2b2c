package net.xiaoxiangshop.dao;

import org.apache.ibatis.annotations.Param;

import net.xiaoxiangshop.entity.Seo;

/**
 * Dao - SEO设置
 * 
 */
public interface SeoDao extends BaseDao<Seo> {

	/**
	 * 查找SEO设置
	 * 
	 * @param type
	 *            类型
	 * @return SEO设置
	 */
	Seo findByType(@Param("attributeName")String attributeName, @Param("type")Seo.Type type);

}