package net.xiaoxiangshop.plugin;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import net.xiaoxiangshop.entity.PaymentTransaction;

/**
 * Plugin - 快钱支付
 * 
 */
@Component("pay99billPaymentPlugin")
public class Pay99billPaymentPlugin extends PaymentPlugin {

	/**
	 * 支付请求URL
	 */
	private static final String PAY_REQUEST_URL = "https://www.99bill.com/gateway/recvMerchantInfoAction.htm";

	@Override
	public String getName() {
		return "快钱支付";
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
		return "/admin/plugin/pay_99bill_payment/install";
	}

	@Override
	public String getUninstallUrl() {
		return "/admin/plugin/pay_99bill_payment/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/pay_99bill_payment/setting";
	}

	@Override
	public void payHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		Map<String, Object> parameterMap = new LinkedHashMap<>();
		parameterMap.put("inputCharset", "1");
		parameterMap.put("pageUrl", getPostPayUrl(paymentPlugin, paymentTransaction));
		parameterMap.put("bgUrl", getPostPayUrl(paymentPlugin, paymentTransaction, "notify"));
		parameterMap.put("version", "v2.0");
		parameterMap.put("language", "1");
		parameterMap.put("signType", "1");
		parameterMap.put("merchantAcctId", getAttribute("partner"));
		parameterMap.put("payerIP", request.getLocalAddr());
		parameterMap.put("orderId", paymentTransaction.getSn());
		parameterMap.put("orderAmount", String.valueOf(paymentTransaction.getAmount().multiply(new BigDecimal(100)).setScale(0)));
		parameterMap.put("orderTime", DateFormatUtils.format(new Date(), "yyyyMMddhhmmss"));
		parameterMap.put("orderTimestamp", DateFormatUtils.format(new Date(), "yyyyMMddhhmmss"));
		parameterMap.put("productName", StringUtils.abbreviate(paymentDescription.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", StringUtils.EMPTY), 100));
		parameterMap.put("productDesc", StringUtils.abbreviate(paymentDescription.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", StringUtils.EMPTY), 400));
		parameterMap.put("ext1", "xiaoxiangshop");
		parameterMap.put("payType", "00");
		parameterMap.put("signMsg", generateSign(parameterMap));

		modelAndView.addObject("requestUrl", PAY_REQUEST_URL);
		modelAndView.addObject("parameterMap", parameterMap);
		modelAndView.setViewName(PaymentPlugin.DEFAULT_PAY_VIEW_NAME);
	}

	@Override
	public void postPayHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, boolean isPaySuccess, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		if (StringUtils.equals(extra, "notify")) {
			OutputStream outputStream = response.getOutputStream();
			IOUtils.write("<result>1</result><redirecturl>" + getPostPayUrl(paymentPlugin, paymentTransaction) + "</redirecturl>", outputStream, "UTF-8");
			outputStream.flush();
		} else {
			super.postPayHandle(paymentPlugin, paymentTransaction, paymentDescription, extra, isPaySuccess, request, response, modelAndView);
		}
	}

	@Override
	public boolean isPaySuccess(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = new LinkedHashMap<>();
		parameterMap.put("merchantAcctId", request.getParameter("merchantAcctId"));
		parameterMap.put("version", request.getParameter("version"));
		parameterMap.put("language", request.getParameter("language"));
		parameterMap.put("signType", request.getParameter("signType"));
		parameterMap.put("payType", request.getParameter("payType"));
		parameterMap.put("bankId", request.getParameter("bankId"));
		parameterMap.put("orderId", request.getParameter("orderId"));
		parameterMap.put("orderTime", request.getParameter("orderTime"));
		parameterMap.put("orderAmount", request.getParameter("orderAmount"));
		parameterMap.put("bindCard", request.getParameter("bindCard"));
		parameterMap.put("bindMobile", request.getParameter("bindMobile"));
		parameterMap.put("dealId", request.getParameter("dealId"));
		parameterMap.put("bankDealId", request.getParameter("bankDealId"));
		parameterMap.put("dealTime", request.getParameter("dealTime"));
		parameterMap.put("payAmount", request.getParameter("payAmount"));
		parameterMap.put("fee", request.getParameter("fee"));
		parameterMap.put("ext1", request.getParameter("ext1"));
		parameterMap.put("ext2", request.getParameter("ext2"));
		parameterMap.put("payResult", request.getParameter("payResult"));
		parameterMap.put("errCode", request.getParameter("errCode"));
		parameterMap.put("signMsg", request.getParameter("signMsg"));
		return paymentTransaction != null && StringUtils.equals(generateSign(parameterMap), request.getParameter("signMsg")) && StringUtils.equals(request.getParameter("merchantAcctId"), getAttribute("partner")) && StringUtils.equals(request.getParameter("payResult"), "10")
				&& paymentTransaction.getAmount().multiply(new BigDecimal(100)).compareTo(new BigDecimal(request.getParameter("payAmount"))) == 0;
	}

	/**
	 * 生成签名
	 * 
	 * @param parameterMap
	 *            参数
	 * @return 签名
	 */
	private String generateSign(Map<String, Object> parameterMap) {
		return DigestUtils.md5Hex(joinKeyValue(parameterMap, null, "&key=" + getAttribute("key"), "&", true, "signMsg")).toUpperCase();
	}

}