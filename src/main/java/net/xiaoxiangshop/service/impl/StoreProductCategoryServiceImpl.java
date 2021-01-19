package net.xiaoxiangshop.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.StoreDao;
import net.xiaoxiangshop.dao.StoreProductCategoryDao;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StoreProductCategory;
import net.xiaoxiangshop.service.StoreProductCategoryService;
import net.xiaoxiangshop.service.StoreService;

/**
 * Service - 店铺商品分类
 * 
 */
@Service
public class StoreProductCategoryServiceImpl extends BaseServiceImpl<StoreProductCategory> implements StoreProductCategoryService {

	@Inject
	private StoreProductCategoryDao storeProductCategoryDao;
	@Inject
	private StoreDao storeDao;
	@Inject
	private StoreService storeService;

	@Override
	@Transactional(readOnly = true)
	public List<StoreProductCategory> findRoots(Long storeId) {
		Store store = storeDao.find(storeId);
		if (storeId != null && store == null) {
			return Collections.emptyList();
		}
		return storeProductCategoryDao.findRoots(store, null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StoreProductCategory> findRoots(Long storeId, Integer count) {
		Store store = storeDao.find(storeId);
		if (storeId != null && store == null) {
			return Collections.emptyList();
		}
		return storeProductCategoryDao.findRoots(store, count);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "storeProductCategory", condition = "#useCache")
	public List<StoreProductCategory> findRoots(Long storeId, Integer count, boolean useCache) {
		Store store = storeDao.find(storeId);
		if (storeId != null && store == null) {
			return Collections.emptyList();
		}
		return storeProductCategoryDao.findRoots(store, count);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "storeProductCategory", condition = "#useCache")
	public List<StoreProductCategory> findParents(Long storeProductCategoryId, boolean recursive, Integer count, boolean useCache) {
		StoreProductCategory storeProductCategory = storeProductCategoryDao.find(storeProductCategoryId);
		if (storeProductCategoryId != null && storeProductCategory == null) {
			return Collections.emptyList();
		}
		return storeProductCategoryDao.findParents(storeProductCategory, recursive, count);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StoreProductCategory> findTree(Store store) {
		List<StoreProductCategory> result = storeProductCategoryDao.findChildren(null, store, true, null);
		sort(result);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<StoreProductCategory> findChildren(StoreProductCategory storeProductCategory, Store store, boolean recursive, Integer count) {
		List<StoreProductCategory> result = storeProductCategoryDao.findChildren(storeProductCategory, store, recursive, count);
		sort(result);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "storeProductCategory", condition = "#useCache")
	public List<StoreProductCategory> findChildren(Long storeProductCategoryId, Long storeId, boolean recursive, Integer count, boolean useCache) {
		StoreProductCategory storeProductCategory = storeProductCategoryDao.find(storeProductCategoryId);
		if (storeProductCategoryId != null && storeProductCategory == null) {
			return Collections.emptyList();
		}
		Store store = storeDao.find(storeId);
		if (storeId == null && store == null) {
			return Collections.emptyList();
		}

		List<StoreProductCategory> result = storeProductCategoryDao.findChildren(storeProductCategory, store, recursive, count);
		sort(result);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StoreProductCategory> findPage(Store store, Pageable pageable) {
		IPage<StoreProductCategory> iPage = getPluginsPage(pageable);
		iPage.setRecords(storeProductCategoryDao.findPage(iPage, getPageable(pageable), store));
		return super.findPage(iPage, pageable);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "storeProductCategory" }, allEntries = true)
	public boolean save(StoreProductCategory storeProductCategory) {
		Assert.notNull(storeProductCategory, "[Assertion failed] - storeProductCategory is required; it must not be null");

		setValue(storeProductCategory);
		return super.save(storeProductCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "storeProductCategory" }, allEntries = true)
	public StoreProductCategory update(StoreProductCategory storeProductCategory) {
		Assert.notNull(storeProductCategory, "[Assertion failed] - storeProductCategory is required; it must not be null");

		setValue(storeProductCategory);
		for (StoreProductCategory children : storeProductCategoryDao.findChildren(storeProductCategory, storeService.getCurrent(), true, null)) {
			setValue(children);
		}
		return super.update(storeProductCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "storeProductCategory" }, allEntries = true)
	public StoreProductCategory update(StoreProductCategory storeProductCategory, String... ignoreProperties) {
		return super.update(storeProductCategory, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "storeProductCategory" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "storeProductCategory" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "storeProductCategory" }, allEntries = true)
	public void delete(StoreProductCategory storeProductCategory) {
		super.delete(storeProductCategory);
	}

	/**
	 * 设置值
	 * 
	 * @param storeProductCategory
	 *            店铺商品分类
	 */
	private void setValue(StoreProductCategory storeProductCategory) {
		if (storeProductCategory == null) {
			return;
		}
		StoreProductCategory parent = storeProductCategory.getParent();
		if (parent != null) {
			storeProductCategory.setTreePath(parent.getTreePath() + parent.getId() + StoreProductCategory.TREE_PATH_SEPARATOR);
		} else {
			storeProductCategory.setTreePath(StoreProductCategory.TREE_PATH_SEPARATOR);
		}
		storeProductCategory.setGrade(storeProductCategory.getParentIds().length);
	}

	/**
	 * 排序店铺商品分类
	 * 
	 * @param storeProductCategorys
	 *            店铺商品分类
	 */
	private void sort(List<StoreProductCategory> storeProductCategorys) {
		if (CollectionUtils.isEmpty(storeProductCategorys)) {
			return;
		}
		final Map<Long, Integer> orderMap = new HashMap<>();
		for (StoreProductCategory shopProductCategory : storeProductCategorys) {
			orderMap.put(shopProductCategory.getId(), shopProductCategory.getOrder());
		}
		Collections.sort(storeProductCategorys, new Comparator<StoreProductCategory>() {
			@Override
			public int compare(StoreProductCategory storeProductCategory1, StoreProductCategory storeProductCategory2) {
				Long[] ids1 = (Long[]) ArrayUtils.add(storeProductCategory1.getParentIds(), storeProductCategory1.getId());
				Long[] ids2 = (Long[]) ArrayUtils.add(storeProductCategory2.getParentIds(), storeProductCategory2.getId());
				Iterator<Long> iterator1 = Arrays.asList(ids1).iterator();
				Iterator<Long> iterator2 = Arrays.asList(ids2).iterator();
				CompareToBuilder compareToBuilder = new CompareToBuilder();
				while (iterator1.hasNext() && iterator2.hasNext()) {
					Long id1 = iterator1.next();
					Long id2 = iterator2.next();
					Integer order1 = orderMap.get(id1);
					Integer order2 = orderMap.get(id2);
					compareToBuilder.append(order1, order2).append(id1, id2);
					if (!iterator1.hasNext() || !iterator2.hasNext()) {
						compareToBuilder.append(storeProductCategory1.getGrade(), storeProductCategory2.getGrade());
					}
				}
				return compareToBuilder.toComparison();
			}
		});
	}
	
}