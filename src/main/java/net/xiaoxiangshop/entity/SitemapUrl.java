package net.xiaoxiangshop.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * Entity - Sitemap URL
 * 
 */
public class SitemapUrl implements Serializable {

	private static final long serialVersionUID = -3028082695610264720L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum<Integer> {

		/**
		 * 文章
		 */
		ARTICLE(0),

		/**
		 * 商品
		 */
		PRODUCT(1);
		
		private int value;

		Type(final int value) {
			this.value = value;
		}
		
		@Override
		public Integer getValue() {
			return this.value;
		}
	}

	/**
	 * 更新频率
	 */
	public enum Changefreq {

		/**
		 * 经常
		 */
		ALWAYS,

		/**
		 * 每小时
		 */
		HOURLY,

		/**
		 * 每天
		 */
		DAILY,

		/**
		 * 每周
		 */
		WEEKLY,

		/**
		 * 每月
		 */
		MONTHLY,

		/**
		 * 每年
		 */
		YEARLY,

		/**
		 * 从不
		 */
		NEVER
	}

	/**
	 * 链接地址
	 */
	private String loc;

	/**
	 * 最后修改日期
	 */
	private Date lastmod;

	/**
	 * 更新频率
	 */
	private Changefreq changefreq;

	/**
	 * 权重
	 */
	private Float priority;

	/**
	 * 获取链接地址
	 * 
	 * @return 链接地址
	 */
	public String getLoc() {
		return loc;
	}

	/**
	 * 设置链接地址
	 * 
	 * @param loc
	 *            链接地址
	 */
	public void setLoc(String loc) {
		this.loc = loc;
	}

	/**
	 * 获取最后修改日期
	 * 
	 * @return 最后修改日期
	 */
	public Date getLastmod() {
		return lastmod;
	}

	/**
	 * 设置最后修改日期
	 * 
	 * @param lastmod
	 *            最后修改日期
	 */
	public void setLastmod(Date lastmod) {
		this.lastmod = lastmod;
	}

	/**
	 * 获取更新频率
	 * 
	 * @return 更新频率
	 */
	public Changefreq getChangefreq() {
		return changefreq;
	}

	/**
	 * 设置更新频率
	 * 
	 * @param changefreq
	 *            更新频率
	 */
	public void setChangefreq(Changefreq changefreq) {
		this.changefreq = changefreq;
	}

	/**
	 * 获取权重
	 * 
	 * @return 权重
	 */
	public Float getPriority() {
		return priority;
	}

	/**
	 * 设置权重
	 * 
	 * @param priority
	 *            权重
	 */
	public void setPriority(Float priority) {
		this.priority = priority;
	}

}