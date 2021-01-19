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
import net.xiaoxiangshop.entity.ArticleTag;
import net.xiaoxiangshop.entity.BaseEntity;
import net.xiaoxiangshop.service.ArticleTagService;

/**
 * Controller - 文章标签
 * 
 */
@Controller("adminArticleTagController")
@RequestMapping("/admin/article_tag")
public class ArticleTagController extends BaseController {

	@Inject
	private ArticleTagService articleTagService;

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		return "admin/article_tag/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(ArticleTag articleTag) {
		if (!isValid(articleTag, BaseEntity.Save.class)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		articleTag.setArticles(null);
		articleTagService.save(articleTag);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("articleTag", articleTagService.find(id));
		return "admin/article_tag/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(ArticleTag articleTag) {
		if (!isValid(articleTag)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		articleTagService.update(articleTag, "articles");
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", articleTagService.findPage(pageable));
		return "admin/article_tag/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		articleTagService.delete(ids);
		return Results.OK;
	}

}