package net.xiaoxiangshop.controller.admin.plugin;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.controller.admin.BaseController;
import net.xiaoxiangshop.entity.PluginConfig;
import net.xiaoxiangshop.plugin.FtpStoragePlugin;
import net.xiaoxiangshop.service.PluginConfigService;

/**
 * Controller - FTP存储
 * 
 */
@Controller("adminPluginFtpStorageController")
@RequestMapping("/admin/plugin/ftp_storage")
public class FtpStorageController extends BaseController {

	@Inject
	private FtpStoragePlugin ftpStoragePlugin;
	@Inject
	private PluginConfigService pluginConfigService;

	/**
	 * 安装
	 */
	@PostMapping("/install")
	public ResponseEntity<?> install() {
		if (!ftpStoragePlugin.getIsInstalled()) {
			PluginConfig pluginConfig = new PluginConfig();
			pluginConfig.setPluginId(ftpStoragePlugin.getId());
			pluginConfig.setIsEnabled(false);
			pluginConfig.setAttributes(null);
			pluginConfigService.save(pluginConfig);
		}
		return Results.OK;
	}

	/**
	 * 卸载
	 */
	@PostMapping("/uninstall")
	public ResponseEntity<?> uninstall() {
		if (ftpStoragePlugin.getIsInstalled()) {
			pluginConfigService.deleteByPluginId(ftpStoragePlugin.getId());
		}
		return Results.OK;
	}

	/**
	 * 设置
	 */
	@GetMapping("/setting")
	public String setting(ModelMap model) {
		PluginConfig pluginConfig = ftpStoragePlugin.getPluginConfig();
		model.addAttribute("pluginConfig", pluginConfig);
		return "/admin/plugin/ftp_storage/setting";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(String host, Integer port, String username, String password, String urlPrefix, @RequestParam(defaultValue = "false") Boolean isEnabled, Integer order) {
		PluginConfig pluginConfig = ftpStoragePlugin.getPluginConfig();
		Map<String, String> attributes = new HashMap<>();
		attributes.put("host", host);
		attributes.put("port", String.valueOf(port));
		attributes.put("username", username);
		attributes.put("password", password);
		attributes.put("urlPrefix", StringUtils.removeEnd(urlPrefix, "/"));
		pluginConfig.setAttributes(attributes);
		pluginConfig.setIsEnabled(isEnabled);
		pluginConfig.setOrder(order);
		pluginConfigService.update(pluginConfig);
		return Results.OK;
	}

}