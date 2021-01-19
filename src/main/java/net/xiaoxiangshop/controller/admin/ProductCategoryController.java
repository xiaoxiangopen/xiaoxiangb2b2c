package net.xiaoxiangshop.controller.admin;

import java.util.*;

import javax.inject.Inject;

import net.xiaoxiangshop.entity.Dict;
import net.xiaoxiangshop.service.DictService;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.entity.ProductCategory;
import net.xiaoxiangshop.service.BrandService;
import net.xiaoxiangshop.service.ProductCategoryService;

/**
 * Controller - 商品分类
 * 
 */
@Controller("adminProductCategoryController")
@RequestMapping("/admin/product_category")
public class ProductCategoryController extends BaseController {

	@Inject
	private ProductCategoryService productCategoryService;
	@Inject
	private BrandService brandService;
	@Inject
	private DictService dictService;

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		Dict dict=new Dict();
		dict.setDictType("categoryType");
		List<Dict>  dictList= dictService.find(dict);
		model.addAttribute("dictList", dictList);

		return "admin/product_category/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(ProductCategory productCategory, Long parentId, Long[] brandIds) {
		productCategory.setParent(productCategoryService.find(parentId));
		productCategory.setBrands(new HashSet<>(brandService.findList(brandIds)));
		if (!isValid(productCategory)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		productCategory.setTreePath(null);
		productCategory.setGrade(null);
		productCategory.setChildren(null);
		productCategory.setProducts(null);
		productCategory.setParameters(null);
		productCategory.setAttributes(null);
		productCategory.setSpecifications(null);
		productCategory.setStores(null);
		productCategory.setCategoryApplications(null);
		productCategoryService.save(productCategory);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		ProductCategory productCategory = productCategoryService.find(id);
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("productCategory", productCategory);
		model.addAttribute("children", productCategoryService.findChildren(productCategory, true, null));

		Dict dict=new Dict();
		dict.setDictType("categoryType");
		List<Dict>  dictList= dictService.find(dict);
		model.addAttribute("dictList", dictList);


		return "admin/product_category/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(ProductCategory productCategory, Long parentId, Long[] brandIds) {
		productCategory.setParent(productCategoryService.find(parentId));
		productCategory.setBrands(new HashSet<>(brandService.findList(brandIds)));
		if (!isValid(productCategory)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (productCategory.getParent() != null) {
			ProductCategory parent = productCategory.getParent();
			if (parent.equals(productCategory)) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			List<ProductCategory> children = productCategoryService.findChildren(parent, true, null);
			if (children != null && children.contains(parent)) {
				return Results.UNPROCESSABLE_ENTITY;
			}
		}
		Boolean f=productCategory.getEffective()==null?false:productCategory.getEffective();

		productCategoryService.update(productCategory, "stores", "categoryApplications", "treePath", "grade", "children", "product", "parameters", "attributes", "specifications");

		productCategory=productCategoryService.find(productCategory.getId());
		productCategory.setEffective(f);
		productCategory.setLastModifiedDate(new Date());
		productCategoryService.updateState(productCategory);

		if(productCategory.getGrade()==0){
			List<ProductCategory> productCategoryList=productCategoryService.findCategoryByPtId(productCategory.getId());
			for (ProductCategory p:productCategoryList){
				p.setEffective(f);
				p.setLastModifiedDate(new Date());
				productCategoryService.updateState(p);
				List<ProductCategory> categoryList=productCategoryService.findCategoryByPtId(p.getId());
				for (ProductCategory pt:categoryList){
					pt.setEffective(f);
					pt.setLastModifiedDate(new Date());
					productCategoryService.updateState(pt);
				}
			}
		}
		if(productCategory.getGrade()==1){
			List<ProductCategory> categoryList=productCategoryService.findCategoryByPtId(productCategory.getId());
			for (ProductCategory pt:categoryList){
				pt.setEffective(f);
				pt.setLastModifiedDate(new Date());
				productCategoryService.updateState(pt);
			}
		}
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(ModelMap model) {
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		return "admin/product_category/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		Set<ProductCategory> children = productCategory.getChildren();
		if (children != null && !children.isEmpty()) {
			return Results.unprocessableEntity("admin.productCategory.deleteExistChildrenNotAllowed");
		}
		Set<Product> products = productCategory.getProducts();
		if (products != null && !products.isEmpty()) {
			return Results.unprocessableEntity("admin.productCategory.deleteExistProductNotAllowed");
		}
		productCategoryService.delete(id);
		return Results.OK;
	}

}