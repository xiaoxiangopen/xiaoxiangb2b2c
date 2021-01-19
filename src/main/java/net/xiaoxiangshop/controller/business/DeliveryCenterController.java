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
import net.xiaoxiangshop.entity.Area;
import net.xiaoxiangshop.entity.DeliveryCenter;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.exception.UnauthorizedException;
import net.xiaoxiangshop.security.CurrentStore;
import net.xiaoxiangshop.service.AreaService;
import net.xiaoxiangshop.service.DeliveryCenterService;

/**
 * Controller - 发货点
 * 
 */
@Controller("businessDeliveryCenterController")
@RequestMapping("/business/delivery_center")
public class DeliveryCenterController extends BaseController {

	@Inject
	private DeliveryCenterService deliveryCenterService;
	@Inject
	private AreaService areaService;

	/**
	 * 添加属性
	 */
	@ModelAttribute
	public void populateModel(Long areaId, Long deliveryCenterId, @CurrentStore Store currentStore, ModelMap model) {
		model.addAttribute("area", areaService.find(areaId));

		DeliveryCenter deliveryCenter = deliveryCenterService.find(deliveryCenterId);
		if (deliveryCenter != null && !currentStore.equals(deliveryCenter.getStore())) {
			throw new UnauthorizedException();
		}
		model.addAttribute("deliveryCenter", deliveryCenter);
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add() {
		return "business/delivery_center/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(@ModelAttribute("deliveryCenterForm") DeliveryCenter deliveryCenterForm, @ModelAttribute(binding = false) Area area, @CurrentStore Store currentStore) {
		if (area == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		deliveryCenterForm.setArea(area);
		if (!isValid(deliveryCenterForm)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		deliveryCenterForm.setAreaName(area.getFullName());
		deliveryCenterForm.setStore(currentStore);
		deliveryCenterService.save(deliveryCenterForm);

		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(@ModelAttribute(binding = false) DeliveryCenter deliveryCenter, Model model) {
		if (deliveryCenter == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}

		model.addAttribute("deliveryCenter", deliveryCenter);
		return "business/delivery_center/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(@ModelAttribute("deliveryCenterForm") DeliveryCenter deliveryCenterForm, @ModelAttribute(binding = false) Area area, @ModelAttribute(binding = false) DeliveryCenter deliveryCenter) {
		deliveryCenterForm.setArea(area);
		if (!isValid(deliveryCenterForm)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (deliveryCenter == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		BeanUtils.copyProperties(deliveryCenterForm, deliveryCenter, "id", "store", "areaName");
		deliveryCenterService.update(deliveryCenter);

		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Model model, @CurrentStore Store currentStore, Pageable pageable) {
		model.addAttribute("page", deliveryCenterService.findPage(currentStore, pageable));
		return "business/delivery_center/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids, @CurrentStore Store currentStore) {
		for (Long id : ids) {
			DeliveryCenter deliveryCenter = deliveryCenterService.find(id);
			if (deliveryCenter == null || !currentStore.equals(deliveryCenter.getStore())) {
				return Results.UNPROCESSABLE_ENTITY;
			}
		}
		deliveryCenterService.delete(ids);
		return Results.OK;
	}

}