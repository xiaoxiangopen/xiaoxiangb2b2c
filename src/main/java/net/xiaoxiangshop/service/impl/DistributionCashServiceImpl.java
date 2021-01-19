package net.xiaoxiangshop.service.impl;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.DistributionCashDao;
import net.xiaoxiangshop.dao.DistributorDao;
import net.xiaoxiangshop.entity.DistributionCash;
import net.xiaoxiangshop.entity.DistributionCash.Status;
import net.xiaoxiangshop.entity.Distributor;
import net.xiaoxiangshop.entity.MemberDepositLog;
import net.xiaoxiangshop.service.DistributionCashService;
import net.xiaoxiangshop.service.MemberService;

/**
 * Service - 分销提现
 * 
 */
@Service
public class DistributionCashServiceImpl extends BaseServiceImpl<DistributionCash> implements DistributionCashService {

	@Inject
	private DistributionCashDao distributionCashDao;
	@Inject
	private DistributorDao distributorDao;
	@Inject
	private MemberService memberService;

	@Override
	public void applyCash(DistributionCash distributionCash, Distributor distributor) {
		Assert.notNull(distributionCash, "[Assertion failed] - distributionCash is required; it must not be null");
		Assert.notNull(distributor, "[Assertion failed] - distributor is required; it must not be null");
		Assert.isTrue(distributionCash.getAmount().compareTo(BigDecimal.ZERO) > 0, "[Assertion failed] - distributionCash amount must be greater than 0");

		distributionCash.setStatus(DistributionCash.Status.PENDING);
		distributionCash.setDistributor(distributor);
		super.save(distributionCash);

		memberService.addFrozenAmount(distributor.getMember(), distributionCash.getAmount());
	}

	@Override
	public void review(DistributionCash distributionCash, Boolean isPassed) {
		Assert.notNull(distributionCash, "[Assertion failed] - distributionCash is required; it must not be null");
		Assert.notNull(isPassed, "[Assertion failed] - isPassed is required; it must not be null");

		Distributor distributor = distributionCash.getDistributor();
		if (isPassed) {
			Assert.notNull(distributionCash.getAmount(), "[Assertion failed] - distributionCash amount is required; it must not be null");
			Assert.notNull(distributionCash.getDistributor(), "[Assertion failed] - distributionCash distributor is required; it must not be null");

			distributionCash.setStatus(DistributionCash.Status.APPROVED);
			memberService.addBalance(distributor.getMember(), distributionCash.getAmount().negate(), MemberDepositLog.Type.DISTRIBUTION_CASH, null);
		} else {
			distributionCash.setStatus(DistributionCash.Status.FAILED);
		}
		super.update(distributionCash);
		memberService.addFrozenAmount(distributor.getMember(), distributionCash.getAmount().negate());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<DistributionCash> findPage(Status status, String bank, String account, String accountHolder, Distributor distributor, Pageable pageable) {
		IPage<DistributionCash> iPage = getPluginsPage(pageable);
		iPage.setRecords(distributionCashDao.findPage(iPage, getPageable(pageable), status, bank, account, accountHolder, distributor));
		return super.findPage(iPage, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Long count(DistributionCash.Status status, String bank, String account, String accountHolder, Distributor distributor) {
		return distributionCashDao.count(status, bank, account, accountHolder, distributor);
	}

	@Override
	@Transactional(readOnly = true)
	public Long count(Status status, String bank, String account, String accountHolder, Long distributorId) {
		Distributor distributor = distributorDao.find(distributorId);
		if (distributorId != null && distributor == null) {
			return 0L;
		}
		return distributionCashDao.count(status, bank, account, accountHolder, distributor);
	}

}