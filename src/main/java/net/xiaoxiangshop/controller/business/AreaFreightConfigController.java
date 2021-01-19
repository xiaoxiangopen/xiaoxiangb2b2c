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
import org.springframework.web.bind.annotation.ResponseBody;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.Area;
import net.xiaoxiangshop.entity.AreaFreightConfig;
import net.xiaoxiangshop.entity.BaseEntity;
import net.xiaoxiangshop.entity.ShippingMethod;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.exception.UnauthorizedException;
import net.xiaoxiangshop.security.CurrentStore;
import net.xiaoxiangshop.service.AreaFreightConfigService;
import net.xiaoxiangshop.service.AreaService;
import net.xiaoxiangshop.service.ShippingMethodService;

/**
 * Controller - 地区运费配置
 * 
 */
@Controller("businessAreaFreightConfigController")
@RequestMapping("/business/area_freight_config")
public class AreaFreightConfigController extends BaseController {

	@Inject
	private AreaFreightConfigService areaFreightConfigService;
	@Inject
	private ShippingMethodService shippingMethodService;
	@Inject
	private AreaService areaService;

	/**
	 * 添加属性
	 */
	@ModelAttribute
	public void populateModel(Long shippingMethodId, Long areaId, Long areaFreightConfigId, @CurrentStore Store currentStore, ModelMap model) {
		model.addAttribute("area", areaService.find(areaId));
		model.addAttribute("shippingMethod", shippingMethodService.find(shippingMethodId));

		AreaFreightConfig areaFreightConfig = areaFreightConfigService.find(areaFreightConfigId);
		if (areaFreightConfig != null && !currentStore.equals(areaFreightConfig.getStore())) {
			throw new UnauthorizedException();
		}
		model.addAttribute("areaFreightConfig", areaFreightConfig);
	}

	/**
	 * 检查地区是否唯一
	 */
	@GetMapping("/check_area")
	public @ResponseBody boolean checkArea(Long id, @ModelAttribute(binding = false) ShippingMethod shippingMethod, @ModelAttribute(binding = false) Area area, @CurrentStore Store currentStore) {
		return shippingMethod != null && area != null && !areaFreightConfigService.unique(id, shippingMethod, currentStore, area);
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(@ModelAttribute(binding = false) ShippingMethod shippingMethod, ModelMap model) {
		model.addAttribute("shippingMethod", shippingMethod);
		return "business/area_freight_config/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(@ModelAttribute("areaFreightConfigForm") AreaFreightConfig areaFreightConfigForm, @ModelAttribute(binding = false) ShippingMethod shippingMethod, @ModelAttribute(binding = false) Area area, @CurrentStore Store currentStore) {
		areaFreightConfigForm.setArea(area);
		areaFreightConfigForm.setShippingMethod(shippingMethod);
		areaFreightConfigForm.setStore(currentStore);
		if (!isValid(areaFreightConfigForm, BaseEntity.Save.class)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (areaFreightConfigService.exists(shippingMethod, currentStore, area)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		areaFreightConfigService.save(areaFreightConfigForm);

		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(@ModelAttribute(binding = false) AreaFreightConfig areaFreightConfig, ModelMap model) {
		if (areaFreightConfig == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}

		model.addAttribute("areaFreightConfig", areaFreightConfig);
		return "business/area_freight_config/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(@ModelAttribute("areaFreightConfigForm") AreaFreightConfig areaFreightConfigForm, @ModelAttribute(binding = false) AreaFreightConfig areaFreightConfig, @ModelAttribute(binding = false) Area area, @CurrentStore Store currentStore) {
		if (areaFreightConfig == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		areaFreightConfigForm.setArea(area);
		if (!isValid(areaFreightConfigForm, BaseEntity.Update.class)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (areaFreightConfigService.unique(areaFreightConfig.getId(), areaFreightConfig.getShippingMethod(), currentStore, area)) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		BeanUtils.copyProperties(areaFreightConfigForm, areaFreightConfig, "id", "store", "shippingMethod");
		areaFreightConfigService.update(areaFreightConfig);

		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, @ModelAttribute(binding = false) ShippingMethod shippingMethod, @CurrentStore Store currentStore, ModelMap model) {
		model.addAttribute("shippingMethod", shippingMethod);
		model.addAttribute("page", areaFreightConfigService.findPage(shippingMethod, currentStore, pageable));
		return "business/area_freight_config/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids, @CurrentStore Store currentStore) {
		for (AreaFreightConfig areaFreightConfig : areaFreightConfigService.findList(ids)) {
			if (!currentStore.equals(areaFreightConfig.getStore())) {
				return Results.UNPROCESSABLE_ENTITY;
			}
		}
		areaFreightConfigService.delete(ids);
		return Results.OK;
	}

}