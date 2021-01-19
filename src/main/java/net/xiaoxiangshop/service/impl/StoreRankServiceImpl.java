package net.xiaoxiangshop.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.dao.StoreRankDao;
import net.xiaoxiangshop.entity.StoreRank;
import net.xiaoxiangshop.service.StoreRankService;

/**
 * Service - 店铺等级
 * 
 */
@Service
public class StoreRankServiceImpl extends BaseServiceImpl<StoreRank> implements StoreRankService {

	@Inject
	private StoreRankDao storeRankDao;

	@Override
	@Transactional(readOnly = true)
	public boolean nameExists(String name) {
		return storeRankDao.exists("name", name);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean nameUnique(Long id, String name) {
		return storeRankDao.unique(id, "name", name);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StoreRank> findList(Boolean isAllowRegister, List<Filter> filters, List<Order> orders) {
		//return storeRankDao.findList(isAllowRegister, filters, orders);
		QueryWrapper<StoreRank> wrapper = createQueryWrapper(null, null, filters, orders);
		return storeRankDao.findList(wrapper, isAllowRegister);
	}

}