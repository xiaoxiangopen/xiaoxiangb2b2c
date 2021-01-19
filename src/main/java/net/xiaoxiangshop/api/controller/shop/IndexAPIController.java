package net.xiaoxiangshop.api.controller.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.service.NavigationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.service.AdPositionService;
import net.xiaoxiangshop.service.ProductCategoryService;
import net.xiaoxiangshop.service.ProductService;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * 首页 - 接口类
 */
@RestController
@RequestMapping("/api/index")
public class IndexAPIController {
	
	@Inject
	private AdPositionService adPositionService;
	@Inject
	private ProductService productService;
	@Inject
	private ProductCategoryService productCategoryService;
	//分类限制
	@Value("${product_category_top}")
	private String product_category_top;

	@Inject
	private NavigationService navigationService;
	/**
	 * 首页
	 */
	@GetMapping
	public ApiResult index() {
		Map<String, Object> data = new HashMap<>();
		// 主轮播广告
		List<Map<String, Object>> ads = new ArrayList<>();
		Setting setting = SystemUtils.getSetting();
		AdPosition adPosition = adPositionService.find(18L);
		for (Ad ad : adPosition.getAds()) {
			Map<String, Object> item = new HashMap<>();
			if (ad.getType() == Ad.Type.IMAGE && ad.hasBegun() && !ad.hasEnded()) {
				String  url="";
				if(ad.getUrl()!=null){
					int result = ad.getUrl().indexOf("list/");
					if(result!=-1){
						String [] us=ad.getUrl().split("list/");
						url="../../pages/category/list?productCategoryId="+ us[1];
					}
//					System.out.println("商品分类ID：" + url);
				}
				item.put("url",  url);
				item.put("path", setting.getSiteUrl() + ad.getPath());
				item.put("title", ad.getTitle());
				ads.add(item);
			}
		}
		// 主页产品分类
		List<Map<String, Object>> mapProductCategories = new ArrayList<>();
		List<ProductCategory> productCategories = productCategoryService.findRoots(Integer.valueOf(product_category_top), true);
		for (ProductCategory productCategory : productCategories) {
			List<Product> products = productService.findList(Product.Type.GENERAL, null, productCategory.getId(), null, null,  1L, null, null, null, null, true, true, null, true, null, null, null,  6, null, null, true);
			Map<String, Object> mapProductCategory = new HashMap<>();
			mapProductCategory.put("name", productCategory.getName());
			mapProductCategory.put("id", String.valueOf(productCategory.getId()));
			List<Map<String, Object>> mapProducts = new ArrayList<>();
			for (Product product : products) {
				Map<String, Object> item = new HashMap<>();
				item.put("id", String.valueOf(product.getId()));
				item.put("name", product.getName());
				item.put("price", product.getPrice());
				item.put("marketPrice", product.getMarketPrice());
				item.put("thumbnail", setting.getSiteUrl() + product.getThumbnail());
				item.put("skuPromotionPro",product.getDefaultSku().getIsPromotion());
				mapProducts.add(item);
			}
			mapProductCategory.put("products", mapProducts);
			mapProductCategories.add(mapProductCategory);
		}
		
		// 猜您喜欢
		List<Map<String, Object>> likes = new ArrayList<>();
		List<Product> likeProducts = productService.findList(Product.Type.GENERAL, null, null, null, null, 2L, null, null, null, null, true, true, null, true, null, null, null, 6, null, null, true);
		for (Product likeProduct : likeProducts) {
			Map<String, Object> item = new HashMap<>();
			item.put("id", String.valueOf(likeProduct.getId()));
			item.put("name", likeProduct.getName());
			item.put("price", likeProduct.getPrice());
			item.put("marketPrice", likeProduct.getMarketPrice());
			item.put("thumbnail", setting.getSiteUrl() + likeProduct.getThumbnail());
			likes.add(item);
		}

		//10个导航
		Long navigationGroupId = 4L;
		Integer count = 10;
		boolean useCache = false;
		List<Map<String, Object>> navList = new ArrayList<>();
		List<Navigation> navigations = navigationService.findList(navigationGroupId, count, null, null, useCache);

		for (int i = 0; i < navigations.size(); i++) {
			Map<String, Object> item = new HashMap<>();
			Navigation navigation = navigations.get(i);
			item.put("name", navigation.getName());
			String url = navigation.getUrl();
			String [] us=url.split("list/");
//			System.out.println("商品分类ID：" + us[1]);
			item.put("id", us[1]);
			String imgSrc = "";
			switch (i) {
				case 0:
					imgSrc = "1.png";
					break;
				case 1:
					imgSrc = "2.png";
					break;
				case 2:
					imgSrc = "3.png";
					break;
				case 3:
					imgSrc = "4.png";
					break;
				case 4:
					imgSrc = "5.png";
					break;
				case 5:
					imgSrc = "6.png";
					break;
				case 6:
					imgSrc = "7.png";
					break;
				case 7:
					imgSrc = "8.png";
					break;
				case 8:
					imgSrc = "9.png";
					break;
				case 9:
					imgSrc = "10.png";
					break;
			}
			item.put("imgSrc", "../../images/navImg/" + imgSrc);
			navList.add(item);
		}

		data.put("navList", navList);

		data.put("ads", ads);
		data.put("likes", likes);
		data.put("productCategories", mapProductCategories);
		return ResultUtils.ok(data);
	}

}
