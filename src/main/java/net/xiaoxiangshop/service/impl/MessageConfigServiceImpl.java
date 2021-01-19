package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.xiaoxiangshop.dao.MessageConfigDao;
import net.xiaoxiangshop.entity.MessageConfig;
import net.xiaoxiangshop.service.MessageConfigService;

/**
 * Service - 消息配置
 * 
 */
@Service
public class MessageConfigServiceImpl extends BaseServiceImpl<MessageConfig> implements MessageConfigService {

	@Inject
	private MessageConfigDao messageConfigDao;

	@Override
	@Transactional(readOnly = true)
	@Cacheable("messageConfig")
	public MessageConfig findByType(MessageConfig.Type type) {
		return messageConfigDao.findByType(type);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public boolean save(MessageConfig messageConfig) {
		return super.save(messageConfig);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public MessageConfig update(MessageConfig messageConfig) {
		return super.update(messageConfig);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public MessageConfig update(MessageConfig messageConfig, String... ignoreProperties) {
		return super.update(messageConfig, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public void delete(MessageConfig messageConfig) {
		super.delete(messageConfig);
	}

}