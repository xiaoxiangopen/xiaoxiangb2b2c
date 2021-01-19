package net.xiaoxiangshop.controller.business;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.BaseEntity;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StoreAdImage;
import net.xiaoxiangshop.exception.UnauthorizedException;
import net.xiaoxiangshop.security.CurrentStore;
import net.xiaoxiangshop.service.StoreAdImageService;

/**
 * Controller - 店铺广告图片
 * 
 */
@Controller("businessStoreAdImageController")
@RequestMapping("/business/store_ad_image")
public class StoreAdImageController extends BaseController {

	@Inject
	private StoreAdImageService storeAdImageService;

	/**
	 * 添加属性
	 */
	@ModelAttribute
	public void populateModel(Long storeAdImageId, @CurrentStore Store currentStore, ModelMap model) {
		StoreAdImage storeAdImage = storeAdImageService.find(storeAdImageId);
		if (storeAdImage != null && !currentStore.equals(storeAdImage.getStore())) {
			throw new UnauthorizedException();
		}
		model.addAttribute("storeAdImage", storeAdImage);
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add() {
		return "business/store_ad_image/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(@ModelAttribute("storeAdImageForm") StoreAdImage storeAdImageForm, @CurrentStore Store currentStore) {
		if (storeAdImageForm == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (StoreAdImage.MAX_COUNT != null && currentStore.getStoreAdImages().size() >= StoreAdImage.MAX_COUNT) {
			return Results.unprocessableEntity("business.storeAdImage.addCountNotAllowed", StoreAdImage.MAX_COUNT);
		}
		storeAdImageForm.setStore(currentStore);
		if (!isValid(storeAdImageForm, BaseEntity.Save.class)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		storeAdImageService.save(storeAdImageForm);

		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(@ModelAttribute(binding = false) StoreAdImage storeAdImage, ModelMap model) {
		if (storeAdImage == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}

		model.addAttribute("storeAdImage", storeAdImage);
		return "business/store_ad_image/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(@ModelAttribute("storeAdImageForm") StoreAdImage storeAdImageForm, @ModelAttribute(binding = false) StoreAdImage storeAdImage, @CurrentStore Store currentStore) {
		if (!isValid(storeAdImageForm, BaseEntity.Update.class)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (storeAdImage == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		BeanUtils.copyProperties(storeAdImageForm, storeAdImage, "id", "store");
		storeAdImageService.update(storeAdImage);

		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, @CurrentStore Store currentStore, ModelMap model) {
		model.addAttribute("page", storeAdImageService.findPage(currentStore, pageable));
		return "business/store_ad_image/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids, @CurrentStore Store currentStore) {
		for (StoreAdImage storeAdImage : storeAdImageService.findList(ids)) {
			if (!currentStore.equals(storeAdImage.getStore())) {
				return Results.UNPROCESSABLE_ENTITY;
			}
		}
		storeAdImageService.delete(ids);
		return Results.OK;
	}
}