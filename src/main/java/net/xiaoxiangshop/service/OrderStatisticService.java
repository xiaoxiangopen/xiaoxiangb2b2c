package net.xiaoxiangshop.service;

import net.xiaoxiangshop.entity.OrderStatistic;
import net.xiaoxiangshop.entity.Statistic;
import net.xiaoxiangshop.entity.Store;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service - 统计
 * 
 */
public interface OrderStatisticService extends BaseService<OrderStatistic> {

	/**
	 * business首页
	 * @param store
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	Map<String,Object> analyze(Store store, Date beginDate, Date endDate);

	/**
	 * 订单来源统计分析
	 * @param store
	 * @param source 订单来源 0:总计  1:PC 2:H5 3:app
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	Map<String,Object> orderSourceDataAnalyze(Store store, Integer source,Date beginDate, Date endDate);


}