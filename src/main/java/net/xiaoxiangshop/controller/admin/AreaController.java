package net.xiaoxiangshop.controller.admin;

import java.util.ArrayList;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.Area;
import net.xiaoxiangshop.service.AreaService;

/**
 * Controller - 地区
 * 
 */
@Controller("adminAreaController")
@RequestMapping("/admin/area")
public class AreaController extends BaseController {

	@Inject
	private AreaService areaService;

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(Long parentId, ModelMap model) {
		model.addAttribute("parent", areaService.find(parentId));
		return "admin/area/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(Area area, Long parentId) {
		area.setParent(areaService.find(parentId));
		if (!isValid(area)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		area.setFullName(null);
		area.setTreePath(null);
		area.setGrade(null);
		area.setChildren(null);
		area.setMembers(null);
		area.setReceivers(null);
		area.setOrders(null);
		area.setDeliveryCenters(null);
		area.setAreaFreightConfigs(null);
		areaService.save(area);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("area", areaService.find(id));
		return "admin/area/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(Area area) {
		if (!isValid(area)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		areaService.update(area, "fullName", "treePath", "grade", "parent", "children", "members", "receivers", "orders", "deliveryCenters", "areaFreightConfigs");
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Long parentId, ModelMap model) {
		Area parent = areaService.find(parentId);
		if (parent != null) {
			model.addAttribute("parent", parent);
			model.addAttribute("areas", new ArrayList<>(parent.getChildren()));
		} else {
			model.addAttribute("areas", areaService.findRoots());
		}
		return "admin/area/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long id) {
		Area area = areaService.find(id);
		Set<Area> children = area.getChildren();
		if (children != null && !children.isEmpty()) {
			return Results.unprocessableEntity("admin.productCategory.deleteExistChildrenNotAllowed");
		}
		areaService.delete(id);
		return Results.OK;
	}

}