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
import net.xiaoxiangshop.entity.Brand;
import net.xiaoxiangshop.service.BrandService;
import net.xiaoxiangshop.util.FreeMarkerUtils;

/**
 * 模板指令 - 品牌列表
 * 
 */
public class BrandListDirective extends BaseDirective {

	/**
	 * "商品分类ID"参数名称
	 */
	private static final String PRODUCT_CATEGORY_ID_PARAMETER_NAME = "productCategoryId";

	/**
	 * 变量名称
	 */
	private static final String VARIABLE_NAME = "brands";

	@Inject
	private BrandService brandService;

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
		Long productCategoryId = FreeMarkerUtils.getParameter(PRODUCT_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
		Integer count = getCount(params);
		List<Filter> filters = getFilters(params, Brand.class);
		List<Order> orders = getOrders(params);
		boolean useCache = useCache(params);

		List<Brand> brands = brandService.findList(productCategoryId, count, filters, orders, useCache);
		setLocalVariable(VARIABLE_NAME, brands, env, body);
	}

}