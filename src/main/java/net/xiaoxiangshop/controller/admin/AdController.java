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
import net.xiaoxiangshop.entity.Ad;
import net.xiaoxiangshop.service.AdPositionService;
import net.xiaoxiangshop.service.AdService;

/**
 * Controller - 广告
 * 
 */
@Controller("adminAdController")
@RequestMapping("/admin/ad")
public class AdController extends BaseController {

	@Inject
	private AdService adService;
	@Inject
	private AdPositionService adPositionService;

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("types", Ad.Type.values());
		model.addAttribute("adPositions", adPositionService.findAll());
		return "admin/ad/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(Ad ad, Long adPositionId) {
		ad.setAdPosition(adPositionService.find(adPositionId));
		if (!isValid(ad)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (ad.getBeginDate() != null && ad.getEndDate() != null && ad.getBeginDate().after(ad.getEndDate())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (Ad.Type.TEXT.equals(ad.getType())) {
			ad.setPath(null);
		} else {
			ad.setContent(null);
		}
		adService.save(ad);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", Ad.Type.values());
		model.addAttribute("ad", adService.find(id));
		model.addAttribute("adPositions", adPositionService.findAll());
		return "admin/ad/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(Ad ad, Long adPositionId) {
		ad.setAdPosition(adPositionService.find(adPositionId));
		if (!isValid(ad)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (ad.getBeginDate() != null && ad.getEndDate() != null && ad.getBeginDate().after(ad.getEndDate())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (Ad.Type.TEXT.equals(ad.getType())) {
			ad.setPath(null);
		} else {
			ad.setContent(null);
		}
		adService.update(ad);
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", adService.findPage(pageable));
		return "admin/ad/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		adService.delete(ids);
		return Results.OK;
	}

}