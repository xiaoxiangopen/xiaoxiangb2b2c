package net.xiaoxiangshop.service;

import net.xiaoxiangshop.entity.AftersalesSetting;
import net.xiaoxiangshop.entity.Store;

/**
 * Service - 售后设置
 * 
 */
public interface AftersalesSettingService extends BaseService<AftersalesSetting> {

	/**
	 * 通过店铺查找售后设置
	 * 
	 * @param store
	 *            店铺
	 * @return 售后设置
	 */
	AftersalesSetting findByStore(Store store);

}