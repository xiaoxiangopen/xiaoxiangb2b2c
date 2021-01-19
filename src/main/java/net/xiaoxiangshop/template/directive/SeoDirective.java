package net.xiaoxiangshop.template.directive;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.xiaoxiangshop.entity.Seo;
import net.xiaoxiangshop.service.SeoService;
import net.xiaoxiangshop.util.FreeMarkerUtils;

/**
 * 模板指令 - SEO设置
 * 
 */
public class SeoDirective extends BaseDirective {

	/**
	 * "类型"参数名称
	 */
	private static final String TYPE_PARAMETER_NAME = "type";

	/**
	 * 变量名称
	 */
	private static final String VARIABLE_NAME = "seo";

	@Inject
	private SeoService seoService;

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
		Seo.Type type = FreeMarkerUtils.getParameter(TYPE_PARAMETER_NAME, Seo.Type.class, params);
		boolean useCache = useCache(params);

		Seo seo = seoService.find(type, useCache);
		setLocalVariable(VARIABLE_NAME, seo, env, body);
	}

}