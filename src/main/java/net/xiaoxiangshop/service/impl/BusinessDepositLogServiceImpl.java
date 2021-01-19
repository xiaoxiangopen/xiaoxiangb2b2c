package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.BusinessDepositLogDao;
import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.BusinessDepositLog;
import net.xiaoxiangshop.service.BusinessDepositLogService;

/**
 * Service - 商家预存款记录
 * 
 */
@Service
public class BusinessDepositLogServiceImpl extends BaseServiceImpl<BusinessDepositLog> implements BusinessDepositLogService {

	@Inject
	private BusinessDepositLogDao businessDepositLogDao;

	@Override
	@Transactional(readOnly = true)
	public Page<BusinessDepositLog> findPage(Business business, Pageable pageable) {
		IPage<BusinessDepositLog> iPage = getPluginsPage(pageable);
		iPage.setRecords(businessDepositLogDao.findPage(iPage, getPageable(pageable), business));
		return super.findPage(iPage, pageable);
	}

}