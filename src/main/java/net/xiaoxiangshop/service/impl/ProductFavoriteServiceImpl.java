package net.xiaoxiangshop.service.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.MemberDao;
import net.xiaoxiangshop.dao.ProductFavoriteDao;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.entity.ProductFavorite;
import net.xiaoxiangshop.service.ProductFavoriteService;

/**
 * Service - 商品收藏
 * 
 */
@Service
public class ProductFavoriteServiceImpl extends BaseServiceImpl<ProductFavorite> implements ProductFavoriteService {

	@Inject
	private ProductFavoriteDao productFavoriteDao;
	@Inject
	private MemberDao memberDao;

	@Override
	@Transactional(readOnly = true)
	public boolean exists(Member member, Product product) {
		return productFavoriteDao.exists(member, product);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductFavorite> findList(Member member, Integer count, List<Filter> filters, List<Order> orders) {
		QueryWrapper<ProductFavorite> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return productFavoriteDao.findList(queryWrapper, member);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductFavorite> findPage(Member member, Pageable pageable) {
		IPage<ProductFavorite> iPage = getPluginsPage(pageable);
		iPage.setRecords(productFavoriteDao.findPage(iPage, getPageable(pageable), member));
		return super.findPage(iPage, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Long count(Member member) {
		return productFavoriteDao.count(member);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "productFavorite", condition = "#useCache")
	public List<ProductFavorite> findList(Long memberId, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		Member member = memberDao.find(memberId);
		if (memberId != null && member == null) {
			return Collections.emptyList();
		}
		QueryWrapper<ProductFavorite> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return productFavoriteDao.findList(queryWrapper, member);
	}

	@Override
	@CacheEvict(value = "productFavorite", allEntries = true)
	public boolean save(ProductFavorite productFavorite) {
		return super.save(productFavorite);
	}

	@Override
	@CacheEvict(value = "productFavorite", allEntries = true)
	public ProductFavorite update(ProductFavorite productFavorite) {
		return super.update(productFavorite);
	}

	@Override
	@CacheEvict(value = "productFavorite", allEntries = true)
	public ProductFavorite update(ProductFavorite productFavorite, String... ignoreProperties) {
		return super.update(productFavorite, ignoreProperties);
	}

	@Override
	@CacheEvict(value = "productFavorite", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@CacheEvict(value = "productFavorite", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@CacheEvict(value = "productFavorite", allEntries = true)
	public void delete(ProductFavorite productFavorite) {
		super.delete(productFavorite);
	}

}