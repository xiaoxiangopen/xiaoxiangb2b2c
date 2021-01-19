package net.xiaoxiangshop.entity;

import java.io.IOException;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;

import freemarker.template.TemplateException;
import net.xiaoxiangshop.util.FreeMarkerUtils;

/**
 * Entity - 广告位
 * 
 */
@Entity
public class AdPosition extends BaseEntity<AdPosition> {

	private static final long serialVersionUID = -7849848867030199578L;

	/**
	 * 名称
	 */
	@NotEmpty
	@Column(nullable = false)
	private String name;

	/**
	 * 宽度
	 */
	@NotNull
	@Min(1)
	@Column(nullable = false)
	private Integer width;

	/**
	 * 高度
	 */
	@NotNull
	@Min(1)
	@Column(nullable = false)
	private Integer height;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 模板
	 */
	@NotEmpty
	@Lob
	private String template;

	/**
	 * 广告
	 */
	@TableField(exist = false)
	private List<Ad> ads = new ArrayList<>();

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
	 * 获取宽度
	 * 
	 * @return 宽度
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * 设置宽度
	 * 
	 * @param width
	 *            宽度
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * 获取高度
	 * 
	 * @return 高度
	 */
	public Integer getHeight() {
		return height;
	}

	/**
	 * 设置高度
	 * 
	 * @param height
	 *            高度
	 */
	public void setHeight(Integer height) {
		this.height = height;
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
	 * 获取模板
	 * 
	 * @return 模板
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * 设置模板
	 * 
	 * @param template
	 *            模板
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	public List<Ad> getAds() {
		return ads;
	}
	public void setAds(List<Ad> ads) {
		this.ads = ads;
	}
	/**
	 * 解析模板
	 * 
	 * @return 内容
	 */
	@Transient
	public String resolveTemplate() {
		try {
			Map<String, Object> model = new HashMap<>();
			model.put("adPosition", this);
			return FreeMarkerUtils.process(getTemplate(), model);
		} catch (IOException e) {
			return null;
		} catch (TemplateException e) {
			return null;
		}
	}

}