package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.xiaoxiangshop.entity.Area;

/**
 * Dao - 地区
 * 
 */
public interface AreaDao extends BaseDao<Area> {

	/**
	 * 查找顶级地区
	 * 
	 * @param count
	 *            数量
	 * @return 顶级地区
	 */
	List<Area> findRoots(@Param("count")Integer count);

	/**
	 * 查找上级地区
	 * 
	 * @param area
	 *            地区
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 上级地区
	 */
	List<Area> findParents(@Param("area")Area area, @Param("recursive")boolean recursive, @Param("count")Integer count);

	/**
	 * 查找下级地区
	 * 
	 * @param area
	 *            地区
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 下级地区
	 */
	List<Area> findChildren(@Param("area")Area area, @Param("recursive")boolean recursive, @Param("count")Integer count);

	List<Area> findSet(@Param("attributeValue")Long attributeValue);

	Area findByLikeName(@Param("likeName")String likeName);

}