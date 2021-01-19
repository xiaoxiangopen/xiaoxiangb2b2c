package net.xiaoxiangshop.service;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.entity.StockLog;
import net.xiaoxiangshop.entity.Store;

/**
 * Service - 库存记录
 * 
 */
public interface StockLogService extends BaseService<StockLog> {

	/**
	 * 查找库存记录分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 库存记录分页
	 */
	Page<StockLog> findPage(Store store, Pageable pageable);

}