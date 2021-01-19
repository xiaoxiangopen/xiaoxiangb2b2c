package net.xiaoxiangshop.controller.admin;

import javax.inject.Inject;

import net.xiaoxiangshop.dao.ProductDao;
import net.xiaoxiangshop.dao.SkuDao;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Controller - 商品
 */
@Controller("adminProductController")
@RequestMapping("/admin/product")
public class ProductController extends BaseController {

    @Inject
    private ProductService productService;

    @Inject
    private ProductDao productDao;

    @Inject
    private SkuDao skuDao;

    @Inject
    private ProductCategoryService productCategoryService;
    @Inject
    private BrandService brandService;
    @Inject
    private ProductTagService productTagService;
    @Inject
    private StoreService storeService;
    @Inject
    private SearchService searchService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public String list(Product.Type type, Long productCategoryId, Long brandId, Long productTagId, Boolean isActive, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable, ModelMap model) {
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        Brand brand = brandService.find(brandId);
        ProductTag productTag = productTagService.find(productTagId);

        model.addAttribute("types", Product.Type.values());
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("productTags", productTagService.findAll());
        model.addAttribute("type", type);
        model.addAttribute("productCategoryId", productCategoryId);
        model.addAttribute("brandId", brandId);
        model.addAttribute("productTagId", productTagId);
        model.addAttribute("isMarketable", isMarketable);
        model.addAttribute("isList", isList);
        model.addAttribute("isTop", isTop);
        model.addAttribute("isActive", isActive);
        model.addAttribute("isOutOfStock", isOutOfStock);
        model.addAttribute("isStockAlert", isStockAlert);
        String searchProperty = pageable.getSearchProperty();
        if ("name".equals(searchProperty)) {
            searchProperty = "product.name";
            pageable.setSearchProperty(searchProperty);
        }
        model.addAttribute("page", productService.findPage(type, null, null, productCategory, null, brand, productTag, null, null, null, null, isMarketable, isList, isTop, isActive, isOutOfStock, isStockAlert, null,  pageable, null,null));
        return "admin/product/list";
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public ResponseEntity<?> delete(Long[] ids) {
        productService.delete(ids);
        return Results.OK;
    }

    /**
     * 上架商品
     */
    @PostMapping("/shelves")
    public ResponseEntity<?> shelves(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                Product product = productService.find(id);
                if (product == null) {
                    return Results.UNPROCESSABLE_ENTITY;
                }
                if (!storeService.productCategoryExists(product.getStore(), product.getProductCategory())) {
                    return Results.unprocessableEntity("admin.product.marketableNotExistCategoryNotAllowed", product.getName());
                }
            }
            productService.shelves(ids);
        }
        return Results.OK;
    }

    /**
     * 下架商品
     */
    @PostMapping("/shelf")
    public ResponseEntity<?> shelf(Long[] ids) {
        productService.shelf(ids);
        return Results.OK;
    }

    /**
     * 更新商品ES
     */
    @GetMapping("/update_product_to_es")
    public ResponseEntity<?> update_product_to_es(Long ids) {
        searchService.add(productService.find(ids));
        return Results.OK;
    }

    /**
     * 更新商品ES
     */
    @GetMapping("/update_product_to_es1")
    public ResponseEntity<?> update_product_to_es1() {

        Set<Sku> skuSet = skuDao.findSet("price", 0);

        for (Sku sku : skuSet) {
            searchService.add(productService.find(sku.getProduct().getId()));
        }

        return Results.OK;
    }

    /**
     * 更新商品ES
     */
    @GetMapping("/update_product_to_es2")
    public ResponseEntity<?> update_product_to_es2() {

        Set<Sku> skuSet = skuDao.findSet("price", 0);

        for (Product product : productService.findAll()) {
            if (!product.getIsMarketable())
                searchService.add(productService.find(product.getId()));
        }

        return Results.OK;
    }

//	/**
//	 * 同步商品
//	 */
//	@GetMapping("/sync_product")
//	public ResponseEntity<?> sync_product() {
//		productService.syncProducts(null);
//		return Results.OK;
//	}
}