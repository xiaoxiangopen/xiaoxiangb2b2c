package net.xiaoxiangshop.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.Admin;
import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.BusinessDepositLog;
import net.xiaoxiangshop.security.CurrentUser;
import net.xiaoxiangshop.service.BusinessDepositLogService;
import net.xiaoxiangshop.service.BusinessService;

/**
 * Controller - 商家预存款
 * 
 */
@Controller("adminBusinessDepositController")
@RequestMapping("/admin/business_deposit")
public class BusinessDepositController extends BaseController {

	@Inject
	private BusinessDepositLogService businessDepositLogService;
	@Inject
	private BusinessService businessService;

	/**
	 * 商家选择
	 */
	@GetMapping("/business_select")
	public ResponseEntity<?> businessSelect(String keyword) {
		List<Map<String, Object>> data = new ArrayList<>();
		if (StringUtils.isEmpty(keyword)) {
			return ResponseEntity.ok(data);
		}
		List<Business> businesses = businessService.search(keyword, null);
		for (Business business : businesses) {
			Map<String, Object> item = new HashMap<>();
			item.put("id", business.getId());
			item.put("name", business.getUsername());
			item.put("availableBalance", business.getAvailableBalance());
			data.add(item);
		}
		return ResponseEntity.ok(data);
	}

	/**
	 * 调整
	 */
	@GetMapping("/adjust")
	public String adjust() {
		return "admin/business_deposit/adjust";
	}

	/**
	 * 调整
	 */
	@PostMapping("/adjust")
	public ResponseEntity<?> adjust(Long businessId, BigDecimal amount, String memo, @CurrentUser Admin currentUser) {
		Business business = businessService.find(businessId);
		if (business == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (business.getBalance() == null || business.getAvailableBalance().add(amount).compareTo(BigDecimal.ZERO) < 0) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		businessService.addBalance(business, amount, BusinessDepositLog.Type.ADJUSTMENT, memo);
		return Results.OK;
	}

	/**
	 * 记录
	 */
	@GetMapping("/log")
	public String log(Long businessId, Pageable pageable, ModelMap model) {
		Business business = businessService.find(businessId);
		if (business != null) {
			model.addAttribute("business", business);
			model.addAttribute("page", businessDepositLogService.findPage(business, pageable));
		} else {
			model.addAttribute("page", businessDepositLogService.findPage(null,pageable));
		}
		return "admin/business_deposit/log";
	}

}