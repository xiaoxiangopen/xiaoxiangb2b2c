package net.xiaoxiangshop.service;

import com.baomidou.mybatisplus.extension.service.IService;

import net.xiaoxiangshop.entity.AftersalesReturns;

/**
 * Service - 退货
 * 
 */
public interface AftersalesReturnsService extends IService<AftersalesReturns> {

	/**
	 * 查找实体对象
	 * 
	 * @param id
	 *            ID
	 * @return 实体对象，若不存在则返回null
	 */
	AftersalesReturns find(Long id);
	
	/**
	 * 保存实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	boolean save(AftersalesReturns aftersalesReturns);
	
}