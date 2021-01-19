package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StoreRank;
import net.xiaoxiangshop.entity.Svc;

/**
 * Dao - 服务
 * 
 */
public interface SvcDao extends BaseDao<Svc> {

	/**
	 * 查找服务
	 * 
	 * @param store
	 *            店铺
	 * @param promotionPluginId
	 *            促销插件Id
	 * @param storeRank
	 *            店铺等级
	 * @param orders
	 *            排序
	 * @return 服务
	 */
	List<Svc> findByStore(@Param("store")Store store, @Param("promotionPluginId")String promotionPluginId, @Param("storeRank")StoreRank storeRank);

}