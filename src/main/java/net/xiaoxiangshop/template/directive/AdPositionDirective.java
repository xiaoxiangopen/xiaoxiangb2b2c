package net.xiaoxiangshop.template.directive;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.xiaoxiangshop.entity.AdPosition;
import net.xiaoxiangshop.service.AdPositionService;

/**
 * 模板指令 - 广告位
 * 
 */
public class AdPositionDirective extends BaseDirective {

	/**
	 * 变量名称
	 */
	private static final String VARIABLE_NAME = "adPosition";

	@Inject
	private AdPositionService adPositionService;

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
		Long id = getId(params);
		boolean useCache = useCache(params);

		AdPosition adPosition = adPositionService.find(id, useCache);
		setLocalVariable(VARIABLE_NAME, adPosition, env, body);
	}

}