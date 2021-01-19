package net.xiaoxiangshop.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.xiaoxiangshop.listener.InitListener;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.TemplateConfig;
import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.MessageConfig;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.entity.ProductNotify;
import net.xiaoxiangshop.entity.SafeKey;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.User;
import net.xiaoxiangshop.entity.User.Type;
import net.xiaoxiangshop.service.MailService;
import net.xiaoxiangshop.service.MessageConfigService;
import net.xiaoxiangshop.util.SpringUtils;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * Service - 邮件
 * 
 */
@Service
public class MailServiceImpl implements MailService {
	private static final Logger LOGGER = Logger.getLogger(MailServiceImpl.class.getName());
	@Inject
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Inject
	private TaskExecutor taskExecutor;
	@Inject
	private MessageConfigService messageConfigService;

	/**
	 * 添加邮件发送任务
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
	 */
	private void addSendTask(final String smtpHost, final int smtpPort, final String smtpUsername, final String smtpPassword, final boolean smtpSSLEnabled, final String smtpFromMail, final String[] toMails, final String subject, final String content) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				send(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSLEnabled, smtpFromMail, toMails, subject, content);
			}
		});
	}

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
	 */
	private void send(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword, boolean smtpSSLEnabled, String smtpFromMail, String[] toMails, String subject, String content) {
		Assert.hasText(smtpHost, "[Assertion failed] - smtpHost must have text; it must not be null, empty, or blank");
		Assert.hasText(smtpUsername, "[Assertion failed] - smtpUsername must have text; it must not be null, empty, or blank");
		Assert.hasText(smtpPassword, "[Assertion failed] - smtpPassword must have text; it must not be null, empty, or blank");
		Assert.hasText(smtpFromMail, "[Assertion failed] - smtpFromMail must have text; it must not be null, empty, or blank");
		Assert.notEmpty(toMails, "[Assertion failed] - toMails must not be empty: it must contain at least 1 element");
		Assert.hasText(subject, "[Assertion failed] - subject must have text; it must not be null, empty, or blank");
		Assert.hasText(content, "[Assertion failed] - content must have text; it must not be null, empty, or blank");

		try {
			Setting setting = SystemUtils.getSetting();
			HtmlEmail email = new HtmlEmail();
			email.setHostName(smtpHost);
			email.setSmtpPort(smtpPort);
			email.setAuthentication(smtpUsername, smtpPassword);
			email.setSSLOnConnect(smtpSSLEnabled);
			email.setFrom(smtpFromMail, setting.getSiteName());
			email.addTo(toMails);
			email.setSubject(subject);
			email.setCharset("UTF-8");
			email.setHtmlMsg(content);
			LOGGER.info("----------------email send: "+email.send());
			LOGGER.info("----------------email end: ");
		} catch (EmailException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void send(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword, boolean smtpSSLEnabled, String smtpFromMail, String[] toMails, String subject, String content, boolean async) {
		Assert.hasText(smtpHost, "[Assertion failed] - smtpHost must have text; it must not be null, empty, or blank");
		Assert.hasText(smtpUsername, "[Assertion failed] - smtpUsername must have text; it must not be null, empty, or blank");
		Assert.hasText(smtpPassword, "[Assertion failed] - smtpPassword must have text; it must not be null, empty, or blank");
		Assert.hasText(smtpFromMail, "[Assertion failed] - smtpFromMail must have text; it must not be null, empty, or blank");
		Assert.notEmpty(toMails, "[Assertion failed] - toMails must not be empty: it must contain at least 1 element");
		Assert.hasText(subject, "[Assertion failed] - subject must have text; it must not be null, empty, or blank");
		Assert.hasText(content, "[Assertion failed] - content must have text; it must not be null, empty, or blank");

		if (async) {
			addSendTask(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSLEnabled, smtpFromMail, toMails, subject, content);
		} else {
			send(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSLEnabled, smtpFromMail, toMails, subject, content);
		}
	}

	@Override
	public void send(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword, boolean smtpSSLEnabled, String smtpFromMail, String[] toMails, String subject, String templatePath, Map<String, Object> model, boolean async) {
		Assert.hasText(smtpHost, "[Assertion failed] - smtpHost must have text; it must not be null, empty, or blank");
		Assert.hasText(smtpUsername, "[Assertion failed] - smtpUsername must have text; it must not be null, empty, or blank");
		Assert.hasText(smtpPassword, "[Assertion failed] - smtpPassword must have text; it must not be null, empty, or blank");
		Assert.hasText(smtpFromMail, "[Assertion failed] - smtpFromMail must have text; it must not be null, empty, or blank");
		Assert.notEmpty(toMails, "[Assertion failed] - toMails must not be empty: it must contain at least 1 element");
		Assert.hasText(subject, "[Assertion failed] - subject must have text; it must not be null, empty, or blank");
		Assert.hasText(templatePath, "[Assertion failed] - templatePath must have text; it must not be null, empty, or blank");

		try {
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate(templatePath);
			String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			send(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSLEnabled, smtpFromMail, toMails, subject, content, async);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void send(String[] toMails, String subject, String content, boolean async) {
		Assert.notEmpty(toMails, "[Assertion failed] - toMails must not be empty: it must contain at least 1 element");
		Assert.hasText(subject, "[Assertion failed] - subject must have text; it must not be null, empty, or blank");
		Assert.hasText(content, "[Assertion failed] - content must have text; it must not be null, empty, or blank");

		Setting setting = SystemUtils.getSetting();
		String smtpHost = setting.getSmtpHost();
		int smtpPort = setting.getSmtpPort();
		String smtpUsername = setting.getSmtpUsername();
		String smtpPassword = setting.getSmtpPassword();
		boolean smtpSSLEnabled = setting.getSmtpSSLEnabled();
		String smtpFromMail = setting.getSmtpFromMail();
		send(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSLEnabled, smtpFromMail, toMails, subject, content, async);
	}

	@Override
	public void send(String[] toMails, String subject, String templatePath, Map<String, Object> model, boolean async) {
		Assert.notEmpty(toMails, "[Assertion failed] - toMails must not be empty: it must contain at least 1 element");
		Assert.hasText(subject, "[Assertion failed] - subject must have text; it must not be null, empty, or blank");
		Assert.hasText(templatePath, "[Assertion failed] - templatePath must have text; it must not be null, empty, or blank");

		Setting setting = SystemUtils.getSetting();
		String smtpHost = setting.getSmtpHost();
		int smtpPort = setting.getSmtpPort();
		String smtpUsername = setting.getSmtpUsername();
		String smtpPassword = setting.getSmtpPassword();
		boolean smtpSSLEnabled = setting.getSmtpSSLEnabled();
		String smtpFromMail = setting.getSmtpFromMail();
		send(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSLEnabled, smtpFromMail, toMails, subject, templatePath, model, async);
	}

	@Override
	public void send(String toMail, String subject, String content) {
		Assert.hasText(toMail, "[Assertion failed] - toMail must have text; it must not be null, empty, or blank");
		Assert.hasText(subject, "[Assertion failed] - subject must have text; it must not be null, empty, or blank");
		Assert.hasText(content, "[Assertion failed] - content must have text; it must not be null, empty, or blank");

		send(new String[] { toMail }, subject, content, true);
	}

	@Override
	public void send(String toMail, String subject, String templatePath, Map<String, Object> model) {
		Assert.hasText(toMail, "[Assertion failed] - toMail must have text; it must not be null, empty, or blank");
		Assert.hasText(subject, "[Assertion failed] - subject must have text; it must not be null, empty, or blank");
		Assert.hasText(templatePath, "[Assertion failed] - templatePath must have text; it must not be null, empty, or blank");

		send(new String[] { toMail }, subject, templatePath, model, true);
	}

	@Override
	public void sendTestSmtpMail(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword, boolean smtpSSLEnabled, String smtpFromMail, String toMail) {
		Assert.hasText(smtpHost, "[Assertion failed] - smtpHost must have text; it must not be null, empty, or blank");
		Assert.hasText(smtpUsername, "[Assertion failed] - smtpUsername must have text; it must not be null, empty, or blank");
		Assert.hasText(smtpPassword, "[Assertion failed] - smtpPassword must have text; it must not be null, empty, or blank");
		Assert.hasText(smtpFromMail, "[Assertion failed] - smtpFromMail must have text; it must not be null, empty, or blank");
		Assert.hasText(toMail, "[Assertion failed] - toMail must have text; it must not be null, empty, or blank");

		Setting setting = SystemUtils.getSetting();
		String subject = SpringUtils.getMessage("admin.mail.testSmtpSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("testSmtpMail");
		send(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSLEnabled, smtpFromMail, new String[] { toMail }, subject, templateConfig.getTemplatePath(), null, false);
	}

	@Override
	public void sendForgotPasswordMail(User user) {
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		SafeKey safeKey = null;
		String toMail = null;
		String username = null;
		Type type = null;
		if (user instanceof Member) {
			Member member = (Member) user;
			toMail = member.getEmail();
			username = member.getUsername();
			safeKey = member.getSafeKey();
			type = User.Type.MEMBER;
		} else if (user instanceof Business) {
			Business business = (Business) user;
			toMail = business.getEmail();
			username = business.getUsername();
			safeKey = business.getSafeKey();
			type = User.Type.BUSINESS;
		}
		if (safeKey == null) {
			return;
		}
		String subject = SpringUtils.getMessage("shop.mail.forgotPasswordSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("forgotPasswordMail");
		model.put("type", type);
		model.put("username", username);
		model.put("safeKey", safeKey);
		send(toMail, subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendProductNotifyMail(ProductNotify productNotify) {
		if (productNotify == null || StringUtils.isEmpty(productNotify.getEmail())) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("productNotify", productNotify);
		String subject = SpringUtils.getMessage("shop.mail.productNotifySubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("productNotifyMail");
		send(productNotify.getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendRegisterMemberMail(Member member) {
		if (member == null || StringUtils.isEmpty(member.getEmail())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.REGISTER_MEMBER);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("member", member);
		String subject = SpringUtils.getMessage("shop.mail.registerMemberSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("registerMemberMail");
		send(member.getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendCreateOrderMail(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getEmail())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.CREATE_ORDER);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.createOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("createOrderMail");
		send(order.getMember().getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendUpdateOrderMail(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getEmail())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.UPDATE_ORDER);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.updateOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("updateOrderMail");
		send(order.getMember().getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendCancelOrderMail(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getEmail())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.CANCEL_ORDER);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.cancelOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("cancelOrderMail");
		send(order.getMember().getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendReviewOrderMail(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getEmail())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.REVIEW_ORDER);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.reviewOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("reviewOrderMail");
		send(order.getMember().getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendPaymentOrderMail(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getEmail())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.PAYMENT_ORDER);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.paymentOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("paymentOrderMail");
		send(order.getMember().getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendRefundsOrderMail(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getEmail())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.REFUNDS_ORDER);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.refundsOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("refundsOrderMail");
		send(order.getMember().getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendShippingOrderMail(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getEmail())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.SHIPPING_ORDER);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.shippingOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("shippingOrderMail");
		send(order.getMember().getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendReturnsOrderMail(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getEmail())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.RETURNS_ORDER);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.returnsOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("returnsOrderMail");
		send(order.getMember().getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendReceiveOrderMail(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getEmail())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.RECEIVE_ORDER);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.receiveOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("receiveOrderMail");
		send(order.getMember().getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendCompleteOrderMail(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getEmail())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.COMPLETE_ORDER);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.completeOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("completeOrderMail");
		send(order.getMember().getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendFailOrderMail(Order order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(order.getMember().getEmail())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.FAIL_ORDER);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.failOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("failOrderMail");
		send(order.getMember().getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendRegisterBusinessMail(Business business) {
		if (business == null || StringUtils.isEmpty(business.getEmail())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.REGISTER_BUSINESS);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("business", business);
		String subject = SpringUtils.getMessage("shop.mail.registerBusinessSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("registerBusinessMail");
		send(business.getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendApprovalStoreMail(Store store) {
		if (store == null || StringUtils.isEmpty(store.getEmail())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.APPROVAL_STORE);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("store", store);
		String subject = SpringUtils.getMessage("shop.mail.approvalStoreSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("approvalStoreMail");
		send(store.getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

	@Override
	public void sendFailStoreMail(Store store, String content) {
		if (store == null || StringUtils.isEmpty(store.getEmail()) || StringUtils.isEmpty(content)) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.findByType(MessageConfig.Type.FAIL_STORE);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<>();
		model.put("store", store);
		model.put("content", content);
		String subject = SpringUtils.getMessage("shop.mail.failStoreSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("failStoreMail");
		send(store.getEmail(), subject, templateConfig.getTemplatePath(), model);
	}

}