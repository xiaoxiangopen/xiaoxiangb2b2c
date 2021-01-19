package net.xiaoxiangshop.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.ConsultationDao;
import net.xiaoxiangshop.dao.MemberDao;
import net.xiaoxiangshop.dao.ProductDao;
import net.xiaoxiangshop.entity.Consultation;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.service.ConsultationService;

/**
 * Service - 咨询
 * 
 */
@Service
public class ConsultationServiceImpl extends BaseServiceImpl<Consultation> implements ConsultationService {

	@Inject
	private ConsultationDao consultationDao;
	@Inject
	private MemberDao memberDao;
	@Inject
	private ProductDao productDao;

	@Override
	@Transactional(readOnly = true)
	public List<Consultation> findList(Member member, Product product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		QueryWrapper<Consultation> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return consultationDao.findList(queryWrapper, member, product, isShow);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "consultation", condition = "#useCache")
	public List<Consultation> findList(Long memberId, Long productId, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		Member member = memberDao.find(memberId);
		if (memberId != null && member == null) {
			return Collections.emptyList();
		}
		Product product = productDao.find(productId);
		if (productId != null && product == null) {
			return Collections.emptyList();
		}
		QueryWrapper<Consultation> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return consultationDao.findList(queryWrapper, member, product, isShow);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Consultation> findPage(Member member, Product product, Store store, Boolean isShow, Pageable pageable) {
		IPage<Consultation> iPage = getPluginsPage(pageable);
		iPage.setRecords(consultationDao.findPage(iPage, getPageable(pageable), member, product, store, isShow));
		return super.findPage(iPage, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Long count(Member member, Product product, Boolean isShow) {
		return consultationDao.count(member, product, isShow);
	}

	@Override
	@CacheEvict(value = { "product", "consultation" }, allEntries = true)
	public void reply(Consultation consultation, Consultation replyConsultation) {
		if (consultation == null || replyConsultation == null) {
			return;
		}
		consultation.setIsShow(true);
		consultationDao.update(consultation);

		replyConsultation.setId(IdWorker.getId());
		replyConsultation.setCreatedDate(new Date());
		replyConsultation.setVersion(0L);
		replyConsultation.setIsShow(true);
		replyConsultation.setProduct(consultation.getProduct());
		replyConsultation.setForConsultation(consultation);
		replyConsultation.setStore(consultation.getStore());
		consultationDao.save(replyConsultation);
	}

	@Override
	public Set<Consultation> findSet(Long id) {
		return consultationDao.findSet("forConsultationId",id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "consultation" }, allEntries = true)
	public boolean save(Consultation consultation) {
		return super.save(consultation);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "consultation" }, allEntries = true)
	public Consultation update(Consultation consultation) {
		return super.update(consultation);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "consultation" }, allEntries = true)
	public Consultation update(Consultation consultation, String... ignoreProperties) {
		return super.update(consultation, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "consultation" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "consultation" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "consultation" }, allEntries = true)
	public void delete(Consultation consultation) {
		super.delete(consultation);
	}

}