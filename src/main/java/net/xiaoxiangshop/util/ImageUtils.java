package net.xiaoxiangshop.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.springframework.util.Assert;

import net.xiaoxiangshop.Setting;

/**
 * Utils - 图片处理(支持JDK、GraphicsMagick、ImageMagick)
 * 
 */
public final class ImageUtils {

	/**
	 * 方式
	 */
	private enum Method {

		/**
		 * 自动
		 */
		AUTO,

		/**
		 * Jdk
		 */
		JDK,

		/**
		 * GraphicsMagick
		 */
		GRAPHICS_MAGICK,

		/**
		 * ImageMagick
		 */
		IMAGE_MAGICK
	}

	/**
	 * 方式
	 */
	private static ImageUtils.Method method = ImageUtils.Method.AUTO;

	/**
	 * GraphicsMagick程序路径
	 */
	private static String graphicsMagickPath;

	/**
	 * ImageMagick程序路径
	 */
	private static String imageMagickPath;

	/**
	 * 背景颜色
	 */
	private static final Color BACKGROUND_COLOR = Color.white;

	/**
	 * 目标图片品质(取值范围: 0 - 100)
	 */
	private static final int DEST_QUALITY = 88;

	static {
		if (SystemUtils.IS_OS_WINDOWS) {
			String pathVariable = System.getenv("Path");
			if (StringUtils.isNotEmpty(pathVariable)) {
				String[] paths = StringUtils.split(pathVariable, ";");
				if (graphicsMagickPath == null) {
					for (String path : paths) {
						File gmFile = new File(path.trim() + "/gm.exe");
						File gmdisplayFile = new File(path.trim() + "/gmdisplay.exe");
						if (gmFile.exists() && gmdisplayFile.exists()) {
							graphicsMagickPath = path.trim();
							break;
						}
					}
				}
				if (imageMagickPath == null) {
					for (String path : paths) {
						File convertFile = new File(path.trim() + "/convert.exe");
						File compositeFile = new File(path.trim() + "/composite.exe");
						if (convertFile.exists() && compositeFile.exists()) {
							imageMagickPath = path.trim();
							break;
						}
					}
				}
			}
		}

		if (ImageUtils.Method.AUTO.equals(method)) {
			try {
				IMOperation operation = new IMOperation();
				operation.version();
				IdentifyCmd identifyCmd = new IdentifyCmd(true);
				if (graphicsMagickPath != null) {
					identifyCmd.setSearchPath(graphicsMagickPath);
				}
				identifyCmd.run(operation);
				method = ImageUtils.Method.GRAPHICS_MAGICK;
			} catch (Throwable e1) {
				try {
					IMOperation operation = new IMOperation();
					operation.version();
					IdentifyCmd identifyCmd = new IdentifyCmd(false);
					identifyCmd.run(operation);
					if (imageMagickPath != null) {
						identifyCmd.setSearchPath(imageMagickPath);
					}
					method = ImageUtils.Method.IMAGE_MAGICK;
				} catch (Throwable e2) {
					method = ImageUtils.Method.JDK;
				}
			}
		}
	}

	/**
	 * 不可实例化
	 */
	private ImageUtils() {
	}

