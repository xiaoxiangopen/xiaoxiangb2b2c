package net.xiaoxiangshop.plugin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import net.xiaoxiangshop.entity.PaymentTransaction;
import net.xiaoxiangshop.util.WebUtils;

/**
 * Plugin - Paypal
 * 
 */
@Component("paypalPaymentPlugin")
public class PaypalPaymentPlugin extends PaymentPlugin {

	/**
	 * 支付请求URL
	 */
	private static final String PAY_REQUEST_URL = "https://www.paypal.com/cgi-bin/webscr";

	/**
	 * 通知验证请求URL
	 */
	private static final String NOTIFY_VALIDATE_REQUEST_URL = "https://www.paypal.com/cgi-bin/webscr";

	/**
	 * 货币
	 */
	public enum Currency {

		/**
		 * 美元
		 */
		USD,

		/**
		 * 澳大利亚元
		 */
		AUD,

		/**
		 * 加拿大元
		 */
		CAD,

		/**
		 * 捷克克郎
		 */
		CZK,

		/**
		 * 丹麦克朗
		 */
		DKK,

		/**
		 * 欧元
		 */
		EUR,

		/**
		 * 港元
		 */
		HKD,

		/**
		 * 匈牙利福林
		 */
		HUF,

		/**
		 * 新西兰元
		 */
		NZD,

		/**
		 * 挪威克朗
		 */
		NOK,

		/**
		 * 波兰兹罗提
		 */
		PLN,

		/**
		 * 英镑
		 */
		GBP,

		/**
		 * 新加坡元
		 */
		SGD,

		/**
		 * 瑞典克朗
		 */
		SEK,

		/**
		 * 瑞士法郎
		 */
		CHF,

		/**
		 * 日元
		 */
		JPY
	}

	@Override
	public String getName() {
		return "Paypal";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "xiaoxiangAI";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.xiaoxiangai.com.cn";
	}

	@Override
	public String getInstallUrl() {
		return "/admin/plugin/paypal_payment/install";
	}

	@Override
	public String getUninstallUrl() {
		return "/admin/plugin/paypal_payment/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/paypal_payment/setting";
	}

	@Override
	public void payHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("cmd", "_xclick");
		parameterMap.put("business", getAttribute("partner"));
		parameterMap.put("item_name", StringUtils.abbreviate(paymentDescription.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", StringUtils.EMPTY), 100));
		parameterMap.put("amount", String.valueOf(paymentTransaction.getAmount().setScale(2)));
		parameterMap.put("currency_code", getAttribute("currency"));
		parameterMap.put("return", getPostPayUrl(paymentPlugin, paymentTransaction));
		parameterMap.put("notify_url", getPostPayUrl(paymentPlugin, paymentTransaction));
		parameterMap.put("invoice", paymentTransaction.getSn());
		parameterMap.put("charset", "UTF-8");
		parameterMap.put("no_shipping", "1");
		parameterMap.put("no_note", "0");
		parameterMap.put("rm", "2");
		parameterMap.put("custom", "xiaoxiangshop");

		modelAndView.addObject("requestUrl", PAY_REQUEST_URL);
		modelAndView.addObject("requestMethod", "post");
		modelAndView.addObject("parameterMap", parameterMap);
		modelAndView.setViewName(PaymentPlugin.DEFAULT_PAY_VIEW_NAME);
	}

	@Override
	public boolean isPaySuccess(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (StringUtils.equals(request.getParameter("receiver_email"), getAttribute("partner")) && StringUtils.equals(request.getParameter("mc_currency"), getAttribute("currency")) && StringUtils.equals(request.getParameter("payment_status"), "Completed")
				&& paymentTransaction.getAmount().compareTo(new BigDecimal(request.getParameter("mc_gross"))) == 0) {
			Map<String, Object> parameterMap = new LinkedHashMap<>();
			parameterMap.put("cmd", "_notify-validate");
			parameterMap.putAll(request.getParameterMap());

			String result = WebUtils.post(NOTIFY_VALIDATE_REQUEST_URL, parameterMap);
			return StringUtils.equals(result, "true");
		}
		return false;
	}

}