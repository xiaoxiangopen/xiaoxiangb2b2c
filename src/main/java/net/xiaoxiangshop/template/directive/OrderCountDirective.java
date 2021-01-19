package net.xiaoxiangshop.template.directive;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.service.OrderService;
import net.xiaoxiangshop.util.FreeMarkerUtils;

/**
 * 模板指令 - 订单数量
 * 
 */
public class OrderCountDirective extends BaseDirective {

	/**
	 * "订单类型"参数名称
	 */
	private static final String TYPE_PARAMETER_NAME = "type";

	/**
	 * "订单状态"参数名称
	 */
	private static final String STATUS_PARAMETER_NAME = "status";

	/**
	 * "店铺ID"参数名称
	 */
	private static final String STORE_ID_PARAMETER_NAME = "storeId";

	/**
	 * "会员ID"参数名称
	 */
	private static final String MEMBER_ID_PARAMETER_NAME = "memberId";

	/**
	 * "商品ID"参数名称
	 */
	private static final String PRODUCT_ID_PARAMETER_NAME = "productId";

	/**
	 * "订单是否等待收款"参数名称
	 */
	private static final String IS_PENDING_RECEIVE_PARAMETER_NAME = "isPendingReceive";

	/**
	 * "订单是否等待退款"参数名称
	 */
	private static final String IS_PENDING_REFUNDS_PARAMETER_NAME = "isPendingRefunds";

	/**
	 * "订单是否已兑换积分"参数名称
	 */
	private static final String IS_EXCHANGE_POINT_PARAMETER_NAME = "isExchangePoint";

	/**
	 * "订单是否已使用优惠码"参数名称
	 */
	private static final String IS_ALLOCATED_STOCK_PARAMETER_NAME = "isAllocatedStock";

	/**
	 * "订单是否已过期"参数名称
	 */
	private static final String HAS_EXPIRED_PARAMETER_NAME = "hasExpired";

	/**
	 * 变量名称
	 */
	private static final String VARIABLE_NAME = "count";

	@Inject
	private OrderService orderService;

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
		Order.Type type = FreeMarkerUtils.getParameter(TYPE_PARAMETER_NAME, Order.Type.class, params);
		Order.Status status = FreeMarkerUtils.getParameter(STATUS_PARAMETER_NAME, Order.Status.class, params);
		Long storeId = FreeMarkerUtils.getParameter(STORE_ID_PARAMETER_NAME, Long.class, params);
		Long memberId = FreeMarkerUtils.getParameter(MEMBER_ID_PARAMETER_NAME, Long.class, params);
		Long productId = FreeMarkerUtils.getParameter(PRODUCT_ID_PARAMETER_NAME, Long.class, params);
		Boolean isPendingReceive = FreeMarkerUtils.getParameter(IS_PENDING_RECEIVE_PARAMETER_NAME, Boolean.class, params);
		Boolean isPendingRefunds = FreeMarkerUtils.getParameter(IS_PENDING_REFUNDS_PARAMETER_NAME, Boolean.class, params);
		Boolean isExchangePoint = FreeMarkerUtils.getParameter(IS_EXCHANGE_POINT_PARAMETER_NAME, Boolean.class, params);
		Boolean isAllocatedStock = FreeMarkerUtils.getParameter(IS_ALLOCATED_STOCK_PARAMETER_NAME, Boolean.class, params);
		Boolean hasExpired = FreeMarkerUtils.getParameter(HAS_EXPIRED_PARAMETER_NAME, Boolean.class, params);

		Long count = orderService.count(type, status, storeId, memberId, productId, isPendingReceive, isPendingRefunds, isExchangePoint, isAllocatedStock, hasExpired);
		setLocalVariable(VARIABLE_NAME, count, env, body);
	}

}