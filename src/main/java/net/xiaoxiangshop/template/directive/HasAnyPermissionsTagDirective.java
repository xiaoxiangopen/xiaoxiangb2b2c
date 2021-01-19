package net.xiaoxiangshop.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.xiaoxiangshop.util.FreeMarkerUtils;

/**
 * 模板指令 - 是否存在任意权限
 * 
 */
public class HasAnyPermissionsTagDirective extends BaseDirective {

	/**
	 * "权限"参数名称
	 */
	private static final String PERMISSIONS_PARAMETER_NAME = "permissions";

	/**
	 * 变量名称
	 */
	private static final String VARIABLE_NAME = "hasPermission";

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
		List<String> permissions = FreeMarkerUtils.getParameter(PERMISSIONS_PARAMETER_NAME, List.class, params);
		boolean hasPermission = false;
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			for (String permission : permissions) {
				if (subject.isPermitted(permission)) {
					hasPermission = true;
					break;
				}
			}
		}
		setLocalVariable(VARIABLE_NAME, hasPermission, env, body);
	}

}