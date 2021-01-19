package net.xiaoxiangshop.audit;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import net.xiaoxiangshop.util.SpringUtils;

/**
 * Audit - 审计实体类监听器
 * 
 */
public class AuditingEntityListener {

	/**
	 * 审计者Provider缓存
	 */
	@SuppressWarnings("rawtypes")
	private static final Map<Class<?>, net.xiaoxiangshop.audit.AuditorProvider> AUDITOR_PROVIDER_CACHE = new ConcurrentHashMap<>();

	/**
	 * 保存前处理
	 * 
	 * @param entity
	 *            实体对象
	 */
	@SuppressWarnings("unchecked")
	public void prePersist(Object entity) {
		net.xiaoxiangshop.audit.AuditingMetadata auditingMetadata = net.xiaoxiangshop.audit.AuditingMetadata.getAuditingMetadata(entity.getClass());
		if (!auditingMetadata.isAuditable()) {
			return;
		}

		List<net.xiaoxiangshop.audit.AuditingMetadata.Property> createdByProperties = auditingMetadata.getCreatedByProperties();
		List<net.xiaoxiangshop.audit.AuditingMetadata.Property> createdDateProperties = auditingMetadata.getCreatedDateProperties();
		List<net.xiaoxiangshop.audit.AuditingMetadata.Property> lastModifiedByProperties = auditingMetadata.getLastModifiedByProperties();
		List<net.xiaoxiangshop.audit.AuditingMetadata.Property> lastModifiedDateProperties = auditingMetadata.getLastModifiedDateProperties();

		List<net.xiaoxiangshop.audit.AuditingMetadata.Property> byProperties = (List<net.xiaoxiangshop.audit.AuditingMetadata.Property>) CollectionUtils.union(createdByProperties, lastModifiedByProperties);
		List<net.xiaoxiangshop.audit.AuditingMetadata.Property> dateProperties = (List<net.xiaoxiangshop.audit.AuditingMetadata.Property>) CollectionUtils.union(createdDateProperties, lastModifiedDateProperties);

		if (CollectionUtils.isNotEmpty(byProperties)) {
			for (net.xiaoxiangshop.audit.AuditingMetadata.Property property : byProperties) {
				net.xiaoxiangshop.audit.AuditorProvider<?> auditorProvider = getAuditorProvider(property.getType());
				Object currentAuditor = auditorProvider != null ? auditorProvider.getCurrentAuditor() : null;
				if (currentAuditor != null) {
					property.setValue(entity, currentAuditor);
				}
			}
		}
		if (CollectionUtils.isNotEmpty(dateProperties)) {
			Date now = new Date();
			for (net.xiaoxiangshop.audit.AuditingMetadata.Property property : dateProperties) {
				property.setValue(entity, now);
			}
		}
	}

	/**
	 * 更新前处理
	 * 
	 * @param entity
	 *            实体对象
	 */
	public void preUpdate(Object entity) {
		net.xiaoxiangshop.audit.AuditingMetadata auditingMetadata = net.xiaoxiangshop.audit.AuditingMetadata.getAuditingMetadata(entity.getClass());
		if (!auditingMetadata.isAuditable()) {
			return;
		}

		List<net.xiaoxiangshop.audit.AuditingMetadata.Property> lastModifiedByProperties = auditingMetadata.getLastModifiedByProperties();
		List<net.xiaoxiangshop.audit.AuditingMetadata.Property> lastModifiedDateProperties = auditingMetadata.getLastModifiedDateProperties();

		if (CollectionUtils.isNotEmpty(lastModifiedByProperties)) {
			for (net.xiaoxiangshop.audit.AuditingMetadata.Property property : lastModifiedByProperties) {
				net.xiaoxiangshop.audit.AuditorProvider<?> auditorProvider = getAuditorProvider(property.getType());
				if (auditorProvider != null) {
					Object currentAuditor = auditorProvider.getCurrentAuditor();
					if (currentAuditor != null) {
						property.setValue(entity, currentAuditor);
					}
				}
			}
		}
		if (CollectionUtils.isNotEmpty(lastModifiedDateProperties)) {
			Date now = new Date();
			for (net.xiaoxiangshop.audit.AuditingMetadata.Property property : lastModifiedDateProperties) {
				property.setValue(entity, now);
			}
		}
	}

	/**
	 * 获取审计者Provider
	 * 
	 * @param auditorClass
	 *            审计者类型
	 * @return 审计者Provider，若不存在则返回null
	 */
	@SuppressWarnings("rawtypes")
	private net.xiaoxiangshop.audit.AuditorProvider<?> getAuditorProvider(Class<?> auditorClass) {
		Assert.notNull(auditorClass, "[Assertion failed] - auditorClass is required; it must not be null");

		if (AUDITOR_PROVIDER_CACHE.containsKey(auditorClass)) {
			return AUDITOR_PROVIDER_CACHE.get(auditorClass);
		}

		Map<String, net.xiaoxiangshop.audit.AuditorProvider> auditorProviderMap = SpringUtils.getBeansOfType(net.xiaoxiangshop.audit.AuditorProvider.class);
		if (MapUtils.isNotEmpty(auditorProviderMap)) {
			for (net.xiaoxiangshop.audit.AuditorProvider<?> auditorProvider : auditorProviderMap.values()) {
				ResolvableType resolvableType = ResolvableType.forClass(ClassUtils.getUserClass(auditorProvider));
				Class<?> genericClass = resolvableType.as(net.xiaoxiangshop.audit.AuditorProvider.class).getGeneric().resolve();
				if (genericClass != null && genericClass.equals(auditorClass)) {
					AUDITOR_PROVIDER_CACHE.put(auditorClass, auditorProvider);
					return auditorProvider;
				}
			}
		}
		return null;
	}

}