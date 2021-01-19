package net.xiaoxiangshop.service;

import java.util.Date;
import java.util.Map;

import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.entity.Store;
import org.springframework.http.ResponseEntity;

/**
 * Service - 短信
 * 
 */
public interface SmsService {

	/**
	 * 发送短信
	 * 
	 * @param mobiles
	 *            手机号码
	 * @param content
	 *            内容
	 * @param sendTime
	 *            发送时间
	 * @param async
	 *            是否异步
	 */
	void send(String[] mobiles, String content, Date sendTime, boolean async);

	/**
	 * 发送短信
	 * 
	 * @param mobiles
	 *            手机号码
	 * @param templatePath
	 *            模板路径
	 * @param model
	 *            数据
	 * @param sendTime
	 *            发送时间
	 * @param async
	 *            是否异步
	 */
	void send(String[] mobiles, String templatePath, Map<String, Object> model, Date sendTime, boolean async);

	/**
	 * 发送短信(异步)
	 * 
	 * @param mobile
	 *            手机号码
	 * @param content
	 *            内容
	 */
	void send(String mobile, String content);

	/**
	 * 发送短信(异步)
	 * 
	 * @param mobile
	 *            手机号码
	 * @param templatePath
	 *            模板路径
	 * @param model
	 *            数据
	 */
	void send(String mobile, String templatePath, Map<String, Object> model);

	/**
	 * 发送会员注册短信(异步)
	 * 
	 * @param member
	 *            会员
	 */
	void sendRegisterMemberSms(Member member);

	/**
	 * 发送订单创建短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendCreateOrderSms(Order order);

	/**
	 * 发送订单更新短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendUpdateOrderSms(Order order);

	/**
	 * 发送订单取消短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendCancelOrderSms(Order order);

	/**
	 * 发送订单审核短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendReviewOrderSms(Order order);

	/**
	 * 发送订单收款短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendPaymentOrderSms(Order order);

	/**
	 * 发送订单退款短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendRefundsOrderSms(Order order);

	/**
	 * 发送订单发货短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendShippingOrderSms(Order order);

	/**
	 * 发送订单退货短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendReturnsOrderSms(Order order);

	/**
	 * 发送订单收货短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendReceiveOrderSms(Order order);

	/**
	 * 发送订单完成短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendCompleteOrderSms(Order order);

	/**
	 * 发送订单失败短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendFailOrderSms(Order order);

	/**
	 * 发送商家注册短信(异步)
	 * 
	 * @param business
	 *            商家
	 */
	void sendRegisterBusinessSms(Business business);

	/**
	 * 发送店铺审核成功短信(异步)
	 * 
	 * @param store
	 *            店铺
	 */
	void sendApprovalStoreSms(Store store);

	/**
	 * 发送店铺审核失败短信(异步)
	 * 
	 * @param store
	 *            店铺
	 * @param content
	 *            内容
	 */
	void sendFailStoreSms(Store store, String content);

	/**
	 * 获取短信余额
	 * 
	 * @return 短信余额
	 */
	long getBalance();

	/**
	 * 发送验证码
	 * @param businessType
	 * @param mobile
	 * @return
	 */
	Integer sendSmsCode(String businessType,String mobile);

	/**
	 * 校验验证码是否有效
	 * @param businessType
	 * @param mobile
	 * @param smsCode
	 * @return
	 */
	ResponseEntity<?> checkSmsCode(String businessType, String mobile, String smsCode);

}