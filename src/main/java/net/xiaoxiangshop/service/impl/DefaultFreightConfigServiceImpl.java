package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.DefaultFreightConfigDao;
import net.xiaoxiangshop.entity.Area;
import net.xiaoxiangshop.entity.DefaultFreightConfig;
import net.xiaoxiangshop.entity.ShippingMethod;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.service.DefaultFreightConfigService;

/**
 * Service - 默认运费配置
 * 
 */
@Service
public class DefaultFreightConfigServiceImpl extends BaseServiceImpl<DefaultFreightConfig> implements DefaultFreightConfigService {

	@Inject
	private DefaultFreightConfigDao defaultFreightConfigDao;
	
	@Override
	public DefaultFreightConfig find(Long id) {
		return defaultFreightConfigDao.find(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean exists(ShippingMethod shippingMethod, Area area) {
		return defaultFreightConfigDao.exists(shippingMethod, area);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean unique(ShippingMethod shippingMethod, Area previousArea, Area currentArea) {
		return (previousArea != null && previousArea.equals(currentArea)) || !defaultFreightConfigDao.exists(shippingMethod, currentArea);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<DefaultFreightConfig> findPage(Store store, Pageable pageable) {
		IPage<DefaultFreightConfig> iPage = getPluginsPage(pageable);
		iPage.setRecords(defaultFreightConfigDao.findPage(iPage, getPageable(pageable), store));
		return super.findPage(iPage, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public DefaultFreightConfig find(ShippingMethod shippingMethod, Store store) {
		return defaultFreightConfigDao.findDefault(shippingMethod, store);
	}

	@Override
	public void update(DefaultFreightConfig defaultFreightConfig, Store store, ShippingMethod shippingMethod) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");
		Assert.notNull(shippingMethod, "[Assertion failed] - shippingMethod is required; it must not be null");
		if (!defaultFreightConfig.isNew()) {
			defaultFreightConfig.setStore(store);
			defaultFreightConfig.setShippingMethod(shippingMethod);
			super.update(defaultFreightConfig);
		} else {
			defaultFreightConfig.setStore(store);
			defaultFreightConfig.setShippingMethod(shippingMethod);
			super.save(defaultFreightConfig);
		}
	}

}