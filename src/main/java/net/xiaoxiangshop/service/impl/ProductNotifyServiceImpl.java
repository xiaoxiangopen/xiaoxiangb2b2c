package net.xiaoxiangshop.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.ProductNotifyDao;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.ProductNotify;
import net.xiaoxiangshop.entity.Sku;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.service.MailService;
import net.xiaoxiangshop.service.ProductNotifyService;

/**
 * Service - 到货通知
 * 
 */
@Service
public class ProductNotifyServiceImpl extends BaseServiceImpl<ProductNotify> implements ProductNotifyService {

	@Inject
	private ProductNotifyDao productNotifyDao;
	@Inject
	private MailService mailService;

	@Override
	@Transactional(readOnly = true)
	public boolean exists(Sku sku, String email) {
		return productNotifyDao.exists(sku, email);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductNotify> findPage(Store store, Member member, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent, Pageable pageable) {
		IPage<ProductNotify> iPage = getPluginsPage(pageable);
		iPage.setRecords(productNotifyDao.findPage(iPage, getPageable(pageable), store, member, isMarketable, isOutOfStock, hasSent));
		return super.findPage(iPage, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Long count(Member member, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent) {
		return productNotifyDao.count(member, isMarketable, isOutOfStock, hasSent);
	}

	@Override
	public int send(List<ProductNotify> productNotifies) {
		if (CollectionUtils.isEmpty(productNotifies)) {
			return 0;
		}

		int count = 0;
		for (ProductNotify productNotify : productNotifies) {
			if (productNotify != null && StringUtils.isNotEmpty(productNotify.getEmail())) {
				mailService.sendProductNotifyMail(productNotify);
				productNotify.setHasSent(true);
				productNotify.setLastModifiedDate(new Date());
				productNotifyDao.update(productNotify);
				count++;
			}
		}
		return count;
	}

}