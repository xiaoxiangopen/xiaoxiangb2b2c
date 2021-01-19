package net.xiaoxiangshop.controller.admin;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.xiaoxiangshop.util.XmlUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.audit.Audit;
import net.xiaoxiangshop.entity.BaseEntity;
import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.BusinessAttribute;
import net.xiaoxiangshop.service.BusinessAttributeService;
import net.xiaoxiangshop.service.BusinessService;
import net.xiaoxiangshop.service.UserService;

/**
 * Controller - 商家
 * 
 */
@Controller("adminBusinessController")
@RequestMapping("/admin/business")
public class BusinessController extends BaseController {

	@Inject
	private BusinessService businessService;
	@Inject
	private UserService userService;
	@Inject
	private BusinessAttributeService businessAttributeService;

	/**
	 * 检查用户名是否存在
	 */
	@GetMapping("/check_username")
	public @ResponseBody boolean checkUsername(String username) {
		return StringUtils.isNotEmpty(username) && !businessService.usernameExists(username);
	}

	/**
	 * 检查E-mail是否存在
	 */
	@GetMapping("/check_email")
	public @ResponseBody boolean checkEmail(Long id, String email) {
		return StringUtils.isNotEmpty(email) && !businessService.emailUnique(id, email);
	}

	/**
	 * 检查手机是否存在
	 */
	@GetMapping("/check_mobile")
	public @ResponseBody boolean checkMobile(Long id, String mobile) {
		return StringUtils.isNotEmpty(mobile) && !businessService.mobileUnique(id, mobile);
	}

	/**
	 * 查看
	 */
	@GetMapping("/view")
	public String view(Long id, ModelMap model) {
		model.addAttribute("businessAttributes", businessAttributeService.findList(true, true));
		model.addAttribute("business", businessService.find(id));
		return "admin/business/view";
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("business", businessService.findAll());
		model.addAttribute("businessAttributes", businessAttributeService.findList(true, true));
		return "admin/business/add";
	}

	/**
	 * 保存
	 */
	@Audit(action = "auditLog.action.admin.business.save")
	@PostMapping("/save")
	public ResponseEntity<?> save(Business business, HttpServletRequest request) {
		if (!isValid(Business.class, BaseEntity.Save.class)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (businessService.usernameExists(business.getUsername())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (businessService.emailExists(business.getEmail())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (businessService.mobileExists(business.getMobile())) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		business.removeAttributeValue();
		for (BusinessAttribute businessAttribute : businessAttributeService.findList(true, true)) {
			String[] values = request.getParameterValues("businessAttribute_" + businessAttribute.getId());
			if (!businessAttributeService.isValid(businessAttribute, values)) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			Object memberAttributeValue = businessAttributeService.toBusinessAttributeValue(businessAttribute, values);
			business.setAttributeValue(businessAttribute, memberAttributeValue);
		}

		business.setBalance(BigDecimal.ZERO);
		business.setFrozenAmount(BigDecimal.ZERO);
		business.setIsLocked(false);
		business.setLockDate(null);
		business.setLastLoginIp(XmlUtils.getIp(request));
		business.setLastLoginDate(new Date());
		business.setStore(null);
		business.setBusinessCashs(null);
		business.setBusinessDepositLogs(null);
		businessService.save(business);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("businessAttributes", businessAttributeService.findList(true, true));
		model.addAttribute("business", businessService.find(id));
		return "admin/business/edit";
	}

	/**
	 * 更新
	 */
	@Audit(action = "auditLog.action.admin.business.update")
	@PostMapping("/update")
	public ResponseEntity<?> update(Business business, Long id, Long businessRankId, Boolean unlock, HttpServletRequest request) {
		if (!isValid(business)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (businessService.emailUnique(id, business.getEmail())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (businessService.mobileUnique(id, business.getMobile())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		business.removeAttributeValue();
		for (BusinessAttribute businessAttribute : businessAttributeService.findList(true, true)) {
			String[] values = request.getParameterValues("businessAttribute_" + businessAttribute.getId());
			if (!businessAttributeService.isValid(businessAttribute, values)) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			Object businessAttributeValue = businessAttributeService.toBusinessAttributeValue(businessAttribute, values);
			business.setAttributeValue(businessAttribute, businessAttributeValue);
		}
		Business pbusiness = businessService.find(id);
		if (pbusiness == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (BooleanUtils.isTrue(pbusiness.getIsLocked()) && BooleanUtils.isTrue(unlock)) {
			userService.unlock(business);
			businessService.update(business, "username", "encodedPassword", "balance", "frozenAmount", "businessDepositLogs", "businessCashs", "lastLoginIp", "lastLoginDate");
		} else {
			businessService.update(business, "username", "encodedPassword", "balance", "frozenAmount", "businessDepositLogs", "businessCashs", "isLocked", "lockDate", "lastLoginIp", "lastLoginDate");
		}
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", businessService.findPage(pageable));
		return "admin/business/list";
	}

	/**
	 * 删除
	 */
	@Audit(action = "auditLog.action.admin.business.delete")
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				Business business = businessService.find(id);
				if (business != null && (business.getBalance().compareTo(BigDecimal.ZERO) > 0 || business.getStore() != null)) {
					return Results.unprocessableEntity("admin.business.deleteExistDepositNotAllowed", business.getUsername());
				}
			}
			businessService.delete(ids);
		}
		return Results.OK;
	}

}