package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.entity.Navigation;
import net.xiaoxiangshop.entity.NavigationGroup;

/**
 * Dao - 导航
 * 
 */
public interface NavigationDao extends BaseDao<Navigation> {

	/**
	 * 查找导航
	 * 
	 * @param navigationGroup
	 *            导航组
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 导航
	 */
	List<Navigation> findList(@Param("navigationGroup")NavigationGroup navigationGroup, Integer count, List<Filter> filters, List<Order> orders);

}