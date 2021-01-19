package net.xiaoxiangshop.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * Dao - 基类
 * 
 */
public interface BaseDao<T> extends BaseMapper<T> {
	
	/**
	 * 查找所有实体对象集合
	 * 
	 * @return 所有实体对象集合
	 */
	List<T> findAll();
	
	/**
	 * 查找实体对象集合
	 * 
	 * @param attributeName
	 *            属性名称
	 * @param attributeValue
	 *            属性值
	 * @return 实体对象，若不存在则返回null
	 */
	Set<T> findSet(@Param("attributeName")String attributeName, @Param("attributeValue")Object attributeValue);
	
	/**
	 * 判断是否存在
	 * 
	 * @param attributeName
	 *            属性名称
	 * @param attributeValue
	 *            属性值
	 * @return 是否存在
	 */
	boolean exists(@Param("attributeName")String attributeName, @Param("attributeValue")Object attributeValue);

	/**
	 * 判断是否唯一
	 * 
	 * @param id
	 *            ID
	 * @param attributeName
	 *            属性名称
	 * @param attributeValue
	 *            属性值
	 * @return 是否唯一
	 */
	boolean unique(@Param("id")Long id, @Param("attributeName")String attributeName,  @Param("attributeValue")Object attributeValue);

	/**
	 * 查找实体对象
	 * 
	 * @param id
	 *            ID
	 * @return 实体对象，若不存在则返回null
	 */
	T find(@Param("id")Long id);

	/**
	 * 查找实体对象
	 * 
	 * @param attributeName
	 *            属性名称
	 * @param attributeValue
	 *            属性值
	 * @return 实体对象，若不存在则返回null
	 */
	T findByAttribute(@Param("attributeName")String attributeName, @Param("attributeValue")Object attributeValue);

	/**
	 * 持久化实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	boolean save(@Param("entity")T entity);
	
	/**
	 * 批量插入
	 * 
	 * @param entitys
	 * @return
	 */
	int saveBatch(@Param("entitys") List<T> entitys);
	
	/**
	 * 更新实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	boolean update(@Param("entity")T entity);

	/**
	 * 删除实体对象
	 * 
	 * @param id
	 *            ID
	 */
	void delete(@Param("ids")List<Long> ids);
	
	/**
	 * 移除实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	void remove(@Param("entity")T entity);

}