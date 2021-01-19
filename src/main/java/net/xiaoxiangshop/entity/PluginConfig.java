package net.xiaoxiangshop.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 插件配置
 * 
 */
@Entity
public class PluginConfig extends OrderedEntity<PluginConfig> {

	private static final long serialVersionUID = -4357367409438384806L;

	/**
	 * 插件ID
	 */
	private String pluginId;

	/**
	 * 是否启用
	 */
	private Boolean isEnabled;

	/**
	 * 属性
	 */
	@TableField(exist = false)
	private Map<String, String> attributes = new HashMap<>();

	/**
	 * 获取插件ID
	 * 
	 * @return 插件ID
	 */
	public String getPluginId() {
		return pluginId;
	}

	/**
	 * 设置插件ID
	 * 
	 * @param pluginId
	 *            插件ID
	 */
	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	/**
	 * 获取是否启用
	 * 
	 * @return 是否启用
	 */
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	/**
	 * 设置是否启用
	 * 
	 * @param isEnabled
	 *            是否启用
	 */
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * 获取属性
	 * 
	 * @return 属性
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * 设置属性
	 * 
	 * @param attributes
	 *            属性
	 */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	/**
	 * 获取属性值
	 * 
	 * @param name
	 *            属性名称
	 * @return 属性值
	 */
	@Transient
	public String getAttribute(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		return getAttributes() != null ? getAttributes().get(name) : null;
	}


}