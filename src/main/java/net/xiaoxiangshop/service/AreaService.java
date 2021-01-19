package net.xiaoxiangshop.service;

import java.util.List;

import net.xiaoxiangshop.entity.Area;
import net.xiaoxiangshop.entity.ProductCategory;

/**
 * Service - 地区
 * 
 */
public interface AreaService extends BaseService<Area> {

	/**
	 * 根据用户名查找会员
	 * 
	 * @param name
	 *            地区(忽略大小写)
	 * @return 地区，若不存在则返回null
	 */
	Area findByFullName(String name);

	/**
	 * 查找顶级地区
	 * 
	 * @return 顶级地区
	 */
	List<Area> findRoots();

	/**
	 * 查找顶级地区
	 * 
	 * @param count
	 *            数量
	 * @return 顶级地区
	 */
	List<Area> findRoots(Integer count);

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
	List<Area> findParents(Area area, boolean recursive, Integer count);

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
	List<Area> findChildren(Area area, boolean recursive, Integer count);

	/**
	 * 查找商品分类树
	 *
	 * @return 商品分类树
	 */
	List<Area> findTree();

	List<Area> findAreaByParentId(Long parentId);

	Area findByLikeName(String likeName);



}