package net.xiaoxiangshop.service.impl;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.entity.NoteTag;
import net.xiaoxiangshop.service.NoteTagService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service - 文章标签
 * 
 */
@Service
public class NoteTagServiceImpl extends BaseServiceImpl<NoteTag> implements NoteTagService {

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "articleTag", condition = "#useCache")
	public List<NoteTag> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return super.findList(null, count, filters, orders);
	}

	@Override
	@Transactional
	@CacheEvict(value = "articleTag", allEntries = true)
	public boolean save(NoteTag articleTag) {
		return super.save(articleTag);
	}

	@Override
	@Transactional
	@CacheEvict(value = "articleTag", allEntries = true)
	public NoteTag update(NoteTag articleTag) {
		return super.update(articleTag);
	}

	@Override
	@Transactional
	@CacheEvict(value = "articleTag", allEntries = true)
	public NoteTag update(NoteTag articleTag, String... ignoreProperties) {
		return super.update(articleTag, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "articleTag", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "articleTag", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "articleTag", allEntries = true)
	public void delete(NoteTag articleTag) {
		super.delete(articleTag);
	}

}