package net.xiaoxiangshop.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.config.UploadConfig;
import net.xiaoxiangshop.entity.ProductImage;
import net.xiaoxiangshop.plugin.StoragePlugin;
import net.xiaoxiangshop.service.PluginService;
import net.xiaoxiangshop.service.ProductImageService;
import net.xiaoxiangshop.util.ImageUtils;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * Service - 商品图片
 * 
 */
@Service
public class ProductImageServiceImpl implements ProductImageService {

	/**
	 * 临时文件扩展名
	 */
	private static final String TEMP_FILE_EXTENSION = "tmp";

	@Inject
	private UploadConfig uploadConfig;
	@Inject
	private TaskExecutor taskExecutor;
	@Inject
	private PluginService pluginService;

	/**
	 * 添加图片处理任务
	 * 
	 * @param storagePlugin
	 *            存储插件
	 * @param sourcePath
	 *            原图片上传路径
	 * @param largePath
	 *            图片文件(大)上传路径
	 * @param mediumPath
	 *            图片文件(小)上传路径
	 * @param thumbnailPath
	 *            图片文件(缩略)上传路径
	 * @param tempFile
	 *            原临时文件
	 * @param contentType
	 *            原文件类型
	 */
	private void addTask(final StoragePlugin storagePlugin, final String sourcePath, final String largePath, final String mediumPath, final String thumbnailPath, final File tempFile, final String contentType) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				Setting setting = SystemUtils.getSetting();
				File watermarkFile = new File(uploadConfig.getUploadPath(), setting.getWatermarkImage());
				File largeTempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + "." + ProductImage.FILE_EXTENSION);
				File mediumTempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + "." + ProductImage.FILE_EXTENSION);
				File thumbnailTempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + "." + ProductImage.FILE_EXTENSION);
				try {
					ImageUtils.zoom(tempFile, largeTempFile, setting.getLargeProductImageWidth(), setting.getLargeProductImageHeight());
					ImageUtils.addWatermark(largeTempFile, largeTempFile, watermarkFile, setting.getWatermarkPosition(), setting.getWatermarkAlpha());
					ImageUtils.zoom(tempFile, mediumTempFile, setting.getMediumProductImageWidth(), setting.getMediumProductImageHeight());
					ImageUtils.addWatermark(mediumTempFile, mediumTempFile, watermarkFile, setting.getWatermarkPosition(), setting.getWatermarkAlpha());
					ImageUtils.zoom(tempFile, thumbnailTempFile, setting.getThumbnailProductImageWidth(), setting.getThumbnailProductImageHeight());
					storagePlugin.upload(sourcePath, tempFile, contentType);
					storagePlugin.upload(largePath, largeTempFile, ProductImage.FILE_CONTENT_TYPE);
					storagePlugin.upload(mediumPath, mediumTempFile, ProductImage.FILE_CONTENT_TYPE);
					storagePlugin.upload(thumbnailPath, thumbnailTempFile, ProductImage.FILE_CONTENT_TYPE);
				} finally {
					FileUtils.deleteQuietly(tempFile);
					FileUtils.deleteQuietly(largeTempFile);
					FileUtils.deleteQuietly(mediumTempFile);
					FileUtils.deleteQuietly(thumbnailTempFile);
				}
			}
		});
	}

	@Override
	public void filter(List<ProductImage> productImages) {
		CollectionUtils.filter(productImages, new Predicate() {
			public boolean evaluate(Object object) {
				ProductImage productImage = (ProductImage) object;
				return productImage != null && StringUtils.isNotEmpty(productImage.getSource()) && StringUtils.isNotEmpty(productImage.getLarge()) && StringUtils.isNotEmpty(productImage.getMedium()) && StringUtils.isNotEmpty(productImage.getThumbnail());
			}
		});
	}

	@Override
	public ProductImage generate(MultipartFile multipartFile) {
		Assert.notNull(multipartFile, "[Assertion failed] - multipartFile is required; it must not be null");
		Assert.state(!multipartFile.isEmpty(), "[Assertion failed] - multipartFile must not be empty");

		try {
			Setting setting = SystemUtils.getSetting();
			Map<String, Object> model = new HashMap<>();
			model.put("uuid", String.valueOf(UUID.randomUUID()));
			String uploadPath = setting.resolveImageUploadPath(model);
			String uuid = String.valueOf(UUID.randomUUID());
			String sourcePath = uploadPath + String.format(ProductImage.SOURCE_FILE_NAME, uuid, FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
			String largePath = uploadPath + String.format(ProductImage.LARGE_FILE_NAME, uuid, ProductImage.FILE_EXTENSION);
			String mediumPath = uploadPath + String.format(ProductImage.MEDIUM_FILE_NAME, uuid, ProductImage.FILE_EXTENSION);
			String thumbnailPath = uploadPath + String.format(ProductImage.THUMBNAIL_FILE_NAME, uuid, ProductImage.FILE_EXTENSION);
			for (StoragePlugin storagePlugin : pluginService.getStoragePlugins(true)) {
				File tempFile = new File(FileUtils.getTempDirectory(), uuid + "." + TEMP_FILE_EXTENSION);
				multipartFile.transferTo(tempFile);
				addTask(storagePlugin, sourcePath, largePath, mediumPath, thumbnailPath, tempFile, multipartFile.getContentType());

				ProductImage productImage = new ProductImage();
				productImage.setSource(storagePlugin.getUrl(sourcePath));
				productImage.setLarge(storagePlugin.getUrl(largePath));
				productImage.setMedium(storagePlugin.getUrl(mediumPath));
				productImage.setThumbnail(storagePlugin.getUrl(thumbnailPath));
				return productImage;
			}
		} catch (IllegalStateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return null;
	}

}