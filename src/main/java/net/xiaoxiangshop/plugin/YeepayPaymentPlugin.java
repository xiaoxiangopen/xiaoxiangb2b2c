package net.xiaoxiangshop.plugin;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import net.xiaoxiangshop.entity.PaymentTransaction;
import net.xiaoxiangshop.util.WebUtils;

/**
 * Plugin - 易宝支付
 * 
 */
@Component("yeepayPaymentPlugin")
public class YeepayPaymentPlugin extends PaymentPlugin {

	/**
	 * 支付请求URL
	 */
	private static final String PAY_REQUEST_URL = "https://www.yeepay.com/app-merchant-proxy/node";

	@Override
	public String getName() {
		return "易宝支付";
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
		return "/admin/plugin/yeepay_payment/install";
	}

	@Override
	public String getUninstallUrl() {
		return "/admin/plugin/yeepay_payment/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/yeepay_payment/setting";
	}

	@Override
	public void payHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		Map<String, Object> parameterMap = new LinkedHashMap<>();
		parameterMap.put("p0_Cmd", "Buy");
		parameterMap.put("p1_MerId", getAttribute("partner"));
		parameterMap.put("p2_Order", paymentTransaction.getSn());
		parameterMap.put("p3_Amt", String.valueOf(paymentTransaction.getAmount().setScale(2)));
		parameterMap.put("p4_Cur", "CNY");
		parameterMap.put("p5_Pid", StringUtils.abbreviate(paymentDescription.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", StringUtils.EMPTY), 20));
		parameterMap.put("p7_Pdesc", StringUtils.abbreviate(paymentDescription.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", StringUtils.EMPTY), 20));
		parameterMap.put("p8_Url", getPostPayUrl(paymentPlugin, paymentTransaction));
		parameterMap.put("p9_SAF", "0");
		parameterMap.put("pa_MP", "xiaoxiangshop");
		parameterMap.put("pr_NeedResponse", "1");
		parameterMap.put("hmac", generateSign(parameterMap));

		modelAndView.addObject("requestUrl", PAY_REQUEST_URL);
		modelAndView.addObject("requestCharset", "GBK");
		modelAndView.addObject("parameterMap", parameterMap);
		modelAndView.setViewName(PaymentPlugin.DEFAULT_PAY_VIEW_NAME);
	}

	@Override
	public void postPayHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, boolean isPaySuccess, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		if (StringUtils.equals("2", WebUtils.parse(request.getQueryString(), "GBK").get("r9_BType"))) {
			OutputStream outputStream = response.getOutputStream();
			IOUtils.write("success", outputStream, "UTF-8");
			outputStream.flush();
		} else {
			super.postPayHandle(paymentPlugin, paymentTransaction, paymentDescription, extra, isPaySuccess, request, response, modelAndView);
		}
	}

	@Override
	public boolean isPaySuccess(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> parameterValuesMap = WebUtils.parse(request.getQueryString(), "GBK");
		Map<String, Object> parameterMap = new LinkedHashMap<>();
		parameterMap.put("p1_MerId", parameterValuesMap.get("p1_MerId"));
		parameterMap.put("r0_Cmd", parameterValuesMap.get("r0_Cmd"));
		parameterMap.put("r1_Code", parameterValuesMap.get("r1_Code"));
		parameterMap.put("r2_TrxId", parameterValuesMap.get("r2_TrxId"));
		parameterMap.put("r3_Amt", parameterValuesMap.get("r3_Amt"));
		parameterMap.put("r4_Cur", parameterValuesMap.get("r4_Cur"));
		parameterMap.put("r5_Pid", parameterValuesMap.get("r5_Pid"));
		parameterMap.put("r6_Order", parameterValuesMap.get("r6_Order"));
		parameterMap.put("r7_Uid", parameterValuesMap.get("r7_Uid"));
		parameterMap.put("r8_MP", parameterValuesMap.get("r8_MP"));
		parameterMap.put("r9_BType", parameterValuesMap.get("r9_BType"));
		return StringUtils.equals(generateSign(parameterMap), parameterValuesMap.get("hmac")) && StringUtils.equals(getAttribute("partner"), parameterValuesMap.get("p1_MerId")) && StringUtils.equals(parameterValuesMap.get("r1_Code"), "1")
				&& paymentTransaction.getAmount().compareTo(new BigDecimal(parameterValuesMap.get("r3_Amt"))) == 0;
	}

	/**
	 * 生成签名
	 * 
	 * @param parameterMap
	 *            参数
	 * @return 签名
	 */
	private String generateSign(Map<String, Object> parameterMap) {
		return hmacDigest(joinValue(parameterMap, null, null, null, false, "hmac"), getAttribute("key"));
	}

	/**
	 * Hmac加密
	 * 
	 * @param value
	 *            值
	 * @param key
	 *            密钥
	 * @return 密文
	 */
	private String hmacDigest(String value, String key) {
		try {
			Mac mac = Mac.getInstance("HmacMD5");
			mac.init(new SecretKeySpec(key.getBytes("UTF-8"), "HmacMD5"));
			byte[] bytes = mac.doFinal(value.getBytes("UTF-8"));

			StringBuilder digest = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					digest.append("0");
				}
				digest.append(hex);
			}
			return String.valueOf(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}