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
import net.xiaoxiangshop.entity.AdPosition;
import net.xiaoxiangshop.service.AdPositionService;

/**
 * Controller - 广告位
 * 
 */
@Controller("adminAdPositionController")
@RequestMapping("/admin/ad_position")
public class AdPositionController extends BaseController {

	@Inject
	private AdPositionService adPositionService;

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		return "admin/ad_position/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(AdPosition adPosition) {
		if (!isValid(adPosition)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		adPosition.setAds(null);
		adPositionService.save(adPosition);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("adPosition", adPositionService.find(id));
		return "admin/ad_position/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(AdPosition adPosition) {
		if (!isValid(adPosition)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		adPositionService.update(adPosition, "ads");
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", adPositionService.findPage(pageable));
		return "admin/ad_position/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		adPositionService.delete(ids);
		return Results.OK;
	}

}