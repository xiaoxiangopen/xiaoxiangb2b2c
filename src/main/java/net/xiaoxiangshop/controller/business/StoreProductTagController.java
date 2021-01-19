package net.xiaoxiangshop.controller.business;

import javax.inject.Inject;

import net.xiaoxiangshop.Order;
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
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StoreProductTag;
import net.xiaoxiangshop.exception.UnauthorizedException;
import net.xiaoxiangshop.security.CurrentStore;
import net.xiaoxiangshop.service.StoreProductTagService;

/**
 * Controller - 店铺商品标签
 * 
 */
@Controller("businessStoreProductTagController")
@RequestMapping("/business/store_product_tag")
public class StoreProductTagController extends BaseController {

	@Inject
	private StoreProductTagService storeProductTagService;

	/**
	 * 添加属性
	 */
	@ModelAttribute
	public void populateModel(Long storeProductTagId, @CurrentStore Store currentStore, ModelMap model) {
		StoreProductTag storeProductTag = storeProductTagService.find(storeProductTagId);
		if (storeProductTag != null && !currentStore.equals(storeProductTag.getStore())) {
			throw new UnauthorizedException();
		}
		model.addAttribute("storeProductTag", storeProductTag);
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		return "business/store_product_tag/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(@ModelAttribute("storeProductTagForm") StoreProductTag storeProductTagForm, @CurrentStore Store currentStore) {
		if (!isValid(storeProductTagForm)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		storeProductTagForm.setStore(currentStore);
		storeProductTagForm.setProducts(null);
		storeProductTagService.save(storeProductTagForm);

		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(@ModelAttribute(binding = false) StoreProductTag storeProductTag, ModelMap model) {
		if (storeProductTag == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}

		model.addAttribute("storeProductTag", storeProductTag);
		return "business/store_product_tag/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(@ModelAttribute("storeProductTagForm") StoreProductTag storeProductTagForm, @ModelAttribute(binding = false) StoreProductTag storeProductTag, @CurrentStore Store currentStore) {
		if (!isValid(storeProductTagForm)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (storeProductTag == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		BeanUtils.copyProperties(storeProductTagForm, storeProductTag, "id", "store", "product");
		storeProductTagService.update(storeProductTag);

		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, @CurrentStore Store currentStore, ModelMap model) {
		if(pageable.getOrderProperty()==null){
			pageable.setOrderProperty("orders");
			pageable.setOrderDirection(Order.Direction.ASC);
		}
		model.addAttribute("page", storeProductTagService.findPage(currentStore, pageable));
		return "business/store_product_tag/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids, @CurrentStore Store currentStore) {
		for (Long id : ids) {
			StoreProductTag storeProductTag = storeProductTagService.find(id);
			if (storeProductTag == null || !currentStore.equals(storeProductTag.getStore())) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			storeProductTagService.delete(id);
		}
		return Results.OK;
	}

}