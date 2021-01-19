package net.xiaoxiangshop.service;

import java.util.Map;

import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.entity.ProductNotify;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.User;

/**
 * Service - 邮件
 * 
 */
public interface MailService {

	/**
	 * 发送邮件
	 * 
	 * @param smtpHost
	 *            SMTP服务器地址
	 * @param smtpPort
	 *            SMTP服务器端口
	 * @param smtpUsername
	 *            SMTP用户名
	 * @param smtpPassword
	 *            SMTP密码
	 * @param smtpSSLEnabled
	 *            SMTP是否启用SSL
	 * @param smtpFromMail
	 *            发件人邮箱
	 * @param toMails
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @param async
	 *            是否异步
	 */
	void send(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword, boolean smtpSSLEnabled, String smtpFromMail, String[] toMails, String subject, String content, boolean async);

	/**
	 * 发送邮件
	 * 
	 * @param smtpHost
	 *            SMTP服务器地址
	 * @param smtpPort
	 *            SMTP服务器端口
	 * @param smtpUsername
	 *            SMTP用户名
	 * @param smtpPassword
	 *            SMTP密码
	 * @param smtpSSLEnabled
	 *            SMTP是否启用SSL
	 * @param smtpFromMail
	 *            发件人邮箱
	 * @param toMails
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param templatePath
	 *            模板路径
	 * @param model
	 *            数据
	 * @param async
	 *            是否异步
	 */
	void send(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword, boolean smtpSSLEnabled, String smtpFromMail, String[] toMails, String subject, String templatePath, Map<String, Object> model, boolean async);

	/**
	 * 发送邮件
	 * 
	 * @param toMails
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @param async
	 *            是否异步
	 */
	void send(String[] toMails, String subject, String content, boolean async);

	/**
	 * 发送邮件
	 * 
	 * @param toMails
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param templatePath
	 *            模板路径
	 * @param model
	 *            数据
	 * @param async
	 *            是否异步
	 */
	void send(String[] toMails, String subject, String templatePath, Map<String, Object> model, boolean async);

	/**
	 * 发送邮件(异步)
	 * 
	 * @param toMail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 */
	void send(String toMail, String subject, String content);

	/**
	 * 发送邮件(异步)
	 * 
	 * @param toMail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param templatePath
	 *            模板路径
	 * @param model
	 *            数据
	 */
	void send(String toMail, String subject, String templatePath, Map<String, Object> model);

	/**
	 * 发送SMTP测试邮件(同步)
	 * 
	 * @param smtpHost
	 *            SMTP服务器地址
	 * @param smtpPort
	 *            SMTP服务器端口
	 * @param smtpUsername
	 *            SMTP用户名
	 * @param smtpPassword
	 *            SMTP密码
	 * @param smtpSSLEnabled
	 *            SMTP是否启用SSL
	 * @param smtpFromMail
	 *            发件人邮箱
	 * @param toMail
	 *            收件人邮箱
	 */
	void sendTestSmtpMail(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword, boolean smtpSSLEnabled, String smtpFromMail, String toMail);

	/**
	 * 发送忘记密码邮件(异步)
	 * 
	 * @param user
	 *            用户
	 */
	void sendForgotPasswordMail(User user);

	/**
	 * 发送到货通知邮件(异步)
	 * 
	 * @param productNotify
	 *            到货通知
	 */
	void sendProductNotifyMail(ProductNotify productNotify);

	/**
	 * 发送会员注册邮件(异步)
	 * 
	 * @param member
	 *            会员
	 */
	void sendRegisterMemberMail(Member member);

	/**
	 * 发送订单创建邮件(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendCreateOrderMail(Order order);

	/**
	 * 发送订单更新邮件(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendUpdateOrderMail(Order order);

	/**
	 * 发送订单取消邮件(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendCancelOrderMail(Order order);

	/**
	 * 发送订单审核邮件(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendReviewOrderMail(Order order);

	/**
	 * 发送订单收款邮件(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendPaymentOrderMail(Order order);

	/**
	 * 发送订单退款邮件(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendRefundsOrderMail(Order order);

	/**
	 * 发送订单发货邮件(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendShippingOrderMail(Order order);

	/**
	 * 发送订单退货邮件(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendReturnsOrderMail(Order order);

	/**
	 * 发送订单收货邮件(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendReceiveOrderMail(Order order);

	/**
	 * 发送订单完成邮件(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendCompleteOrderMail(Order order);

	/**
	 * 发送订单失败邮件(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendFailOrderMail(Order order);

	/**
	 * 发送商家注册邮件(异步)
	 * 
	 * @param business
	 *            商家
	 */
	void sendRegisterBusinessMail(Business business);

	/**
	 * 发送店铺审核成功邮件(异步)
	 * 
	 * @param store
	 *            店铺
	 */
	void sendApprovalStoreMail(Store store);

	/**
	 * 发送店铺审核失败邮件(异步)
	 * 
	 * @param store
	 *            店铺
	 * @param content
	 *            内容
	 */
	void sendFailStoreMail(Store store, String content);

}