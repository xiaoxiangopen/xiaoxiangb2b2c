package net.xiaoxiangshop.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

/**
 * Utils - FreeMarker
 * 
 */
public final class FreeMarkerUtils {

	/**
	 * FreeMarker默认配置
	 */
	private static final Configuration DEFAULT_CONFIGURATION = new Configuration(Configuration.VERSION_2_3_26);

	/**
	 * BeansWrapper
	 */
	private static final BeansWrapper DEFAULT_BEANS_WRAPPER = new BeansWrapperBuilder(Configuration.VERSION_2_3_26).build();

	/**
	 * ConversionService
	 */
	private static final ConversionService CONVERSION_SERVICE = new DefaultConversionService();

	/**
	 * 不可实例化
	 */
	private FreeMarkerUtils() {
	}

	/**
	 * 获取当前环境变量
	 * 
	 * @return 当前环境变量
	 */
	public static Environment getCurrentEnvironment() {
		return Environment.getCurrentEnvironment();
	}

	/**
	 * 解析字符串模板
	 * 
	 * @param template
	 *            字符串模板
	 * @return 解析后内容
	 */
	public static String process(String template) throws IOException, TemplateException {
		return process(template, null);
	}

	/**
	 * 解析字符串模板
	 * 
	 * @param template
	 *            字符串模板
	 * @param model
	 *            数据
	 * @return 解析后内容
	 */
	public static String process(String template, Object model) throws IOException, TemplateException {
		Configuration configuration = null;
		ApplicationContext applicationContext = SpringUtils.getApplicationContext();
		if (applicationContext != null) {
			FreeMarkerConfigurer freeMarkerConfigurer = SpringUtils.getBean("freeMarkerConfigurer", FreeMarkerConfigurer.class);
			if (freeMarkerConfigurer != null) {
				configuration = freeMarkerConfigurer.getConfiguration();
			}
		}
		return process(template, model, configuration);
	}

	/**
	 * 解析字符串模板
	 * 
	 * @param template
	 *            字符串模板
	 * @param model
	 *            数据
	 * @param configuration
	 *            配置
	 * @return 解析后内容
	 */
	public static String process(String template, Object model, Configuration configuration) throws IOException, TemplateException {
		if (template == null) {
			return null;
		}
		StringWriter out = new StringWriter();
		new Template("template", new StringReader(template), configuration != null ? configuration : DEFAULT_CONFIGURATION).process(model, out);
		return String.valueOf(out);
	}

	/**
	 * 获取参数
	 * 
	 * @param name
	 *            名称
	 * @param type
	 *            类型
	 * @param params
	 *            参数
	 * @return 参数，若不存在则返回null
	 */
	public static <T> T getParameter(String name, Class<T> type, Map<String, TemplateModel> params) throws TemplateModelException {
		Assert.hasText(name, "[Assertion failed] - name must have text; it must not be null, empty, or blank");
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");
		Assert.notNull(params, "[Assertion failed] - params is required; it must not be null");

		TemplateModel templateModel = params.get(name);
		if (templateModel != null) {
			Object value = DeepUnwrap.unwrap(templateModel);
			if (value != null) {
				return (T) CONVERSION_SERVICE.convert(value, type);
			}
		}
		return null;
	}

	/**
	 * 获取参数
	 * 
	 * @param index
	 *            索引
	 * @param type
	 *            类型
	 * @param arguments
	 *            参数
	 * @return 参数，若不存在则返回null
	 */
	public static <T> T getArgument(int index, Class<T> type, List<?> arguments) throws TemplateModelException {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");
		Assert.notNull(arguments, "[Assertion failed] - arguments is required; it must not be null");

		if (index >= 0 && index < arguments.size()) {
			Object argument = arguments.get(index);
			Object value;
			if (argument != null) {
				if (argument instanceof TemplateModel) {
					value = DeepUnwrap.unwrap((TemplateModel) argument);
				} else {
					value = argument;
				}
				if (value != null) {
					return (T) CONVERSION_SERVICE.convert(value, type);
				}
			}
		}
		return null;
	}

	/**
	 * 获取变量
	 * 
	 * @param name
	 *            名称
	 * @param env
	 *            环境变量
	 * @return 变量
	 */
	public static TemplateModel getVariable(String name, Environment env) throws TemplateModelException {
		Assert.hasText(name, "[Assertion failed] - name must have text; it must not be null, empty, or blank");
		Assert.notNull(env, "[Assertion failed] - env is required; it must not be null");

		return env.getVariable(name);
	}

	/**
	 * 设置变量
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            变量值
	 * @param env
	 *            环境变量
	 */
	public static void setVariable(String name, Object value, Environment env) throws TemplateException {
		Assert.hasText(name, "[Assertion failed] - name must have text; it must not be null, empty, or blank");
		Assert.notNull(env, "[Assertion failed] - env is required; it must not be null");

		if (value instanceof TemplateModel) {
			env.setVariable(name, (TemplateModel) value);
		} else {
			env.setVariable(name, DEFAULT_BEANS_WRAPPER.wrap(value));
		}
	}

	/**
	 * 设置变量
	 * 
	 * @param variables
	 *            变量
	 * @param env
	 *            环境变量
	 */
	public static void setVariables(Map<String, Object> variables, Environment env) throws TemplateException {
		Assert.notNull(variables, "[Assertion failed] - variables is required; it must not be null");
		Assert.notNull(env, "[Assertion failed] - env is required; it must not be null");

		for (Map.Entry<String, Object> entry : variables.entrySet()) {
			String name = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof TemplateModel) {
				env.setVariable(name, (TemplateModel) value);
			} else {
				env.setVariable(name, DEFAULT_BEANS_WRAPPER.wrap(value));
			}
		}
	}

}