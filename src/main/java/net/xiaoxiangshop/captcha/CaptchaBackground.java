package net.xiaoxiangshop.captcha;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.util.ClassUtils;

import com.google.code.kaptcha.BackgroundProducer;
import com.google.code.kaptcha.util.Configurable;

/**
 * Captcha - 验证码背景
 * 
 */
public class CaptchaBackground extends Configurable implements BackgroundProducer {

	/**
	 * "背景图片路径"属性名称
	 */
	private static final String BACKGROUND_IMAGE_PATH_PROPERTY_NAME = "kaptcha.background.imagePath";

	/**
	 * 背景图片缓存
	 */
	private static final List<BufferedImage> BACKGROUND_IMAGES_CACHE = new CopyOnWriteArrayList<>();
	
	/**
	 * 添加背景
	 * 
	 * @param baseImage
	 *            基本图片
	 * @return 目标图片
	 */
	@Override
	public BufferedImage addBackground(BufferedImage baseImage) {
		List<BufferedImage> backgroundImages = getBackgroundImages();
		if (CollectionUtils.isEmpty(backgroundImages)) {
			return baseImage;
		}
		
		Graphics2D graphics2D = null;
		try {
			int width = baseImage.getWidth();
			int height = baseImage.getHeight();
			BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			graphics2D = destImage.createGraphics();
			int random = RandomUtils.nextInt(backgroundImages.size());
			BufferedImage backgroundImage = backgroundImages.get(random);
			graphics2D.drawImage(backgroundImage, 0, 0, null);
			graphics2D.drawImage(baseImage, 0, 0, null);
			return destImage;
		} finally {
			if (graphics2D != null) {
				graphics2D.dispose();
			}
		}
	}

	/**
	 * 获取背景图片
	 * 
	 * @return 背景图片
	 */
	private List<BufferedImage> getBackgroundImages() {
		if (CollectionUtils.isNotEmpty(BACKGROUND_IMAGES_CACHE)) {
			return BACKGROUND_IMAGES_CACHE;
		}

		Properties properties = getConfig().getProperties();
		String backgroundImagePath = properties.getProperty(BACKGROUND_IMAGE_PATH_PROPERTY_NAME);
		if (StringUtils.isNotEmpty(backgroundImagePath)) {
			String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
			File backgroundImageDir = new File(path + backgroundImagePath);
			for (File file : FileUtils.listFiles(backgroundImageDir, null, true)) {
				try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
					BufferedImage backgroundImage = ImageIO.read(inputStream);
					if (backgroundImage != null) {
						BACKGROUND_IMAGES_CACHE.add(backgroundImage);
					}
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
		return BACKGROUND_IMAGES_CACHE;
	}

}