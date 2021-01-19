package net.xiaoxiangshop.audit;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import net.xiaoxiangshop.util.BeanUtils;

/**
 * Audit - 审计元数据
 * 
 */
public final class AuditingMetadata {

	/**
	 * 审计元数据缓存
	 */
	private static final Map<Class<?>, AuditingMetadata> AUDITING_METADATA_CACHE = new ConcurrentHashMap<>();

	/**
	 * "创建者"属性
	 */
	private final List<AuditingMetadata.Property> createdByProperties;

	/**
	 * "创建日期"属性
	 */
	private final List<AuditingMetadata.Property> createdDateProperties;

	/**
	 * "最后修改者"属性
	 */
	private final List<AuditingMetadata.Property> lastModifiedByProperties;

	/**
	 * "最后修改日期"属性
	 */
	private final List<AuditingMetadata.Property> lastModifiedDateProperties;

	/**
	 * 构造方法
	 * 
	 * @param type
	 *            类
	 */
	private AuditingMetadata(Class<?> type) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

		this.createdByProperties = findProperties(type, CreatedBy.class);
		this.createdDateProperties = findProperties(type, CreatedDate.class);
		this.lastModifiedByProperties = findProperties(type, LastModifiedBy.class);
		this.lastModifiedDateProperties = findProperties(type, LastModifiedDate.class);
	}

	/**
	 * 获取"创建者"属性
	 * 
	 * @return "创建者"属性
	 */
	public List<AuditingMetadata.Property> getCreatedByProperties() {
		return createdByProperties;
	}

	/**
	 * 获取"创建日期"属性
	 * 
	 * @return "创建日期"属性
	 */
	public List<AuditingMetadata.Property> getCreatedDateProperties() {
		return createdDateProperties;
	}

	/**
	 * 获取"最后修改者"属性
	 * 
	 * @return "最后修改者"属性
	 */
	public List<AuditingMetadata.Property> getLastModifiedByProperties() {
		return lastModifiedByProperties;
	}

	/**
	 * 获取"最后修改日期"属性
	 * 
	 * @return "最后修改日期"属性
	 */
	public List<AuditingMetadata.Property> getLastModifiedDateProperties() {
		return lastModifiedDateProperties;
	}

	/**
	 * 获取审计元数据
	 * 
	 * @param type
	 *            类
	 * @return 审计元数据
	 */
	public static AuditingMetadata getAuditingMetadata(Class<?> type) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

		AuditingMetadata auditingMetadata = AUDITING_METADATA_CACHE.get(type);
		if (auditingMetadata == null) {
			auditingMetadata = new AuditingMetadata(type);
			AUDITING_METADATA_CACHE.put(type, auditingMetadata);
		}
		return auditingMetadata;
	}

	/**
	 * 判断是否为审计元数据
	 * 
	 * @return 是否为审计元数据
	 */
	public boolean isAuditable() {
		return CollectionUtils.isNotEmpty(createdByProperties) || CollectionUtils.isNotEmpty(createdDateProperties) || CollectionUtils.isNotEmpty(lastModifiedByProperties) || CollectionUtils.isNotEmpty(lastModifiedDateProperties);
	}

	/**
	 * 查找属性
	 * 
	 * @param type
	 *            类
	 * @param annotationType
	 *            Annotation类
	 * @return 属性
	 */
	private List<AuditingMetadata.Property> findProperties(Class<?> type, Class<? extends Annotation> annotationType) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");
		Assert.notNull(annotationType, "[Assertion failed] - annotationType is required; it must not be null");

		Map<String, PropertyDescriptor> propertyDescriptorMap = new HashMap<>();
		Map<String, PropertyDescriptor> remainingPropertyDescriptorMap = new HashMap<>();

		for (PropertyDescriptor propertyDescriptor : BeanUtils.getPropertyDescriptors(type, annotationType)) {
			propertyDescriptorMap.put(propertyDescriptor.getName(), propertyDescriptor);
		}
		remainingPropertyDescriptorMap.putAll(propertyDescriptorMap);

		List<AuditingMetadata.Property> result = new ArrayList<>();
		for (Field field : BeanUtils.findFields(type, annotationType)) {
			String fieldName = field.getName();
			result.add(new AuditingMetadata.Property(field, propertyDescriptorMap.get(fieldName)));
			remainingPropertyDescriptorMap.remove(fieldName);
		}
		for (PropertyDescriptor propertyDescriptor : remainingPropertyDescriptorMap.values()) {
			result.add(new AuditingMetadata.Property(null, propertyDescriptor));
		}
		return result;
	}

	/**
	 * 属性
	 * 
	 */
	public static class Property {

		/**
		 * 名称
		 */
		private String name;

		/**
		 * 类型
		 */
		private Class<?> type;

		/**
		 * Field
		 */
		private Field field;

		/**
		 * PropertyDescriptor
		 */
		private PropertyDescriptor propertyDescriptor;

		/**
		 * 构造方法
		 * 
		 * @param field
		 *            Field
		 * @param propertyDescriptor
		 *            PropertyDescriptor
		 */
		public Property(Field field, PropertyDescriptor propertyDescriptor) {
			Assert.isTrue(field != null || propertyDescriptor != null, "[Assertion failed] - At least one of field and propertyDescriptor is not null");

			this.name = field != null ? field.getName() : propertyDescriptor.getName();
			this.type = field != null ? field.getType() : propertyDescriptor.getPropertyType();
			this.field = field;
			this.propertyDescriptor = propertyDescriptor;
		}

		/**
		 * 获取名称
		 * 
		 * @return 名称
		 */
		public String getName() {
			return name;
		}

		/**
		 * 获取类型
		 * 
		 * @return 类型
		 */
		public Class<?> getType() {
			return type;
		}

		/**
		 * 获取Field
		 * 
		 * @return Field
		 */
		public Field getField() {
			return field;
		}

		/**
		 * 获取PropertyDescriptor
		 * 
		 * @return PropertyDescriptor
		 */
		public PropertyDescriptor getPropertyDescriptor() {
			return propertyDescriptor;
		}

		/**
		 * 设置值
		 * 
		 * @param target
		 *            目标
		 * @param value
		 *            值
		 */
		public void setValue(Object target, Object value) {
			Assert.notNull(target, "[Assertion failed] - target is required; it must not be null");

			if (field != null) {
				BeanUtils.setAccessible(field);
				BeanUtils.setField(field, target, value);
			} else if (propertyDescriptor != null) {
				Method writeMethod = propertyDescriptor.getWriteMethod();
				if (writeMethod != null) {
					BeanUtils.setAccessible(writeMethod);
					BeanUtils.invokeMethod(writeMethod, target, value);
				}
			}
		}
	}

}