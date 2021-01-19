package net.xiaoxiangshop.template.directive;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.xiaoxiangshop.entity.DistributionCash;
import net.xiaoxiangshop.service.DistributionCashService;
import net.xiaoxiangshop.util.FreeMarkerUtils;

/**
 * 模板指令 - 分销提现数量
 * 
 */
public class DistributionCashCountDirective extends BaseDirective {

	/**
	 * "状态"参数名称
	 */
	private static final String STATUS_PARAMETER_NAME = "status";

	/**
	 * "收款银行"参数名称
	 */
	private static final String BANK_PARAMETER_NAME = "bank";

	/**
	 * "收款账号"参数名称
	 */
	private static final String ACCOUNT_PARAMETER_NAME = "account";

	/**
	 * "开户人"参数名称
	 */
	private static final String ACCOUNT_HOLDER_PARAMETER_NAME = "accountHolder";

	/**
	 * "分销员ID"参数名称
	 */
	private static final String DISTRIBUTOR_ID_PARAMETER_NAME = "distributorId";

	/**
	 * 变量名称
	 */
	private static final String VARIABLE_NAME = "count";

	@Inject
	private DistributionCashService distributionCashService;

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
		DistributionCash.Status status = FreeMarkerUtils.getParameter(STATUS_PARAMETER_NAME, DistributionCash.Status.class, params);
		String bank = FreeMarkerUtils.getParameter(BANK_PARAMETER_NAME, String.class, params);
		String account = FreeMarkerUtils.getParameter(ACCOUNT_PARAMETER_NAME, String.class, params);
		String accountHolder = FreeMarkerUtils.getParameter(ACCOUNT_HOLDER_PARAMETER_NAME, String.class, params);
		Long distributorId = FreeMarkerUtils.getParameter(DISTRIBUTOR_ID_PARAMETER_NAME, Long.class, params);

		Long count = distributionCashService.count(status, bank, account, accountHolder, distributorId);
		setLocalVariable(VARIABLE_NAME, count, env, body);
	}

}