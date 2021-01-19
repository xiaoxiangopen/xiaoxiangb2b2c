package net.xiaoxiangshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.*;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.repository.ArticleRepository;
import net.xiaoxiangshop.service.ArticleService;
import net.xiaoxiangshop.service.NoteService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Service - 文章
 * 
 */
@Service
public class NoteServiceImpl extends BaseServiceImpl<Note> implements NoteService {

	@Inject
	private CacheManager cacheManager;
	@Inject
	private NoteDao articleDao;
	@Inject
	private NoteCategoryDao articleCategoryDao;
	@Inject
	private NoteTagDao articleTagDao;

	@Override
	@Transactional(readOnly = true)
	public List<Note> findList(NoteCategory articleCategory, NoteTag articleTag, Boolean isPublication, Integer count, List<Filter> filters, List<Order> orders) {
		QueryWrapper<Note> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return articleDao.findByWrapperList(queryWrapper, articleCategory, articleTag, isPublication);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "note", condition = "#useCache")
	public List<Note> findList(Long articleCategoryId, Long articleTagId, Boolean isPublication, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		NoteCategory articleCategory = articleCategoryDao.find(articleCategoryId);
		if (articleCategoryId != null && articleCategory == null) {
			return Collections.emptyList();
		}
		NoteTag articleTag = articleTagDao.find(articleTagId);
		if (articleTagId != null && articleTag == null) {
			return Collections.emptyList();
		}
		QueryWrapper<Note> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return articleDao.findByWrapperList(queryWrapper, articleCategory, articleTag, isPublication);
	}



	@Override
	@Transactional(readOnly = true)
	public Page<Note> findPage(NoteCategory articleCategory, NoteTag articleTag, Boolean isPublication, Pageable pageable) {
		IPage<Note> iPage = getPluginsPage(pageable);
		iPage.setRecords(articleDao.findPage(iPage, getPageable(pageable), articleCategory, articleTag, isPublication));
		return super.findPage(iPage, pageable);
	}



	@Override
	public long viewHits(Long id) {
		Assert.notNull(id, "[Assertion failed] - id is required; it must not be null");

		return 0l;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public boolean save(Note article) {
		return super.save(article);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public Note update(Note article) {
		return super.update(article);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public Note update(Note article, String... ignoreProperties) {
		return super.update(article, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Long id) {
		articleDao.delete(Arrays.asList(id));
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Long... ids) {
		articleDao.delete(Arrays.asList(ids));
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Note article) {
		articleDao.delete(Arrays.asList(article.getId()));
	}
	
}