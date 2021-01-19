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
import net.xiaoxiangshop.dao.InstantMessageDao;
import net.xiaoxiangshop.dao.StoreDao;
import net.xiaoxiangshop.entity.InstantMessage;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.service.InstantMessageService;

/**
 * Service - 即时通讯
 * 
 */
@Service
public class InstantMessageServiceImpl extends BaseServiceImpl<InstantMessage> implements InstantMessageService {

	@Inject
	private InstantMessageDao instantMessageDao;
	@Inject
	private StoreDao storeDao;

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "instantMessage", condition = "#useCache")
	public List<InstantMessage> findList(InstantMessage.Type type, Long storeId, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		Store store = storeDao.find(storeId);
		if (storeId != null && store == null) {
			return Collections.emptyList();
		}
		QueryWrapper<InstantMessage> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return instantMessageDao.findList(queryWrapper, type, store);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<InstantMessage> findPage(Store store, Pageable pageable) {
		IPage<InstantMessage> iPage = getPluginsPage(pageable);
		iPage.setRecords(instantMessageDao.findPage(iPage, getPageable(pageable), store));
		return super.findPage(iPage, pageable);
	}

	@Override
	@CacheEvict(value = "instantMessage", allEntries = true)
	public boolean save(InstantMessage entity) {
		return super.save(entity);
	}

	@Override
	@CacheEvict(value = "instantMessage", allEntries = true)
	public InstantMessage update(InstantMessage entity) {
		return super.update(entity);
	}

	@Override
	@CacheEvict(value = "instantMessage", allEntries = true)
	public InstantMessage update(InstantMessage entity, String... ignoreProperties) {
		return super.update(entity, ignoreProperties);
	}

	@Override
	@CacheEvict(value = "instantMessage", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@CacheEvict(value = "instantMessage", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@CacheEvict(value = "instantMessage", allEntries = true)
	public void delete(InstantMessage entity) {
		super.delete(entity);
	}

}