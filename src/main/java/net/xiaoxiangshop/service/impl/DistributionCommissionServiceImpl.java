package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.DistributionCommissionDao;
import net.xiaoxiangshop.entity.DistributionCommission;
import net.xiaoxiangshop.entity.Distributor;
import net.xiaoxiangshop.service.DistributionCommissionService;

/**
 * Service - 分销佣金
 * 
 */
@Service
public class DistributionCommissionServiceImpl extends BaseServiceImpl<DistributionCommission> implements DistributionCommissionService {

	@Inject
	private DistributionCommissionDao distributionCommissionDao;

	@Override
	@Transactional(readOnly = true)
	public Page<DistributionCommission> findPage(Distributor distributor, Pageable pageable) {
		IPage<DistributionCommission> iPage = getPluginsPage(pageable);
		iPage.setRecords(distributionCommissionDao.findPage(iPage, getPageable(pageable), distributor));
		return super.findPage(iPage, pageable);
	}

}