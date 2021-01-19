package net.xiaoxiangshop.service.impl;

import net.xiaoxiangshop.dao.ArticleCategoryDao;
import net.xiaoxiangshop.dao.NoteCategoryDao;
import net.xiaoxiangshop.entity.ArticleCategory;
import net.xiaoxiangshop.entity.NoteCategory;
import net.xiaoxiangshop.service.ArticleCategoryService;
import net.xiaoxiangshop.service.NoteCategoryService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * Service - 文章分类
 * 
 */
@Service
public class NoteCategoryServiceImpl extends BaseServiceImpl<NoteCategory> implements NoteCategoryService {

	@Inject
	private NoteCategoryDao articleCategoryDao;

	@Override
	@Transactional(readOnly = true)
	public List<NoteCategory> findRoots() {
		return articleCategoryDao.findRoots(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<NoteCategory> findRoots(Integer count) {
		return articleCategoryDao.findRoots(count);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "articleCategory", condition = "#useCache")
	public List<NoteCategory> findRoots(Integer count, boolean useCache) {
		return articleCategoryDao.findRoots(count);
	}

	@Override
	@Transactional(readOnly = true)
	public List<NoteCategory> findParents(NoteCategory articleCategory, boolean recursive, Integer count) {
		return articleCategoryDao.findParents(articleCategory, recursive, count);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "articleCategory", condition = "#useCache")
	public List<NoteCategory> findParents(Long articleCategoryId, boolean recursive, Integer count, boolean useCache) {
		NoteCategory articleCategory = articleCategoryDao.find(articleCategoryId);
		if (articleCategoryId != null && articleCategory == null) {
			return Collections.emptyList();
		}
		return articleCategoryDao.findParents(articleCategory, recursive, count);
	}

	@Override
	@Transactional(readOnly = true)
	public List<NoteCategory> findTree() {
		return articleCategoryDao.findChildren(null, true, null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<NoteCategory> findChildren(NoteCategory articleCategory, boolean recursive, Integer count) {
		return articleCategoryDao.findChildren(articleCategory, recursive, count);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "articleCategory", condition = "#useCache")
	public List<NoteCategory> findChildren(Long articleCategoryId, boolean recursive, Integer count, boolean useCache) {
		NoteCategory articleCategory = articleCategoryDao.find(articleCategoryId);
		if (articleCategoryId != null && articleCategory == null) {
			return Collections.emptyList();
		}
		return articleCategoryDao.findChildren(articleCategory, recursive, count);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public boolean save(NoteCategory articleCategory) {
		Assert.notNull(articleCategory, "[Assertion failed] - articleCategory is required; it must not be null");

		setValue(articleCategory);
		return super.save(articleCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public NoteCategory update(NoteCategory articleCategory) {
		Assert.notNull(articleCategory, "[Assertion failed] - articleCategory is required; it must not be null");

		setValue(articleCategory);
		for (NoteCategory children : articleCategoryDao.findChildren(articleCategory, true, null)) {
			setValue(children);
		}
		return super.update(articleCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public NoteCategory update(NoteCategory articleCategory, String... ignoreProperties) {
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
	public void delete(NoteCategory articleCategory) {
		super.delete(articleCategory);
	}

	/**
	 * 设置值
	 * 
	 * @param articleCategory
	 *            文章分类
	 */
	private void setValue(NoteCategory articleCategory) {
		if (articleCategory == null) {
			return;
		}
		NoteCategory parent = articleCategory.getParent();
		if (parent != null) {
			articleCategory.setTreePath(parent.getTreePath() + parent.getId() + ArticleCategory.TREE_PATH_SEPARATOR);
		} else {
			articleCategory.setTreePath(ArticleCategory.TREE_PATH_SEPARATOR);
		}
		articleCategory.setGrade(articleCategory.getParentIds().length);
	}

}