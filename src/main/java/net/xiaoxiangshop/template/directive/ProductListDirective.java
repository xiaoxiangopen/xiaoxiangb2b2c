package net.xiaoxiangshop.template.directive;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.math.NumberUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.service.ProductService;
import net.xiaoxiangshop.util.FreeMarkerUtils;

/**
 * 模板指令 - 商品列表
 * 
 */
public class ProductListDirective extends BaseDirective {

	/**
	 * "类型"参数名称
	 */
	private static final String TYPE_PARAMETER_NAME = "type";

	/**
	 * "商品分类ID"参数名称
	 */
	private static final String PRODUCT_CATEGORY_ID_PARAMETER_NAME = "productCategoryId";

	/**
	 * "店铺商品分类ID"参数名称
	 */
	private static final String STORE_PRODUCT_CATEGORY_ID_PARAMETER_NAME = "storeProductCategoryId";

	/**
	 * "品牌ID"参数名称
	 */
	private static final String BRAND_ID_PARAMETER_NAME = "brandId";

	/**
	 * "店铺ID"参数名称
	 */
	private static final String STORE_ID_PARAMETER_NAME = "storeId";

	/**
	 * "商品标签ID"参数名称
	 */
	private static final String PRODUCT_TAG_ID_PARAMETER_NAME = "productTagId";

	/**
	 * "店铺商品标签ID"参数名称
	 */
	private static final String STORE_PRODUCT_TAG_ID_PARAMETER_NAME = "storeProductTagId";

	/**
	 * "属性值"参数名称
	 */
	private static final String ATTRIBUTE_VALUE_PARAMETER_NAME = "attributeValue";

	/**
	 * "最低价格"参数名称
	 */
	private static final String START_PRICE_PARAMETER_NAME = "startPrice";

	/**
	 * "最高价格"参数名称
	 */
	private static final String END_PRICE_PARAMETER_NAME = "endPrice";



	/**
	 * "排序类型"参数名称
	 */
	private static final String ORDER_TYPE_PARAMETER_NAME = "orderType";

	/**
	 * 变量名称
	 */
	private static final String VARIABLE_NAME = "products";

	@Inject
	private ProductService productService;

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
		Product.Type type = FreeMarkerUtils.getParameter(TYPE_PARAMETER_NAME, Product.Type.class, params);
		Long productCategoryId = FreeMarkerUtils.getParameter(PRODUCT_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
		Long storeProductCategoryId = FreeMarkerUtils.getParameter(STORE_PRODUCT_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
		Long brandId = FreeMarkerUtils.getParameter(BRAND_ID_PARAMETER_NAME, Long.class, params);
		Long storeId = FreeMarkerUtils.getParameter(STORE_ID_PARAMETER_NAME, Long.class, params);
		Long productTagId = FreeMarkerUtils.getParameter(PRODUCT_TAG_ID_PARAMETER_NAME, Long.class, params);
		Long storeProductTagId = FreeMarkerUtils.getParameter(STORE_PRODUCT_TAG_ID_PARAMETER_NAME, Long.class, params);
		Map<String, String> attributeValue = FreeMarkerUtils.getParameter(ATTRIBUTE_VALUE_PARAMETER_NAME, Map.class, params);
		BigDecimal startPrice = FreeMarkerUtils.getParameter(START_PRICE_PARAMETER_NAME, BigDecimal.class, params);
		BigDecimal endPrice = FreeMarkerUtils.getParameter(END_PRICE_PARAMETER_NAME, BigDecimal.class, params);

		Product.OrderType orderType = FreeMarkerUtils.getParameter(ORDER_TYPE_PARAMETER_NAME, Product.OrderType.class, params);
		Integer count = getCount(params);
		List<Filter> filters = getFilters(params, Product.class);
		List<Order> orders = getOrders(params);
		boolean useCache = useCache(params);
		Map<Long, String> attributeValueMap = new HashMap<>();
		if (attributeValue != null) {
			for (Map.Entry<String, String> entry : attributeValue.entrySet()) {
				if (NumberUtils.isDigits(entry.getKey())) {
					Long attributeId = Long.valueOf(entry.getKey());
					attributeValueMap.put(attributeId, entry.getValue());
				}
			}
		}

		List<Product> products = productService.findList(type, storeId, productCategoryId, storeProductCategoryId, brandId, productTagId, storeProductTagId, attributeValueMap, startPrice, endPrice, true, true, null, true, null, null, orderType, count, filters, orders,
				useCache);
		setLocalVariable(VARIABLE_NAME, products, env, body);
	}

}