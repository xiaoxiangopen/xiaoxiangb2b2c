package net.xiaoxiangshop.controller.business;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.controller.admin.BaseController;
import net.xiaoxiangshop.entity.Note;
import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.service.NoteCategoryService;
import net.xiaoxiangshop.service.NoteService;
import net.xiaoxiangshop.service.NoteTagService;
import net.xiaoxiangshop.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller - 文章
 * 
 */
@Controller("businessNoteController")
@RequestMapping("/business/note")
public class NoteController extends BaseController {

	@Inject
	private NoteService articleService;
	@Inject
	private NoteCategoryService articleCategoryService;
	@Inject
	private NoteTagService articleTagService;
	@Inject
	private ProductService productService;

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("articleTags", articleTagService.findAll());
		return "business/note/add";
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
		return "business/note/edit";
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
		return "business/note/list";
	}
	/**
	 * 商品的标签列表
	 */
	@GetMapping("/productnotelist")
	public String productnotelist(Long productId,ModelMap model) {
		List<Note> noteList=new ArrayList<>();
		/**先查询商品*/
		Product product=productService.find(productId);
		String noteIds=product.getNoteIds();
		if(noteIds!=null&&noteIds.length()>0){
			List<String> noteIdlist =new ArrayList<>();
			if(noteIds.indexOf(",")!=-1){
				noteIdlist = Arrays.asList(noteIds.split(","));
			}else{
				noteIdlist.add(noteIds);
			}
			Long[] ids=new Long[noteIdlist.size()];
			for(int i = 0;i<noteIdlist.size();i++){
				ids[i] = Long.parseLong(noteIdlist.get(i));
			}
			noteList=articleService.findList(ids);
		}
		model.addAttribute("noteList",noteList);
		return "business/note/productnotelist";
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