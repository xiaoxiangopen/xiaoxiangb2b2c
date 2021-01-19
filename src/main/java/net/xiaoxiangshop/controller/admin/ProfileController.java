package net.xiaoxiangshop.controller.admin;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.Admin;
import net.xiaoxiangshop.security.CurrentUser;
import net.xiaoxiangshop.service.AdminService;

/**
 * Controller - 个人资料
 * 
 */
@Controller("adminProfileController")
@RequestMapping("/admin/profile")
public class ProfileController extends BaseController {

	@Inject
	private AdminService adminService;

	/**
	 * 验证当前密码
	 */
	@GetMapping("/check_current_password")
	public @ResponseBody boolean checkCurrentPassword(String currentPassword, @CurrentUser Admin currentUser) {
		return StringUtils.isNotEmpty(currentPassword) && currentUser.isValidCredentials(currentPassword);
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(@CurrentUser Admin currentUser, ModelMap model) {
		model.addAttribute("admin", currentUser);
		return "admin/profile/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(String currentPassword, String password, String email, @CurrentUser Admin currentUser) {
		if (!isValid(Admin.class, "email", email)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (StringUtils.isNotEmpty(currentPassword) && StringUtils.isNotEmpty(password)) {
			if (!isValid(Admin.class, "password", password)) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			if (!currentUser.isValidCredentials(currentPassword)) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			currentUser.setPassword(password);
		}
		currentUser.setEmail(email);
		adminService.update(currentUser);
		return Results.OK;
	}

}