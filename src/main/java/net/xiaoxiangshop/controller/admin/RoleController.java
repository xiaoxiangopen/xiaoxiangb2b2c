package net.xiaoxiangshop.controller.admin;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.Role;
import net.xiaoxiangshop.service.RoleService;

/**
 * Controller - 角色
 * 
 */
@Controller("adminRoleController")
@RequestMapping("/admin/role")
public class RoleController extends BaseController {

	@Inject
	private RoleService roleService;

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("maxPermissionSize", Role.MAX_PERMISSION_SIZE);
		return "admin/role/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(Role role) {
		if (!isValid(role)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		role.setIsSystem(false);
		role.setAdmins(null);
		roleService.save(role);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("maxPermissionSize", Role.MAX_PERMISSION_SIZE);
		model.addAttribute("role", roleService.find(id));
		return "admin/role/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(Role role) {
		if (!isValid(role)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		Role pRole = roleService.find(role.getId());
		if (pRole == null || pRole.getIsSystem()) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		roleService.update(role, "isSystem", "admins");
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", roleService.findPage(pageable));
		return "admin/role/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				Role role = roleService.find(id);
				if (role != null && role.getIsSystem()) {
					return Results.unprocessableEntity("admin.role.deleteSystemNotAllowed", role.getName());
				}
				if (role != null && CollectionUtils.isNotEmpty(role.getAdmins())) {
					return Results.unprocessableEntity("admin.role.deleteExistNotAllowed", role.getName());
				}
			}
			roleService.delete(ids);
		}
		return Results.OK;
	}

}