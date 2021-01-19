package net.xiaoxiangshop.service.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.xiaoxiangshop.dao.ArticleCategoryDao;
import net.xiaoxiangshop.entity.ArticleCategory;
import net.xiaoxiangshop.service.ArticleCategoryService;

/**
 * Service - 文章分类
 * 
 */
@Service
public class ArticleCategoryServiceImpl extends BaseServiceImpl<ArticleCategory> implements ArticleCategoryService {

	@Inject
	private ArticleCategoryDao articleCategoryDao;

	@Override
	@Transactional(readOnly = true)
	public List<ArticleCategory> findRoots() {
		return articleCategoryDao.findRoots(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArticleCategory> findRoots(Integer count) {
		return articleCategoryDao.findRoots(count);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "articleCategory", condition = "#useCache")
	public List<ArticleCategory> findRoots(Integer count, boolean useCache) {
		return articleCategoryDao.findRoots(count);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArticleCategory> findParents(ArticleCategory articleCategory, boolean recursive, Integer count) {
		return articleCategoryDao.findParents(articleCategory, recursive, count);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "articleCategory", condition = "#useCache")
	public List<ArticleCategory> findParents(Long articleCategoryId, boolean recursive, Integer count, boolean useCache) {
		ArticleCategory articleCategory = articleCategoryDao.find(articleCategoryId);
		if (articleCategoryId != null && articleCategory == null) {
			return Collections.emptyList();
		}
		return articleCategoryDao.findParents(articleCategory, recursive, count);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArticleCategory> findTree() {
		return articleCategoryDao.findChildren(null, true, null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArticleCategory> findChildren(ArticleCategory articleCategory, boolean recursive, Integer count) {
		return articleCategoryDao.findChildren(articleCategory, recursive, count);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "articleCategory", condition = "#useCache")
	public List<ArticleCategory> findChildren(Long articleCategoryId, boolean recursive, Integer count, boolean useCache) {
		ArticleCategory articleCategory = articleCategoryDao.find(articleCategoryId);
		if (articleCategoryId != null && articleCategory == null) {
			return Collections.emptyList();
		}
		return articleCategoryDao.findChildren(articleCategory, recursive, count);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public boolean save(ArticleCategory articleCategory) {
		Assert.notNull(articleCategory, "[Assertion failed] - articleCategory is required; it must not be null");

		setValue(articleCategory);
		return super.save(articleCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public ArticleCategory update(ArticleCategory articleCategory) {
		Assert.notNull(articleCategory, "[Assertion failed] - articleCategory is required; it must not be null");

		setValue(articleCategory);
		for (ArticleCategory children : articleCategoryDao.findChildren(articleCategory, true, null)) {
			setValue(children);
		}
		return super.update(articleCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public ArticleCategory update(ArticleCategory articleCategory, String... ignoreProperties) {
		return super.update(articleCategory, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(ArticleCategory articleCategory) {
		super.delete(articleCategory);
	}

	/**
	 * 设置值
	 * 
	 * @param articleCategory
	 *            文章分类
	 */
	private void setValue(ArticleCategory articleCategory) {
		if (articleCategory == null) {
			return;
		}
		ArticleCategory parent = articleCategory.getParent();
		if (parent != null) {
			articleCategory.setTreePath(parent.getTreePath() + parent.getId() + ArticleCategory.TREE_PATH_SEPARATOR);
		} else {
			articleCategory.setTreePath(ArticleCategory.TREE_PATH_SEPARATOR);
		}
		articleCategory.setGrade(articleCategory.getParentIds().length);
	}

}