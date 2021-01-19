package net.xiaoxiangshop.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import net.xiaoxiangshop.TemplateConfig;
import net.xiaoxiangshop.service.TemplateService;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * Service - 模板
 * 
 */
@Service
public class TemplateServiceImpl implements TemplateService {

	@Value("${template.loader_path}")
	private String templateLoaderPath;

	@Override
	public String read(String templateConfigId) {
		Assert.hasText(templateConfigId, "[Assertion failed] - templateConfigId must have text; it must not be null, empty, or blank");

		TemplateConfig templateConfig = SystemUtils.getTemplateConfig(templateConfigId);
		return read(templateConfig);
	}

	@Override
	public String read(TemplateConfig templateConfig) {
		Assert.notNull(templateConfig, "[Assertion failed] - templateConfig is required; it must not be null");

		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource(templateLoaderPath + templateConfig.getTemplatePath());
		try (InputStream inputStream = new BufferedInputStream(resource.getInputStream())){
			return IOUtils.toString(inputStream, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void write(String templateConfigId, String content) {
		Assert.hasText(templateConfigId, "[Assertion failed] - templateConfigId must have text; it must not be null, empty, or blank");

		TemplateConfig templateConfig = SystemUtils.getTemplateConfig(templateConfigId);
		write(templateConfig, content);
	}

	@Override
	public void write(TemplateConfig templateConfig, String content) {
		Assert.notNull(templateConfig, "[Assertion failed] - templateConfig is required; it must not be null");

		try {
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource resource = resolver.getResource(templateLoaderPath + templateConfig.getTemplatePath());
			File templateFile = new File(resource.getFile().getPath());
			FileUtils.writeStringToFile(templateFile, content, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}