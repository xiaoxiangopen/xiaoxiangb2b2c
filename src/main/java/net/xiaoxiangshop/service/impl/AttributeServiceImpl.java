package net.xiaoxiangshop.service.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.dao.AttributeDao;
import net.xiaoxiangshop.dao.ProductCategoryDao;
import net.xiaoxiangshop.dao.ProductDao;
import net.xiaoxiangshop.entity.Attribute;
import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.entity.ProductCategory;
import net.xiaoxiangshop.service.AttributeService;

/**
 * Service - 属性
 * 
 */
@Service
public class AttributeServiceImpl extends BaseServiceImpl<Attribute> implements AttributeService {

	@Inject
	private AttributeDao attributeDao;
	@Inject
	private ProductCategoryDao productCategoryDao;
	@Inject
	private ProductDao productDao;

	@Override
	@Transactional(readOnly = true)
	public Integer findUnusedPropertyIndex(ProductCategory productCategory) {
		for (int i = 0; i < Product.ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
			Integer count = attributeDao.findUnusedPropertyIndex( productCategory, i);
			if (count == 0) {
				return i;
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Attribute> findList(ProductCategory productCategory, Integer count, List<Filter> filters, List<Order> orders) {
		QueryWrapper<Attribute> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return attributeDao.findList(queryWrapper, productCategory);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "attribute", condition = "#useCache")
	public List<Attribute> findList(Long productCategoryId, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		QueryWrapper<Attribute> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return attributeDao.findList(queryWrapper, productCategory);
	}

	@Override
	@Transactional(readOnly = true)
	public String toAttributeValue(Attribute attribute, String value) {
		Assert.notNull(attribute, "[Assertion failed] - attribute is required; it must not be null");

		if (StringUtils.isEmpty(value) || CollectionUtils.isEmpty(attribute.getOptions()) || !attribute.getOptions().contains(value)) {
			return null;
		}
		return value;
	}

	@Override
	@Transactional
	@CacheEvict(value = "attribute", allEntries = true)
	public boolean save(Attribute attribute) {
		Assert.notNull(attribute, "[Assertion failed] - attribute is required; it must not be null");

		Integer unusedPropertyIndex = attributeDao.findUnusedPropertyIndex(attribute.getProductCategory(), null);
		Assert.notNull(unusedPropertyIndex, "[Assertion failed] - unusedPropertyIndex is required; it must not be null");

		attribute.setPropertyIndex(unusedPropertyIndex);
		return super.save(attribute);
	}

	@Override
	@Transactional
	@CacheEvict(value = "attribute", allEntries = true)
	public Attribute update(Attribute attribute) {
		return super.update(attribute);
	}

	@Override
	@Transactional
	@CacheEvict(value = "attribute", allEntries = true)
	public Attribute update(Attribute attribute, String... ignoreProperties) {
		return super.update(attribute, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "attribute", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "attribute", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "attribute", allEntries = true)
	public void delete(Attribute attribute) {
		if (attribute != null) {
			productDao.clearAttributeValue(attribute);
		}

		super.delete(attribute);
	}

}