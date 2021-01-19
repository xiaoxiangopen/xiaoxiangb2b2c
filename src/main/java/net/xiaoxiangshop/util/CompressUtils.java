package net.xiaoxiangshop.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * Utils - 压缩/解压缩
 * 
 */
public final class CompressUtils {

	/**
	 * 不可实例化
	 */
	private CompressUtils() {
	}

	/**
	 * 压缩
	 * 
	 * @param srcFiles
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param archiverName
	 *            压缩方式
	 * @param encoding
	 *            字符集编码
	 */
	public static void archive(File[] srcFiles, File destFile, String archiverName, String encoding) {
		Assert.notNull(destFile, "[Assertion failed] - destFile is required; it must not be null");
		Assert.state(!destFile.exists() || destFile.isFile(), "[Assertion failed] - destFile must not exists or destFile must be file");
		Assert.hasText(archiverName, "[Assertion failed] - archiverName must have text; it must not be null, empty, or blank");

		File destDir = destFile.getParentFile();
		if (destDir != null) {
			destDir.mkdirs();
		}
		try (ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory(encoding).createArchiveOutputStream(archiverName, new BufferedOutputStream(new FileOutputStream(destFile)))) {
			if (ArrayUtils.isNotEmpty(srcFiles)) {
				for (File srcFile : srcFiles) {
					if (srcFile == null || !srcFile.exists()) {
						continue;
					}
					Set<File> files = new HashSet<>();
					if (srcFile.isFile()) {
						files.add(srcFile);
					}
					if (srcFile.isDirectory()) {
						files.addAll(FileUtils.listFilesAndDirs(srcFile, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE));
					}
					String basePath = FilenameUtils.getFullPath(srcFile.getCanonicalPath());
					for (File file : files) {
						try {
							String entryName = FilenameUtils.separatorsToUnix(StringUtils.substring(file.getCanonicalPath(), basePath.length()));
							ArchiveEntry archiveEntry = archiveOutputStream.createArchiveEntry(file, entryName);
							archiveOutputStream.putArchiveEntry(archiveEntry);
							if (file.isFile()) {
								try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))){
									IOUtils.copy(inputStream, archiveOutputStream);
								} catch (FileNotFoundException e) {
									throw new RuntimeException(e.getMessage(), e);
								} catch (IOException e) {
									throw new RuntimeException(e.getMessage(), e);
								}
							}
						} catch (IOException e) {
							throw new RuntimeException(e.getMessage(), e);
						} finally {
							archiveOutputStream.closeArchiveEntry();
						}
					}
				}
			}
		} catch (ArchiveException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 压缩
	 * 
	 * @param srcFiles
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param archiverName
	 *            压缩方式
	 */
	public static void archive(File[] srcFiles, File destFile, String archiverName) {
		archive(srcFiles, destFile, archiverName, null);
	}

	/**
	 * 压缩
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param archiverName
	 *            压缩方式
	 * @param encoding
	 *            字符集编码
	 */
	public static void archive(File srcFile, File destFile, String archiverName, String encoding) {
		archive(new File[] { srcFile }, destFile, archiverName, encoding);
	}

	/**
	 * 压缩
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param archiverName
	 *            压缩方式
	 */
	public static void archive(File srcFile, File destFile, String archiverName) {
		archive(new File[] { srcFile }, destFile, archiverName, null);
	}

	/**
	 * 解压缩
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destDir
	 *            目标目录
	 * @param encoding
	 *            字符集编码
	 */
	public static void extract(File srcFile, File destDir, String encoding) {
		Assert.notNull(srcFile, "[Assertion failed] - srcFile is required; it must not be null");
		Assert.state(srcFile.exists(), "[Assertion failed] - srcFile must exists");
		Assert.state(srcFile.isFile(), "[Assertion failed] - srcFile must be file");
		Assert.notNull(destDir, "[Assertion failed] - destDir is required; it must not be null");
		Assert.state(destDir.mkdirs() || destDir.isDirectory(), "[Assertion failed] - destDir must make directorys or is directory");

		try (ArchiveInputStream archiveInputStream = new ArchiveStreamFactory(encoding).createArchiveInputStream(new BufferedInputStream(new FileInputStream(srcFile)))){
			ArchiveEntry archiveEntry;
			while ((archiveEntry = archiveInputStream.getNextEntry()) != null) {
				if (archiveEntry.isDirectory()) {
					new File(destDir, archiveEntry.getName()).mkdirs();
				} else {
					try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(destDir, archiveEntry.getName())))){
						IOUtils.copy(archiveInputStream, outputStream);
					} catch (FileNotFoundException e) {
						throw new RuntimeException(e.getMessage(), e);
					} catch (IOException e) {
						throw new RuntimeException(e.getMessage(), e);
					}
				}
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ArchiveException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 解压缩
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destDir
	 *            目标目录
	 */
	public static void extract(File srcFile, File destDir) {
		extract(srcFile, destDir, null);
	}

}