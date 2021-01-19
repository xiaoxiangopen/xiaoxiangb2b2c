package net.xiaoxiangshop.controller.business;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.DefaultFreightConfig;
import net.xiaoxiangshop.entity.ShippingMethod;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.exception.UnauthorizedException;
import net.xiaoxiangshop.security.CurrentStore;
import net.xiaoxiangshop.service.DefaultFreightConfigService;
import net.xiaoxiangshop.service.ShippingMethodService;

/**
 * Controller - 配送方式
 * 
 */
@Controller("businessShippingMethodController")
@RequestMapping("/business/shipping_method")
public class ShippingMethodController extends BaseController {

	@Inject
	private ShippingMethodService shippingMethodService;
	@Inject
	private DefaultFreightConfigService defaultFreightConfigService;

	/**
	 * 添加属性
	 */
	@ModelAttribute
	public void populateModel(Long shippingMethodId, Long defaultFreightConfigId, @CurrentStore Store currentStore, ModelMap model) {
		model.addAttribute("shippingMethod", shippingMethodService.find(shippingMethodId));

		DefaultFreightConfig defaultFreightConfig = defaultFreightConfigService.find(defaultFreightConfigId);
		if (defaultFreightConfig != null && !currentStore.equals(defaultFreightConfig.getStore())) {
			throw new UnauthorizedException();
		}
		model.addAttribute("defaultFreightConfig", defaultFreightConfig);
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", shippingMethodService.findPage(pageable));
		return "business/shipping_method/list";
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(@ModelAttribute(binding = false) ShippingMethod shippingMethod, @CurrentStore Store currentStore, ModelMap model) {
		if (shippingMethod == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}

		DefaultFreightConfig defaultFreightConfig = defaultFreightConfigService.find(shippingMethod, currentStore);
		if (null != defaultFreightConfig) {
			model.addAttribute("defaultFreightConfig", defaultFreightConfig);
		} else {
			model.addAttribute("defaultFreightConfig", null);
		}
		model.addAttribute("shippingMethod", shippingMethod);
		return "business/shipping_method/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(@ModelAttribute("defaultFreightConfigForm") DefaultFreightConfig defaultFreightConfigForm, @ModelAttribute(binding = false) ShippingMethod shippingMethod, @CurrentStore Store currentStore) {
		if (shippingMethod == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		defaultFreightConfigService.update(defaultFreightConfigForm, currentStore, shippingMethod);

		return Results.OK;
	}

}