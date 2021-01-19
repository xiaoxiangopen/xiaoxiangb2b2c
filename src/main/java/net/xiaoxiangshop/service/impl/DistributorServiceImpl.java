package net.xiaoxiangshop.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import net.xiaoxiangshop.dao.DistributorDao;
import net.xiaoxiangshop.entity.Distributor;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.service.DistributorService;

/**
 * Service - 分销员
 * 
 */
@Service
public class DistributorServiceImpl extends BaseServiceImpl<Distributor> implements DistributorService {

	@Inject
	private DistributorDao distributorDao;

	@Override
	public void create(Member member) {
		Assert.notNull(member, "[Assertion failed] - member is required; it must not be null");

		Distributor distributor = new Distributor();
		distributor.setId(IdWorker.getId());
		distributor.setCreatedDate(new Date());
		distributor.setVersion(0L);
		distributor.setMember(member);
		distributor.setParent(null);
		distributor.setChildren(null);
		distributor.setDistributionCashs(null);
		distributor.setDistributionCommissions(null);
		distributorDao.save(distributor);
	}

	@Override
	public void create(Member member, Member spreadMember) {
		Assert.notNull(member, "[Assertion failed] - member is required; it must not be null");
		Assert.notNull(spreadMember, "[Assertion failed] - spreadMember is required; it must not be null");
		Assert.state(!member.equals(spreadMember), "[Assertion failed] - member must not be spreadMember");

		Distributor spreadDistributor = spreadMember.getDistributor();

		if (spreadDistributor == null) {
			spreadDistributor = new Distributor();
			spreadDistributor.setId(IdWorker.getId());
			spreadDistributor.setCreatedDate(new Date());
			spreadDistributor.setVersion(0L);
			spreadDistributor.setMember(spreadMember);
			spreadDistributor.setParent(null);
			spreadDistributor.setChildren(null);
			spreadDistributor.setDistributionCashs(null);
			spreadDistributor.setDistributionCommissions(null);
			distributorDao.save(spreadDistributor);
		}

		Distributor distributor = new Distributor();
		distributor.setId(IdWorker.getId());
		distributor.setCreatedDate(new Date());
		distributor.setVersion(0L);
		distributor.setMember(member);
		distributor.setParent(spreadDistributor);
		distributor.setChildren(null);
		distributor.setDistributionCashs(null);
		distributor.setDistributionCommissions(null);
		distributorDao.save(distributor);

		Set<Distributor> childrens = spreadDistributor.getChildren();
		if (childrens == null) {
			childrens = new HashSet<>();
		}
		childrens.add(distributor);
		spreadDistributor.setChildren(childrens);
	}

	@Override
	public void modify(Distributor distributor, Member spreadMember) {
		Assert.notNull(distributor, "[Assertion failed] - distributor is required; it must not be null");
		Assert.state(!distributor.getMember().equals(spreadMember), "[Assertion failed] - distributor must not be spreadMember");

		if (spreadMember == null) {
			distributor.setParent(null);
		} else {
			distributor.setParent(spreadMember.getDistributor());
		}
		distributorDao.update(distributor);
	}

}