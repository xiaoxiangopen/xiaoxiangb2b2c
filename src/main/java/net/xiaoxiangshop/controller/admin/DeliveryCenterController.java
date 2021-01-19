package net.xiaoxiangshop.controller.admin;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.service.DeliveryCenterService;

/**
 * Controller - 发货点
 * 
 */
@Controller("adminDeliveryCenterController")
@RequestMapping("/admin/delivery_center")
public class DeliveryCenterController extends BaseController {

	@Inject
	private DeliveryCenterService deliveryCenterService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Model model, Pageable pageable) {
		model.addAttribute("page", deliveryCenterService.findPage(pageable));
		return "admin/delivery_center/list";
	}
}