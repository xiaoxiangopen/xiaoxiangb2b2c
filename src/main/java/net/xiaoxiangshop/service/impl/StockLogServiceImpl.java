package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.StockLogDao;
import net.xiaoxiangshop.entity.StockLog;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.service.StockLogService;

/**
 * Service - 库存记录
 * 
 */
@Service
public class StockLogServiceImpl extends BaseServiceImpl<StockLog> implements StockLogService {

	@Inject
	private StockLogDao stockLogDao;

	@Override
	@Transactional(readOnly = true)
	public Page<StockLog> findPage(Store store, Pageable pageable) {
		IPage<StockLog> iPage = getPluginsPage(pageable);
		iPage.setRecords(stockLogDao.findPage(iPage, getPageable(pageable), store));
		return super.findPage(iPage, pageable);
	}

}