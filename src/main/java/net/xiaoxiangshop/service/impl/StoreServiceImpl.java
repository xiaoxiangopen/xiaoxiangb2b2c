package net.xiaoxiangshop.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.ProductDao;
import net.xiaoxiangshop.dao.StoreDao;
import net.xiaoxiangshop.entity.AftersalesSetting;
import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.CategoryApplication;
import net.xiaoxiangshop.entity.ProductCategory;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.repository.StoreRepository;
import net.xiaoxiangshop.service.AftersalesSettingService;
import net.xiaoxiangshop.service.BusinessService;
import net.xiaoxiangshop.service.StorePluginStatusService;
import net.xiaoxiangshop.service.StoreService;
import net.xiaoxiangshop.service.UserService;

/**
 * Service - 店铺
 * 
 */
@Service
public class StoreServiceImpl extends BaseServiceImpl<Store> implements StoreService {

	@Inject
	private StoreDao storeDao;
	@Inject
	private ProductDao productDao;
	@Inject
	private UserService userService;
	@Inject
	private BusinessService businessService;
	@Inject
	private AftersalesSettingService aftersalesSettingService;
	@Inject
	private StorePluginStatusService storePluginStatusService;
	@Inject
	private StoreRepository storeRepository;

	@Override
	@Transactional(readOnly = true)
	public boolean nameExists(String name) {
		return storeDao.exists("name", name);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean nameUnique(Long id, String name) {
		return storeDao.unique(id, "name", name);
	}

	@Override
	public boolean productCategoryExists(Store store, final ProductCategory productCategory) {
		Assert.notNull(productCategory, "[Assertion failed] - productCategory is required; it must not be null");
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

		return CollectionUtils.exists(store.getProductCategories(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				ProductCategory storeProductCategory = (ProductCategory) object;
				return storeProductCategory != null && storeProductCategory.getId().compareTo(productCategory.getId()) == 0;
			}
		});
	}

