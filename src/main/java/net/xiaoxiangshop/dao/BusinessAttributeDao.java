package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import net.xiaoxiangshop.entity.BusinessAttribute;

/**
 * Dao - 商家注册项
 * 
 */
public interface BusinessAttributeDao extends BaseDao<BusinessAttribute> {

	/**
	 * 查找未使用的对象属性序号
	 * 
	 * @return 未使用的对象属性序号，若无可用序号则返回null
	 */
	Integer findUnusedPropertyIndex(@Param("index")Integer index);

	/**
	 * 查找商家注册项
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 会员注册项
	 */
	List<BusinessAttribute> findList(@Param("ew")QueryWrapper<BusinessAttribute> queryWrapper, @Param("isEnabled")Boolean isEnabled);

	/**
	 * 清空商家注册项值
	 * 
	 * @param businessAttribute
	 *            商家注册项
	 */
	void clearAttributeValue(@Param("propertyName")String propertyName);

}