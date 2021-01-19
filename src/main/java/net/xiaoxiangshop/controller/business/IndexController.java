package net.xiaoxiangshop.controller.business;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller - 商家中心
 * 
 */
@Controller("businessIndexController")
@RequestMapping("/business/index")
public class IndexController extends BaseController {

	/**
	 * 首页
	 */
	@GetMapping
	public String index() {
		return "/business/index";
	}

}