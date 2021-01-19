package net.xiaoxiangshop.entity;

import java.io.Serializable;

import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * Entity - Sitemap索引
 * 
 */
public class SitemapIndex implements Serializable {

	private static final long serialVersionUID = 4238589433765772175L;

	/**
	 * 链接地址
	 */
	private static final String LOC = "%s/sitemap/%s/%d.xml";

	/**
	 * 类型
	 */
	private SitemapUrl.Type type;

	/**
	 * 索引
	 */
	private Integer index;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public SitemapUrl.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(SitemapUrl.Type type) {
		this.type = type;
	}

	/**
	 * 获取索引
	 * 
	 * @return 索引
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * 设置索引
	 * 
	 * @param index
	 *            索引
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * 获取链接地址
	 * 
	 * @return 链接地址
	 */
	public String getLoc() {
		Setting setting = SystemUtils.getSetting();
		return String.format(SitemapIndex.LOC, setting.getSiteUrl(), getType(), getIndex());
	}

}