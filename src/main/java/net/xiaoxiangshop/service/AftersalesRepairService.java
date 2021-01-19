package net.xiaoxiangshop.service;

import com.baomidou.mybatisplus.extension.service.IService;

import net.xiaoxiangshop.entity.AftersalesRepair;

/**
 * Service - 维修
 * 
 */
public interface AftersalesRepairService extends IService<AftersalesRepair> {

	/**
	 * 查找实体对象
	 * 
	 * @param id
	 *            ID
	 * @return 实体对象，若不存在则返回null
	 */
	AftersalesRepair find(Long id);
	
	/**
	 * 保存实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	boolean save(AftersalesRepair aftersalesRepair);
	
}