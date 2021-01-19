package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.xiaoxiangshop.entity.OrderReturns;
import net.xiaoxiangshop.entity.Sn;
import net.xiaoxiangshop.service.OrderReturnsService;
import net.xiaoxiangshop.service.SnService;

/**
 * Service - 订单退货
 * 
 */
@Service
public class OrderReturnsServiceImpl extends BaseServiceImpl<OrderReturns> implements OrderReturnsService {

	@Inject
	private SnService snService;

	@Override
	@Transactional
	public boolean save(OrderReturns orderReturns) {
		Assert.notNull(orderReturns, "[Assertion failed] - orderReturns is required; it must not be null");

		orderReturns.setSn(snService.generate(Sn.Type.ORDER_RETURNS));

		return super.save(orderReturns);
	}

}