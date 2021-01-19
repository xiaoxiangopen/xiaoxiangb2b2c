package net.xiaoxiangshop.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.Assert;

/**
 * Utils - Bean
 * 
 */
public final class BeanUtils {

	/**
	 * Field缓存
	 */
	private static final Map<Class<?>, Field[]> DECLARED_FIELDS_CACHE = new ConcurrentHashMap<>();

	/**
	 * Method缓存
	 */
	private static final Map<Class<?>, Method[]> DECLARED_METHODS_CACHE = new ConcurrentHashMap<>();

	/**
	 * PropertyDescriptor缓存
	 */
	private static final Map<Class<?>, PropertyDescriptor[]> PROPERTY_DESCRIPTORS_CACHE = new ConcurrentHashMap<>();

	/**
	 * 不可实例化
	 */
	private BeanUtils() {
	}

	/**
	 * 设置Field允许访问
	 * 
	 * @param field
	 *            Field
	 */
	public static void setAccessible(Field field) {
		if (field != null && !field.isAccessible() && (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers()))) {
			field.setAccessible(true);
		}
	}

	/**
	 * 设置Method允许访问
	 * 
	 * @param method
	 *            Method
	 */
	public static void setAccessible(Method method) {
		if (method != null && !method.isAccessible() && (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))) {
			method.setAccessible(true);
		}
	}

	/**
	 * 设置Field值
	 * 
	 * @param field
	 *            Field
	 * @param target
	 *            目标
	 * @param value
	 *            值
	 */
	public static void setField(Field field, Object target, Object value) {
		Assert.notNull(field, "[Assertion failed] - field is required; it must not be null");
		Assert.notNull(target, "[Assertion failed] - target is required; it must not be null");

		try {
			field.set(target, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 执行Method
	 * 
	 * @param method
	 *            Method
	 * @param target
	 *            目标
	 * @param args
	 *            参数
	 * @return 结果
	 */
	public static Object invokeMethod(Method method, Object target, Object... args) {
		Assert.notNull(method, "[Assertion failed] - method is required; it must not be null");
		Assert.notNull(target, "[Assertion failed] - target is required; it must not be null");

		try {
			return method.invoke(target, args);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 查找Field
	 * 
	 * @param type
	 *            类
	 * @param annotationType
	 *            Annotation类
	 * @return Field，包含父类Field
	 */
	public static List<Field> findFields(Class<?> type, Class<? extends Annotation> annotationType) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");
		Assert.notNull(annotationType, "[Assertion failed] - annotationType is required; it must not be null");

		List<Field> result = new ArrayList<>();
		Class<?> targetClass = type;
		while (targetClass != null && !Object.class.equals(targetClass)) {
			for (Field field : getDeclaredFields(targetClass)) {
				if (getAnnotation(field, annotationType) != null) {
					result.add(field);
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		return result;
	}

	/**
	 * 查找Method
	 * 
	 * @param type
	 *            类
	 * @param annotationType
	 *            Annotation类
	 * @return Method，包含父类Method
	 */
	public static List<Method> findMethods(Class<?> type, Class<? extends Annotation> annotationType) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");
		Assert.notNull(annotationType, "[Assertion failed] - annotationType is required; it must not be null");

		List<Method> result = new ArrayList<>();
		Class<?> targetClass = type;
		while (targetClass != null && !Object.class.equals(targetClass)) {
			for (Method method : getDeclaredMethods(targetClass)) {
				if (getAnnotation(method, annotationType) != null) {
					result.add(method);
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		return result;
	}

	/**
	 * 查找PropertyDescriptor
	 * 
	 * @param type
	 *            类
	 * @param annotationType
	 *            Annotation类
	 * @return PropertyDescriptor
	 */
	public static List<PropertyDescriptor> getPropertyDescriptors(Class<?> type, Class<? extends Annotation> annotationType) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");
		Assert.notNull(annotationType, "[Assertion failed] - annotationType is required; it must not be null");

		List<PropertyDescriptor> result = new ArrayList<>();
		for (PropertyDescriptor propertyDescriptor : getPropertyDescriptors(type)) {
			Method readMethod = propertyDescriptor.getReadMethod();
			Method writeMethod = propertyDescriptor.getWriteMethod();
			for (Method method : Arrays.asList(readMethod, writeMethod)) {
				if (method == null) {
					continue;
				}
				if (getAnnotation(method, annotationType) != null) {
					result.add(propertyDescriptor);
				}
			}
		}
		return result;
	}

	/**
	 * 获取Annotation
	 * 
	 * @param annotatedElement
	 *            Annotation元素
	 * @param annotationType
	 *            Annotation类
	 * @return Annotation
	 */
	private static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
		Assert.notNull(annotatedElement, "[Assertion failed] - annotatedElement is required; it must not be null");
		Assert.notNull(annotationType, "[Assertion failed] - annotationType is required; it must not be null");

		A annotation = annotatedElement.getAnnotation(annotationType);
		if (annotation == null) {
			for (Annotation metaAnnotation : annotatedElement.getAnnotations()) {
				annotation = metaAnnotation.annotationType().getAnnotation(annotationType);
				if (annotation != null) {
					break;
				}
			}
		}
		return annotation;
	}

	/**
	 * 获取Field
	 * 
	 * @param type
	 *            类
	 * @return Field
	 */
	private static Field[] getDeclaredFields(Class<?> type) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

		Field[] result = DECLARED_FIELDS_CACHE.get(type);
		if (result == null) {
			result = type.getDeclaredFields();
			DECLARED_FIELDS_CACHE.put(type, result != null ? result : new Field[0]);
		}
		return result;
	}

	/**
	 * 获取Method
	 * 
	 * @param type
	 *            类
	 * @return Method
	 */
	private static Method[] getDeclaredMethods(Class<?> type) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

		Method[] result = DECLARED_METHODS_CACHE.get(type);
		if (result == null) {
			Method[] declaredMethods = type.getDeclaredMethods();
			Method[] defaultMethods = findConcreteMethodsOnInterfaces(type);
			result = (Method[]) ArrayUtils.addAll(declaredMethods, defaultMethods);
			DECLARED_METHODS_CACHE.put(type, result != null ? result : new Method[0]);
		}
		return result;
	}

	/**
	 * 获取接口实现Method
	 * 
	 * @param type
	 *            类
	 * @return 接口实现Method
	 */
	private static Method[] findConcreteMethodsOnInterfaces(Class<?> type) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

		List<Method> foundMethods = new ArrayList<>();
		for (Class<?> ifc : type.getInterfaces()) {
			for (Method method : ifc.getMethods()) {
				if (!Modifier.isAbstract(method.getModifiers())) {
					foundMethods.add(method);
				}
			}
		}
		return foundMethods.toArray(new Method[foundMethods.size()]);
	}

	/**
	 * 获取PropertyDescriptor
	 * 
	 * @param type
	 *            类
	 * @return PropertyDescriptor
	 */
	private static PropertyDescriptor[] getPropertyDescriptors(Class<?> type) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

		PropertyDescriptor[] result = PROPERTY_DESCRIPTORS_CACHE.get(type);
		if (result == null) {
			try {
				result = Introspector.getBeanInfo(type).getPropertyDescriptors();
			} catch (IntrospectionException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			PROPERTY_DESCRIPTORS_CACHE.put(type, result != null ? result : new PropertyDescriptor[0]);
		}
		return result;
	}

}