package net.xiaoxiangshop.plugin;

import java.io.OutputStream;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;

import net.xiaoxiangshop.entity.PaymentTransaction;
import net.xiaoxiangshop.util.SpringUtils;

/**
 * Plugin - 支付宝(手机网站)
 * 
 */
@Component("alipayWapPaymentPlugin")
public class AlipayWapPaymentPlugin extends PaymentPlugin {

	/**
	 * 网关URL
	 */
	private static final String SERVER_URL = "https://openapi.alipay.com/gateway.do";

	@Override
	public String getName() {
		return "支付宝(手机网站)|0204";
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
		return "http://www.xiaoxiangshop.net";
	}

	@Override
	public String getInstallUrl() {
		return "/admin/plugin/alipay_wap_payment/install";
	}

	@Override
	public String getUninstallUrl() {
		return "/admin/plugin/alipay_wap_payment/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/alipay_wap_payment/setting";
	}

	@Override
	public boolean supports(HttpServletRequest request) {
		Device device = DeviceUtils.getCurrentDevice(request);
		return device != null && (device.isMobile() || device.isTablet());
	}

	@Override
	public void prePayHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		modelAndView.addObject("payUrl", getPayUrl(paymentPlugin, paymentTransaction, extra));
		modelAndView.addObject("paymentTransactionSn", paymentTransaction.getSn());
		modelAndView.setViewName("shop/plugin/alipay_wap_payment/pre_pay");
	}

	@Override
	public void payHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		AlipayTradeWapPayModel alipayTradeWapPayModel = new AlipayTradeWapPayModel();
		alipayTradeWapPayModel.setOutTradeNo(paymentTransaction.getSn());
//		String  sn=paymentTransaction.getOrder().getSn();
//		alipayTradeWapPayModel.setOutTradeNo(sn);
		alipayTradeWapPayModel.setProductCode("QUICK_WAP_PAY");
		alipayTradeWapPayModel.setTotalAmount(String.valueOf(paymentTransaction.getAmount().setScale(2)));
		alipayTradeWapPayModel.setSubject(StringUtils.abbreviate(paymentDescription.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", StringUtils.EMPTY), 60));

		AlipayTradeWapPayRequest alipayTradeWapPayRequest = new AlipayTradeWapPayRequest();
		alipayTradeWapPayRequest.setReturnUrl(getPostPayUrl(paymentPlugin, paymentTransaction));
		alipayTradeWapPayRequest.setNotifyUrl(getPostPayUrl(paymentPlugin, paymentTransaction));
		alipayTradeWapPayRequest.setBizModel(alipayTradeWapPayModel);
		try {
			modelAndView.addObject("body", getAlipayClient().pageExecute(alipayTradeWapPayRequest).getBody());
			modelAndView.setViewName("shop/plugin/alipay_wap_payment/pay");
		} catch (AlipayApiException e) {
			modelAndView.addObject("errorMessage", SpringUtils.getMessage("admin.plugin.alipayWapPayment.paymentConfigurationError", getName()));
			modelAndView.setViewName("common/error/unprocessable_entity");
		}
	}

	@Override
	public void postPayHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, boolean isPaySuccess, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		if (StringUtils.equals(request.getParameter("notify_type"), "trade_status_sync")) {
			OutputStream outputStream = response.getOutputStream();
			IOUtils.write("success", outputStream, "UTF-8");
			outputStream.flush();
		} else {
			super.postPayHandle(paymentPlugin, paymentTransaction, paymentDescription, extra, isPaySuccess, request, response, modelAndView);
		}
	}

	@Override
	public boolean isPaySuccess(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response) throws Exception {
		AlipayTradeQueryModel alipayTradeQueryModel = new AlipayTradeQueryModel();
		alipayTradeQueryModel.setOutTradeNo(paymentTransaction.getSn());
		alipayTradeQueryModel.setTradeNo(request.getParameter("trade_no"));

		AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
		alipayTradeQueryRequest.setBizModel(alipayTradeQueryModel);
		try {
			AlipayTradeQueryResponse alipayTradeQueryResponse = getAlipayClient().execute(alipayTradeQueryRequest);
			return alipayTradeQueryResponse.isSuccess() && (StringUtils.equalsIgnoreCase(alipayTradeQueryResponse.getTradeStatus(), "TRADE_SUCCESS") || StringUtils.equalsIgnoreCase(alipayTradeQueryResponse.getTradeStatus(), "TRADE_FINISHED"))
					&& paymentTransaction.getAmount().compareTo(new BigDecimal(alipayTradeQueryResponse.getTotalAmount())) == 0;
		} catch (AlipayApiException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 获取AppID
	 * 
	 * @return AppID
	 */
	private String getAppId() {
		return getAttribute("appId");
	}

	/**
	 * 获取开发者应用私钥
	 * 
	 * @return 开发者应用私钥
	 */
	private String getAppPrivateKey() {
		return getAttribute("appPrivateKey");
	}

	/**
	 * 获取支付宝公钥
	 * 
	 * @return 支付宝公钥
	 */
	private String getAlipayPublicKey() {
		return getAttribute("alipayPublicKey");
	}

	/**
	 * 获取AlipayClient
	 * 
	 * @return AlipayClient
	 */
	private AlipayClient getAlipayClient() {
		return new DefaultAlipayClient(SERVER_URL, getAppId(), getAppPrivateKey(), "json", "UTF-8", getAlipayPublicKey(), "RSA2");
	}

}