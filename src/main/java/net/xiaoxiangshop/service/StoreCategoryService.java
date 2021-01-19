package net.xiaoxiangshop.service;

import java.util.List;

import net.xiaoxiangshop.entity.StoreCategory;

/**
 * Service - 店铺分类
 * 
 */
public interface StoreCategoryService extends BaseService<StoreCategory> {

	/**
	 * 判断名称是否存在
	 * 
	 * @param name
	 *            名称(忽略大小写)
	 * @return 名称是否存在
	 */
	boolean nameExists(String name);
	
	/**
	 * 查找所有实体对象集合
	 * 
	 * @return 所有实体对象集合
	 */
	List<StoreCategory> findAll();

}