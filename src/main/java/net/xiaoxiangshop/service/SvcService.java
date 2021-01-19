package net.xiaoxiangshop.service;

import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StoreRank;
import net.xiaoxiangshop.entity.Svc;

/**
 * Service - 服务
 * 
 */
public interface SvcService extends BaseService<Svc> {

	/**
	 * 根据编号查找服务
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 服务，若不存在则返回null
	 */
	Svc findBySn(String sn);

	/**
	 * 查找最新服务
	 * 
	 * @param store
	 *            店铺
	 * @param promotionPluginId
	 *            促销插件Id
	 * @param storeRank
	 *            店铺等级
	 * @return 最新服务
	 */
	Svc findTheLatest(Store store, String promotionPluginId, StoreRank storeRank);

}