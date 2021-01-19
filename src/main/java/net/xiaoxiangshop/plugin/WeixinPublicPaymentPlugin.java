package net.xiaoxiangshop.plugin;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.TokenRequestBuilder;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;

import net.xiaoxiangshop.entity.PaymentTransaction;
import net.xiaoxiangshop.util.WebUtils;
import net.xiaoxiangshop.util.XmlUtils;
import net.xiaoxiangshop.plugin.PaymentPlugin;

/**
 * Plugin - 微信支付(公众号支付)
 * 
 */
@Component("weixinPublicPaymentPlugin")
public class WeixinPublicPaymentPlugin extends PaymentPlugin {


	/**
	 * code请求URL
	 */
	private static final String CODE_REQUEST_URL = "https://open.weixin.qq.com/connect/oauth2/authorize#wechat_redirect";

	/**
	 * openId请求URL
	 */
	private static final String OPEN_ID_REQUEST_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

	/**
	 * prepay_id请求URL
	 */
	private static final String PREPAY_ID_REQUEST_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**
	 * 查询订单请求URL
	 */
	private static final String ORDER_QUERY_REQUEST_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

	/**
	 * 微信版本配比
	 */
	private static final Pattern WEIXIN_VERSION_PATTERN = Pattern.compile("MicroMessenger/(?<majorVersion>\\d+)(\\.(?<minorVersion>\\d+))?", Pattern.CASE_INSENSITIVE);

	@Override
	public String getName() {
		return "微信支付(公众号支付)";
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
		return "/admin/plugin/weixin_public_payment/install";
	}

	@Override
	public String getUninstallUrl() {
		return "/admin/plugin/weixin_public_payment/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/weixin_public_payment/setting";
	}

	@Override
	public boolean supports(HttpServletRequest request) {
		Matcher matcher = WEIXIN_VERSION_PATTERN.matcher(request.getHeader("USER-AGENT"));
		return matcher.find() && Integer.valueOf(matcher.group("majorVersion")) >= 5;
	}

	@Override
	public void prePayHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("appid", getAppId());
		parameterMap.put("redirect_uri", getPayUrl(paymentPlugin, paymentTransaction));
		parameterMap.put("response_type", "code");
		parameterMap.put("scope", "snsapi_base");

		modelAndView.addObject("requestUrl", CODE_REQUEST_URL);
		modelAndView.addObject("parameterMap", parameterMap);
		modelAndView.setViewName("shop/plugin/weixin_public_payment/pre_pay");
	}

	@Override
	public void payHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		String code = request.getParameter("code");
		if (StringUtils.isEmpty(code)) {
			modelAndView.setViewName("common/error/unprocessable_entity");
			return;
		}

		Map<String, Object> parameterMap = new TreeMap<>();
		parameterMap.put("appid", getAppId());
		parameterMap.put("mch_id", getMchId());
		parameterMap.put("nonce_str", DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		parameterMap.put("body", StringUtils.abbreviate(paymentDescription.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", StringUtils.EMPTY), 600));
		parameterMap.put("out_trade_no", paymentTransaction.getSn());
		parameterMap.put("total_fee", String.valueOf(paymentTransaction.getAmount().multiply(new BigDecimal(100)).setScale(0)));
		parameterMap.put("spbill_create_ip", request.getLocalAddr());
		parameterMap.put("notify_url", getPostPayUrl(paymentPlugin, paymentTransaction));
		parameterMap.put("trade_type", "JSAPI");
		parameterMap.put("openid", getOpenId(code));
		parameterMap.put("sign", generateSign(parameterMap));

		String result = WebUtils.post(PREPAY_ID_REQUEST_URL, XmlUtils.toXml(parameterMap));
		Map<String, String> resultMap = XmlUtils.toObject(result, new TypeReference<Map<String, String>>() {
		});

		String prepayId = resultMap.get("prepay_id");
		String tradeType = resultMap.get("trade_type");
		String resultCode = resultMap.get("result_code");

		if (StringUtils.equals(tradeType, "JSAPI") && StringUtils.equals(resultCode, "SUCCESS")) {
			Map<String, Object> modelMap = new TreeMap<>();
			modelMap.put("appId", getAppId());
			modelMap.put("timeStamp", new Date().getTime());
			modelMap.put("nonceStr", DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
			modelMap.put("package", "prepay_id=" + prepayId);
			modelMap.put("signType", "MD5");
			modelMap.put("paySign", generateSign(modelMap));
			modelMap.put("postPayUrl", getPostPayUrl(paymentPlugin, paymentTransaction));
			modelAndView.addAllObjects(modelMap);
			modelAndView.setViewName("shop/plugin/weixin_public_payment/pay");
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
	 * 获取AppID
	 * 
	 * @return AppID
	 */
	private String getAppId() {
		return getAttribute("appId");
	}

	/**
	 * 获取AppSecret
	 * 
	 * @return AppSecret
	 */
	private String getAppSecret() {
		return getAttribute("appSecret");
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
	 * 获取API密钥
	 * 
	 * @return API密钥
	 */
	private String getApiKey() {
		return getAttribute("apiKey");
	}

	/**
	 * 获取OpenID
	 * 
	 * @param code
	 *            code值
	 * @return OpenID
	 */
	private String getOpenId(String code) {
		try {
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
			TokenRequestBuilder tokenRequestBuilder = OAuthClientRequest.tokenLocation(OPEN_ID_REQUEST_URL);
			tokenRequestBuilder.setParameter("appid", getAppId());
			tokenRequestBuilder.setParameter("secret", getAppSecret());
			tokenRequestBuilder.setCode(code);
			tokenRequestBuilder.setGrantType(GrantType.AUTHORIZATION_CODE);
			OAuthClientRequest accessTokenRequest = tokenRequestBuilder.buildQueryMessage();
			OAuthJSONAccessTokenResponse authJSONAccessTokenResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.GET);
			return authJSONAccessTokenResponse.getParam("openid");
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (OAuthProblemException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 生成签名
	 * 
	 * @param parameterMap
	 *            参数
	 * @return 签名
	 */
	private String generateSign(Map<String, ?> parameterMap) {
		return StringUtils.upperCase(DigestUtils.md5Hex(joinKeyValue(new TreeMap<>(parameterMap), null, "&key=" + getApiKey(), "&", true)));
	}


}