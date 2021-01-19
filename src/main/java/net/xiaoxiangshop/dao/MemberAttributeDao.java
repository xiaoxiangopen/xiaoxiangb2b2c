package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import net.xiaoxiangshop.entity.MemberAttribute;

/**
 * Dao - 会员注册项
 * 
 */
public interface MemberAttributeDao extends BaseDao<MemberAttribute> {

	/**
	 * 查找未使用的属性序号
	 * 
	 * @return 未使用的属性序号，若不存在则返回null
	 */
	Integer findUnusedPropertyIndex(@Param("index")Integer index);

	/**
	 * 查找会员注册项
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
	List<MemberAttribute> findList(@Param("ew")QueryWrapper<MemberAttribute> queryWrapper, @Param("isEnabled")Boolean isEnabled);

}