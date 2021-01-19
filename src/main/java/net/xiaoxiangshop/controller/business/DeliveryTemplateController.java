package net.xiaoxiangshop.controller.business;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.DeliveryTemplate;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.exception.UnauthorizedException;
import net.xiaoxiangshop.security.CurrentStore;
import net.xiaoxiangshop.service.DeliveryTemplateService;

/**
 * Controller - 快递单模板
 * 
 */
@Controller("businessDeliveryTemplateController")
@RequestMapping("/business/delivery_template")
public class DeliveryTemplateController extends BaseController {

	@Inject
	private DeliveryTemplateService deliveryTemplateService;

	/**
	 * 添加属性
	 */
	@ModelAttribute
	public void populateModel(Long deliveryTemplateId, @CurrentStore Store currentStore, ModelMap model) {
		DeliveryTemplate deliveryTemplate = deliveryTemplateService.find(deliveryTemplateId);
		if (deliveryTemplate != null && !currentStore.equals(deliveryTemplate.getStore())) {
			throw new UnauthorizedException();
		}
		model.addAttribute("deliveryTemplate", deliveryTemplate);
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("storeAttributes", DeliveryTemplate.StoreAttribute.values());
		model.addAttribute("deliveryCenterAttributes", DeliveryTemplate.DeliveryCenterAttribute.values());
		model.addAttribute("orderAttributes", DeliveryTemplate.OrderAttribute.values());
		return "business/delivery_template/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(@ModelAttribute("deliveryTemplateForm") DeliveryTemplate deliveryTemplateForm, @CurrentStore Store currentStore) {
		if (!isValid(deliveryTemplateForm)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		deliveryTemplateForm.setStore(currentStore);
		deliveryTemplateService.save(deliveryTemplateForm);

		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String eidt(@ModelAttribute(binding = false) DeliveryTemplate deliveryTemplate, Model model) {
		if (deliveryTemplate == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}

		model.addAttribute("storeAttributes", DeliveryTemplate.StoreAttribute.values());
		model.addAttribute("deliveryCenterAttributes", DeliveryTemplate.DeliveryCenterAttribute.values());
		model.addAttribute("orderAttributes", DeliveryTemplate.OrderAttribute.values());
		model.addAttribute("deliveryTemplate", deliveryTemplate);
		return "business/delivery_template/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> udpate(@ModelAttribute("deliveryTemplateForm") DeliveryTemplate deliveryTemplateForm, @ModelAttribute(binding = false) DeliveryTemplate deliveryTemplate) {
		if (!isValid(deliveryTemplateForm)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (deliveryTemplate == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		BeanUtils.copyProperties(deliveryTemplateForm, deliveryTemplate, "id", "store");
		deliveryTemplateService.update(deliveryTemplate);

		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, @CurrentStore Store currentStore, Model model) {
		model.addAttribute("page", deliveryTemplateService.findPage(currentStore, pageable));
		return "business/delivery_template/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids, @CurrentStore Store currentStore) {
		for (Long id : ids) {
			DeliveryTemplate deliveryTemplate = deliveryTemplateService.find(id);
			if (deliveryTemplate == null || !currentStore.equals(deliveryTemplate.getStore())) {
				return Results.UNPROCESSABLE_ENTITY;
			}
		}
		deliveryTemplateService.delete(ids);
		return Results.OK;
	}
}