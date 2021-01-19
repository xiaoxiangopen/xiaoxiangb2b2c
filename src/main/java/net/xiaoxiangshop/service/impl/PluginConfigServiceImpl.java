package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.xiaoxiangshop.dao.PluginConfigDao;
import net.xiaoxiangshop.entity.PluginConfig;
import net.xiaoxiangshop.service.PluginConfigService;

/**
 * Service - 插件配置
 * 
 */
@Service
public class PluginConfigServiceImpl extends BaseServiceImpl<PluginConfig> implements PluginConfigService {

	@Inject
	private PluginConfigDao pluginConfigDao;

	@Override
	@Transactional(readOnly = true)
	public boolean pluginIdExists(String pluginId) {
		return pluginConfigDao.exists("plugin_id", pluginId);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable("pluginConfig")
	public PluginConfig findByPluginId(String pluginId) {
		return pluginConfigDao.findByAttribute("plugin_id", pluginId);
	}

	@Override
	@CacheEvict(value = "pluginConfig", allEntries = true)
	public void deleteByPluginId(String pluginId) {
		PluginConfig pluginConfig = findByPluginId(pluginId);
		//pluginConfigDao.remove(pluginConfig);
		super.removeById(pluginConfig.getId());
	}

	@Override
	@Transactional
	@CacheEvict(value = "pluginConfig", allEntries = true)
	public boolean save(PluginConfig pluginConfig) {
		return super.save(pluginConfig);
	}

	@Override
	@Transactional
	@CacheEvict(value = "pluginConfig", allEntries = true)
	public PluginConfig update(PluginConfig pluginConfig) {
		return super.update(pluginConfig);
	}

	@Override
	@Transactional
	@CacheEvict(value = "pluginConfig", allEntries = true)
	public PluginConfig update(PluginConfig pluginConfig, String... ignoreProperties) {
		return super.update(pluginConfig, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "pluginConfig", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "pluginConfig", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "pluginConfig", allEntries = true)
	public void delete(PluginConfig pluginConfig) {
		super.delete(pluginConfig);
	}

}