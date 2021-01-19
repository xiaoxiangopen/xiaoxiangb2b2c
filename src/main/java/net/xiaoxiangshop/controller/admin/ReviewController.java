package net.xiaoxiangshop.controller.admin;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.Review;
import net.xiaoxiangshop.service.ReviewService;

/**
 * Controller - 评论
 * 
 */
@Controller("adminReviewController")
@RequestMapping("/admin/review")
public class ReviewController extends BaseController {

	@Inject
	private ReviewService reviewService;

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("review", reviewService.find(id));
		return "admin/review/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(Long id, @RequestParam(defaultValue = "false") Boolean isShow) {
		Review review = reviewService.find(id);
		if (review == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		review.setIsShow(isShow);
		reviewService.update(review);
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Review.Type type, Pageable pageable, ModelMap model) {
		model.addAttribute("type", type);
		model.addAttribute("types", Review.Type.values());
		model.addAttribute("page", reviewService.findPage(null, null, null, type, null, pageable));
		return "admin/review/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		reviewService.delete(ids);
		return Results.OK;
	}

}