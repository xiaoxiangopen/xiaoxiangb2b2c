package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.xiaoxiangshop.service.CacheService;
import net.xiaoxiangshop.service.ConfigService;

/**
 * Service - 缓存
 * 
 */
@Service
public class CacheServiceImpl implements CacheService {

	@Inject
	private CacheManager cacheManager;
	@Inject
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Inject
	private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;
	@Inject
	private ConfigService configService;

	@Override
	public String getDiskStorePath() {
		return cacheManager.getConfiguration().getDiskStoreConfiguration().getPath();
	}

	@Override
	public int getCacheSize() {
		int cacheSize = 0;
		String[] cacheNames = cacheManager.getCacheNames();
		if (cacheNames != null) {
			for (String cacheName : cacheNames) {
				Ehcache cache = cacheManager.getEhcache(cacheName);
				if (cache != null) {
					cacheSize += cache.getSize();
				}
			}
		}
		return cacheSize;
	}

	@Override
	@CacheEvict(value = { "setting", "templateConfig", "pluginConfig", "messageConfig", "indexPage", "storeIndexPage", "articleListPage", "articleDetailPage", "productListPage", "productDetailPage", "brandListPage", "brandDetailPage", "productCategoryPage", "friendLinkPage",
			"consultationDetailPage", "reviewDetailPage", "areaPage", "baseJsPage", "sitemapPage", "seo", "adPosition", "memberAttribute", "businessAttribute", "navigation", "friendLink", "instantMessage", "brand", "attribute", "article", "articleCategory", "articleTag", "product",
			"productCategory", "productTag", "storeProductCategory", "storeProductTag", "storeAdImage", "productFavorite", "storeFavorite", "consultation", "review", "transitSteps", "authorization" }, allEntries = true)
	public void clear() {
		freeMarkerConfigurer.getConfiguration().clearTemplateCache();
		reloadableResourceBundleMessageSource.clearCache();
		configService.init();
	}

}