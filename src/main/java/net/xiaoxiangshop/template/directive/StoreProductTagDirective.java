package net.xiaoxiangshop.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.entity.Consultation;
import net.xiaoxiangshop.entity.StoreProductTag;
import net.xiaoxiangshop.service.StoreProductTagService;
import net.xiaoxiangshop.util.FreeMarkerUtils;

/**
 * 模板指令 - 店铺商品标签
 * 
 */
public class StoreProductTagDirective extends BaseDirective {

	/**
	 * 变量名称
	 */
	private static final String VARIABLE_NAME = "storeProductTags";

	@Inject
	private StoreProductTagService storeProductTagService;

	/**
	 * "店铺ID"参数名称
	 */
	private static final String STORE_ID_PARAMETER_NAME = "storeId";

	/**
	 * 执行
	 * 
	 * @param env
	 *            环境变量
	 * @param params
	 *            参数
	 * @param loopVars
	 *            循环变量
	 * @param body
	 *            模板内容
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long storeId = FreeMarkerUtils.getParameter(STORE_ID_PARAMETER_NAME, Long.class, params);
		Integer count = getCount(params);
		List<Filter> filters = getFilters(params, Consultation.class);
		List<Order> orders = getOrders(params);
		boolean useCache = useCache(params);

		List<StoreProductTag> storeProductTags = storeProductTagService.findList(storeId, true, count, filters, orders, useCache);
		setLocalVariable(VARIABLE_NAME, storeProductTags, env, body);
	}

}