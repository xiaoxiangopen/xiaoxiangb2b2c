package net.xiaoxiangshop.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.xiaoxiangshop.CommonAttributes;
import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.TemplateConfig;

/**
 * Utils - 系统
 * 
 */
public final class SystemUtils {

	/**
	 * ConversionService
	 */
	private static final ConversionService CONVERSION_SERVICE = new DefaultConversionService();

	/**
	 * CacheManager
	 */
	private static final CacheManager CACHE_MANAGER = CacheManager.create();

	/**
	 * 不可实例化
	 */
	private SystemUtils() {
	}

	/**
	 * 获取系统设置
	 * 
	 * @return 系统设置
	 */
	@SuppressWarnings("unchecked")
	public static Setting getSetting() {
		Ehcache cache = CACHE_MANAGER.getEhcache(Setting.CACHE_NAME);
		String cacheKey = "setting";
		Element cacheElement = cache.get(cacheKey);
		if (cacheElement == null) {
			Setting setting = new Setting();
			try {
				File xiaoxiangshopXmlFile = ResourceUtils.getFile(CommonAttributes.XIAOXIANGSHOP_XML_PATH);
				Document document = new SAXReader().read(xiaoxiangshopXmlFile);
				List<org.dom4j.Element> elements = document.selectNodes("/xiaoxiangshop/setting");
				for (org.dom4j.Element element : elements) {
					try {
						String name = element.attributeValue("name");
						String value = element.attributeValue("value");
						Object property = CONVERSION_SERVICE.convert(value, PropertyUtils.getPropertyType(setting, name));
						PropertyUtils.setProperty(setting, name, property);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e.getMessage(), e);
					} catch (InvocationTargetException e) {
						throw new RuntimeException(e.getMessage(), e);
					} catch (NoSuchMethodException e) {
						throw new RuntimeException(e.getMessage(), e);
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			cache.put(new Element(cacheKey, setting));
			cacheElement = cache.get(cacheKey);
		}
		return (Setting) cacheElement.getObjectValue();
	}

	/**
	 * 设置系统设置
	 * 
	 * @param setting
	 *            系统设置
	 */
	@SuppressWarnings("unchecked")
	public static void setSetting(Setting setting) {
		Assert.notNull(setting, "[Assertion failed] - setting is required; it must not be null");

		try {
			File xiaoxiangshopXmlFile = ResourceUtils.getFile(CommonAttributes.XIAOXIANGSHOP_XML_PATH);
			Document document = new SAXReader().read(xiaoxiangshopXmlFile);
			List<org.dom4j.Element> elements = document.selectNodes("/xiaoxiangshop/setting");
			for (org.dom4j.Element element : elements) {
				try {
					String name = element.attributeValue("name");
					String value = CONVERSION_SERVICE.convert(PropertyUtils.getProperty(setting, name), String.class);
					Attribute attribute = element.attribute("value");
					attribute.setValue(value);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
			XMLWriter xmlWriter = null;
			try {
				OutputFormat outputFormat = OutputFormat.createPrettyPrint();
				outputFormat.setEncoding("UTF-8");
				outputFormat.setIndent(true);
				outputFormat.setIndent("	");
				outputFormat.setNewlines(true);
				xmlWriter = new XMLWriter(new FileOutputStream(xiaoxiangshopXmlFile), outputFormat);
				xmlWriter.write(document);
				xmlWriter.flush();
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				try {
					if (xmlWriter != null) {
						xmlWriter.close();
					}
				} catch (IOException e) {
				}
			}
			Ehcache cache = CACHE_MANAGER.getEhcache(Setting.CACHE_NAME);
			String cacheKey = "setting";
			cache.put(new Element(cacheKey, setting));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (DocumentException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 获取模板配置
	 * 
	 * @param id
	 *            ID
	 * @return 模板配置
	 */
	public static TemplateConfig getTemplateConfig(String id) {
		Assert.hasText(id, "[Assertion failed] - id must have text; it must not be null, empty, or blank");

		Ehcache cache = CACHE_MANAGER.getEhcache(TemplateConfig.CACHE_NAME);
		String cacheKey = "templateConfig_" + id;
		Element cacheElement = cache.get(cacheKey);
		if (cacheElement == null) {
			TemplateConfig templateConfig = null;
			try {
				File xiaoxiangshopXmlFile = ResourceUtils.getFile(CommonAttributes.XIAOXIANGSHOP_XML_PATH);
				Document document = new SAXReader().read(xiaoxiangshopXmlFile);
				org.dom4j.Element element = (org.dom4j.Element) document.selectSingleNode("/xiaoxiangshop/templateConfig[@id='" + id + "']");
				if (element != null) {
					templateConfig = getTemplateConfig(element);
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			cache.put(new Element(cacheKey, templateConfig));
			cacheElement = cache.get(cacheKey);
		}
		return (TemplateConfig) cacheElement.getObjectValue();
	}

	/**
	 * 获取模板配置
	 * 
	 * @param type
	 *            类型
	 * @return 模板配置
	 */
	@SuppressWarnings("unchecked")
	public static List<TemplateConfig> getTemplateConfigs(TemplateConfig.Type type) {
		Ehcache cache = CACHE_MANAGER.getEhcache(TemplateConfig.CACHE_NAME);
		String cacheKey = "templateConfigs_" + type;
		Element cacheElement = cache.get(cacheKey);
		if (cacheElement == null) {
			List<TemplateConfig> templateConfigs = new ArrayList<>();
			try {
				File xiaoxiangshopXmlFile = ResourceUtils.getFile(CommonAttributes.XIAOXIANGSHOP_XML_PATH);
				Document document = new SAXReader().read(xiaoxiangshopXmlFile);
				List<org.dom4j.Element> elements = document.selectNodes(type != null ? "/xiaoxiangshop/templateConfig[@type='" + type + "']" : "/xiaoxiangshop/templateConfig");
				for (org.dom4j.Element element : elements) {
					templateConfigs.add(getTemplateConfig(element));
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			cache.put(new Element(cacheKey, templateConfigs));
			cacheElement = cache.get(cacheKey);
		}
		return (List<TemplateConfig>) cacheElement.getObjectValue();
	}

	/**
	 * 获取所有模板配置
	 * 
	 * @return 所有模板配置
	 */
	public static List<TemplateConfig> getTemplateConfigs() {
		return getTemplateConfigs(null);
	}

	/**
	 * 获取模板配置
	 * 
	 * @param element
	 *            元素
	 * @return 模板配置
	 */
	private static TemplateConfig getTemplateConfig(org.dom4j.Element element) {
		Assert.notNull(element, "[Assertion failed] - element is required; it must not be null");

		String id = element.attributeValue("id");
		String type = element.attributeValue("type");
		String name = element.attributeValue("name");
		String templatePath = element.attributeValue("templatePath");
		String description = element.attributeValue("description");

		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setId(id);
		templateConfig.setType(TemplateConfig.Type.valueOf(type));
		templateConfig.setName(name);
		templateConfig.setTemplatePath(templatePath);
		templateConfig.setDescription(description);
		return templateConfig;
	}

}