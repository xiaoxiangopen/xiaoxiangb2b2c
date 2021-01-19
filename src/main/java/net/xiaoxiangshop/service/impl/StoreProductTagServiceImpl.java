package net.xiaoxiangshop.service.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.StoreDao;
import net.xiaoxiangshop.dao.StoreProductTagDao;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StoreProductTag;
import net.xiaoxiangshop.service.StoreProductTagService;

/**
 * Service - 店铺商品标签
 * 
 */
@Service
public class StoreProductTagServiceImpl extends BaseServiceImpl<StoreProductTag> implements StoreProductTagService {

	@Inject
	private StoreProductTagDao storeProductTagDao;
	@Inject
	private StoreDao storeDao;

	@Override
	@Transactional(readOnly = true)
	public List<StoreProductTag> findList(Store store, Boolean isEnabled) {
		QueryWrapper<StoreProductTag> queryWrapper = createQueryWrapper(null, null, null, null);
		return storeProductTagDao.findList(queryWrapper, store, isEnabled);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "storeProductTag", condition = "#useCache")
	public List<StoreProductTag> findList(Long storeId, Boolean isEnabled, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		Store store = storeDao.find(storeId);
		if (storeId != null && store == null) {
			return Collections.emptyList();
		}

		QueryWrapper<StoreProductTag> queryWrapper = createQueryWrapper(null, null, null, null);
		return storeProductTagDao.findList(queryWrapper, store, isEnabled);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StoreProductTag> findPage(Store store, Pageable pageable) {
		IPage<StoreProductTag> iPage = getPluginsPage(pageable);
		iPage.setRecords(storeProductTagDao.findPage(iPage, getPageable(pageable), store));
		return super.findPage(iPage, pageable);
	}

	@Override
	@CacheEvict(value = "storeProductTag", allEntries = true)
	public boolean save(StoreProductTag entity) {
		return super.save(entity);
	}

	@Override
	@CacheEvict(value = "storeProductTag", allEntries = true)
	public StoreProductTag update(StoreProductTag entity) {
		return super.update(entity);
	}

	@Override
	@CacheEvict(value = "storeProductTag", allEntries = true)
	public StoreProductTag update(StoreProductTag entity, String... ignoreProperties) {
		return super.update(entity, ignoreProperties);
	}

	@Override
	@CacheEvict(value = "storeProductTag", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@CacheEvict(value = "storeProductTag", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@CacheEvict(value = "storeProductTag", allEntries = true)
	public void delete(StoreProductTag entity) {
		super.delete(entity);
	}

}