package net.xiaoxiangshop.controller.admin;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.service.PluginService;

/**
 * Controller - 存储插件
 * 
 */
@Controller("adminStoragePluginController")
@RequestMapping("/admin/storage_plugin")
public class StoragePluginController extends BaseController {

	@Inject
	private PluginService pluginService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(ModelMap model) {
		model.addAttribute("storagePlugins", pluginService.getStoragePlugins());
		return "admin/storage_plugin/list";
	}

}