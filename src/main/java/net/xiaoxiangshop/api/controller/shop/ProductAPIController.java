package net.xiaoxiangshop.api.controller.shop;

import java.math.BigDecimal;
import java.util.*;

import javax.inject.Inject;

import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.service.NoteService;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.entity.SpecificationItem.Entry;
import net.xiaoxiangshop.service.ProductCategoryService;
import net.xiaoxiangshop.service.ProductService;
import net.xiaoxiangshop.service.StoreService;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * 商品 - 接口类
 */
@RestController
@RequestMapping("/api/product")
public class ProductAPIController {

	/**
	 * 最大浏览记录商品数
	 */
	public static final Integer MAX_HISTORY_PRODUCT_COUNT = 10;
	
	@Inject
	private ProductService productService;
	@Inject
	private StoreService storeService;
	@Inject
	private ProductCategoryService productCategoryService;
	//搜索控制显示
	@Value("${app_search_text}")
	private String app_search_text;
	@Inject
	private NoteService noteService;
	/**
	 * 列表
	 */
	@GetMapping("/list")
	public ApiResult list(Long productCategoryId, Product.OrderType orderType, Integer pageNumber, Integer pageSize) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);

		Setting setting = SystemUtils.getSetting();
		Pageable pageable = new Pageable(pageNumber, pageSize);

		if(orderType==null){
			pageable.setOrderProperty("createdDate");
			pageable.setOrderDirection(Order.Direction.DESC);
		}

		Page<Product> pages = productService.findPage(null, null, null, productCategory, null,  null, null, null, null, null, null, true, true, null, true, null, null,  orderType, pageable,null,null);
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> productItems = new ArrayList<>();
		for (Product product : pages.getContent()) {
			Map<String, Object> item = new HashMap<>();
			item.put("id", String.valueOf(product.getId()));
			item.put("name", product.getName());
			item.put("price", product.getDefaultSku().getPrice());
			item.put("marketPrice", product.getDefaultSku().getMarketPrice());
			item.put("thumbnail", setting.getSiteUrl() + product.getThumbnail());

			item.put("skuPromotionPro",product.getDefaultSku().getIsPromotion());
			productItems.add(item);
		}
		
		data.put("pageNumber", pages.getPageNumber());
		data.put("pageSize", pages.getPageSize());
		data.put("lastPage", pages.isLastPage());
		data.put("total", pages.getTotal());
		data.put("productItems", productItems);
		return ResultUtils.ok(data);
	}
	
	/**
	 * 详情
	 */
	@GetMapping("/detail")
	public ApiResult detail(Long productId) {
		Product product = productService.find(productId);
		if (product == null || BooleanUtils.isNotTrue(product.getIsActive()) || BooleanUtils.isNotTrue(product.getIsMarketable())) {
			return ResultUtils.NOT_FOUND;
		}
		Map<String, Object> data = new HashMap<>();
		Setting setting = SystemUtils.getSetting();
		
		Map<String, Object> detail = new HashMap<>();
		detail.put("id", String.valueOf(product.getId()));
		detail.put("name", product.getName());
		detail.put("caption", product.getCaption()==null?"":product.getCaption());
		detail.put("price", product.getPrice());
		detail.put("marketPrice", product.getMarketPrice());
		detail.put("thumbnail", product.getThumbnail());

		if(product.getNoteContents()==null||product.getNoteContents()=="null"){
			product.setNoteContents("");
		}
		detail.put("noteContents", product.getNoteContents());
		// 订单详情
		String introduction = product.getIntroduction();
		int index = 0;

		if(introduction!=null){
			while ((index = introduction.indexOf("src=\"", index)) != -1) {
				index = index + "src=\"".length();
				StringBuilder sb = new StringBuilder(introduction);
				introduction = String.valueOf(sb.insert(index, setting.getSiteUrl()));// 插入
			}
		}

		detail.put("introduction", introduction);
		List<ProductImage> productImages = product.getProductImages();
		for (ProductImage productImage : productImages) {
			productImage.setMedium(setting.getSiteUrl() + productImage.getMedium());
		}
		detail.put("productImages", productImages);
		detail.put("sales", product.getSales());
		detail.put("hasSpecification", product.hasSpecification());
		
		// 默认sku
		Sku defaultSku = product.getDefaultSku();
		Map<String, Object> dSku = new HashMap<>();
		dSku.put("skuId", String.valueOf(defaultSku.getId()));
		dSku.put("marketPrice", defaultSku.getMarketPrice());
		dSku.put("price", defaultSku.getPrice());
		dSku.put("stock", defaultSku.getAvailableStock());
		dSku.put("specificationValues", defaultSku.getSpecificationValues());

		dSku.put("skuNormalPrice0", defaultSku.getSkuNormalPrice0());
		dSku.put("skuNormalPrice1", defaultSku.getSkuNormalPrice1());
		dSku.put("skuNormalPrice2", defaultSku.getSkuNormalPrice2());
		dSku.put("skuNormalPrice3", defaultSku.getSkuNormalPrice3());

		dSku.put("skuPromotionPrice0", defaultSku.getSkuPromotionPrice0());
		dSku.put("skuPromotionPrice1", defaultSku.getSkuPromotionPrice1());
		dSku.put("skuPromotionPrice2", defaultSku.getSkuPromotionPrice2());
		dSku.put("skuPromotionPrice3", defaultSku.getSkuPromotionPrice3());
		//黄麻子商品
		dSku.put("isZsz", defaultSku.getIsZsz());

		dSku.put("internalNumber", defaultSku.getInternalNumber());
		//普通促销
		dSku.put("skuPromotionPro",defaultSku.getIsPromotion());

		detail.put("defaultSku", dSku);
		
		// 多规格sku
		Map<String, Object> specData = new HashMap<>();
		List<Map<String, Object>> skus = new ArrayList<>();
		for (Sku sku : product.getSkus()) {
			Map<String, Object> item = new HashMap<>();
			item.put("skuId", String.valueOf(sku.getId()));
			item.put("skuName", sku.getName());
			item.put("skuThumbnail", sku.getThumbnail() != null ? sku.getThumbnail() : setting.getDefaultThumbnailProductImage());
			item.put("price", sku.getPrice());
			item.put("specificationValues", sku.getSpecificationValues());
			item.put("marketPrice", sku.getMarketPrice());
			item.put("stock", sku.getStock());

			//默认八个价格
			item.put("skuNormalPrice0", sku.getSkuNormalPrice0());
			item.put("skuNormalPrice1", sku.getSkuNormalPrice1());
			item.put("skuNormalPrice2", sku.getSkuNormalPrice2());
			item.put("skuNormalPrice3", sku.getSkuNormalPrice3());

			item.put("skuPromotionPrice0", sku.getSkuPromotionPrice0());
			item.put("skuPromotionPrice1", sku.getSkuPromotionPrice1());
			item.put("skuPromotionPrice2", sku.getSkuPromotionPrice2());
			item.put("skuPromotionPrice3", sku.getSkuPromotionPrice3());
			//普通促销
			item.put("skuPromotionPro",sku.getIsPromotion());
			//黄麻子商品
			item.put("isZsz", sku.getIsZsz());

			item.put("internalNumber", sku.getInternalNumber());

			List<String> specSkus = new ArrayList<>();
			for (Integer i : sku.getSpecificationValueIds()) {
				specSkus.add(String.valueOf(i));
			}
			item.put("specSkuId", String.join("_", specSkus));
			skus.add(item);
		}
		specData.put("skus", skus);
		// 查出有效规格项
		for (SpecificationItem specificationItem : product.getSpecificationItems()) {
			List<Entry> entries = specificationItem.getEntries();
			List<Entry> pEntries = new ArrayList<>();
			for (Entry entrie : entries) {
				if (entrie.getIsSelected() == Boolean.TRUE) {
					pEntries.add(entrie);
				}
			}
			specificationItem.setEntries(pEntries);
		}
		specData.put("specificationItems", product.getSpecificationItems());
		
		// 评价
		List<Map<String, Object>> reviews = new ArrayList<>();
		for (Review review : product.getReviews()) {
			Member member = review.getMember();
			Map<String, Object> item = new HashMap<>();
			item.put("username", member.getUsername());
			item.put("score", review.getScore());
			item.put("content", review.getContent());
			item.put("createdDate", review.getCreatedDate());
			reviews.add(item);
		}

		String contents = "";
		String ids = product.getNoteIds();
		Date beginDate = product.getBeginDate();
		Date endDate = product.getEndDate();

		Date dt = new Date();
		if (null != ids && !ids.equals("")) {
			if (null != beginDate && null != endDate) {
				boolean diff_begin = beginDate.before(dt);
				boolean diff_end = endDate.after(dt);

				if (diff_begin && diff_end) {
					String[] arrId = ids.split(",");
					for (int i = 0; i < arrId.length; i++) {
						Note note = noteService.find(Long.parseLong(arrId[i]));
						if(note!=null){
							contents += note.getContent();
						}
					}
				}
			}
		}

		data.put("contents", contents);

		data.put("reviews", reviews);
		data.put("specData", specData);
		data.put("detail", detail);
		return ResultUtils.ok(data);
	}
	
	
	
	/**用户请求登录获取Token
	 * 搜索
	 */
	@GetMapping("/search")
	public ApiResult search(@RequestParam("keyword") String keyword,
			 Product.OrderType orderType, Integer pageNumber, Integer pageSize) {
		if(app_search_text.equals("ALL")){
			keyword="化妆品";
		}
		if (StringUtils.isEmpty(keyword)) {
			return ResultUtils.NOT_FOUND;
		}
		Setting setting = SystemUtils.getSetting();
		Pageable pageable = new Pageable(pageNumber, pageSize);

		Page<Product> pages = productService.search(keyword, null, null, null, null, null, null, null, orderType, pageable);
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> productItems = new ArrayList<>();
		for (Product product : pages.getContent()) {
			Map<String, Object> item = new HashMap<>();
			item.put("id", String.valueOf(product.getId()));
			item.put("name", product.getName());
			item.put("price", product.getDefaultSku().getPrice());
			item.put("marketPrice", product.getDefaultSku().getMarketPrice());
			item.put("thumbnail", setting.getSiteUrl() + product.getThumbnail());
			item.put("skuPromotionPro",product.getDefaultSku().getIsPromotion());
			productItems.add(item);
		}

		data.put("pageNumber", pages.getPageNumber());
		data.put("pageSize", pages.getPageSize());
		data.put("lastPage", pages.isLastPage());
		data.put("total", pages.getTotal());
		data.put("productItems", productItems);
		return ResultUtils.ok(data);
	}
	
}
