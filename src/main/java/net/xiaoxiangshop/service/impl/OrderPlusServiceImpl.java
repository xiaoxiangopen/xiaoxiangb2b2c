package net.xiaoxiangshop.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.sf.ehcache.CacheManager;
import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.*;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.entity.Order.CommissionType;
import net.xiaoxiangshop.entity.Order.Status;
import net.xiaoxiangshop.entity.Order.Type;
import net.xiaoxiangshop.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

/**
 * Service - 订单
 * 
 */
@Service
public class OrderPlusServiceImpl extends BaseServiceImpl<OrderPlus> implements OrderPlusService {

	@Inject
	private CacheManager cacheManager;
	@Inject
	private StoreDao storeDao;
	@Inject
	private MemberDao memberDao;
	@Inject
	private ProductDao productDao;
	@Inject
	private OrderPlusDao orderDao;
	@Inject
	private OrderPlusItemDao orderItemDao;
	@Inject
	private CartDao cartDao;
	@Inject
	private OrderShippingDao orderShippingDao;
	@Inject
	private OrderShippingItemDao orderShippingItemDao;



	@Override
	@Transactional(readOnly = true)
	public Page<OrderPlus> findPage(Type type, OrderPlus.Status status, Store store, Member member, Product product, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable) {
		IPage<OrderPlus> iPage = getPluginsPage(pageable);
		if (pageable.getOrderDirection() == null && pageable.getOrderProperty() == null) {
			pageable.setOrderDirection(net.xiaoxiangshop.Order.Direction.DESC);
			pageable.setOrderProperty("createdDate");
		}
		iPage.setRecords(orderDao.findPage(iPage, getPageable(pageable), type, status, store, member, product, isPendingReceive, isPendingRefunds, isExchangePoint, isAllocatedStock, hasExpired));
		return super.findPage(iPage, pageable);
	}

	@Override
	public Set<OrderPlus> findListByUserId(Member member) {
		return orderDao.findSet("member_id",member.getId());
	}
}