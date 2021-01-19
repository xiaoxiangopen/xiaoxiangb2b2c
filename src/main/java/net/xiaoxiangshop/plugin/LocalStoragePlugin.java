package net.xiaoxiangshop.plugin;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import net.xiaoxiangshop.config.UploadConfig;

/**
 * Plugin - 本地文件存储
 * 
 */
@Component("localStoragePlugin")
public class LocalStoragePlugin extends StoragePlugin {

	@Inject
	private UploadConfig uploadConfig;
	
	@Override
	public String getName() {
		return "本地文件存储";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "xiaoxiangAI";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.xiaoxiangshop.net";
	}

	@Override
	public String getInstallUrl() {
		return null;
	}

	@Override
	public String getUninstallUrl() {
		return null;
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/local_storage/setting";
	}

	@Override
	public void upload(String path, File file, String contentType) {
		File destFile = new File(uploadConfig.getUploadPath(), path);
		try {
			FileUtils.moveFile(file, destFile);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public String getUrl(String path) {
//		Setting setting = SystemUtils.getSetting();
//		return setting.getSiteUrl() + path;
		return path;
	}

}