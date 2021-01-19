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
import net.xiaoxiangshop.dao.MemberDao;
import net.xiaoxiangshop.dao.StoreFavoriteDao;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StoreFavorite;
import net.xiaoxiangshop.service.StoreFavoriteService;

/**
 * Service - 店铺收藏
 * 
 */
@Service
public class StoreFavoriteServiceImpl extends BaseServiceImpl<StoreFavorite> implements StoreFavoriteService {

	@Inject
	private StoreFavoriteDao storeFavoriteDao;
	@Inject
	private MemberDao memberDao;

	@Override
	@Transactional(readOnly = true)
	public boolean exists(Member member, Store store) {
		return storeFavoriteDao.exists(member, store);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StoreFavorite> findList(Member member, Integer count, List<Filter> filters, List<Order> orders) {
		QueryWrapper<StoreFavorite> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return storeFavoriteDao.findList(queryWrapper, member);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StoreFavorite> findPage(Member member, Pageable pageable) {
		IPage<StoreFavorite> iPage = getPluginsPage(pageable);
		iPage.setRecords(storeFavoriteDao.findPage(iPage, getPageable(pageable), member));
		return super.findPage(iPage, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Long count(Member member) {
		return storeFavoriteDao.count(member);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "storeFavorite", condition = "#useCache")
	public List<StoreFavorite> findList(Long memberId, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		Member member = memberDao.find(memberId);
		if (memberId != null && member == null) {
			return Collections.emptyList();
		}
		QueryWrapper<StoreFavorite> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return storeFavoriteDao.findList(queryWrapper, member);
	}

	@Override
	@CacheEvict(value = "storeFavorite", allEntries = true)
	public boolean save(StoreFavorite storeFavorite) {
		return super.save(storeFavorite);
	}

	@Override
	@CacheEvict(value = "storeFavorite", allEntries = true)
	public StoreFavorite update(StoreFavorite storeFavorite) {
		return super.update(storeFavorite);
	}

	@Override
	@CacheEvict(value = "storeFavorite", allEntries = true)
	public StoreFavorite update(StoreFavorite storeFavorite, String... ignoreProperties) {
		return super.update(storeFavorite, ignoreProperties);
	}

	@Override
	@CacheEvict(value = "storeFavorite", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@CacheEvict(value = "storeFavorite", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@CacheEvict(value = "storeFavorite", allEntries = true)
	public void delete(StoreFavorite storeFavorite) {
		super.delete(storeFavorite);
	}

}