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
import net.xiaoxiangshop.dao.StoreAdImageDao;
import net.xiaoxiangshop.dao.StoreDao;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StoreAdImage;
import net.xiaoxiangshop.service.StoreAdImageService;

/**
 * Service - 店铺广告图片
 * 
 */
@Service
public class StoreAdImageServiceImpl extends BaseServiceImpl<StoreAdImage> implements StoreAdImageService {

	@Inject
	private StoreAdImageDao storeAdImageDao;
	@Inject
	private StoreDao storeDao;

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "storeAdImage", condition = "#useCache")
	public List<StoreAdImage> findList(Long storeId, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		Store store = storeDao.find(storeId);
		if (storeId != null && store == null) {
			return Collections.emptyList();
		}

		QueryWrapper<StoreAdImage> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return storeAdImageDao.findList(queryWrapper, store);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StoreAdImage> findPage(Store store, Pageable pageable) {
		IPage<StoreAdImage> iPage = getPluginsPage(pageable);
		iPage.setRecords(storeAdImageDao.findPage(iPage, getPageable(pageable), store));
		return super.findPage(iPage, pageable);
	}

	@Override
	@CacheEvict(value = "storeAdImage", allEntries = true)
	public boolean save(StoreAdImage entity) {
		return super.save(entity);
	}

	@Override
	@CacheEvict(value = "storeAdImage", allEntries = true)
	public StoreAdImage update(StoreAdImage entity) {
		return super.update(entity);
	}

	@Override
	@CacheEvict(value = "storeAdImage", allEntries = true)
	public StoreAdImage update(StoreAdImage entity, String... ignoreProperties) {
		return super.update(entity, ignoreProperties);
	}

	@Override
	@CacheEvict(value = "storeAdImage", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@CacheEvict(value = "storeAdImage", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@CacheEvict(value = "storeAdImage", allEntries = true)
	public void delete(StoreAdImage entity) {
		super.delete(entity);
	}

}