package net.xiaoxiangshop.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import net.xiaoxiangshop.dao.SkuDao;
import net.xiaoxiangshop.dao.StockLogDao;
import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.entity.Product.Type;
import net.xiaoxiangshop.entity.Sku;
import net.xiaoxiangshop.entity.StockLog;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.service.SkuService;

/**
 * Service - SKU
 * 
 */
@Service
public class SkuServiceImpl extends BaseServiceImpl<Sku> implements SkuService {

	@Inject
	private SkuDao skuDao;
	@Inject
	private StockLogDao stockLogDao;

	@Override
	@Transactional(readOnly = true)
	public boolean snExists(String sn) {
		return skuDao.exists("sn", StringUtils.lowerCase(sn));
	}

	@Override
	@Transactional(readOnly = true)
	public Sku findBySn(String sn) {
		return skuDao.findByAttribute("sn", StringUtils.lowerCase(sn));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Sku> search(Store store, Product.Type type, String keyword, Set<Sku> excludes, Integer count) {
		return skuDao.search(store, type, keyword, excludes, count);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Sku> findList(Store store, Type type, Set<Sku> matchs, Integer count) {
		return skuDao.findList(store, type, matchs, count);
	}

	@Override
	public void addStock(Sku sku, int amount, StockLog.Type type, String memo) {
		Assert.notNull(sku, "[Assertion failed] - sku is required; it must not be null");
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

		if (amount == 0) {
			return;
		}

		Assert.notNull(sku.getStock(), "[Assertion failed] - sku stock is required; it must not be null");
//		Assert.state(sku.getStock() + amount >= 0, "[Assertion failed] - sku stock must be equal or greater than 0");

		sku.setStock(sku.getStock() + amount);
		super.update(sku);

		StockLog stockLog = new StockLog();
		stockLog.setType(type);
		stockLog.setInQuantity(amount > 0 ? amount : 0);
		stockLog.setOutQuantity(amount < 0 ? Math.abs(amount) : 0);
		stockLog.setStock(sku.getStock());
		stockLog.setMemo(memo);
		stockLog.setSku(sku);
		stockLog.setId(IdWorker.getId());
		stockLog.setCreatedDate(new Date());
		stockLog.setVersion(0L);
		stockLogDao.save(stockLog);
	}

	@Override
	public void addAllocatedStock(Sku sku, int amount) {
		Assert.notNull(sku, "[Assertion failed] - sku is required; it must not be null");
		//		if (amount <= 0) {
		//			return;
		//		}
		sku.setAllocatedStock(sku.getAllocatedStock()==null?0:sku.getAllocatedStock());
		Assert.notNull(sku.getAllocatedStock(), "[Assertion failed] - sku allocatedStock is required; it must not be null");
		Assert.state(sku.getAllocatedStock() + amount >= 0, "[Assertion failed] - sku allocatedStock must be equal or greater than 0");

		sku.setAllocatedStock(sku.getAllocatedStock() + amount);
		super.update(sku);
	}

	@Override
	@Transactional(readOnly = true)
	public void filter(List<Sku> skus) {
		CollectionUtils.filter(skus, new Predicate() {
			public boolean evaluate(Object object) {
				Sku sku = (Sku) object;
				return sku != null && sku.getStock() != null;
			}
		});
	}

}