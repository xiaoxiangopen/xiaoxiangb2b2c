package net.xiaoxiangshop.template.directive;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.util.FreeMarkerUtils;

/**
 * 模板指令 - 基类
 * 
 */
public abstract class BaseDirective implements TemplateDirectiveModel {

	/**
	 * "是否使用缓存"参数名称
	 */
	private static final String USE_CACHE_PARAMETER_NAME = "useCache";

	/**
	 * "ID"参数名称
	 */
	private static final String ID_PARAMETER_NAME = "id";

	/**
	 * "数量"参数名称
	 */
	private static final String COUNT_PARAMETER_NAME = "count";

	/**
	 * "排序"参数名称
	 */
	private static final String ORDER_BY_PARAMETER_NAME = "orderBy";

	/**
	 * 排序项分隔符
	 */
	private static final String ORDER_BY_ITEM_SEPARATOR = "\\s*,\\s*";

	/**
	 * 排序字段分隔符
	 */
	private static final String ORDER_BY_FIELD_SEPARATOR = "\\s+";

	/**
	 * 是否使用缓存
	 * 
	 * @param params
	 *            参数
	 * @return 使用缓存
	 */
	protected boolean useCache(Map<String, TemplateModel> params) throws TemplateModelException {
		Boolean useCache = FreeMarkerUtils.getParameter(USE_CACHE_PARAMETER_NAME, Boolean.class, params);
		return useCache != null ? useCache : true;
	}

	/**
	 * 获取ID
	 * 
	 * @param params
	 *            参数
	 * @return ID
	 */
	protected Long getId(Map<String, TemplateModel> params) throws TemplateModelException {
		return FreeMarkerUtils.getParameter(ID_PARAMETER_NAME, Long.class, params);
	}

	/**
	 * 获取数量
	 * 
	 * @param params
	 *            参数
	 * @return 数量
	 */
	protected Integer getCount(Map<String, TemplateModel> params) throws TemplateModelException {
		return FreeMarkerUtils.getParameter(COUNT_PARAMETER_NAME, Integer.class, params);
	}

	/**
	 * 获取筛选
	 * 
	 * @param params
	 *            参数
	 * @param type
	 *            参数类型
	 * @param ignoreProperties
	 *            忽略属性
	 * @return 筛选
	 */
	protected List<Filter> getFilters(Map<String, TemplateModel> params, Class<?> type, String... ignoreProperties) throws TemplateModelException {
		List<Filter> filters = new ArrayList<>();
		PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(type);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			String propertyName = propertyDescriptor.getName();
			Class<?> propertyType = propertyDescriptor.getPropertyType();
			if (!ArrayUtils.contains(ignoreProperties, propertyName) && params.containsKey(propertyName)) {
				Object value = FreeMarkerUtils.getParameter(propertyName, propertyType, params);
				filters.add(Filter.eq(propertyName, value));
			}
		}
		return filters;
	}

	/**
	 * 获取排序
	 * 
	 * @param params
	 *            参数
	 * @param ignoreProperties
	 *            忽略属性
	 * @return 排序
	 */
	protected List<Order> getOrders(Map<String, TemplateModel> params, String... ignoreProperties) throws TemplateModelException {
		String orderBy = StringUtils.trim(FreeMarkerUtils.getParameter(ORDER_BY_PARAMETER_NAME, String.class, params));
		List<Order> orders = new ArrayList<>();
		if (StringUtils.isNotEmpty(orderBy)) {
			String[] orderByItems = orderBy.split(ORDER_BY_ITEM_SEPARATOR);
			for (String orderByItem : orderByItems) {
				if (StringUtils.isNotEmpty(orderByItem)) {
					String property = null;
					Order.Direction direction = null;
					String[] orderBys = orderByItem.split(ORDER_BY_FIELD_SEPARATOR);
					if (orderBys.length == 1) {
						property = orderBys[0];
					} else if (orderBys.length >= 2) {
						property = orderBys[0];
						try {
							direction = Order.Direction.valueOf(orderBys[1]);
						} catch (IllegalArgumentException e) {
							continue;
						}
					} else {
						continue;
					}
					if (!ArrayUtils.contains(ignoreProperties, property)) {
						orders.add(new Order(property, direction));
					}
				}
			}
		}
		return orders;
	}

	/**
	 * 设置局部变量
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            变量值
	 * @param env
	 *            环境变量
	 * @param body
	 *            模板内容
	 */
	protected void setLocalVariable(String name, Object value, Environment env, TemplateDirectiveBody body) throws TemplateException, IOException {
		TemplateModel preVariable = FreeMarkerUtils.getVariable(name, env);
		try {
			FreeMarkerUtils.setVariable(name, value, env);
			body.render(env.getOut());
		} finally {
			FreeMarkerUtils.setVariable(name, preVariable, env);
		}
	}

	/**
	 * 设置局部变量
	 * 
	 * @param variables
	 *            变量
	 * @param env
	 *            环境变量
	 * @param body
	 *            模板内容
	 */
	protected void setLocalVariables(Map<String, Object> variables, Environment env, TemplateDirectiveBody body) throws TemplateException, IOException {
		Map<String, Object> preVariables = new HashMap<>();
		for (String name : variables.keySet()) {
			TemplateModel preVariable = FreeMarkerUtils.getVariable(name, env);
			preVariables.put(name, preVariable);
		}
		try {
			FreeMarkerUtils.setVariables(variables, env);
			body.render(env.getOut());
		} finally {
			FreeMarkerUtils.setVariables(preVariables, env);
		}
	}

}