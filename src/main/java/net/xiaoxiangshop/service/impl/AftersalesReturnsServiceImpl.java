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
import net.xiaoxiangshop.dao.AftersalesReturnsDao;
import net.xiaoxiangshop.entity.AftersalesItem;
import net.xiaoxiangshop.entity.AftersalesReturns;
import net.xiaoxiangshop.service.AftersalesReturnsService;

/**
 * Service - 退货
 * 
 */
@Service
public class AftersalesReturnsServiceImpl extends ServiceImpl<AftersalesReturnsDao, AftersalesReturns>
		implements AftersalesReturnsService {

	@Inject
	private AftersalesReturnsDao aftersalesReturnsDao;
	@Inject
	private AftersalesItemDao aftersalesItemDao;

	@Override
	public AftersalesReturns find(Long id) {
		return aftersalesReturnsDao.find(id);
	}

	@Override
	@Transactional
	public boolean save(AftersalesReturns aftersalesReturns) {
		Assert.notNull(aftersalesReturns, "[Assertion failed] - entity is required; it must not be null");
		Assert.isTrue(aftersalesReturns.isNew(), "[Assertion failed] - entity must be new");

		aftersalesReturns.setId(IdWorker.getId());
		aftersalesReturns.setCreatedDate(new Date());
		aftersalesReturns.setVersion(0L);

		boolean result = aftersalesReturnsDao.save(aftersalesReturns);
		if (CollectionUtils.isNotEmpty(aftersalesReturns.getAftersalesItems())) {
			List<AftersalesItem> getAftersalesItems = new ArrayList<>();
			for (AftersalesItem aftersalesItem : aftersalesReturns.getAftersalesItems()) {
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