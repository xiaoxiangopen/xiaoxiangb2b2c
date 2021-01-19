package net.xiaoxiangshop.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.ArticleCategoryDao;
import net.xiaoxiangshop.dao.ArticleDao;
import net.xiaoxiangshop.dao.ArticleTagDao;
import net.xiaoxiangshop.entity.Article;
import net.xiaoxiangshop.entity.ArticleCategory;
import net.xiaoxiangshop.entity.ArticleTag;
import net.xiaoxiangshop.repository.ArticleRepository;
import net.xiaoxiangshop.service.ArticleService;

/**
 * Service - 文章
 * 
 */
@Service
public class ArticleServiceImpl extends BaseServiceImpl<Article> implements ArticleService {

	@Inject
	private CacheManager cacheManager;
	@Inject
	private ArticleDao articleDao;
	@Inject
	private ArticleCategoryDao articleCategoryDao;
	@Inject
	private ArticleTagDao articleTagDao;
	@Inject
	private ArticleRepository articleRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Article> findList(ArticleCategory articleCategory, ArticleTag articleTag, Boolean isPublication, Integer count, List<Filter> filters, List<Order> orders) {
		QueryWrapper<Article> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return articleDao.findByWrapperList(queryWrapper, articleCategory, articleTag, isPublication);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "article", condition = "#useCache")
	public List<Article> findList(Long articleCategoryId, Long articleTagId, Boolean isPublication, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		ArticleCategory articleCategory = articleCategoryDao.find(articleCategoryId);
		if (articleCategoryId != null && articleCategory == null) {
			return Collections.emptyList();
		}
		ArticleTag articleTag = articleTagDao.find(articleTagId);
		if (articleTagId != null && articleTag == null) {
			return Collections.emptyList();
		}
		QueryWrapper<Article> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return articleDao.findByWrapperList(queryWrapper, articleCategory, articleTag, isPublication);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Article> findPage(ArticleCategory articleCategory, ArticleTag articleTag, Boolean isPublication, Pageable pageable) {
		IPage<Article> iPage = getPluginsPage(pageable);
		iPage.setRecords(articleDao.findPage(iPage, getPageable(pageable), articleCategory, articleTag, isPublication));
		return super.findPage(iPage, pageable);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Page<Article> search(String keyword, Pageable pageable) {
		if (StringUtils.isEmpty(keyword)) {
			return Page.emptyPage(pageable);
		}

		if (pageable == null) {
			pageable = new Pageable();
		}

		// es原因page从0开始不是从1开始
		Integer pageNumber = 0;
		if (pageable.getPageNumber() >= 1) {
			pageNumber = pageable.getPageNumber() - 1;
		}

		//多条件设置
		QueryBuilder titleQuery = QueryBuilders.matchPhraseQuery("title", keyword).boost(1.5F);
		QueryBuilder contentQuery = QueryBuilders.matchPhraseQuery("content", keyword);
		QueryBuilder isPublicationQuery = QueryBuilders.matchPhraseQuery("isPublication", true);
		BoolQueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.boolQuery().should(titleQuery).should(contentQuery)).must(isPublicationQuery);

		Sort sort = Sort.by(Direction.DESC, "isTop");
		org.springframework.data.domain.Page<Article> articles = articleRepository.search(query, PageRequest.of(pageNumber, pageable.getPageSize(), sort));
		return new Page<>(articles.getContent(), articles.getSize(), pageable);
	}

	@Override
	public long viewHits(Long id) {
		Assert.notNull(id, "[Assertion failed] - id is required; it must not be null");

		Ehcache cache = cacheManager.getEhcache(Article.HITS_CACHE_NAME);
		cache.acquireWriteLockOnKey(id);
		try {
			Element element = cache.get(id);
			Long hits;
			if (element != null) {
				hits = (Long) element.getObjectValue() + 1;
			} else {
				Article article = articleDao.find(id);
				if (article == null) {
					return 0L;
				}
				hits = article.getHits() + 1;
			}
			cache.put(new Element(id, hits));
			return hits;
		} finally {
			cache.releaseWriteLockOnKey(id);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public boolean save(Article article) {
		return super.save(article);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public Article update(Article article) {
		return super.update(article);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public Article update(Article article, String... ignoreProperties) {
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
	public void delete(Article article) {
		articleDao.delete(Arrays.asList(article.getId()));
	}
	
}