package net.xiaoxiangshop.service;

import java.util.List;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.entity.MemberAttribute;

/**
 * Service - 会员注册项
 * 
 */
public interface MemberAttributeService extends BaseService<MemberAttribute> {

	/**
	 * 查找未使用的对象属性序号
	 * 
	 * @return 未使用的对象属性序号，若无可用序号则返回null
	 */
	Integer findUnusedPropertyIndex();

	/**
	 * 查找会员注册项
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 会员注册项
	 */
	List<MemberAttribute> findList(Boolean isEnabled, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找会员注册项
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 会员注册项
	 */
	List<MemberAttribute> findList(Boolean isEnabled, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	/**
	 * 查找会员注册项
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param useCache
	 *            是否使用缓存
	 * @return 会员注册项
	 */
	List<MemberAttribute> findList(Boolean isEnabled, boolean useCache);

	/**
	 * 会员注册项值验证
	 * 
	 * @param memberAttribute
	 *            会员注册项
	 * @param values
	 *            值
	 * @return 是否验证通过
	 */
	boolean isValid(MemberAttribute memberAttribute, String[] values);

	/**
	 * 转换为会员注册项值
	 * 
	 * @param memberAttribute
	 *            会员注册项
	 * @param values
	 *            值
	 * @return 会员注册项值
	 */
	Object toMemberAttributeValue(MemberAttribute memberAttribute, String[] values);

}