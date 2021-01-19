package net.xiaoxiangshop.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.xiaoxiangshop.entity.NavigationGroup;
import net.xiaoxiangshop.service.NavigationGroupService;

/**
 * Service - 导航组
 * 
 */
@Service
public class NavigationGroupServiceImpl extends BaseServiceImpl<NavigationGroup> implements NavigationGroupService {

	@Override
	@Transactional
	@CacheEvict(value = "navigation", allEntries = true)
	public boolean save(NavigationGroup navigationGroup) {
		return super.save(navigationGroup);
	}

	@Override
	@Transactional
	@CacheEvict(value = "navigation", allEntries = true)
	public NavigationGroup update(NavigationGroup navigationGroup) {
		return super.update(navigationGroup);
	}

	@Override
	@Transactional
	@CacheEvict(value = "navigation", allEntries = true)
	public NavigationGroup update(NavigationGroup navigationGroup, String... ignoreProperties) {
		return super.update(navigationGroup, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "navigation", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "navigation", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "navigation", allEntries = true)
	public void delete(NavigationGroup navigationGroup) {
		super.delete(navigationGroup);
	}

}