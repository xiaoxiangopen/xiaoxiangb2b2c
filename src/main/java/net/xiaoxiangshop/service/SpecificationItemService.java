package net.xiaoxiangshop.service;

import java.util.List;

import net.xiaoxiangshop.entity.SpecificationItem;

/**
 * Service - 规格项
 * 
 */
public interface SpecificationItemService {

	/**
	 * 规格项过滤
	 * 
	 * @param specificationItems
	 *            规格项
	 */
	void filter(List<SpecificationItem> specificationItems);

}