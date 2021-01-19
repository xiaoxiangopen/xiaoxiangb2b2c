package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.StockLog;
import net.xiaoxiangshop.entity.Store;

/**
 * Dao - 库存记录
 * 
 */
public interface StockLogDao extends BaseDao<StockLog> {

	/**
	 * 查找库存记录分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 库存记录分页
	 */
	List<StockLog> findPage(IPage<StockLog> iPage, @Param("ew")QueryWrapper<StockLog> queryWrapper, @Param("store")Store store);

}