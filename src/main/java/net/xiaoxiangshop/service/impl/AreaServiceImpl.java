package net.xiaoxiangshop.service.impl;

import java.util.*;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Comparator;
import net.xiaoxiangshop.entity.ProductCategory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.xiaoxiangshop.dao.AreaDao;
import net.xiaoxiangshop.entity.Area;
import net.xiaoxiangshop.service.AreaService;

/**
 * Service - 地区
 * 
 */
@Service
public class AreaServiceImpl extends BaseServiceImpl<Area> implements AreaService {

	@Inject
	private AreaDao areaDao;

	@Override
	@Transactional(readOnly = true)
	public Area findByFullName(String fullName) {
		return areaDao.findByAttribute("full_name", StringUtils.lowerCase(fullName));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Area> findRoots() {
		return areaDao.findRoots(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Area> findRoots(Integer count) {
		return areaDao.findRoots(count);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Area> findParents(Area area, boolean recursive, Integer count) {
		return areaDao.findParents(area, recursive, count);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Area> findChildren(Area area, boolean recursive, Integer count) {
		return areaDao.findChildren(area, recursive, count);
	}

	@Override
	@Transactional
	@CacheEvict(value = "areaPage", allEntries = true)
	public boolean save(Area area) {
		Assert.notNull(area, "[Assertion failed] - area is required; it must not be null");

		setValue(area);
		return super.save(area);
	}

	@Override
	@Transactional
	@CacheEvict(value = "areaPage", allEntries = true)
	public Area update(Area area) {
		Assert.notNull(area, "[Assertion failed] - area is required; it must not be null");

		setValue(area);
		for (Area children : areaDao.findChildren(area, true, null)) {
			setValue(children);
		}
		return super.update(area);
	}

	@Override
	@Transactional
	@CacheEvict(value = "areaPage", allEntries = true)
	public Area update(Area area, String... ignoreProperties) {
		return super.update(area, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "areaPage", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "areaPage", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "areaPage", allEntries = true)
	public void delete(Area area) {
		super.delete(area);
	}

	/**
	 * 设置值
	 * 
	 * @param area
	 *            地区
	 */
	private void setValue(Area area) {
		if (area == null) {
			return;
		}
		Area parent = area.getParent();
		if (parent != null) {
			area.setFullName(parent.getFullName() + Area.TREE_PATH_SEPARATOR + area.getName());
			area.setTreePath(parent.getTreePath() + parent.getId() + Area.TREE_PATH_SEPARATOR);
		} else {
			area.setFullName(area.getName());
			area.setTreePath(Area.TREE_PATH_SEPARATOR);
		}
		area.setGrade(area.getParentIds().length);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Area> findTree() {
		List<Area> productCategories = areaDao.findChildren(null, true, null);
		sort(productCategories);
		return productCategories;
	}

	@Override
	public List<Area> findAreaByParentId(Long parentId) {
		List<Area> areaList = areaDao.findSet(parentId);
		return areaList;
	}

	@Override
	public Area findByLikeName(String likeName) {
		return areaDao.findByLikeName(likeName);
	}

	/**
	 * 排序商品分类
	 *
	 * @param productCategories
	 *            商品分类
	 */
	private void sort(List<Area> productCategories) {
		if (CollectionUtils.isEmpty(productCategories)) {
			return;
		}
		final Map<Long, Integer> orderMap = new HashMap<>();
		for (Area productCategory : productCategories) {
			orderMap.put(productCategory.getId(), productCategory.getOrder());
		}
		Collections.sort(productCategories, new Comparator<Area>() {
			@Override
			public int compare(Area productCategory1, Area productCategory2) {
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

}