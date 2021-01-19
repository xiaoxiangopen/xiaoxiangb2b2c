package net.xiaoxiangshop.config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mobile.device.DeviceWebArgumentResolver;
import org.springframework.mobile.device.site.SitePreferenceWebArgumentResolver;
import org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver;
import org.springframework.stereotype.Controller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.audit.AuditLogInterceptor;
import net.xiaoxiangshop.audit.AuditLogMethodArgumentResolver;
import net.xiaoxiangshop.captcha.CaptchaInterceptor;
import net.xiaoxiangshop.entity.Admin;
import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.security.CsrfInterceptor;
import net.xiaoxiangshop.security.CurrentCartHandlerInterceptor;
import net.xiaoxiangshop.security.CurrentCartMethodArgumentResolver;
import net.xiaoxiangshop.security.CurrentStoreHandlerInterceptor;
import net.xiaoxiangshop.security.CurrentStoreMethodArgumentResolver;
import net.xiaoxiangshop.security.CurrentUserHandlerInterceptor;
import net.xiaoxiangshop.security.CurrentUserMethodArgumentResolver;
import net.xiaoxiangshop.security.XssInterceptor;
import net.xiaoxiangshop.security.XssInterceptor.WhitelistType;

@Configuration
@PropertySource("classpath:xiaoxiangshop.properties")
@ComponentScan(includeFilters = {
        @org.springframework.context.annotation.ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Controller.class),
        @org.springframework.context.annotation.ComponentScan.Filter(type = FilterType.ANNOTATION, classes = ControllerAdvice.class)})
public class ApplicationContextMVC implements WebMvcConfigurer {

    @Value("${html_content_type}")
    private String contentType;

    @Value("${template.suffix}")
    private String suffix;

    @Value("${json_content_type}")
    private String jsonContentType;

    @Value("${html_content_type}")
    private String htmlContentType;

    @Value("${basic_image_path}")
    private String basicImagePath;

    @Value("${before_image_upload_path}")
    private String beforeImageUploadPath;

    @Value("${before_image_userfiles_path}")
    private String beforeImageUserfilespath;

    @Value("${image_upload_path}")
    private String imageUploadPath;


