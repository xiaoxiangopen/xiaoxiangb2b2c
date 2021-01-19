package net.xiaoxiangshop.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import net.xiaoxiangshop.template.directive.*;
import org.apache.commons.lang3.StringUtils;
import org.perf4j.log4j.aop.TimingAspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.jagregory.shiro.freemarker.ShiroTags;

import freemarker.template.utility.StandardCompress;
import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.captcha.CaptchaFilter;
import net.xiaoxiangshop.template.method.AbbreviateMethod;
import net.xiaoxiangshop.template.method.CurrencyMethod;
import net.xiaoxiangshop.template.method.MessageMethod;
import net.xiaoxiangshop.util.SystemUtils;

@Configuration
@PropertySource("classpath:xiaoxiangshop.properties")
@EnableCaching
@ComponentScan(excludeFilters = {
		@org.springframework.context.annotation.ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Controller.class),
		@org.springframework.context.annotation.ComponentScan.Filter(type = FilterType.ANNOTATION, classes = ControllerAdvice.class) })
public class ApplicationContext {

	@Value("${template.datetime_format}")
	private String dateFormat;

	@Value("${template.time_format}")
	private String timeFormat;

	@Value("${template.datetime_format}")
	private String dateTimeFormat;

	@Value("${template.update_delay}")
	private String update_delay;

	@Value("${template.encoding}")
	private String encoding;

	@Value("${template.number_format}")
	private String numberFormat;

	@Value("${template.boolean_format}")
	private String booleanFormat;

	@Value("${template.loader_path}")
	private String templateLoaderPath;

	@Value("${url_escaping_charset}")
	private String urlEscapingCharset;

	@Value("${task.pool_size}")
	private int corePoolSize;

	@Value("${task.max_pool_size}")
	private int maxPoolSize;

	@Value("${task.queue_capacity}")
	private int queueCapacity;

	@Value("${message.cache_seconds}")
	private int cacheSeconds;

	@Value("${message.basenames}")
	private String basenames;

	@Value("${captcha.imageWidth}")
	private String imageWidth;

	@Value("${captcha.imageHeight}")
	private String imageHeight;

	@Value("${captcha.charString}")
	private String charString;

	@Value("${captcha.charLength}")
	private String charLength;

	@Value("${captcha.charSpace}")
	private String charSpace;

	@Value("${captcha.fontColor}")
	private String fontColor;

	@Value("${captcha.fontSize}")
	private String fontSize;

	@Value("${captcha.background_image_path}")
	private String background_image_path;

	@Inject
	private WebApplicationContext webApplicationContext;

	@Bean
	public MessageMethod messageMethod() {
		return new MessageMethod();
	}

	@Bean
	public AbbreviateMethod abbreviateMethod() {
		return new AbbreviateMethod();
	}

	@Bean
	public CurrencyMethod currencyMethod() {
		return new CurrencyMethod();
	}

	@Bean
	public AdPositionDirective adPositionDirective() {
		return new AdPositionDirective();
	}

	@Bean
	public ArticleCategoryChildrenListDirective articleCategoryChildrenListDirective() {
		return new ArticleCategoryChildrenListDirective();
	}

	@Bean
	public ArticleCategoryParentListDirective articleCategoryParentListDirective() {
		return new ArticleCategoryParentListDirective();
	}

	@Bean
	public ArticleCategoryRootListDirective articleCategoryRootListDirective() {
		return new ArticleCategoryRootListDirective();
	}

	@Bean
	public ArticleListDirective articleListDirective() {
		return new ArticleListDirective();
	}

	@Bean
	public ArticleTagListDirective articleTagListDirective() {
		return new ArticleTagListDirective();
	}

	@Bean
	public AttributeListDirective attributeListDirective() {
		return new AttributeListDirective();
	}

	@Bean
	public AftersalesCountDirective aftersalesCountDirective(){return new AftersalesCountDirective();}
	@Bean
	public BrandListDirective brandListDirective() {
		return new BrandListDirective();
	}

	@Bean
	public BusinessAttributeListDirective businessAttributeListDirective() {
		return new BusinessAttributeListDirective();
	}

	@Bean
	public BusinessCashCountDirective businessCashCountDirective() {
		return new BusinessCashCountDirective();
	}

	@Bean
	public CategoryApplicationCountDirective categoryApplicationCountDirective() {
		return new CategoryApplicationCountDirective();
	}

	@Bean
	public ConsultationListDirective consultationListDirective() {
		return new ConsultationListDirective();
	}

	@Bean
	public DistributionCashCountDirective distributionCashCountDirective() {
		return new DistributionCashCountDirective();
	}

	@Bean
	public FriendLinkListDirective friendLinkListDirective() {
		return new FriendLinkListDirective();
	}

	@Bean
	public InstantMessageListDirective instantMessageListDirective() {
		return new InstantMessageListDirective();
	}

	@Bean
	public MemberAttributeListDirective memberAttributeListDirective() {
		return new MemberAttributeListDirective();
	}

	@Bean
	public NavigationListDirective navigationListDirective() {
		return new NavigationListDirective();
	}

	@Bean
	public OrderCountDirective orderCountDirective() {
		return new OrderCountDirective();
	}

