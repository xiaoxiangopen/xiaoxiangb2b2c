package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.xiaoxiangshop.entity.OrderRefunds;
import net.xiaoxiangshop.entity.Sn;
import net.xiaoxiangshop.service.OrderRefundsService;
import net.xiaoxiangshop.service.SnService;

/**
 * Service - 订单退款
 * 
 */
@Service
public class OrderRefundsServiceImpl extends BaseServiceImpl<OrderRefunds> implements OrderRefundsService {

	@Inject
	private SnService snService;

	@Override
	@Transactional
	public boolean save(OrderRefunds orderRefunds) {
		Assert.notNull(orderRefunds, "[Assertion failed] - orderRefunds is required; it must not be null");

		orderRefunds.setSn(snService.generate(Sn.Type.ORDER_REFUNDS));

		return super.save(orderRefunds);
	}

}