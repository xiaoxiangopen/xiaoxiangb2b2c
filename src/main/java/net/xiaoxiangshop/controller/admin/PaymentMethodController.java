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
import net.xiaoxiangshop.entity.PaymentMethod;
import net.xiaoxiangshop.service.PaymentMethodService;

/**
 * Controller - 支付方式
 * 
 */
@Controller("adminPaymentMethodController")
@RequestMapping("/admin/payment_method")
public class PaymentMethodController extends BaseController {

	@Inject
	private PaymentMethodService paymentMethodService;

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("types", PaymentMethod.Type.values());
		model.addAttribute("methods", PaymentMethod.Method.values());
		return "admin/payment_method/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(PaymentMethod paymentMethod) {
		if (!isValid(paymentMethod)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		paymentMethod.setShippingMethods(null);
		paymentMethod.setOrders(null);
		paymentMethodService.save(paymentMethod);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", PaymentMethod.Type.values());
		model.addAttribute("methods", PaymentMethod.Method.values());
		model.addAttribute("paymentMethod", paymentMethodService.find(id));
		return "admin/payment_method/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(PaymentMethod paymentMethod) {
		if (!isValid(paymentMethod)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		paymentMethodService.update(paymentMethod, "shippingMethods", "orders");
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", paymentMethodService.findPage(pageable));
		return "admin/payment_method/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		if (ids.length >= paymentMethodService.count()) {
			return Results.unprocessableEntity("common.deleteAllNotAllowed");
		}
		paymentMethodService.delete(ids);
		return Results.OK;
	}

}