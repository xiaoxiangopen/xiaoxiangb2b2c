package net.xiaoxiangshop.template.method;

import java.math.BigDecimal;
import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.util.FreeMarkerUtils;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * 模板方法 - 货币格式化
 * 
 */
public class CurrencyMethod implements TemplateMethodModelEx {

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
		BigDecimal amount = FreeMarkerUtils.getArgument(0, BigDecimal.class, arguments);
		Boolean showSign = FreeMarkerUtils.getArgument(1, Boolean.class, arguments);
		Boolean showUnit = FreeMarkerUtils.getArgument(2, Boolean.class, arguments);
		if (amount != null) {
			Setting setting = SystemUtils.getSetting();
			String price = String.valueOf(setting.setScale(amount));
			if (showSign != null && showSign) {
				price = setting.getCurrencySign() + price;
			}
			if (showUnit != null && showUnit) {
				price += setting.getCurrencyUnit();
			}
			return new SimpleScalar(price);
		}
		return null;
	}

}