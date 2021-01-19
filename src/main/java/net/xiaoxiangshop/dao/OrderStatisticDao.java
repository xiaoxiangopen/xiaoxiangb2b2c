package net.xiaoxiangshop.dao;

import net.xiaoxiangshop.entity.OrderStatistic;
import net.xiaoxiangshop.entity.Statistic;
import net.xiaoxiangshop.entity.Store;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Dao -订单统计
 * 
 */
public interface OrderStatisticDao extends BaseDao<OrderStatistic> {

   List<OrderStatistic> findList(@Param("store")Store store, @Param("beginDate") String beginDate, @Param("endDate")String endDate);
}