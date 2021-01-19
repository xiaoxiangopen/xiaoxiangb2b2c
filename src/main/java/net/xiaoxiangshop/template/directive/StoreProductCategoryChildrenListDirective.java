package net.xiaoxiangshop.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.xiaoxiangshop.entity.StoreProductCategory;
import net.xiaoxiangshop.service.StoreProductCategoryService;
import net.xiaoxiangshop.util.FreeMarkerUtils;

/**
 * 模板指令 - 下级店铺商品分类列表
 * 
 */
public class StoreProductCategoryChildrenListDirective extends BaseDirective {

	/**
	 * "商品分类ID"参数名称
	 */
	private static final String STORE_PRODUCT_CATEGORY_ID_PARAMETER_NAME = "storeProductCategoryId";

	/**
	 * "店铺ID"参数名称
	 */
	private static final String STORE_ID = "storeId";

	/**
	 * "是否递归"参数名称
	 */
	private static final String RECURSIVE_PARAMETER_NAME = "recursive";

	/**
	 * 变量名称
	 */
	private static final String VARIABLE_NAME = "storeProductCategories";

	@Inject
	private StoreProductCategoryService storeProductCategoryService;

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
		Long storeProductCategoryId = FreeMarkerUtils.getParameter(STORE_PRODUCT_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
		Long storeId = FreeMarkerUtils.getParameter(STORE_ID, Long.class, params);
		Boolean recursive = FreeMarkerUtils.getParameter(RECURSIVE_PARAMETER_NAME, Boolean.class, params);
		Integer count = getCount(params);
		boolean useCache = useCache(params);

		List<StoreProductCategory> storeProductCategories = storeProductCategoryService.findChildren(storeProductCategoryId, storeId, recursive != null ? recursive : true, count, useCache);
		setLocalVariable(VARIABLE_NAME, storeProductCategories, env, body);
	}

}