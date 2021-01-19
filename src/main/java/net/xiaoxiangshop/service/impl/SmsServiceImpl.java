package net.xiaoxiangshop.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

import com.alibaba.fastjson.JSONObject;
import net.sf.json.JSONArray;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.dao.SmsDao;
import net.xiaoxiangshop.service.RedisService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.fasterxml.jackson.core.type.TypeReference;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.TemplateConfig;
import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.MessageConfig;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.service.MessageConfigService;
import net.xiaoxiangshop.service.SmsService;
import net.xiaoxiangshop.util.JsonUtils;
import net.xiaoxiangshop.util.SecurityUtils;
import net.xiaoxiangshop.util.SystemUtils;
import net.xiaoxiangshop.util.WebUtils;

/**
 * Service - 短信
 * 
 */
@Service
public class SmsServiceImpl implements SmsService {
	private static final Logger _logger = LoggerFactory.getLogger(SmsServiceImpl.class);

	@Inject
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Inject
	private TaskExecutor taskExecutor;
	@Inject
	private MessageConfigService messageConfigService;
	@Inject
	private SmsDao smsDao;
	@Inject
	private RedisService redisService;

	/**
	 * 添加短信发送任务
	 * 
	 * @param mobiles
	 *            手机号码
	 * @param content
	 *            内容
	 * @param sendTime
	 *            发送时间
	 */
	private void addSendTask(final String[] mobiles, final String content, final Date sendTime) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				send(mobiles, content, sendTime);
			}
		});
	}

	/**
	 * 发送短信
	 * 
	 * @param mobiles
	 *            手机号码
	 * @param content
	 *            内容
	 * @param sendTime
	 *            发送时间
	 */
	private void send(String[] mobiles, String content, Date sendTime) {
		Assert.notEmpty(mobiles, "[Assertion failed] - mobiles must not be empty: it must contain at least 1 element");
		Assert.hasText(content, "[Assertion failed] - content must have text; it must not be null, empty, or blank");

		Setting setting = SystemUtils.getSetting();
		String smsAppId = setting.getSmsAppId();
		String smsSecretKey = setting.getSmsSecretKey();
		String youyiSmsUrl=setting.getYouyiSmsUrl();
		if (StringUtils.isEmpty(smsAppId) || StringUtils.isEmpty(smsSecretKey)||mobiles.length<1) {
			return;
		}
		try {
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("account",smsAppId);//账号
			parameterMap.put("pswd",smsSecretKey);//密码
			parameterMap.put("msg",content);//短信内容
			parameterMap.put("needstatus", true);//是否需要状态报告
			parameterMap.put("extno", "");//扩展码
			String reslut="";
		_logger.info("------------sms mobile:"+ JSONArray.fromObject(mobiles));
			if(mobiles.length==1){//------单发
				parameterMap.put("mobile",mobiles[0]);
				String url = youyiSmsUrl+"/msg/HttpBatchSendSM";
				String parameterMapStr = JSONObject.toJSONString(parameterMap);
				_logger.debug("------------sms send"+"url:"+url+";para:"+parameterMapStr);
				reslut=WebUtils.post(url,parameterMap);

			}else{//------群发
				String mobile="";
				for(String m:mobiles){
					if("".equals(mobile)){
						mobile=m;
					}else{
						mobile+=","+m;
					}
				}
				parameterMap.put("mobile",mobile);
				reslut=WebUtils.post(youyiSmsUrl+"/msg/HttpBatchSendSM",parameterMap);
			}
			_logger.info("------------sms return"+"url:"+reslut);
			//添加发送记录
				String resptime="";
				String respstatus="";
				String msgid="";
				if(reslut!=null&&!"".equals(reslut)){
					if(reslut.indexOf("\n")!=-1){
						String[] r= reslut.split("\n");
						resptime=r[0].split(",")[0];
						respstatus=r[0].split(",")[1];
						if("0".equals(respstatus)){
							msgid=r[1];
						}
					}else{
						String[] r= reslut.split(",");
						resptime=r[0];
						respstatus=r[1];
					}
					parameterMap.put("resptime",resptime);
					parameterMap.put("respstatus",respstatus);
					parameterMap.put("msgid",msgid);
					//插入短信发送记录表
					smsDao.insetSmsInfo(parameterMap);
		        }
		}
		catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void send(String[] mobiles, String content, Date sendTime, boolean async) {
		Assert.notEmpty(mobiles, "[Assertion failed] - mobiles must not be empty: it must contain at least 1 element");
		Assert.hasText(content, "[Assertion failed] - content must have text; it must not be null, empty, or blank");

		if (async) {
			addSendTask(mobiles, content, sendTime);
		} else {
			send(mobiles, content, sendTime);
		}
	}

	@Override
	public void send(String[] mobiles, String templatePath, Map<String, Object> model, Date sendTime, boolean async) {
		Assert.notEmpty(mobiles, "[Assertion failed] - mobiles must not be empty: it must contain at least 1 element");
		Assert.hasText(templatePath, "[Assertion failed] - templatePath must have text; it must not be null, empty, or blank");

		try {
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate(templatePath);
			String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			send(mobiles, content, sendTime, async);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void send(String mobile, String content) {
		Assert.hasText(mobile, "[Assertion failed] - mobile must have text; it must not be null, empty, or blank");
		Assert.hasText(content, "[Assertion failed] - content must have text; it must not be null, empty, or blank");

		send(new String[] { mobile }, content, null, true);
	}

	@Override
	public void send(String mobile, String templatePath, Map<String, Object> model) {
		Assert.hasText(mobile, "[Assertion failed] - mobile must have text; it must not be null, empty, or blank");
		Assert.hasText(templatePath, "[Assertion failed] - templatePath must have text; it must not be null, empty, or blank");

		send(new String[] { mobile }, templatePath, model, null, true);
	}

	@Override
	public void sendRegisterMemberSms(Member member) {
		if (member == null || StringUtils.isEmpty(member.getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.REGISTER_MEMBER);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("member", member);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("registerMemberSms");
		send(member.getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendCreateOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.CREATE_ORDER);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("createOrderSms");
		send(order.getMember().getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendUpdateOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.UPDATE_ORDER);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("updateOrderSms");
		send(order.getMember().getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendCancelOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.CANCEL_ORDER);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("cancelOrderSms");
		send(order.getMember().getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendReviewOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.REVIEW_ORDER);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("reviewOrderSms");
		send(order.getMember().getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendPaymentOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.PAYMENT_ORDER);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("paymentOrderSms");
		send(order.getMember().getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendRefundsOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.REFUNDS_ORDER);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("refundsOrderSms");
		send(order.getMember().getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendShippingOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.SHIPPING_ORDER);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("shippingOrderSms");
		send(order.getMember().getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendReturnsOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.RETURNS_ORDER);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("returnsOrderSms");
		send(order.getMember().getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendReceiveOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.RECEIVE_ORDER);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("receiveOrderSms");
		send(order.getMember().getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendCompleteOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.COMPLETE_ORDER);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("completeOrderSms");
		send(order.getMember().getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendFailOrderSms(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.FAIL_ORDER);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("failOrderSms");
		send(order.getMember().getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendRegisterBusinessSms(Business business) {
		if (business == null || StringUtils.isEmpty(business.getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.REGISTER_BUSINESS);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("business", business);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("registerBusinessSms");
		send(business.getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendApprovalStoreSms(Store store) {
		if (store == null || StringUtils.isEmpty(store.getMobile())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.APPROVAL_STORE);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("store", store);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("approvalStoreSms");
		send(store.getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendFailStoreSms(Store store, String content) {
		if (store == null || StringUtils.isEmpty(store.getMobile()) || StringUtils.isEmpty(content)) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.FAIL_STORE);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<>();
		model.put("store", store);
		model.put("content", content);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("failStoreSms");
		send(store.getMobile(), templateConfig.getTemplatePath(), model);
	}

	@Override
	public long getBalance() {
		Setting setting = SystemUtils.getSetting();
		String smsAppId = setting.getSmsAppId();
		String smsSecretKey = setting.getSmsSecretKey();

		Assert.hasText(smsAppId, "[Assertion failed] - smsAppId must have text; it must not be null, empty, or blank");
		Assert.hasText(smsSecretKey, "[Assertion failed] - smsSecretKey must have text; it must not be null, empty, or blank");

		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(smsSecretKey.getBytes(), SecurityUtils.AES_KEY_ALGORITHM);
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("requestTime", new Date().getTime());
			parameterMap.put("requestValidPeriod", 60);
			byte[] encodedParameter = SecurityUtils.encrypt(secretKeySpec, JsonUtils.toJson(parameterMap).getBytes("UTF-8"), SecurityUtils.AES_TRANSFORMATION);
			Header header = new BasicHeader("appId", smsAppId);
			byte[] byteArrayResult = WebUtils.post("http://bjmtn.b2m.cn/inter/getBalance", header, new ByteArrayEntity(encodedParameter), byte[].class);
			if (ArrayUtils.isEmpty(byteArrayResult)) {
				throw new ConnectException();
			}

			String result = new String(SecurityUtils.decrypt(secretKeySpec, byteArrayResult, SecurityUtils.AES_TRANSFORMATION));
			Map<String, Object> data = JsonUtils.toObject(result, new TypeReference<Map<String, Object>>() {
			});
			return Long.parseLong(data.get("balance").toString());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	/**-------------短信验证码------------------------------*/

	/**
	 * 发送验证码接口
	 * @param businessType 0会员卡绑定 1登陆 2修改密码
	 * @param mobile 手机号
	 * @return 返回验证码有效时间(秒)
	 */
	public Integer sendSmsCode(String businessType,String mobile){
		//
		long expire =redisService.getExpire(businessType+mobile);
//		System.out.println("000000000:"+expire);
		if(expire>0){
			return Integer.valueOf(String.valueOf(expire));
		}

		String verificationCode = String.valueOf((int)((Math.random()*9+1)*1000));
		Setting setting = SystemUtils.getSetting();
		Integer youyiHySmsCodeTime=setting.getYouyiHySmsCodeTime();
		String content="";
		if("0".equals(businessType)){//会员绑定验证码短信模板
			content = setting.getYouyiHySmsTemplate();
		}else if("1".equals(businessType)){//登陆验证码短信模板
			content = setting.getYouyiLoginSmsTemplate();
		}else if("2".equals(businessType)){//修改密码验证码短信模板
			content = setting.getYouyiEditPwdSmsTemplate();
		}else{
			//其他模板--后续可扩展
		}
		content=content.replace("code",verificationCode);
		String[] mobiles={mobile};
		//发送短信
		//需要发送短信放开
//		send(mobiles,content,null);
		//验证码存入redis
//		redisService.del(businessType+mobile);
//		System.out.println("==================:"+redisService.get(businessType+mobile));
		redisService.set(businessType+mobile,verificationCode,youyiHySmsCodeTime);
//		System.out.println("++++++++++++++++++"+redisService.get(businessType+mobile));
		System.out.println("验证码为："+verificationCode);
		System.out.println("短信内容：：："+content);
		return youyiHySmsCodeTime;
	}

	public ResponseEntity<?> checkSmsCode(String businessType, String mobile, String smsCode){
		if(businessType==null||"".equals(businessType)){
			return Results.badRequest("业务类型不能为空!");
		}
		if(mobile==null||"".equals(mobile)){
			return Results.badRequest("手机号不能为空!");
		}
		if(smsCode==null||"".equals(smsCode)){
			return Results.badRequest("验证码不能为空!");
		}
		System.out.println("============:"+businessType+mobile);
		Object redisObj=redisService.get(businessType+mobile);
		if(redisObj==null){
			return Results.badRequest("验证码已失效!");
		}
		String sms_code=redisObj.toString();
		if(smsCode.equals(sms_code)){
			return Results.ok("验证码校验通过!");
		}
		return Results.badRequest("验证码不正确!");
	}
}