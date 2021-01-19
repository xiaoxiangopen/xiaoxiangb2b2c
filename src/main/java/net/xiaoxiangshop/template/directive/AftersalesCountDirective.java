package net.xiaoxiangshop.template.directive;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.xiaoxiangshop.entity.Aftersales;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.service.AftersalesService;
import net.xiaoxiangshop.service.OrderService;
import net.xiaoxiangshop.util.FreeMarkerUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;

/**
 * 模板指令 - 售后申请数量
 * 
 */
public class AftersalesCountDirective extends BaseDirective {

	/**
	 * "售后申请类型"参数名称
	 */
	private static final String TYPE_PARAMETER_NAME = "type";

	/**
	 * "售后申请状态"参数名称
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
	 * 变量名称
	 */
	private static final String VARIABLE_NAME = "count";

	@Inject
	private AftersalesService aftersalesService;

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
		Aftersales.Type type = FreeMarkerUtils.getParameter(TYPE_PARAMETER_NAME, Aftersales.Type.class, params);
		Aftersales.Status status = FreeMarkerUtils.getParameter(STATUS_PARAMETER_NAME, Aftersales.Status.class, params);
		Long storeId = FreeMarkerUtils.getParameter(STORE_ID_PARAMETER_NAME, Long.class, params);
		Long memberId = FreeMarkerUtils.getParameter(MEMBER_ID_PARAMETER_NAME, Long.class, params);
		Long count =aftersalesService.count(type, status, storeId, memberId);
		setLocalVariable(VARIABLE_NAME, count, env, body);
	}

}