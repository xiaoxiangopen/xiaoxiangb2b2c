package net.xiaoxiangshop;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 模板配置
 * 
 */
public class TemplateConfig implements Serializable {

	private static final long serialVersionUID = -517540800437045215L;

	/**
	 * 缓存名称
	 */
	public static final String CACHE_NAME = "templateConfig";

	/**
	 * 类型
	 */
	public enum Type {

		/**
		 * 页面模板
		 */
		PAGE,

		/**
		 * 打印模板
		 */
		PRINT,

		/**
		 * 邮件模板
		 */
		MAIL,

		/**
		 * 短信模板
		 */
		SMS
	}

	/**
	 * ID
	 */
	private String id;

	/**
	 * 类型
	 */
	private TemplateConfig.Type type;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 模板文件路径
	 */
	private String templatePath;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置ID
	 * 
	 * @param id
	 *            ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public TemplateConfig.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(TemplateConfig.Type type) {
		this.type = type;
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
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取模板文件路径
	 * 
	 * @return 模板文件路径
	 */
	public String getTemplatePath() {
		return templatePath;
	}

	/**
	 * 设置模板文件路径
	 * 
	 * @param templatePath
	 *            模板文件路径
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	/**
	 * 获取描述
	 * 
	 * @return 描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置描述
	 * 
	 * @param description
	 *            描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 重写equals方法
	 * 
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

}