	@Override
	@Transactional(readOnly = true)
	public Store findByName(String name) {
		return storeDao.findByAttribute("name", name);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Store> findList(Store.Type type, Store.Status status, Boolean isEnabled, Boolean hasExpired, Integer first, Integer count) {
		return storeDao.findList(type, status, isEnabled, hasExpired, first, count);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findProductCategoryList(Store store, CategoryApplication.Status status) {
		return storeDao.findProductCategoryList(store, status);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Store> findPage(Store.Type type, Store.Status status, Boolean isEnabled, Boolean hasExpired, Pageable pageable) {
		IPage<Store> iPage = getPluginsPage(pageable);
		iPage.setRecords(storeDao.findPage(iPage, getPageable(pageable), type, status, isEnabled, hasExpired));
		return super.findPage(iPage, pageable);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Page<Store> search(String keyword, Pageable pageable) {
		if (StringUtils.isEmpty(keyword)) {
			return Page.emptyPage(pageable);
		}

		if (pageable == null) {
			pageable = new Pageable();
		}
		
		// es原因page从0开始不是从1开始
		Integer pageNumber = 0;
		if (pageable.getPageNumber() >= 1) {
			pageNumber = pageable.getPageNumber() - 1;
		}
		
		//多条件设置
		QueryBuilder nameQuery = QueryBuilders.matchPhraseQuery("name", keyword).boost(1.5F);
		QueryBuilder keywordQuery = QueryBuilders.matchPhraseQuery("keyword", keyword);
		QueryBuilder statusQuery = QueryBuilders.matchPhraseQuery("status", String.valueOf(Store.Status.SUCCESS));
		QueryBuilder isEnabledQuery = QueryBuilders.matchPhraseQuery("isEnabled", true);
		
		BoolQueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.boolQuery().should(nameQuery).should(keywordQuery)).must(statusQuery).must(isEnabledQuery);
		org.springframework.data.domain.Page<Store> stores = storeRepository.search(query, PageRequest.of(pageNumber, pageable.getPageSize()));
		return new Page<>(stores.getContent(), stores.getSize(), pageable);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<Store> search(String keyword) {
		if (StringUtils.isEmpty(keyword)) {
			return Collections.emptyList();
		}
		
		//多条件设置
		QueryBuilder nameQuery = QueryBuilders.matchPhraseQuery("name", keyword).boost(1.5F);
		QueryBuilder keywordQuery = QueryBuilders.matchPhraseQuery("keyword", keyword);
		QueryBuilder statusQuery = QueryBuilders.matchPhraseQuery("status", String.valueOf(Store.Status.SUCCESS));
		QueryBuilder isEnabledQuery = QueryBuilders.matchPhraseQuery("isEnabled", true);
		
		BoolQueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.boolQuery().should(nameQuery).should(keywordQuery)).must(statusQuery).must(isEnabledQuery);
		Iterator<Store> iterator = storeRepository.search(query).iterator();
		return IteratorUtils.toList(iterator);
	}

	@Override
	@Transactional(readOnly = true)
	public Store getCurrent() {
		Business currentUser = userService.getCurrent(Business.class);
		return currentUser != null ? currentUser.getStore() : null;
	}

	@Override
	@CacheEvict(value = "authorization", allEntries = true)
	public void addEndDays(Store store, int amount) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

		if (amount == 0) {
			return;
		}

		Date now = new Date();
		Date currentEndDate = store.getEndDate();
		if (amount > 0) {
			store.setEndDate(DateUtils.addDays(currentEndDate.after(now) ? currentEndDate : now, amount));
		} else {
			store.setEndDate(DateUtils.addDays(currentEndDate, amount));
		}
		storeDao.update(store);
	}

	@Override
	@CacheEvict(value = "authorization", allEntries = true)
	public void addBailPaid(Store store, BigDecimal amount) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");
		Assert.notNull(amount, "[Assertion failed] - amount is required; it must not be null");

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		Assert.notNull(store.getBailPaid(), "[Assertion failed] - store bailPaid is required; it must not be null");
		Assert.state(store.getBailPaid().add(amount).compareTo(BigDecimal.ZERO) >= 0, "[Assertion failed] - store bailPaid must be equal or greater than 0");

		store.setBailPaid(store.getBailPaid().add(amount));
		storeDao.update(store);
	}

	@Override
	@CacheEvict(value = "authorization", allEntries = true)
	public void review(Store store, boolean passed, String content) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");
		Assert.state(Store.Status.PENDING.equals(store.getStatus()), "[Assertion failed] - store status must be PENDING");
		Assert.state(passed || StringUtils.isNotEmpty(content), "[Assertion failed] - passed or content must not be empty");

		if (passed) {
			BigDecimal serviceFee = store.getStoreRank().getServiceFee();
			BigDecimal bail = store.getStoreCategory().getBail();
			if (serviceFee.compareTo(BigDecimal.ZERO) <= 0 && bail.compareTo(BigDecimal.ZERO) <= 0) {
				store.setStatus(Store.Status.SUCCESS);
				store.setEndDate(DateUtils.addYears(new Date(), 1));
			} else {
				store.setStatus(Store.Status.APPROVED);
				store.setEndDate(new Date());
			}
			AftersalesSetting aftersalesSetting = new AftersalesSetting();
			aftersalesSetting.setStore(store);
			aftersalesSettingService.save(aftersalesSetting);
		} else {
			store.setStatus(Store.Status.FAILED);
		}
		super.update(store);
	}

	@Override
	@CacheEvict(value = { "authorization", "product", "productCategory" }, allEntries = true)
	public void expiredStoreProcessing() {
		productDao.refreshExpiredStoreProductActive();
	}

	@Override
	@Transactional
	@CacheEvict(value = { "authorization", "product", "productCategory" }, allEntries = true)
	public Store update(Store store) {
		productDao.refreshActive(store);

		return super.update(store);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "authorization", "product", "productCategory" }, allEntries = true)
	public Store update(Store store, String... ignoreProperties) {
		return super.update(store, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "authorization", "product", "productCategory" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "authorization", "product", "productCategory" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "authorization", "product", "productCategory" }, allEntries = true)
	public void delete(Store store) {
		super.delete(store);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal bailPaidTotalAmount() {
		return storeDao.bailPaidTotalAmount();
	}

	@Override
	@Transactional(readOnly = true)
	public Long count(Store.Type type, Store.Status status, Boolean isEnabled, Boolean hasExpired) {
		return storeDao.count(type, status, isEnabled, hasExpired);
	}

}