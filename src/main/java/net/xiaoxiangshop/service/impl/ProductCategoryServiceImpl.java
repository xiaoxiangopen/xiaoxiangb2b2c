package net.xiaoxiangshop.service.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.alibaba.fastjson.JSON;
import com.alipay.api.domain.Person;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.dao.ProductCategoryDao;
import net.xiaoxiangshop.entity.ProductCategory;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.service.ProductCategoryService;

/**
 * Service - 商品分类
 * 
 */
@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategory> implements ProductCategoryService {

	@Inject
	private ProductCategoryDao productCategoryDao;

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findList(Store store, Integer count, List<Filter> filters, List<Order> orders) {
		QueryWrapper<ProductCategory> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return productCategoryDao.findList(queryWrapper, store);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findRoots() {
		return productCategoryDao.findRoots(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findRoots(Integer count) {
		return productCategoryDao.findRoots(count);
	}

	@Override
	@Transactional(readOnly = true)
//	@Cacheable(value = "productCategory", condition = "#useCache")
	public List<ProductCategory> findRoots(Integer count, boolean useCache) {
		return productCategoryDao.findRoots(count);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findParents(ProductCategory productCategory, boolean recursive, Integer count) {
		return productCategoryDao.findParents(productCategory, recursive, count);
	}

	@Override
	@Transactional(readOnly = true)
//	@Cacheable(value = "productCategory", condition = "#useCache")
	public List<ProductCategory> findParents(Long productCategoryId, boolean recursive, Integer count, boolean useCache) {
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		return productCategoryDao.findParents(productCategory, recursive, count);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findTree() {
		List<ProductCategory> productCategories = productCategoryDao.findChildren(null, true, null);
		sort(productCategories);
		return productCategories;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findChildren(ProductCategory productCategory, boolean recursive, Integer count) {
		return productCategoryDao.findChildren(productCategory, recursive, count);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "productCategory", condition = "#useCache")
	public List<ProductCategory> findChildren(Long productCategoryId, boolean recursive, Integer count, boolean useCache) {
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		return productCategoryDao.findChildren(productCategory, recursive, count);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public boolean save(ProductCategory productCategory) {
		Assert.notNull(productCategory, "[Assertion failed] - productCategory is required; it must not be null");

		setValue(productCategory);
		return super.save(productCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public ProductCategory update(ProductCategory productCategory) {
		Assert.notNull(productCategory, "[Assertion failed] - productCategory is required; it must not be null");

		setValue(productCategory);
		for (ProductCategory children : productCategoryDao.findChildren(productCategory, true, null)) {
			setValue(children);
		}
		return super.update(productCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public ProductCategory update(ProductCategory productCategory, String... ignoreProperties) {
		return super.update(productCategory, ignoreProperties);
	}



	/**
	 * 函数式接口 T -> bollean
	 * @param keyExtractor
	 * @param <T>
	 * @return
	 */
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		ConcurrentHashMap<Object, Boolean> map = new ConcurrentHashMap<>(16);
		return t -> map.putIfAbsent(keyExtractor.apply(t),Boolean.TRUE) == null;
	}
	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public void delete(ProductCategory productCategory) {
		super.delete(productCategory);
	}

	/**
	 * 设置值
	 * 
	 * @param productCategory
	 *            商品分类
	 */
	private void setValue(ProductCategory productCategory) {
		if (productCategory == null) {
			return;
		}
		ProductCategory parent = productCategory.getParent();
		if (parent != null) {
			productCategory.setTreePath(parent.getTreePath() + parent.getId() + ProductCategory.TREE_PATH_SEPARATOR);
		} else {
			productCategory.setTreePath(ProductCategory.TREE_PATH_SEPARATOR);
		}
		productCategory.setGrade(productCategory.getParentIds().length);
	}

	/**
	 * 排序商品分类
	 * 
	 * @param productCategories
	 *            商品分类
	 */
	private void sort(List<ProductCategory> productCategories) {
		if (CollectionUtils.isEmpty(productCategories)) {
			return;
		}
		final Map<Long, Integer> orderMap = new HashMap<>();
		for (ProductCategory productCategory : productCategories) {
			orderMap.put(productCategory.getId(), productCategory.getOrder());
		}
		Collections.sort(productCategories, new Comparator<ProductCategory>() {
			@Override
			public int compare(ProductCategory productCategory1, ProductCategory productCategory2) {
				Long[] ids1 = (Long[]) ArrayUtils.add(productCategory1.getParentIds(), productCategory1.getId());
				Long[] ids2 = (Long[]) ArrayUtils.add(productCategory2.getParentIds(), productCategory2.getId());
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
						compareToBuilder.append(productCategory1.getGrade(), productCategory2.getGrade());
					}
				}
				return compareToBuilder.toComparison();
			}
		});
	}

	@Override
	public List<ProductCategory> findCategoryByPtId(Long id) {
		return productCategoryDao.findCategoryByPtId(id);
	}


	@Override
	public void updateState(ProductCategory p) {
		productCategoryDao.updateState(p);
	}

}