	@Bean
	public PaginationDirective paginationDirective() {
		return new PaginationDirective();
	}

	@Bean
	public ProductCategoryChildrenListDirective productCategoryChildrenListDirective() {
		return new ProductCategoryChildrenListDirective();
	}

	@Bean
	public StoreProductCategoryParentListDirective storeProductCategoryParentListDirective() {
		return new StoreProductCategoryParentListDirective();
	}

	@Bean
	public ProductCategoryParentListDirective productCategoryParentListDirective() {
		return new ProductCategoryParentListDirective();
	}

	@Bean
	public ProductCategoryRootListDirective productCategoryRootListDirective() {
		return new ProductCategoryRootListDirective();
	}

	@Bean
	public ProductCountDirective productCountDirective() {
		return new ProductCountDirective();
	}

	@Bean
	public ProductFavoriteDirective productFavoriteDirective() {
		return new ProductFavoriteDirective();
	}

	@Bean
	public ProductListDirective productListDirective() {
		return new ProductListDirective();
	}

	@Bean
	public ProductTagListDirective productTagListDirective() {
		return new ProductTagListDirective();
	}

	@Bean
	public ReviewCountDirective reviewCountDirective() {
		return new ReviewCountDirective();
	}

	@Bean
	public ReviewListDirective reviewListDirective() {
		return new ReviewListDirective();
	}

	@Bean
	public SeoDirective seoDirective() {
		return new SeoDirective();
	}

	@Bean
	public StoreAdImageListDirective storeAdImageListDirective() {
		return new StoreAdImageListDirective();
	}

	@Bean
	public StoreCountDirective storeCountDirective() {
		return new StoreCountDirective();
	}

	@Bean
	public StoreFavoriteDirective storeFavoriteDirective() {
		return new StoreFavoriteDirective();
	}

	@Bean
	public StoreProductCategoryChildrenListDirective storeProductCategoryChildrenListDirective() {
		return new StoreProductCategoryChildrenListDirective();
	}
	
	@Bean
	public StoreProductCategoryRootListDirective storeProductCategoryRootListDirective() {
		return new StoreProductCategoryRootListDirective();
	}
	
	@Bean
	public StoreProductTagDirective storeProductTagDirective() {
		return new StoreProductTagDirective();
	}

	@Bean
	public HasPermissionTagDirective hasPermissionTagDirective() {
		return new HasPermissionTagDirective();
	}

	@Bean
	public HasAnyPermissionsTagDirective hasAnyPermissionsTagDirective() {
		return new HasAnyPermissionsTagDirective();
	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheManager() {
		EhCacheManagerFactoryBean ehCacheManager = new EhCacheManagerFactoryBean();
		ehCacheManager.setConfigLocation(new ClassPathResource("ehcache.xml"));
		ehCacheManager.setShared(true);
		return ehCacheManager;
	}

//	@Bean
//	public CacheManager cacheManager() {
//		return new EhCacheCacheManager(ehCacheManager().getObject());
//	}

	@Bean
	public ShiroTags shiroTags() {
		return new ShiroTags();
	}

	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		freeMarkerConfigurer.setTemplateLoaderPaths("classpath:/", templateLoaderPath);
		freeMarkerConfigurer.setServletContext(webApplicationContext.getServletContext());

		Properties settings = new Properties();
		settings.setProperty("default_encoding", encoding);
		settings.setProperty("url_escaping_charset", urlEscapingCharset);
		settings.setProperty("output_format", "HTMLOutputFormat");
		settings.setProperty("template_update_delay", update_delay);
		settings.setProperty("tag_syntax", "auto_detect");
		settings.setProperty("classic_compatible", "true");
		settings.setProperty("number_format", numberFormat);
		settings.setProperty("boolean_format", booleanFormat);
		settings.setProperty("datetime_format", dateTimeFormat);
		settings.setProperty("date_format", dateFormat);
		settings.setProperty("time_format", timeFormat);
		settings.setProperty("object_wrapper", "freemarker.ext.beans.BeansWrapper");
		freeMarkerConfigurer.setFreemarkerSettings(settings);

		Map<String, Object> variables = new HashMap<String, Object>();

		variables.put("base", "");
		variables.put("showPowered", true);
		variables.put("setting", SystemUtils.getSetting());
		variables.put("message", messageMethod());
		variables.put("abbreviate", abbreviateMethod());
		variables.put("currency", currencyMethod());
		variables.put("ad_position", adPositionDirective());
		variables.put("article_category_children_list", articleCategoryChildrenListDirective());
		variables.put("article_category_parent_list", articleCategoryParentListDirective());
		variables.put("article_category_root_list", articleCategoryRootListDirective());
		variables.put("article_list", articleListDirective());
		variables.put("article_tag_list", articleTagListDirective());
		variables.put("attribute_list", attributeListDirective());
		variables.put("aftersales_count", aftersalesCountDirective());
		variables.put("brand_list", brandListDirective());
		variables.put("business_attribute_list", businessAttributeListDirective());
		variables.put("business_cash_count", businessCashCountDirective());
		variables.put("category_application_count", categoryApplicationCountDirective());
		variables.put("consultation_list", consultationListDirective());
		variables.put("distribution_cash_count", distributionCashCountDirective());
		variables.put("friend_link_list", friendLinkListDirective());
		variables.put("instant_message_list", instantMessageListDirective());
		variables.put("member_attribute_list", memberAttributeListDirective());
		variables.put("navigation_list", navigationListDirective());
		variables.put("order_count", orderCountDirective());
		variables.put("pagination", paginationDirective());
		variables.put("product_category_children_list", productCategoryChildrenListDirective());
		variables.put("product_category_parent_list", productCategoryParentListDirective());
		variables.put("product_category_root_list", productCategoryRootListDirective());
		variables.put("product_count", productCountDirective());
		variables.put("product_favorite", productFavoriteDirective());
		variables.put("product_list", productListDirective());
		variables.put("product_tag_list", productTagListDirective());
		variables.put("review_count", reviewCountDirective());
		variables.put("review_list", reviewListDirective());
		variables.put("seo", seoDirective());
		variables.put("store_ad_image_list", storeAdImageListDirective());
		variables.put("store_count", storeCountDirective());
		variables.put("store_favorite", storeFavoriteDirective());
		variables.put("store_product_category_children_list", storeProductCategoryChildrenListDirective());
		variables.put("store_product_category_parent_list", storeProductCategoryParentListDirective());
		variables.put("store_product_category_root_list", storeProductCategoryRootListDirective());
		variables.put("store_product_tag_list", storeProductTagDirective());
		variables.put("has_permission_tag", hasPermissionTagDirective());
		variables.put("has_any_permissions_tag", hasAnyPermissionsTagDirective());
		variables.put("shiro", shiroTags());
		variables.put("compress", StandardCompress.INSTANCE);
		freeMarkerConfigurer.setFreemarkerVariables(variables);
		return freeMarkerConfigurer;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		return executor;
	}