    @Inject
    private MessageSource messageSource;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ServletWebArgumentResolverAdapter(new DeviceWebArgumentResolver()));
        resolvers.add(new ServletWebArgumentResolverAdapter(new SitePreferenceWebArgumentResolver()));
        resolvers.add(currentUserMethodArgumentResolver());
        resolvers.add(currentCartMethodArgumentResolver());
        resolvers.add(currentStoreMethodArgumentResolver());
        resolvers.add(auditLogMethodArgumentResolver());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper objectMapper = new ObjectMapper();
        // null字段不返回
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // mybatis 使用懒加载后，返回JSON报错
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        converters.add(new BufferedImageHttpMessageConverter());
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        supportedMediaTypes.add(MediaType.parseMediaType(jsonContentType));
        supportedMediaTypes.add(MediaType.parseMediaType(htmlContentType));
        jackson2HttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:static/favicon.ico").setCachePeriod(86400);
        registry.addResourceHandler("/robots.txt").addResourceLocations("classpath:static/robots.txt").setCachePeriod(86400);
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:static/resources/").setCachePeriod(86400);

        // 本地测试时要这样配置实际的目录
        registry.addResourceHandler("/userfiles/**").addResourceLocations("file:" + beforeImageUserfilespath).setCachePeriod(86400);
        registry.addResourceHandler("/UploadFile/**").addResourceLocations("file:" + beforeImageUploadPath).setCachePeriod(86400);
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + imageUploadPath).setCachePeriod(86400);
        registry.addResourceHandler("/6.0/**").addResourceLocations("file:" + basicImagePath).setCachePeriod(86400);
    }

    @Bean
    public WebContentInterceptor resourcesInterceptor() {
        WebContentInterceptor resourcesInterceptor = new WebContentInterceptor();
        resourcesInterceptor.setCacheSeconds(86400);
        return resourcesInterceptor;
    }

    @Bean
    public WebContentInterceptor webContentInterceptor() {
        WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
        webContentInterceptor.setCacheSeconds(0);
        return webContentInterceptor;
    }

    /**
     * 当前用户MethodArgumentResolver
     */
    @Bean
    public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
        return new CurrentUserMethodArgumentResolver();
    }

    /**
     * 当前购物车MethodArgumentResolver
     */
    @Bean
    public CurrentCartMethodArgumentResolver currentCartMethodArgumentResolver() {
        return new CurrentCartMethodArgumentResolver();
    }

    /**
     * 当前店铺MethodArgumentResolver
     */
    @Bean
    public CurrentStoreMethodArgumentResolver currentStoreMethodArgumentResolver() {
        return new CurrentStoreMethodArgumentResolver();
    }

    /**
     * 审计日志MethodArgumentResolver
     */
    @Bean
    public AuditLogMethodArgumentResolver auditLogMethodArgumentResolver() {
        return new AuditLogMethodArgumentResolver();
    }

    /**
     * 当前用户拦截器
     */
    @Bean
    public CurrentUserHandlerInterceptor memberInterceptor() {
        CurrentUserHandlerInterceptor memberInterceptor = new CurrentUserHandlerInterceptor();
        memberInterceptor.setUserClass(Member.class);
        return memberInterceptor;
    }

    /**
     * 当前商家拦截器
     */
    @Bean
    public CurrentUserHandlerInterceptor businessInterceptor() {
        CurrentUserHandlerInterceptor businessInterceptor = new CurrentUserHandlerInterceptor();
        businessInterceptor.setUserClass(Business.class);
        return businessInterceptor;
    }

    /**
     * 当前管理员拦截器
     */
    @Bean
    public CurrentUserHandlerInterceptor adminInterceptor() {
        CurrentUserHandlerInterceptor adminInterceptor = new CurrentUserHandlerInterceptor();
        adminInterceptor.setUserClass(Admin.class);
        return adminInterceptor;
    }

    /**
     * 当前购物车拦截器
     */
    @Bean
    public CurrentCartHandlerInterceptor currentCartHandlerInterceptor() {
        return new CurrentCartHandlerInterceptor();
    }

    /**
     * 当前店铺拦截器
     */
    @Bean
    public CurrentStoreHandlerInterceptor currentStoreHandlerInterceptor() {
        return new CurrentStoreHandlerInterceptor();
    }

    /**
     * CSRF拦截器
     */
    @Bean
    public CsrfInterceptor csrfInterceptor() {
        return new CsrfInterceptor();
    }

    /**
     * XSS拦截器
     */
    @Bean
    public XssInterceptor xssInterceptor() {
        return new XssInterceptor();
    }

    /**
     * XSS拦截器XSS拦截器白名单XSS拦截器白名单
     */
    @Bean
    public XssInterceptor whiteListXssInterceptor() {
        XssInterceptor xssInterceptor = new XssInterceptor();
        xssInterceptor.setWhitelistType(WhitelistType.RELAXED);
        return xssInterceptor;
    }

    /**
     * 会员验证码拦截器
     */
    @Bean
    public CaptchaInterceptor memberRegister() {
        CaptchaInterceptor memberRegister = new CaptchaInterceptor();
        memberRegister.setCaptchaType(Setting.CaptchaType.MEMBER_REGISTER);
        return memberRegister;
    }

    /**
     * 商家验证码拦截器
     */
    @Bean
    public CaptchaInterceptor businessRegister() {
        CaptchaInterceptor businessRegister = new CaptchaInterceptor();
        businessRegister.setCaptchaType(Setting.CaptchaType.BUSINESS_REGISTER);
        return businessRegister;
    }

    /**
     * 评论验证码拦截器
     */
    @Bean
    public CaptchaInterceptor review() {
        CaptchaInterceptor review = new CaptchaInterceptor();
        review.setCaptchaType(Setting.CaptchaType.REVIEW);
        return review;
    }

    /**
     * 咨询验证码拦截器
     */
    @Bean
    public CaptchaInterceptor consultation() {
        CaptchaInterceptor consultation = new CaptchaInterceptor();
        consultation.setCaptchaType(Setting.CaptchaType.CONSULTATION);
        return consultation;
    }

    /**
     * 找回密码验证码拦截器
     */
    @Bean
    public CaptchaInterceptor forgotPassword() {
        CaptchaInterceptor resetPassword = new CaptchaInterceptor();
        resetPassword.setCaptchaType(Setting.CaptchaType.FORGOT_PASSWORD);
        return resetPassword;
    }

    /**
     * 找回密码验证码拦截器
     */
    @Bean
    public CaptchaInterceptor resetPassword() {
        CaptchaInterceptor resetPassword = new CaptchaInterceptor();
        resetPassword.setCaptchaType(Setting.CaptchaType.RESET_PASSWORD);
        return resetPassword;
    }

    /**
     * 审计日志拦截器
     */
    @Bean
    public AuditLogInterceptor auditLogInterceptor() {
        return new AuditLogInterceptor();
    }

    /**
     * 增加拦截器
     */
    public void addInterceptors(InterceptorRegistry registry) {
        // RESOURCES
        registry.addInterceptor(resourcesInterceptor()).addPathPatterns("/resources/**");
        // WEB
        registry.addInterceptor(webContentInterceptor()).addPathPatterns("/cart/**", "/order/**", "/member/**", "/business/**", "/admin/**");
        // MEMBER
        registry.addInterceptor(memberInterceptor()).addPathPatterns("/cart/**", "/order/**", "/member/**");
        // BUSINESS
        registry.addInterceptor(businessInterceptor()).addPathPatterns("/business/**");
        // ADMIN
        registry.addInterceptor(adminInterceptor()).addPathPatterns("/admin/**");
        // CART
        registry.addInterceptor(currentCartHandlerInterceptor()).addPathPatterns("/cart/**", "/order/**");
        // STORE
        registry.addInterceptor(currentStoreHandlerInterceptor()).addPathPatterns("/business/**");
        // CSRF
        registry.addInterceptor(csrfInterceptor()).addPathPatterns("/**").excludePathPatterns("/payment/**", "/api/**");
        // XSS拦截器
        registry.addInterceptor(xssInterceptor()).addPathPatterns("/**").excludePathPatterns("/admin/**", "/business/**");
        // XSS拦截器白名单
        registry.addInterceptor(whiteListXssInterceptor()).addPathPatterns("/admin/**", "/business/**").excludePathPatterns("/admin/template/**", "/admin/ad_position/**");

        // memberCaptcha
        registry.addInterceptor(memberRegister()).addPathPatterns("/member/register/submit");
        // businessCaptcha
        registry.addInterceptor(businessRegister()).addPathPatterns("/business/register/submit");
        // consultationCaptcha
        registry.addInterceptor(consultation()).addPathPatterns("/consultation/save");
        // forgotPasswordCaptcha
        registry.addInterceptor(forgotPassword()).addPathPatterns("/password/forgot");
        // resetPasswordCaptcha
        registry.addInterceptor(resetPassword()).addPathPatterns("/password/reset");
        // auditLog
        registry.addInterceptor(auditLogInterceptor()).addPathPatterns("/admin/**");

    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }

    @Bean
    public LiteDeviceDelegatingViewResolver viewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setContentType(contentType);
        resolver.setSuffix(suffix);

        LiteDeviceDelegatingViewResolver viewResolver = new LiteDeviceDelegatingViewResolver(resolver);
        viewResolver.setMobilePrefix("mobile/");
        viewResolver.setTabletPrefix("tablet/");
        viewResolver.setEnableFallback(true);
        return viewResolver;
    }


}
