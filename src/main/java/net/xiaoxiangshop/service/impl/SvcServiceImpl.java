package net.xiaoxiangshop.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.dao.SvcDao;
import net.xiaoxiangshop.entity.Sn;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StoreRank;
import net.xiaoxiangshop.entity.Svc;
import net.xiaoxiangshop.service.SnService;
import net.xiaoxiangshop.service.SvcService;

/**
 * Service - 服务
 * 
 */
@Service
public class SvcServiceImpl extends BaseServiceImpl<Svc> implements SvcService {

	@Inject
	private SvcDao svcDao;
	@Inject
	private SnService snService;

	@Override
	@Transactional(readOnly = true)
	public Svc findBySn(String sn) {
		return svcDao.findByAttribute("sn", StringUtils.lowerCase(sn));
	}

	@Override
	@Transactional(readOnly = true)
	public Svc findTheLatest(Store store, String promotionPluginId, StoreRank storeRank) {

		List<Order> orderList = new ArrayList<>();
		orderList.add(new Order("createdDate", Order.Direction.DESC));
		List<Svc> serviceOrders = svcDao.findByStore(store, promotionPluginId, storeRank);

		return CollectionUtils.isNotEmpty(serviceOrders) ? serviceOrders.get(0) : null;
	}

	@Override
	@Transactional
	public boolean save(Svc svc) {
		Assert.notNull(svc, "[Assertion failed] - svc is required; it must not be null");

		svc.setSn(snService.generate(Sn.Type.PLATFORM_SERVICE));

		return super.save(svc);
	}

}