	@Bean
	public FixedLocaleResolver localeResolver() {
		FixedLocaleResolver fixedLocaleResolver = new FixedLocaleResolver();
		return fixedLocaleResolver;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setCacheSeconds(cacheSeconds);
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setBasenames(StringUtils.split(basenames, ","));
		return messageSource;
	}

	@Bean
	public DefaultKaptcha captchaProducer() {
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		Properties properties = new Properties();
		properties.setProperty("kaptcha.border", "no");
		properties.setProperty("kaptcha.image.width", imageWidth);
		properties.setProperty("kaptcha.image.height", imageHeight);
		properties.setProperty("kaptcha.textproducer.char.string", charString);
		properties.setProperty("kaptcha.textproducer.char.length", charLength);
		properties.setProperty("kaptcha.textproducer.char.space", charSpace);
		properties.setProperty("kaptcha.textproducer.font.color", fontColor);
		properties.setProperty("kaptcha.textproducer.font.size", fontSize);
		properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
		properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
		properties.setProperty("kaptcha.background.impl", "net.xiaoxiangshop.captcha.CaptchaBackground");
		properties.setProperty("kaptcha.background.imagePath", background_image_path);

		Config config = new Config(properties);
		defaultKaptcha.setConfig(config);
		return defaultKaptcha;
	}

	@Bean
	public TimingAspect timingAspect() {
		return new TimingAspect();
	}

	@Bean
	@Scope(value = "prototype")
	public CaptchaFilter adminCaptchaFilter() {
		CaptchaFilter captchaFilter = new CaptchaFilter();
		captchaFilter.setCaptchaType(Setting.CaptchaType.ADMIN_LOGIN);
		return captchaFilter;
	}

	@Bean
	@Scope(value = "prototype")
	public CaptchaFilter businessCaptchaFilter() {
		CaptchaFilter captchaFilter = new CaptchaFilter();
		captchaFilter.setCaptchaType(Setting.CaptchaType.BUSINESS_LOGIN);
		return captchaFilter;
	}

	@Bean
	@Scope(value = "prototype")
	public CaptchaFilter memberCaptchaFilter() {
		CaptchaFilter captchaFilter = new CaptchaFilter();
		captchaFilter.setCaptchaType(Setting.CaptchaType.MEMBER_LOGIN);
		return captchaFilter;
	}

	@Bean
	public FilterRegistrationBean<CaptchaFilter> adminCaptchaBean() {
		FilterRegistrationBean<CaptchaFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(adminCaptchaFilter());
		registration.addUrlPatterns("/admin/login");
		registration.setName("adminCaptchaFilter");
		return registration;
	}

	@Bean
	public FilterRegistrationBean<CaptchaFilter> businessCaptchaBean() {
		FilterRegistrationBean<CaptchaFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(businessCaptchaFilter());
		registration.addUrlPatterns("/business/login");
		registration.setName("businessCaptchaFilter");
		return registration;
	}

	@Bean
	public FilterRegistrationBean<CaptchaFilter> memberCaptchaBean() {
		FilterRegistrationBean<CaptchaFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(memberCaptchaFilter());
		registration.addUrlPatterns("/member/login");
		registration.setName("memberCaptchaFilter");
		return registration;
	}
	
    
}
