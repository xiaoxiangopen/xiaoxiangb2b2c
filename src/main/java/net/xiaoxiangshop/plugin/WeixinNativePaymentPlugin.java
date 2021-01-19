package net.xiaoxiangshop.plugin;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;

import net.xiaoxiangshop.entity.PaymentTransaction;
import net.xiaoxiangshop.util.WebUtils;
import net.xiaoxiangshop.util.XmlUtils;

/**
 * Plugin - 微信支付(扫码支付)
 * 
 */
@Component("weixinNativePaymentPlugin")
public class WeixinNativePaymentPlugin extends PaymentPlugin {

	/**
	 * code_url请求URL
	 */
	private static final String CODE_URL_REQUEST_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**
	 * 查询订单请求URL
	 */
	private static final String ORDER_QUERY_REQUEST_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

	@Override
	public String getName() {
		return "微信支付(扫码支付)|0301";
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
		return "http://www.xiaoxiangai.com";
	}

	@Override
	public String getInstallUrl() {
		return "/admin/plugin/weixin_native_payment/install";
	}

	@Override
	public String getUninstallUrl() {
		return "/admin/plugin/weixin_native_payment/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/weixin_native_payment/setting";
	}

	@Override
	public boolean supports(HttpServletRequest request) {
		Device device = DeviceUtils.getCurrentDevice(request);
		return device != null && (device.isNormal() || device.isTablet());
	}

	@Override
	public void payHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("appid", getAppId());
		parameterMap.put("mch_id", getMchId());
		parameterMap.put("nonce_str", DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		parameterMap.put("body", StringUtils.abbreviate(paymentDescription.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", StringUtils.EMPTY), 600));
		//parameterMap.put("out_trade_no", paymentTransaction.getSn());
		//微信扫码支付
//		String sn=paymentTransaction.getOrder().getSn();
		parameterMap.put("out_trade_no", paymentTransaction.getSn());
		parameterMap.put("total_fee", String.valueOf(paymentTransaction.getAmount().multiply(new BigDecimal(100)).setScale(0)));
		parameterMap.put("spbill_create_ip", request.getLocalAddr());
		parameterMap.put("notify_url", getPostPayUrl(paymentPlugin, paymentTransaction));
		parameterMap.put("trade_type", "NATIVE");
		parameterMap.put("product_id", paymentTransaction.getSn());
		parameterMap.put("sign", generateSign(parameterMap));
		String result = WebUtils.post(CODE_URL_REQUEST_URL, XmlUtils.toXml(parameterMap));
		Map<String, String> resultMap = XmlUtils.toObject(result, new TypeReference<Map<String, String>>() {
		});
		String returnCode = resultMap.get("return_code");
		String resultCode = resultMap.get("result_code");
		String tradeType = resultMap.get("trade_type");
		if (StringUtils.equals(returnCode, "SUCCESS") && StringUtils.equals(resultCode, "SUCCESS") && StringUtils.equals(tradeType, "NATIVE")) {
			modelAndView.addObject("codeUrl", resultMap.get("code_url"));
			modelAndView.addObject("paymentTransactionSn", paymentTransaction.getSn());
			modelAndView.setViewName("shop/plugin/weixin_native_payment/pay");
		} else if (StringUtils.equals(returnCode, "FAIL") || StringUtils.equals(resultCode, "FAIL")) {
			String returnMsg = resultMap.get("return_msg");
			modelAndView.addObject("errorMessage", returnMsg);
			modelAndView.setViewName("common/error/unprocessable_entity");
		}
	}

	@Override
	public void postPayHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, boolean isPaySuccess, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		String xml = IOUtils.toString(request.getInputStream(), "UTF-8");
		if (StringUtils.isNotEmpty(xml)) {
			Map<String, String> resultMap = XmlUtils.toObject(xml, new TypeReference<Map<String, String>>() {
			});
			if (StringUtils.equals(resultMap.get("return_code"), "SUCCESS")) {
				OutputStream outputStream = response.getOutputStream();
				IOUtils.write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>", outputStream, "UTF-8");
				outputStream.flush();
			} else {
				super.postPayHandle(paymentPlugin, paymentTransaction, paymentDescription, extra, isPaySuccess, request, response, modelAndView);
			}
		} else {
			super.postPayHandle(paymentPlugin, paymentTransaction, paymentDescription, extra, isPaySuccess, request, response, modelAndView);
		}
	}

	@Override
	public boolean isPaySuccess(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = new TreeMap<>();
		parameterMap.put("appid", getAppId());
		parameterMap.put("mch_id", getMchId());
		parameterMap.put("out_trade_no", paymentTransaction.getSn());
		parameterMap.put("nonce_str", DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		parameterMap.put("sign", generateSign(parameterMap));
		String result = WebUtils.post(ORDER_QUERY_REQUEST_URL, XmlUtils.toXml(parameterMap));
		Map<String, String> resultMap = XmlUtils.toObject(result, new TypeReference<Map<String, String>>() {
		});
		return StringUtils.equals(resultMap.get("return_code"), "SUCCESS") && StringUtils.equals(resultMap.get("result_code"), "SUCCESS") && StringUtils.equals(resultMap.get("trade_state"), "SUCCESS")
				&& paymentTransaction.getAmount().multiply(new BigDecimal(100)).compareTo(new BigDecimal(resultMap.get("total_fee"))) == 0;
	}

	/**
	 * 获取公众号ID
	 * 
	 * @return 公众号ID
	 */
	private String getAppId() {
		return getAttribute("appId");
	}

	/**
	 * 获取商户号
	 * 
	 * @return 商户号
	 */
	private String getMchId() {
		return getAttribute("mchId");
	}

	/**
	 * 生成签名
	 * 
	 * @param parameterMap
	 *            参数
	 * @return 签名
	 */
	private String generateSign(Map<String, ?> parameterMap) {
		return StringUtils.upperCase(DigestUtils.md5Hex(joinKeyValue(new TreeMap<>(parameterMap), null, "&key=" + getAttribute("apiKey"), "&", true)));
	}

}