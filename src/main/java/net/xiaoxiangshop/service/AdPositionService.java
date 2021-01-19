package net.xiaoxiangshop.service;

import net.xiaoxiangshop.entity.AdPosition;

/**
 * Service - 广告位
 * 
 */
public interface AdPositionService extends BaseService<AdPosition> {

	/**
	 * 查找广告位
	 * 
	 * @param id
	 *            ID
	 * @param useCache
	 *            是否使用缓存
	 * @return 广告位
	 */
	AdPosition find(Long id, boolean useCache);

}