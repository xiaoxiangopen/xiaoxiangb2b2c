package net.xiaoxiangshop.service.impl;

import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.xiaoxiangshop.dao.StorePluginStatusDao;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StorePluginStatus;
import net.xiaoxiangshop.service.StorePluginStatusService;

/**
 * Service - 店铺插件状态
 * 
 */
@Service
public class StorePluginStatusServiceImpl extends BaseServiceImpl<StorePluginStatus> implements StorePluginStatusService {

	@Inject
	private StorePluginStatusDao storePluginStatusDao;

	@Override
	@Transactional(readOnly = true)
	public StorePluginStatus find(Store store, String pluginId) {
		return storePluginStatusDao.findByStore(store, pluginId);
	}

	@Override
	public void addPluginEndDays(StorePluginStatus storePluginStatus, int amount) {
		Assert.notNull(storePluginStatus, "[Assertion failed] - storePluginStatus is required; it must not be null");

		if (amount == 0) {
			return;
		}

		Date now = new Date();
		Date currentEndDate = storePluginStatus.getEndDate() != null ? storePluginStatus.getEndDate() : now;
		if (amount > 0) {
			storePluginStatus.setEndDate(DateUtils.addDays(currentEndDate.after(now) ? currentEndDate : now, amount));
		} else {
			storePluginStatus.setEndDate(DateUtils.addDays(currentEndDate, amount));
		}
		super.update(storePluginStatus);
	}

	public StorePluginStatus create(Store store, String pluginId, Integer amount) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");
		Assert.hasText(pluginId, "[Assertion failed] - pluginId must have text; it must not be null, empty, or blank");

		StorePluginStatus storePluginStatus = new StorePluginStatus();
		storePluginStatus.setEndDate(amount != null ? DateUtils.addDays(new Date(), amount) : null);
		storePluginStatus.setStore(store);
		storePluginStatus.setPluginId(pluginId);
		storePluginStatus.setCreatedDate(new Date());
		super.save(storePluginStatus);
		return storePluginStatus;
	}

}