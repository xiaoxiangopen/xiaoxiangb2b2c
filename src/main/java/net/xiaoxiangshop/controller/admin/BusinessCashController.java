package net.xiaoxiangshop.controller.admin;

import javax.inject.Inject;

import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.service.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.BusinessCash;
import net.xiaoxiangshop.service.BusinessCashService;

/**
 * Controller - 商家提现
 * 
 */
@Controller("adminBusinessCashController")
@RequestMapping("/admin/business_cash")
public class BusinessCashController extends BaseController {

	@Inject
	private BusinessCashService businessCashService;
	@Inject
	private BusinessService businessService;

	/**
	 * 审核
	 */
	@PostMapping("/review")
	public ResponseEntity<?> review(Long id, Boolean isPassed) {
		BusinessCash businessCash = businessCashService.find(id);
		if (isPassed == null || businessCash == null || !BusinessCash.Status.PENDING.equals(businessCash.getStatus())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		businessCashService.review(businessCash, isPassed);
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(BusinessCash.Status status, Pageable pageable, ModelMap model) {
		model.addAttribute("status", status);
		model.addAttribute("page", businessCashService.findPage(status, null, null, null, pageable));
//		String searchValue=pageable.getSearchValue();
//		Business business=businessService.findByUsername(searchValue);
//		if(business!=null){
//			pageable.setSearchValue(business.getId()+"");
//			model.addAttribute("page", businessCashService.findPage(status, null, null, null, pageable));
//			pageable.setSearchValue(searchValue);
//		}else {
//			model.addAttribute("page", businessCashService.findPage(status, null, null, null, pageable));
//
//		}
		return "admin/business_cash/list";
	}

}