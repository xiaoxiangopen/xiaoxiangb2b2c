package net.xiaoxiangshop.controller.admin;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.Navigation;
import net.xiaoxiangshop.service.ArticleCategoryService;
import net.xiaoxiangshop.service.NavigationGroupService;
import net.xiaoxiangshop.service.NavigationService;
import net.xiaoxiangshop.service.ProductCategoryService;

/**
 * Controller - 导航
 * 
 */
@Controller("adminNavigationController")
@RequestMapping("/admin/navigation")
public class NavigationController extends BaseController {

	@Inject
	private NavigationService navigationService;
	@Inject
	private NavigationGroupService navigationGroupService;
	@Inject
	private ArticleCategoryService articleCategoryService;
	@Inject
	private ProductCategoryService productCategoryService;

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("navigationGroups", navigationGroupService.findAll());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		return "admin/navigation/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(Navigation navigation, Long navigationGroupId) {
		navigation.setNavigationGroup(navigationGroupService.find(navigationGroupId));
		if (!isValid(navigation)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		navigationService.save(navigation);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("navigation", navigationService.find(id));
		model.addAttribute("navigationGroups", navigationGroupService.findAll());
		return "admin/navigation/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(Navigation navigation, Long navigationGroupId) {
		navigation.setNavigationGroup(navigationGroupService.find(navigationGroupId));
		if (!isValid(navigation)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		navigationService.update(navigation);
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", navigationService.findPage(pageable));
		return "admin/navigation/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		navigationService.delete(ids);
		return Results.OK;
	}

}