	/**
	 * 等比例图片缩放
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param destWidth
	 *            目标宽度
	 * @param destHeight
	 *            目标高度
	 */
	public static void zoom(File srcFile, File destFile, int destWidth, int destHeight) {
		Assert.notNull(srcFile, "[Assertion failed] - srcFile is required; it must not be null");
		Assert.state(srcFile.exists(), "[Assertion failed] - srcFile must exists");
		Assert.state(srcFile.isFile(), "[Assertion failed] - srcFile must be file");
		Assert.notNull(destFile, "[Assertion failed] - destFile is required; it must not be null");
		Assert.state(destWidth > 0, "[Assertion failed] - destWidth must be greater than 0");
		Assert.state(destHeight > 0, "[Assertion failed] - destHeight must be greater than 0");

		if (ImageUtils.Method.JDK.equals(method)) {
			Graphics2D graphics2D = null;
			ImageWriter imageWriter = null;
			try (ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(destFile)){
				BufferedImage srcBufferedImage = ImageIO.read(srcFile);
				int srcWidth = srcBufferedImage.getWidth();
				int srcHeight = srcBufferedImage.getHeight();
				int width = destWidth;
				int height = destHeight;
				if (srcHeight >= srcWidth) {
					width = (int) Math.round((destHeight * 1.0 / srcHeight) * srcWidth);
				} else {
					height = (int) Math.round((destWidth * 1.0 / srcWidth) * srcHeight);
				}
				BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
				graphics2D = destBufferedImage.createGraphics();
				graphics2D.setBackground(BACKGROUND_COLOR);
				graphics2D.clearRect(0, 0, destWidth, destHeight);
				graphics2D.drawImage(srcBufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), (destWidth / 2) - (width / 2), (destHeight / 2) - (height / 2), null);

				imageWriter = ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(destFile.getName())).next();
				imageWriter.setOutput(imageOutputStream);
				ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
				imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				imageWriteParam.setCompressionQuality((float) (DEST_QUALITY / 100.0));
				imageWriter.write(null, new IIOImage(destBufferedImage, null, null), imageWriteParam);
				imageOutputStream.flush();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				if (graphics2D != null) {
					graphics2D.dispose();
				}
				if (imageWriter != null) {
					imageWriter.dispose();
				}
			}
		} else {
			IMOperation operation = new IMOperation();
			operation.thumbnail(destWidth, destHeight);
			operation.gravity("center");
			operation.background(toHexEncoding(BACKGROUND_COLOR));
			operation.extent(destWidth, destHeight);
			operation.quality((double) DEST_QUALITY);
			try {
				operation.addImage(srcFile.getCanonicalPath());
				operation.addImage(destFile.getCanonicalPath());
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			if (ImageUtils.Method.GRAPHICS_MAGICK.equals(method)) {
				ConvertCmd convertCmd = new ConvertCmd(true);
				if (graphicsMagickPath != null) {
					convertCmd.setSearchPath(graphicsMagickPath);
				}
				try {
					convertCmd.run(operation);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InterruptedException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (IM4JavaException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			} else {
				ConvertCmd convertCmd = new ConvertCmd(false);
				if (imageMagickPath != null) {
					convertCmd.setSearchPath(imageMagickPath);
				}
				try {
					convertCmd.run(operation);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InterruptedException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (IM4JavaException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 添加水印
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param watermarkFile
	 *            水印文件
	 * @param watermarkPosition
	 *            水印位置
	 * @param alpha
	 *            水印透明度
	 */
	public static void addWatermark(File srcFile, File destFile, File watermarkFile, Setting.WatermarkPosition watermarkPosition, int alpha) {
		Assert.notNull(srcFile, "[Assertion failed] - srcFile is required; it must not be null");
		Assert.state(srcFile.exists(), "[Assertion failed] - srcFile must exists");
		Assert.state(srcFile.isFile(), "[Assertion failed] - srcFile must be file");
		Assert.notNull(destFile, "[Assertion failed] - destFile is required; it must not be null");
		Assert.state(alpha >= 0, "[Assertion failed] - alpha must be equal or greater than 0");
		Assert.state(alpha <= 100, "[Assertion failed] - alpha must be equal or less than 0");

		if (watermarkFile == null || !watermarkFile.exists() || !watermarkFile.isFile() || watermarkPosition == null || Setting.WatermarkPosition.NO.equals(watermarkPosition)) {
			try {
				if (!StringUtils.equals(srcFile.getCanonicalPath(), destFile.getCanonicalPath())) {
					FileUtils.copyFile(srcFile, destFile);
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			return;
		}
		if (ImageUtils.Method.JDK.equals(method)) {
			Graphics2D graphics2D = null;
			ImageWriter imageWriter = null;
			try (ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(destFile)){
				BufferedImage srcBufferedImage = ImageIO.read(srcFile);
				int srcWidth = srcBufferedImage.getWidth();
				int srcHeight = srcBufferedImage.getHeight();
				BufferedImage destBufferedImage = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_RGB);
				graphics2D = destBufferedImage.createGraphics();
				graphics2D.setBackground(BACKGROUND_COLOR);
				graphics2D.clearRect(0, 0, srcWidth, srcHeight);
				graphics2D.drawImage(srcBufferedImage, 0, 0, null);
				graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha / 100F));

				BufferedImage watermarkBufferedImage = ImageIO.read(watermarkFile);
				int watermarkImageWidth = watermarkBufferedImage.getWidth();
				int watermarkImageHeight = watermarkBufferedImage.getHeight();
				int x;
				int y;
				switch (watermarkPosition) {
				case TOP_LEFT:
					x = 0;
					y = 0;
					break;
				case TOP_RIGHT:
					x = srcWidth - watermarkImageWidth;
					y = 0;
					break;
				case CENTER:
					x = (srcWidth - watermarkImageWidth) / 2;
					y = (srcHeight - watermarkImageHeight) / 2;
					break;
				case BOTTOM_LEFT:
					x = 0;
					y = srcHeight - watermarkImageHeight;
					break;
				case BOTTOM_RIGHT:
					x = srcWidth - watermarkImageWidth;
					y = srcHeight - watermarkImageHeight;
					break;
				default:
					x = srcWidth - watermarkImageWidth;
					y = srcHeight - watermarkImageHeight;
					break;
				}
				graphics2D.drawImage(watermarkBufferedImage, x, y, watermarkImageWidth, watermarkImageHeight, null);

				imageWriter = ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(destFile.getName())).next();
				imageWriter.setOutput(imageOutputStream);
				ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
				imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				imageWriteParam.setCompressionQuality(DEST_QUALITY / 100F);
				imageWriter.write(null, new IIOImage(destBufferedImage, null, null), imageWriteParam);
				imageOutputStream.flush();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				if (graphics2D != null) {
					graphics2D.dispose();
				}
				if (imageWriter != null) {
					imageWriter.dispose();
				}
			}
		} else {
			String gravity;
			switch (watermarkPosition) {
			case TOP_LEFT:
				gravity = "NorthWest";
				break;
			case TOP_RIGHT:
				gravity = "NorthEast";
				break;
			case CENTER:
				gravity = "Center";
				break;
			case BOTTOM_LEFT:
				gravity = "SouthWest";
				break;
			case BOTTOM_RIGHT:
				gravity = "SouthEast";
				break;
			default:
				gravity = "SouthEast";
				break;
			}
			IMOperation operation = new IMOperation();
			operation.gravity(gravity);
			operation.dissolve(alpha);
			operation.quality((double) DEST_QUALITY);
			try {
				operation.addImage(watermarkFile.getCanonicalPath());
				operation.addImage(srcFile.getCanonicalPath());
				operation.addImage(destFile.getCanonicalPath());
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			if (ImageUtils.Method.GRAPHICS_MAGICK.equals(method)) {
				CompositeCmd compositeCmd = new CompositeCmd(true);
				if (graphicsMagickPath != null) {
					compositeCmd.setSearchPath(graphicsMagickPath);
				}
				try {
					compositeCmd.run(operation);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InterruptedException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (IM4JavaException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			} else {
				CompositeCmd compositeCmd = new CompositeCmd(false);
				if (imageMagickPath != null) {
					compositeCmd.setSearchPath(imageMagickPath);
				}
				try {
					compositeCmd.run(operation);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InterruptedException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (IM4JavaException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 转换颜色为十六进制代码
	 * 
	 * @param color
	 *            颜色
	 * @return 十六进制代码
	 */
	private static String toHexEncoding(Color color) {
		Assert.notNull(color, "[Assertion failed] - color is required; it must not be null");

		String r, g, b;
		StringBuilder result = new StringBuilder();
		r = Integer.toHexString(color.getRed());
		g = Integer.toHexString(color.getGreen());
		b = Integer.toHexString(color.getBlue());
		r = r.length() == 1 ? "0" + r : r;
		g = g.length() == 1 ? "0" + g : g;
		b = b.length() == 1 ? "0" + b : b;
		result.append("#").append(r).append(g).append(b);
		return String.valueOf(result);
	}

}