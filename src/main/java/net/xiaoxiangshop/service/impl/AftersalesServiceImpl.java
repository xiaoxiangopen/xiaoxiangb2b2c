package net.xiaoxiangshop.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import net.xiaoxiangshop.dao.MemberDao;
import net.xiaoxiangshop.dao.StoreDao;
import net.xiaoxiangshop.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.AftersalesDao;
import net.xiaoxiangshop.dao.OrderItemDao;
import net.xiaoxiangshop.exception.ResourceNotFoundException;
import net.xiaoxiangshop.service.AftersalesService;
import net.xiaoxiangshop.service.OrderService;

/**
 * Service - 售后
 * 
 */
@Service
public class AftersalesServiceImpl extends BaseServiceImpl<Aftersales> implements AftersalesService {

	@Inject
	private AftersalesDao aftersalesDao;
	@Inject
	private OrderService orderService;
	@Inject
	private OrderItemDao orderItemDao;
	@Inject
	private StoreDao storeDao;
	@Inject
	private MemberDao memberDao;
	@Override
	@Transactional(readOnly = true)
	public List<Aftersales> findList(List<OrderItem> orderItems) {
		return aftersalesDao.findList(orderItems);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Aftersales> findPage(Aftersales.Type type, Aftersales.Status status, Member member, Store store, Pageable pageable) {
		IPage<Aftersales> iPage = getPluginsPage(pageable);
		iPage.setRecords(aftersalesDao.findPage(iPage, getPageable(pageable), type, status, member, store));
		return super.findPage(iPage, pageable);
	}

	@Override
	public void review(Aftersales aftersales, boolean passed) {
		Assert.notNull(aftersales, "[Assertion failed] - aftersales is required; it must not be null");
		Assert.state(Aftersales.Status.PENDING.equals(aftersales.getStatus()), "[Assertion failed] - aftersales status must be PENDING");

		if (passed) {
			aftersales.setStatus(Aftersales.Status.APPROVED);
		} else {
			aftersales.setStatus(Aftersales.Status.FAILED);
		}
		super.update(aftersales);
	}

	@Override
	public void complete(Aftersales aftersales) {
		Assert.notNull(aftersales, "[Assertion failed] - aftersales is required; it must not be null");
		Assert.notEmpty(aftersales.getOrderItems(), "[Assertion failed] - aftersales orderItems must not be empty: it must contain at least 1 element");
		Assert.isTrue(Aftersales.Status.APPROVED.equals(aftersales.getStatus()), "[Assertion failed] - aftersales status must be APPROVED");

		if (Aftersales.Type.AFTERSALES_RETURNS.equals(aftersales.getType())) {
//			completeReturns((AftersalesReturns) aftersales);
			AftersalesReturns aftersalesReturns =new AftersalesReturns();
			aftersalesReturns.setStore(aftersales.getStore());
			aftersalesReturns.setStatus(aftersales.getStatus());
			aftersalesReturns.setLastModifiedDate(aftersales.getLastModifiedDate());
			aftersalesReturns.setDeliveryCorp(aftersales.getDeliveryCorp());
			aftersalesReturns.setId(aftersales.getId());
			aftersalesReturns.setVersion(aftersales.getVersion());
			aftersalesReturns.setType(aftersales.getType());
			aftersalesReturns.setAftersalesItems(aftersales.getAftersalesItems());
			aftersalesReturns.setReason(aftersales.getReason());
			aftersalesReturns.setMember(aftersales.getMember());
			aftersalesReturns.setTrackingNo(aftersales.getTrackingNo());
			aftersalesReturns.setAftersalesItems(aftersales.getAftersalesItems());

			completeReturns(aftersalesReturns);
		}
		aftersales.setStatus(Aftersales.Status.COMPLETED);
		super.update(aftersales);
	}

	@Override
	public void completeReturns(AftersalesReturns aftersalesReturns) {
		Assert.notNull(aftersalesReturns, "[Assertion failed] - aftersalesReturns is required; it must not be null");
		Assert.isTrue(Aftersales.Status.APPROVED.equals(aftersalesReturns.getStatus()), "[Assertion failed] - aftersalesReturns status must be APPROVED");
		Assert.state(CollectionUtils.isNotEmpty(aftersalesReturns.getOrderItems()), "[Assertion failed] - aftersalesReturns orderItems must not be empty: it must contain at least 1 element");

		List<OrderItem> orderItems = aftersalesReturns.getOrderItems();
		if (orderItems!=null&&orderItems.get(0)!=null){
			Order order = orderItems.get(0).getOrder();
			OrderReturns orderReturns = new OrderReturns();
			List<OrderReturnsItem> orderReturnsItems = new ArrayList<>();
			for (AftersalesItem aftersalesItem : aftersalesReturns.getAftersalesItems()) {
				OrderReturnsItem orderReturnsItem = new OrderReturnsItem();
				orderReturnsItem.setSn(aftersalesItem.getOrderItem().getSn());
				orderReturnsItem.setName(aftersalesItem.getOrderItem().getName());
				orderReturnsItem.setQuantity(aftersalesItem.getQuantity());
				orderReturnsItem.setOrderReturns(orderReturns);
				orderReturnsItem.setSpecifications(aftersalesItem.getOrderItem().getSpecifications());
				orderReturnsItems.add(orderReturnsItem);
			}
			orderReturns.setOrderReturnsItems(orderReturnsItems);
			orderReturns.setShippingMethod(order.getShippingMethod());
			orderReturns.setDeliveryCorp(aftersalesReturns.getDeliveryCorp());
			orderReturns.setTrackingNo(aftersalesReturns.getTrackingNo());
			orderReturns.setShipper(order.getMember().getDisplayName());
			orderService.returns(order, orderReturns);
		}
	}

	@Override
	public void cancle(Aftersales aftersales) {
		Assert.notNull(aftersales, "[Assertion failed] - aftersales is required; it must not be null");
		Assert.isTrue(Aftersales.Status.PENDING.equals(aftersales.getStatus()) || Aftersales.Status.APPROVED.equals(aftersales.getStatus()), "[Assertion failed] - aftersales status must be PENDING or APPROVED");

		aftersales.setStatus(Aftersales.Status.CANCELED);
		super.update(aftersales);
	}

	@Override
	@Transactional(readOnly = true)
	public Long count(Aftersales.Type type, Aftersales.Status status, Long storeId, Long memberId) {
		Store store = storeDao.find(storeId);
		if (storeId != null && store == null) {
			return 0L;
		}
		Member member = memberDao.find(memberId);
		if (memberId != null && member == null) {
			return 0L;
		}
		return aftersalesDao.count(type, status, store, member);
	}

	@Override
	public void filterNotActiveAftersalesItem(Aftersales aftersales) {
		for (Iterator<AftersalesItem> iterator = aftersales.getAftersalesItems().iterator(); iterator.hasNext();) {
			AftersalesItem aftersalesItem = iterator.next();
			if (aftersalesItem.getOrderItem() == null || aftersalesItem.getQuantity() == null) {
				iterator.remove();
				continue;
			}

			OrderItem orderItem = orderItemDao.find(aftersalesItem.getOrderItem().getId());
			if (orderItem == null) {
				throw new ResourceNotFoundException();
			}
			aftersalesItem.setOrderItem(orderItem);
			aftersalesItem.setAftersales(aftersales);
		}
	}

	@Override
	public boolean existsIllegalAftersalesItems(List<AftersalesItem> aftersalesItems) {

		return CollectionUtils.exists(aftersalesItems, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				AftersalesItem aftersalesItem = (AftersalesItem) object;
				return aftersalesItem == null || aftersalesItem.getOrderItem() == null || aftersalesItem.getOrderItem().getAllowApplyAftersalesQuantity() < aftersalesItem.getQuantity();
			}
		});
	}

}