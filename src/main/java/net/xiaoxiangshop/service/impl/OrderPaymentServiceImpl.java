package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.xiaoxiangshop.dao.OrderPaymentDao;
import net.xiaoxiangshop.entity.OrderPayment;
import net.xiaoxiangshop.entity.Sn;
import net.xiaoxiangshop.service.OrderPaymentService;
import net.xiaoxiangshop.service.SnService;

/**
 * Service - 订单支付
 * 
 */
@Service
public class OrderPaymentServiceImpl extends BaseServiceImpl<OrderPayment> implements OrderPaymentService {

	@Inject
	private OrderPaymentDao orderPaymentDao;
	@Inject
	private SnService snService;

	@Override
	@Transactional(readOnly = true)
	public OrderPayment findBySn(String sn) {
		return orderPaymentDao.findByAttribute("sn", StringUtils.lowerCase(sn));
	}

	@Override
	@Transactional
	public boolean save(OrderPayment orderPayment) {
		Assert.notNull(orderPayment, "[Assertion failed] - orderPayment is required; it must not be null");

		orderPayment.setSn(snService.generate(Sn.Type.ORDER_PAYMENT));

		return super.save(orderPayment);
	}

}