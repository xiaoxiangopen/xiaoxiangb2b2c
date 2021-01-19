package net.xiaoxiangshop.controller.admin;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.Article;
import net.xiaoxiangshop.entity.Note;
import net.xiaoxiangshop.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.HashSet;

/**
 * Controller - 文章
 * 
 */
@Controller("adminNoteController")
@RequestMapping("/admin/note")
public class NoteController extends BaseController {

	@Inject
	private NoteService articleService;
	@Inject
	private NoteCategoryService articleCategoryService;
	@Inject
	private NoteTagService articleTagService;

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("articleTags", articleTagService.findAll());
		return "admin/note/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(Note article, Long articleCategoryId, Long[] articleTagIds) {
//		article.setArticleCategory(articleCategoryService.find(articleCategoryId));
//		article.setArticleTags(new HashSet<>(articleTagService.findList(articleTagIds)));
		if (!isValid(article)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		article.setHits(0L);
		articleService.save(article);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("articleTags", articleTagService.findAll());
		model.addAttribute("article", articleService.find(id));
		return "admin/note/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(Note article, Long articleCategoryId, Long[] articleTagIds) {
//		article.setArticleCategory(articleCategoryService.find(articleCategoryId));
//		article.setArticleTags(new HashSet<>(articleTagService.findList(articleTagIds)));
		if (!isValid(article)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		articleService.update(article, "hits");
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", articleService.findPage(pageable));
		return "admin/note/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		articleService.delete(ids);
		return Results.OK;
	}

}