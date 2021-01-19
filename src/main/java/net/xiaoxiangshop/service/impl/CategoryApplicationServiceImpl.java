package net.xiaoxiangshop.service.impl;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.CategoryApplicationDao;
import net.xiaoxiangshop.dao.ProductCategoryDao;
import net.xiaoxiangshop.dao.ProductCategoryStoreDao;
import net.xiaoxiangshop.dao.ProductDao;
import net.xiaoxiangshop.dao.StoreDao;
import net.xiaoxiangshop.entity.CategoryApplication;
import net.xiaoxiangshop.entity.CategoryApplication.Status;
import net.xiaoxiangshop.entity.ProductCategory;
import net.xiaoxiangshop.entity.ProductCategoryStore;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.service.CategoryApplicationService;

/**
 * Service - 经营分类申请
 * 
 */
@Service
public class CategoryApplicationServiceImpl extends BaseServiceImpl<CategoryApplication> implements CategoryApplicationService {

	@Inject
	private CategoryApplicationDao categoryApplicationDao;
	@Inject
	private ProductDao productDao;
	@Inject
	private StoreDao storeDao;
	@Inject
	private ProductCategoryDao productCategoryDao;
	@Inject
	private ProductCategoryStoreDao productCategoryStoreDao;

	@Override
	@Transactional(readOnly = true)
	public boolean exist(Store store, ProductCategory productCategory, CategoryApplication.Status status) {
		Assert.notNull(status, "[Assertion failed] - status is required; it must not be null");
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");
		Assert.notNull(productCategory, "[Assertion failed] - productCategory is required; it must not be null");

		return categoryApplicationDao.findList(store, productCategory, status).size() > 0;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CategoryApplication> findPage(Status status, Store store, ProductCategory productCategory, Pageable pageable) {
		IPage<CategoryApplication> iPage = getPluginsPage(pageable);
		iPage.setRecords(categoryApplicationDao.findPage(iPage, getPageable(pageable), status, store, productCategory));
		return super.findPage(iPage, pageable);
	}

	@Override
	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public void review(CategoryApplication categoryApplication, boolean isPassed) {
		Assert.notNull(categoryApplication, "[Assertion failed] - categoryApplication is required; it must not be null");

		if (isPassed) {
			Store store = categoryApplication.getStore();
			ProductCategory productCategory = categoryApplication.getProductCategory();

			categoryApplication.setStatus(CategoryApplication.Status.APPROVED);
			
			store.getProductCategories().add(productCategory);
			Set<ProductCategory> productCategories = new HashSet<>();
			productCategories.add(productCategory);
			
			ProductCategoryStore productCategoryStore = new ProductCategoryStore();
			productCategoryStore.setProductCategoriesId(productCategory.getId());
			productCategoryStore.setStoresId(store.getId());
			productCategoryStoreDao.insert(productCategoryStore);
			
			productDao.refreshActive(store);
		} else {
			categoryApplication.setStatus(CategoryApplication.Status.FAILED);
		}
		super.update(categoryApplication);
	}

	@Override
	@Transactional(readOnly = true)
	public Long count(CategoryApplication.Status status, Store store, ProductCategory productCategory) {
		return categoryApplicationDao.count(status, store, productCategory);
	}

	@Override
	public Long count(Status status, Long storeId, Long productCategoryId) {
		Store store = storeDao.find(storeId);
		if (storeId != null && store == null) {
			return 0L;
		}
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return 0L;
		}
		return categoryApplicationDao.count(status, store, productCategory);
	}

}