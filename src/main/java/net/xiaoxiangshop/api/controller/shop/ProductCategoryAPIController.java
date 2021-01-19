package net.xiaoxiangshop.api.controller.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.entity.ProductCategory;
import net.xiaoxiangshop.service.ProductCategoryService;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * 商品分类 - 接口类
 */
@RestController
@RequestMapping("/api/product_category")
public class ProductCategoryAPIController {

	@Inject
	private ProductCategoryService productCategoryService;

	//分类限制
	@Value("${product_category_id}")
	private String product_category_id;


	/**
	 * 首页
	 */
	@GetMapping("/index")
	public ApiResult index() {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();

		if(product_category_id.equals("110")){
			for (ProductCategory productCategory : productCategoryService.findRoots()) {
				Map<String, Object> category = new HashMap<>();
				category.put("id", String.valueOf(productCategory.getId()));
				category.put("name", productCategory.getName());

				List<Map<String, Object>> items = new ArrayList<>();
				Set<ProductCategory> childrens = productCategory.getChildren();
				Setting setting = SystemUtils.getSetting();
				for (ProductCategory children : childrens) {
					Map<String, Object> item = new HashMap<>();
					item.put("id", String.valueOf(children.getId()));
					item.put("name", children.getName());
					item.put("image", setting.getSiteUrl() + setting.getDefaultThumbnailProductImage());
					items.add(item);
				}
				category.put("childrens", items);
				list.add(category);
			}

		}else{
			String [] ship=product_category_id.split(",");
			for (ProductCategory productCategory : productCategoryService.findRoots()) {

				if(ship.length>0){
					for (String s:ship){
						if(s.equals(String.valueOf(productCategory.getId()))){
							Map<String, Object> category = new HashMap<>();
							category.put("id", String.valueOf(productCategory.getId()));
							category.put("name", productCategory.getName());

							List<Map<String, Object>> items = new ArrayList<>();
							Set<ProductCategory> childrens = productCategory.getChildren();
							Setting setting = SystemUtils.getSetting();
							for (ProductCategory children : childrens) {
								Map<String, Object> item = new HashMap<>();
								item.put("id", String.valueOf(children.getId()));
								item.put("name", children.getName());
								item.put("image", setting.getSiteUrl() + setting.getDefaultThumbnailProductImage());
								items.add(item);
							}
							category.put("childrens", items);
							list.add(category);
						}
					}
				}

			}
		}
		data.put("list", list);
		return ResultUtils.ok(data);
	}

	

}
