package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.xiaoxiangshop.dao.AftersalesSettingDao;
import net.xiaoxiangshop.entity.AftersalesSetting;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.service.AftersalesSettingService;

/**
 * Service - 售后设置
 * 
 */
@Service
public class AftersalesSettingServiceImpl extends BaseServiceImpl<AftersalesSetting> implements AftersalesSettingService {

	@Inject
	private AftersalesSettingDao aftersalesSettingDao;

	@Override
	@Transactional(readOnly = true)
	public AftersalesSetting findByStore(Store store) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

		return aftersalesSettingDao.findByAttribute("store_id", store.getId());
	}

}