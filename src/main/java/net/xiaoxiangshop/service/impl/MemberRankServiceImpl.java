package net.xiaoxiangshop.service.impl;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.xiaoxiangshop.dao.MemberRankDao;
import net.xiaoxiangshop.entity.MemberRank;
import net.xiaoxiangshop.service.MemberRankService;

/**
 * Service - 会员等级
 * 
 */
@Service
public class MemberRankServiceImpl extends BaseServiceImpl<MemberRank> implements MemberRankService {

	@Inject
	private MemberRankDao memberRankDao;

	@Override
	@Transactional(readOnly = true)
	public boolean amountExists(BigDecimal amount) {
		return !memberRankDao.exists("amount", amount);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean amountUnique(Long id, BigDecimal amount) {
		return !memberRankDao.unique(id, "amount", amount);
	}

	@Override
	@Transactional(readOnly = true)
	public MemberRank findDefault() {
		return memberRankDao.findDefault();
	}

	@Override
	@Transactional(readOnly = true)
	public MemberRank findByAmount(BigDecimal amount) {
		return memberRankDao.findByAmount(amount);
	}

	@Override
	@Transactional
	public boolean save(MemberRank memberRank) {
		Assert.notNull(memberRank, "[Assertion failed] - memberRank is required; it must not be null");

		if (BooleanUtils.isTrue(memberRank.getIsDefault())) {
			memberRankDao.clearDefault();
		}
		return super.save(memberRank);
	}

	@Override
	@Transactional
	public MemberRank update(MemberRank memberRank) {
		Assert.notNull(memberRank, "[Assertion failed] - memberRank is required; it must not be null");

		MemberRank pMemberRank = super.update(memberRank);
		if (BooleanUtils.isTrue(pMemberRank.getIsDefault())) {
			memberRankDao.clearDefaultExclude(pMemberRank);
		}
		return pMemberRank;
	}

}