package net.xiaoxiangshop.template.method;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import net.xiaoxiangshop.util.FreeMarkerUtils;

/**
 * 模板方法 - 字符串缩略
 * 
 */
public class AbbreviateMethod implements TemplateMethodModelEx {

	/**
	 * 中文字符配比
	 */
	private static final Pattern PATTERN = Pattern.compile("[\\u4e00-\\u9fa5\\ufe30-\\uffa0]");

	/**
	 * 执行
	 * 
	 * @param arguments
	 *            参数
	 * @return 结果
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		String str = FreeMarkerUtils.getArgument(0, String.class, arguments);
		Integer width = FreeMarkerUtils.getArgument(1, Integer.class, arguments);
		String ellipsis = FreeMarkerUtils.getArgument(2, String.class, arguments);
		if (StringUtils.isEmpty(str) || width == null) {
			return str;
		}
		int i = 0;
		for (int strWidth = 0; i < str.length(); i++) {
			strWidth = PATTERN.matcher(String.valueOf(str.charAt(i))).find() ? strWidth + 2 : strWidth + 1;
			if (strWidth == width) {
				break;
			} else if (strWidth > width) {
				i--;
				break;
			}
		}
		return ellipsis != null && i < str.length() - 1 ? StringUtils.substring(str, 0, i + 1) + ellipsis : StringUtils.substring(str, 0, i + 1);
	}

}