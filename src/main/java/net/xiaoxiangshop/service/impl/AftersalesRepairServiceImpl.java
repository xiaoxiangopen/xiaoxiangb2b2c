package net.xiaoxiangshop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.xiaoxiangshop.dao.AftersalesItemDao;
import net.xiaoxiangshop.dao.AftersalesRepairDao;
import net.xiaoxiangshop.entity.AftersalesItem;
import net.xiaoxiangshop.entity.AftersalesRepair;
import net.xiaoxiangshop.service.AftersalesRepairService;

/**
 * Service - 维修
 * 
 */
@Service
public class AftersalesRepairServiceImpl extends ServiceImpl<AftersalesRepairDao, AftersalesRepair> implements AftersalesRepairService {

	@Inject
	private AftersalesRepairDao aftersalesRepairDao;
	@Inject
	private AftersalesItemDao aftersalesItemDao;
	
	@Override
	public AftersalesRepair find(Long id) {
		return aftersalesRepairDao.find(id);
	}

	@Override
	@Transactional
	public boolean save(AftersalesRepair aftersalesRepair) {
		Assert.notNull(aftersalesRepair, "[Assertion failed] - entity is required; it must not be null");
		Assert.isTrue(aftersalesRepair.isNew(), "[Assertion failed] - entity must be new");

		aftersalesRepair.setId(IdWorker.getId());
		aftersalesRepair.setCreatedDate(new Date());
		aftersalesRepair.setVersion(0L);
		
		boolean result = aftersalesRepairDao.save(aftersalesRepair);
		if (CollectionUtils.isNotEmpty(aftersalesRepair.getAftersalesItems())) {
			List<AftersalesItem> getAftersalesItems = new ArrayList<>();
			for (AftersalesItem aftersalesItem : aftersalesRepair.getAftersalesItems()) {
				aftersalesItem.setId(IdWorker.getId());
				aftersalesItem.setCreatedDate(new Date());
				aftersalesItem.setVersion(0L);
				getAftersalesItems.add(aftersalesItem);
			}
			aftersalesItemDao.saveBatch(getAftersalesItems);
		}
		return result;
	}
	
}