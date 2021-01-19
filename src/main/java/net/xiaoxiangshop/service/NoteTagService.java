package net.xiaoxiangshop.service;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.entity.NoteTag;

import java.util.List;

/**
 * Service - 文章标签
 * 
 */
public interface NoteTagService extends BaseService<NoteTag> {

	/**
	 * 查找文章标签
	 * 
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 文章标签
	 */
	List<NoteTag> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

}