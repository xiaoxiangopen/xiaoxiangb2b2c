package net.xiaoxiangshop.service;

import java.util.List;

import net.xiaoxiangshop.entity.SitemapIndex;
import net.xiaoxiangshop.entity.SitemapUrl;

/**
 * Service - Sitemap索引
 * 
 */
public interface SitemapIndexService {

	/**
	 * 生成Sitemap索引
	 * 
	 * @param type
	 *            类型
	 * @param maxSitemapUrlSize
	 *            最大Sitemap URL数量
	 * @return Sitemap索引
	 */
	List<SitemapIndex> generate(SitemapUrl.Type type, int maxSitemapUrlSize);

}