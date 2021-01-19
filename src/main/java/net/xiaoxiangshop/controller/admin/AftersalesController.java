package net.xiaoxiangshop.controller.admin;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.entity.Aftersales;
import net.xiaoxiangshop.service.AftersalesService;

/**
 * Controller - 售后
 * 
 */
@Controller("adminAftersalesController")
@RequestMapping("admin/aftersales")
public class AftersalesController extends BaseController {

	@Inject
	private AftersalesService aftersalesService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Aftersales.Type type, Aftersales.Status status, Pageable pageable, ModelMap model) {
		model.addAttribute("types", Aftersales.Type.values());
		model.addAttribute("statuses", Aftersales.Status.values());
		model.addAttribute("type", type);
		model.addAttribute("status", status);
		model.addAttribute("page", aftersalesService.findPage(type, status, null, null, pageable));
		return "admin/aftersales/list";
	}

	/**
	 * 查看
	 */
	@GetMapping("/view")
	public String view(Long aftersalesId, ModelMap model) {
		model.addAttribute("aftersales", aftersalesService.find(aftersalesId));
		return "admin/aftersales/view";
	}

}