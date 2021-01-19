package net.xiaoxiangshop.api.controller.member;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.entity.Aftersales;
import net.xiaoxiangshop.entity.AftersalesReplacement;
import net.xiaoxiangshop.entity.Area;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.service.AftersalesReplacementService;
import net.xiaoxiangshop.service.AftersalesService;
import net.xiaoxiangshop.service.AreaService;
import net.xiaoxiangshop.service.OrderService;

/**
 * 换货 - 接口类
 */
@RestController("memberApiAftersalesReplacementController")
@RequestMapping("/api/member/aftersales_replacement")
public class AftersalesReplacementAPIController extends BaseAPIController {

	@Inject
	private AftersalesReplacementService aftersalesReplacementService;
	@Inject
	private OrderService orderService;
	@Inject
	private AftersalesService aftersalesService;
	@Inject
	private AreaService areaService;

	/**
	 * 换货
	 */
	@PostMapping("/replacement")
	public ApiResult replacement(@RequestBody AftersalesReplacement aftersalesReplacementForm, @RequestParam("orderId") Long orderId, @RequestParam("areaId") Long areaId) {
		
		Order order = orderService.find(orderId);
		if (order == null) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}
		Member currentUser = getCurrent();
		if (order != null && !currentUser.equals(order.getMember())) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}
		
		aftersalesService.filterNotActiveAftersalesItem(aftersalesReplacementForm);
		if (aftersalesService.existsIllegalAftersalesItems(aftersalesReplacementForm.getAftersalesItems())) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}

		Area area = areaService.find(areaId);
		aftersalesReplacementForm.setStatus(Aftersales.Status.PENDING);
		aftersalesReplacementForm.setMember(order.getMember());
		aftersalesReplacementForm.setStore(order.getStore());
		aftersalesReplacementForm.setArea(area);

		if (!isValid(aftersalesReplacementForm)) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}
		aftersalesReplacementService.save(aftersalesReplacementForm);
		return ResultUtils.ok();
	}

}