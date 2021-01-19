package net.xiaoxiangshop.controller.business;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.xiaoxiangshop.*;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.dao.SkuDao;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.security.CurrentUser;
import net.xiaoxiangshop.service.*;
import net.xiaoxiangshop.util.SystemUtils;
import net.xiaoxiangshop.util.XmlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.xiaoxiangshop.exception.UnauthorizedException;
import net.xiaoxiangshop.security.CurrentStore;

/**
 * Controller - 商品
 */
@Controller("businessProductController")
@RequestMapping("/business/product")
public class ProductController extends BaseController {

    @Inject
    private ProductService productService;
    @Inject
    private SkuService skuService;
    @Inject
    private StoreService storeService;

    @Inject
    private AreaService areaService;
    @Inject
    private ProductCategoryService productCategoryService;
    @Inject
    private StoreProductCategoryService storeProductCategoryService;
    @Inject
    private BrandService brandService;
    @Inject
    private ProductTagService productTagService;
    @Inject
    private StoreProductTagService storeProductTagService;
    @Inject
    private ProductImageService productImageService;
    @Inject
    private ParameterValueService parameterValueService;
    @Inject
    private SpecificationItemService specificationItemService;
    @Inject
    private AttributeService attributeService;
    @Inject
    private SpecificationService specificationService;
    @Inject
    private FileService fileService;
    @Inject
    private BusinessService businessService;
    @Inject
    private MemberRankService memberRankService;
    @Inject
    private SearchService searchService;
    @Inject
    private SkuDao skuDao;
    @Inject
    private RedisService redisService;

    /**
     * 添加属性
     */
    @ModelAttribute
    public void populateModel(Long productId, Long productCategoryId, @CurrentStore Store currentStore, ModelMap model) {
        Product product = productService.find(productId);

        if(product!=null){

            Setting setting = SystemUtils.getSetting();
            product.setPrice(setting.setScale(product.getPrice()));
            product.setCost(setting.setScale(product.getCost()));
            product.setMarketPrice(setting.setScale(product.getMarketPrice()));

            product.getDefaultSku().setSkuNormalPrice0(setting.setScale(product.getDefaultSku().getSkuNormalPrice0()));
            product.getDefaultSku().setSkuNormalPrice1(setting.setScale(product.getDefaultSku().getSkuNormalPrice1()));
            product.getDefaultSku().setSkuNormalPrice2(setting.setScale(product.getDefaultSku().getSkuNormalPrice2()));
            product.getDefaultSku().setSkuNormalPrice3(setting.setScale(product.getDefaultSku().getSkuNormalPrice3()));

            product.getDefaultSku().setSkuPromotionPrice0(setting.setScale(product.getDefaultSku().getSkuPromotionPrice0()));
            product.getDefaultSku().setSkuPromotionPrice1(setting.setScale(product.getDefaultSku().getSkuPromotionPrice1()));
            product.getDefaultSku().setSkuPromotionPrice2(setting.setScale(product.getDefaultSku().getSkuPromotionPrice2()));
            product.getDefaultSku().setSkuPromotionPrice3(setting.setScale(product.getDefaultSku().getSkuPromotionPrice3()));
        }


        if (product != null && !currentStore.equals(product.getStore())) {
            throw new UnauthorizedException();
        }
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        if (productCategory != null && !storeService.productCategoryExists(currentStore, productCategory)) {
            throw new UnauthorizedException();
        }

        model.addAttribute("product", product);
        model.addAttribute("productCategory", productCategory);
    }

    /**
     * 检查编号是否存在
     */
    @GetMapping("/check_sn")
    public @ResponseBody
    boolean checkSn(String sn) {
        return StringUtils.isNotEmpty(sn) && !productService.snExists(sn);
    }

    /**
     * 校验商品编码
     */
    @GetMapping("/check_internal_number")
    public @ResponseBody
    boolean checkInternalNumber(String internalNumber) {

        return StringUtils.isNotEmpty(internalNumber) && !productService.internalNumberExists(internalNumber);
    }

    /**
     * 上传商品图片
     */
    @PostMapping("/upload_product_image")
    public ResponseEntity<?> uploadProductImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Results.UNPROCESSABLE_ENTITY;
        }
        if (!fileService.isValid(FileType.IMAGE, file)) {
            return Results.unprocessableEntity("common.upload.invalid");
        }
        ProductImage productImage = productImageService.generate(file);
        if (productImage == null) {
            return Results.unprocessableEntity("common.upload.error");
        }
        return ResponseEntity.ok(productImage);
    }

    /**
     * 删除商品图片
     */
    @PostMapping("/delete_product_image")
    public ResponseEntity<?> deleteProductImage() {
        return Results.OK;
    }

    /**
     * 获取参数
     */
    @GetMapping("/parameters")
    public @ResponseBody
    List<Map<String, Object>> parameters(@ModelAttribute(binding = false) ProductCategory productCategory) {
        List<Map<String, Object>> data = new ArrayList<>();
        if (productCategory == null || CollectionUtils.isEmpty(productCategory.getParameters())) {
            return data;
        }
        for (Parameter parameter : productCategory.getParameters()) {
            Map<String, Object> item = new HashMap<>();
            item.put("group", parameter.getGroup());
            item.put("names", parameter.getNames());
            data.add(item);
        }
        return data;
    }

    /**
     * 获取属性
     */
    @GetMapping("/attributes")
    public @ResponseBody
    List<Map<String, Object>> attributes(@ModelAttribute(binding = false) ProductCategory productCategory) {
        List<Map<String, Object>> data = new ArrayList<>();
        if (productCategory == null || CollectionUtils.isEmpty(productCategory.getAttributes())) {
            return data;
        }
        for (Attribute attribute : productCategory.getAttributes()) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", attribute.getId());
            item.put("name", attribute.getName());
            item.put("options", attribute.getOptions());
            data.add(item);
        }
        return data;
    }

    /**
     * 获取规格
     */
    @GetMapping("/specifications")
    public @ResponseBody
    List<Map<String, Object>> specifications(@ModelAttribute(binding = false) ProductCategory productCategory) {
        List<Map<String, Object>> data = new ArrayList<>();
        if (productCategory == null || CollectionUtils.isEmpty(productCategory.getSpecifications())) {
            return data;
        }
        for (Specification specification : productCategory.getSpecifications()) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", specification.getName());
            item.put("options", specification.getOptions());
            data.add(item);
        }
        return data;
    }

    /**
     * 添加
     */
    @GetMapping("/add")
    public String add(@CurrentStore Store currentStore, ModelMap model) {
        model.addAttribute("maxProductImageSize", Product.MAX_PRODUCT_IMAGE_SIZE);
        model.addAttribute("areaTree", areaService.findTree());
        model.addAttribute("maxParameterValueSize", Product.MAX_PARAMETER_VALUE_SIZE);
        model.addAttribute("maxParameterValueEntrySize", ParameterValue.MAX_ENTRY_SIZE);
        model.addAttribute("maxSpecificationItemSize", Product.MAX_SPECIFICATION_ITEM_SIZE);
        model.addAttribute("maxSpecificationItemEntrySize", SpecificationItem.MAX_ENTRY_SIZE);
        model.addAttribute("types", Product.Type.values());
        model.addAttribute("productCategoryTree",productCategoryTreeFilter(productCategoryService.findTree(),currentStore));
        //model.addAttribute("productCategoryTree", productCategoryService.findTree());
        //model.addAttribute("allowedProductCategories", productCategoryService.findList(currentStore, null, null, null));
        //model.addAttribute("allowedProductCategoryParents", getAllowedProductCategoryParents(currentStore));
        model.addAttribute("storeProductCategoryTree", storeProductCategoryService.findTree(currentStore));
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("productTags", productTagService.findAll());
        model.addAttribute("storeProductTags", storeProductTagService.findList(currentStore, true));
        model.addAttribute("specifications", specificationService.findAll());

        //查询出会员等级折扣率显示到页面上
        //普通会员
        MemberRank memberRank1 = memberRankService.find(1L);
        //金卡会员
        MemberRank memberRank2 = memberRankService.find(2L);
        //白金会员
        MemberRank memberRank3 = memberRankService.find(3L);
        //钻石会员
        MemberRank memberRank4 = memberRankService.find(4L);

        model.addAttribute("discountRate1", memberRank1.getDiscountRate());
        model.addAttribute("discountRate2", memberRank2.getDiscountRate());
        model.addAttribute("discountRate3", memberRank3.getDiscountRate());
        model.addAttribute("discountRate4", memberRank4.getDiscountRate());


        return "business/product/add";
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public ResponseEntity<?> save(@ModelAttribute(name = "productForm") Product productForm, @ModelAttribute(binding = false) ProductCategory productCategory, SkuForm skuForm, SkuListForm skuListForm, Long brandId,  Long[] productTagIds, Long[] storeProductTagIds,
                                  Long storeProductCategoryId, HttpServletRequest request, @CurrentStore Store currentStore, @CurrentUser Business currentUser) {
        productImageService.filter(productForm.getProductImages());
        parameterValueService.filter(productForm.getParameterValues());
        specificationItemService.filter(productForm.getSpecificationItems());
        skuService.filter(skuListForm.getSkuList());

        Product product = null;

        Long productCount = productService.count(null, currentStore, null, null, null, null, null, null);
        if (currentStore.getStoreRank() != null && currentStore.getStoreRank().getQuantity() != null && productCount >= currentStore.getStoreRank().getQuantity()) {
            return Results.unprocessableEntity("business.product.addCountNotAllowed", currentStore.getStoreRank().getQuantity());
        }
        if (productCategory == null) {
            return Results.UNPROCESSABLE_ENTITY;
        }
        if (storeProductCategoryId != null) {
            StoreProductCategory storeProductCategory = storeProductCategoryService.find(storeProductCategoryId);
            if (storeProductCategory == null || !currentStore.equals(storeProductCategory.getStore())) {
                return Results.UNPROCESSABLE_ENTITY;
            }
            productForm.setStoreProductCategory(storeProductCategory);
        }
        productForm.setStore(currentStore);
        productForm.setProductCategory(productCategory);
        productForm.setBrand(brandService.find(brandId));
        productForm.setProductTags(new HashSet<>(productTagService.findList(productTagIds)));
        productForm.setStoreProductTags(new HashSet<>(storeProductTagService.findList(storeProductTagIds)));
        productForm.setBusinessId(currentUser.getId());
        productForm.setBusinessName(currentUser.getName());
        productForm.setCounterNo(skuForm.getSku().getCounterNo());
        productForm.removeAttributeValue();

        if (productForm.getAreaIds() != null) {
            productForm.setIsAreaLock(true);
        }

        for (Attribute attribute : productForm.getProductCategory().getAttributes()) {
            String value = request.getParameter("attribute_" + attribute.getId());
            String attributeValue = attributeService.toAttributeValue(attribute, value);
            productForm.setAttributeValue(attribute, attributeValue);
        }

        if (!isValid(productForm, BaseEntity.Save.class)) {
            return Results.UNPROCESSABLE_ENTITY;
        }
        if (StringUtils.isNotEmpty(productForm.getSn()) && productService.snExists(productForm.getSn())) {
            return Results.UNPROCESSABLE_ENTITY;
        }
        if (productForm.hasSpecification()) {
            List<Sku> skus = skuListForm.getSkuList();
            if (CollectionUtils.isEmpty(skus) || !isValid(skus, getValidationGroup(productForm.getType()), BaseEntity.Save.class)) {
                return Results.UNPROCESSABLE_ENTITY;
            }
            product = productService.create(productForm, skus);
        } else {
            Sku sku = skuForm.getSku();
            if (sku == null || !isValid(sku, getValidationGroup(productForm.getType()), BaseEntity.Save.class)) {
                return Results.UNPROCESSABLE_ENTITY;
            }
            product = productService.create(productForm, sku);
        }

        if (null != product)
            searchService.add(product);

        return Results.OK;
    }

    /**
     * 编辑
     */
    @GetMapping("/edit")
    public String edit(@ModelAttribute(binding = false) Product product, @CurrentStore Store currentStore, ModelMap model) {
        if (product == null) {
            return UNPROCESSABLE_ENTITY_VIEW;
        }

        model.addAttribute("maxProductImageSize", Product.MAX_PRODUCT_IMAGE_SIZE);
        model.addAttribute("areaTree", areaService.findTree());
        model.addAttribute("maxParameterValueSize", Product.MAX_PARAMETER_VALUE_SIZE);
        model.addAttribute("maxParameterValueEntrySize", ParameterValue.MAX_ENTRY_SIZE);
        model.addAttribute("maxSpecificationItemSize", Product.MAX_SPECIFICATION_ITEM_SIZE);
        model.addAttribute("maxSpecificationItemEntrySize", SpecificationItem.MAX_ENTRY_SIZE);
        model.addAttribute("types", Product.Type.values());
        model.addAttribute("productCategoryTree",productCategoryTreeFilter(productCategoryService.findTree(),currentStore));
        //model.addAttribute("productCategoryTree", productCategoryService.findTree());
        //model.addAttribute("allowedProductCategories", productCategoryService.findList(currentStore, null, null, null));
        //model.addAttribute("allowedProductCategoryParents", getAllowedProductCategoryParents(currentStore));
        model.addAttribute("storeProductCategoryTree", storeProductCategoryService.findTree(currentStore));
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("productTags", productTagService.findAll());
        model.addAttribute("storeProductTags", storeProductTagService.findList(currentStore, true));
        model.addAttribute("specifications", specificationService.findAll());
        model.addAttribute("product", product);


        //查询出会员等级折扣率显示到页面上
        //普通会员
        MemberRank memberRank1 = memberRankService.find(1L);
        //金卡会员
        MemberRank memberRank2 = memberRankService.find(2L);
        //白金会员
        MemberRank memberRank3 = memberRankService.find(3L);
        //钻石会员
        MemberRank memberRank4 = memberRankService.find(4L);

        model.addAttribute("discountRate1", memberRank1.getDiscountRate());
        model.addAttribute("discountRate2", memberRank2.getDiscountRate());
        model.addAttribute("discountRate3", memberRank3.getDiscountRate());
        model.addAttribute("discountRate4", memberRank4.getDiscountRate());

        return "business/product/edit";
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@ModelAttribute("productForm") Product productForm, @ModelAttribute(binding = false) Product product, @ModelAttribute(binding = false) ProductCategory productCategory, SkuForm skuForm, SkuListForm skuListForm, Long brandId,
                                    Long[] productTagIds, Long[] storeProductTagIds, Long storeProductCategoryId, HttpServletRequest request, @CurrentStore Store currentStore) {

        Product product_es = null;
        productImageService.filter(productForm.getProductImages());
        parameterValueService.filter(productForm.getParameterValues());
        specificationItemService.filter(productForm.getSpecificationItems());
        skuService.filter(skuListForm.getSkuList());


        if (product == null) {
            return Results.UNPROCESSABLE_ENTITY;
        }
        if (productCategory == null) {
            return Results.UNPROCESSABLE_ENTITY;
        }

        if (storeProductCategoryId != null) {
            StoreProductCategory storeProductCategory = storeProductCategoryService.find(storeProductCategoryId);
            if (storeProductCategory == null || !currentStore.equals(storeProductCategory.getStore())) {
                return Results.UNPROCESSABLE_ENTITY;
            }
            productForm.setStoreProductCategory(storeProductCategory);
        }
        productForm.setId(product.getId());
        productForm.setType(product.getType());
        productForm.setIsActive(true);
        productForm.setProductCategory(productCategory);
        productForm.setBrand(brandService.find(brandId));
        productForm.setProductTags(new HashSet<>(productTagService.findList(productTagIds)));
        productForm.setStoreProductTags(new HashSet<>(storeProductTagService.findList(storeProductTagIds)));
        productForm.setCounterNo(skuForm.getSku().getCounterNo());
        if (productForm.getAreaIds() != null) {
            productForm.setIsAreaLock(true);
        } else {
            productForm.setIsAreaLock(false);
        }
        productForm.removeAttributeValue();
        for (Attribute attribute : productForm.getProductCategory().getAttributes()) {
            String value = request.getParameter("attribute_" + attribute.getId());
            String attributeValue = attributeService.toAttributeValue(attribute, value);
            productForm.setAttributeValue(attribute, attributeValue);
        }

        if (!isValid(productForm, BaseEntity.Update.class)) {
            return Results.UNPROCESSABLE_ENTITY;
        }

        if (productForm.hasSpecification()) {
            List<Sku> skus = skuListForm.getSkuList();
            if (CollectionUtils.isEmpty(skus) || !isValid(skus, getValidationGroup(productForm.getType()), BaseEntity.Update.class)) {
                return Results.UNPROCESSABLE_ENTITY;
            }
            product_es = productService.modify(productForm, skus);
        } else {
            Sku sku = skuForm.getSku();
            if (sku == null || !isValid(sku, getValidationGroup(productForm.getType()), BaseEntity.Update.class)) {
                return Results.UNPROCESSABLE_ENTITY;
            }
            product_es = productService.modify(productForm, sku);
        }

        searchService.add(product_es);
        return Results.OK;
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public String list(@ModelAttribute(binding = false) ProductCategory productCategory, Product.Type type, Long brandId,  Long productTagId, Long storeProductTagId, Boolean isActive, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
                       Pageable pageable, @CurrentUser Business currentUser, @CurrentStore Store currentStore, ModelMap model, String noteId,String erp_Flag) {

        if (pageable.getOrderProperty() == null) {
            pageable.setOrderProperty("createdDate");
            pageable.setOrderDirection(Order.Direction.DESC);
        }

        Brand brand = brandService.find(brandId);
        ProductTag productTag = productTagService.find(productTagId);
        StoreProductTag storeProductTag = storeProductTagService.find(storeProductTagId);
        if (storeProductTag != null) {
            if (!currentStore.equals(storeProductTag.getStore())) {
                return UNPROCESSABLE_ENTITY_VIEW;
            }
        }

        model.addAttribute("types", Product.Type.values());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("productCategoryTree",productCategoryTreeFilter(productCategoryService.findTree(),currentStore));
        //model.addAttribute("productCategoryTree", productCategoryService.findTree());
        //model.addAttribute("allowedProductCategories", productCategoryService.findList(currentStore, null, null, null));
        //model.addAttribute("allowedProductCategoryParents", getAllowedProductCategoryParents(currentStore));
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("productTags", productTagService.findAll());
        model.addAttribute("storeProductTags", storeProductTagService.findList(currentStore, true));
        model.addAttribute("type", type);
        model.addAttribute("productCategoryId", productCategory != null ? productCategory.getId() : null);
        model.addAttribute("brandId", brandId);
        model.addAttribute("productTagId", productTagId);
        model.addAttribute("storeProductTagId", storeProductTagId);
        model.addAttribute("isMarketable", isMarketable);
        model.addAttribute("isList", isList);
        model.addAttribute("isTop", isTop);
        model.addAttribute("isActive", isActive);
        model.addAttribute("isOutOfStock", isOutOfStock);
        model.addAttribute("isStockAlert", isStockAlert);
        model.addAttribute("erp_Flag", erp_Flag);

//        business.setId();

        //查询标签关联的商品在一个列表里显示
        if (noteId != null) {
            pageable.setPageSize(100);
        }
        Page<Product> productPage = productService.findPage(type, null, currentStore, productCategory,
                null, brand,  productTag, storeProductTag,
                null, null, null, isMarketable, isList, isTop, isActive, isOutOfStock, isStockAlert, null, pageable, noteId,erp_Flag);

        Map<String, Object>    map=new HashMap<>();
        map.put("type",type);

        map.put("productCategory",productCategory==null?null:productCategory.getId());
        map.put("brand",brand==null?null:brand.getId());
        map.put("productTag",productTag==null?null:productTag.getId());
        map.put("storeProductTag",storeProductTag==null?null:storeProductTag.getId());

        map.put("isMarketable",isMarketable);
        map.put("isList",isList);
        map.put("isTop",isTop);
        map.put("isActive",isActive);
        map.put("isOutOfStock",isOutOfStock);
        map.put("isStockAlert",isStockAlert);
        map.put("noteId",noteId);
        map.put("erpFlag",erp_Flag);

        map.put("searchProperty",pageable.getSearchProperty());
        map.put("searchValue",pageable.getSearchValue());

//        Setting setting = SystemUtils.getSetting();
//        Integer youyiHySmsCodeTime=setting.getYouyiHySmsCodeTime();
        redisService.del("exportProductList");
        redisService.hmset("exportProductList",map,3000);

        model.addAttribute("page", productPage);
        return "business/product/list";
    }

    /**
     * 过滤商品分类
     * @param productCategoryTree
     * @param currentStore
     * @return
     */
    public List<ProductCategory> productCategoryTreeFilter(List<ProductCategory> productCategoryTree,Store currentStore){

        //自营店铺不需要对比
       if(currentStore.getType()==Store.Type.SELF){
           return productCategoryTree;
       }
        List<ProductCategory>  allowedProductCategories=productCategoryService.findList(currentStore, null, null, null);
        Set<ProductCategory>  allowedProductCategoryParents= getAllowedProductCategoryParents(currentStore);
        List<ProductCategory>  list=new ArrayList<>();
        if(productCategoryTree!=null){
            for(ProductCategory  pcate:productCategoryTree){
                if(allowedProductCategories.contains(pcate)||allowedProductCategoryParents.contains(pcate)){
                    if(!allowedProductCategories.contains(pcate)){
                        pcate.setDisabledFlag("disabled");
                    }
                    list.add(pcate);
                }
            }

        }
        return list;
    }

    /**
     * 列表
     */
    @GetMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, Long[] ids) {
        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        HSSFWorkbook wb = null;
        try {
            InputStream fi = new FileInputStream("/www/doc/product_info.xls");
            wb = (HSSFWorkbook) WorkbookFactory.create(fi);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFSheet xssfSheet = wb.getSheetAt(0);
        Integer row_num = 1;

        for (Long id : ids) {
            Product product = productService.find(id);

            HSSFRow xssfRow = xssfSheet.createRow(row_num);

            HSSFRow xssfRow_tmp = xssfSheet.getRow(0);
            HSSFCellStyle rowStyle = xssfRow_tmp.getRowStyle();

            Iterator<Cell> iterator = xssfRow_tmp.cellIterator();
            int cell_number = 0;
            while (iterator.hasNext()) {
                Cell cell_tmp = iterator.next();
                Cell cell = xssfRow.createCell(cell_number);
                cell.setCellStyle(cell_tmp.getCellStyle());
                cell_number++;
            }

            xssfRow.getCell(0).setCellValue(product.getName());
            xssfRow.getCell(1).setCellValue(product.getCounterNo());
            xssfRow.getCell(2).setCellValue(product.getInternalNumber());
            xssfRow.getCell(3).setCellValue(product.getKeyword());
            xssfRow.getCell(4).setCellValue(product.getCaption());

            List<SpecificationValue> specificationValueList = product.getDefaultSku().getSpecificationValues();
            StringBuffer stringBuffer = new StringBuffer();
            for (SpecificationValue sp : specificationValueList) {
                stringBuffer.append(sp.getValue()).append(",");
            }

            xssfRow.getCell(5).setCellValue(stringBuffer.toString());
            xssfRow.getCell(6).setCellValue(product.getDefaultSku().getStock());
            xssfRow.getCell(7).setCellValue(product.getUnit());
            xssfRow.getCell(8).setCellValue(String.valueOf(product.getDefaultSku().getMarketPrice()));
            xssfRow.getCell(9).setCellValue(String.valueOf(product.getDefaultSku().getPrice()));
            xssfRow.getCell(10).setCellValue(String.valueOf(product.getDefaultSku().getSkuNormalPrice0()));
            xssfRow.getCell(11).setCellValue(String.valueOf(product.getDefaultSku().getSkuNormalPrice1()));
            xssfRow.getCell(12).setCellValue(String.valueOf(product.getDefaultSku().getSkuNormalPrice2()));
            xssfRow.getCell(13).setCellValue(String.valueOf(product.getDefaultSku().getSkuNormalPrice3()));
            xssfRow.getCell(14).setCellValue(String.valueOf(product.getDefaultSku().getSkuPromotionPrice0()));
            xssfRow.getCell(15).setCellValue(String.valueOf(product.getDefaultSku().getSkuPromotionPrice1()));
            xssfRow.getCell(16).setCellValue(String.valueOf(product.getDefaultSku().getSkuPromotionPrice2()));
            xssfRow.getCell(17).setCellValue(String.valueOf(product.getDefaultSku().getSkuPromotionPrice3()));
            xssfRow.getCell(18).setCellValue(product.getDefaultSku().getIsPro()==true?"是":"否");
            xssfRow.getCell(19).setCellValue(formatter.format(product.getDefaultSku().getPromStartTime()));
            xssfRow.getCell(20).setCellValue(formatter.format(product.getDefaultSku().getPromEndTime()));
            String  s="";
            if(product.getLastModifiedDate()!=null){
                s=formatter.format(product.getLastModifiedDate());
            }
            xssfRow.getCell(21).setCellValue(s);
            row_num++;
        }
            OutputStream out = null;
            String extfilename = "product.xls";
            try {
                String userAgent = request.getHeader("user-agent");

                if (userAgent != null && userAgent.indexOf("Firefox") >= 0 || userAgent.indexOf("Chrome") >= 0
                        || userAgent.indexOf("Safari") >= 0) {
                    extfilename = new String((extfilename).getBytes(), "ISO8859-1");//IE浏览器
                } else {
                    extfilename = URLEncoder.encode(extfilename, "UTF8"); //其他浏览器
                }

                response.setContentType("application/msexcel");
                response.setHeader("Content-disposition", "attachment;filename=\"" + extfilename + "\"");
                out = response.getOutputStream();
                wb.write(out);
                wb.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                }
            }
    }


    /**
     * 列表
     */
    @GetMapping("/exportAll")
    public void exportAll(HttpServletRequest request, HttpServletResponse response, @CurrentStore Store currentStore) {
        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        HSSFWorkbook wb = null;
        try {
            InputStream fi = new FileInputStream("/www/doc/product_info.xls");
            wb = (HSSFWorkbook) WorkbookFactory.create(fi);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFSheet xssfSheet = wb.getSheetAt(0);
        Integer row_num = 1;


        Pageable pageable = new Pageable(1, 50000);
        pageable.setOrderProperty("createdDate");
        pageable.setOrderDirection(Order.Direction.DESC);

        Map<Object, Object> map=redisService.hmget("exportProductList");
        String  types=map.get("type")==null?null:String.valueOf(map.get("type")) ;
        Product.Type type=null;
        if(types!=null){
            if("GENERAL".equals(types)){
                type=Product.Type.GENERAL;
            }else  if("EXCHANGE".equals(types)){
                type=Product.Type.EXCHANGE;

            }else{
                type=Product.Type.GIFT;
            }
        }

        String productCategoryId=map.get("productCategory")==null?null: String.valueOf(map.get("productCategory"));
        ProductCategory productCategory=null;
        if(productCategoryId!=null){
            productCategory=productCategoryService.find(Long.valueOf(productCategoryId));
        }

        String  brandId=map.get("brand")==null?null:String.valueOf(map.get("brand"));
        Brand brand= null;
        if(brandId!=null){
            brand=brandService.find(Long.valueOf(brandId));
        }

        String productTagId= map.get("productTag")==null?null:String.valueOf( map.get("productTag"));
        ProductTag productTag=null;
        if(productTagId!=null){
            productTag=productTagService.find(Long.valueOf(productTagId));
        }

        String storeProductTagId=map.get("storeProductTag")==null?null:String.valueOf(map.get("storeProductTag"));
        StoreProductTag storeProductTag=null;
        if(storeProductTagId!=null){
            storeProductTag=storeProductTagService.find(Long.valueOf(storeProductTagId));
        }

        Boolean isMarketable=map.get("isMarketable")==null?null: (Boolean) map.get("isMarketable");
        Boolean isList=map.get("isList")==null?null: (Boolean) map.get("isList");
        Boolean isTop= map.get("isTop")==null?null:(Boolean) map.get("isTop");
        Boolean isActive= map.get("isActive")==null?null:(Boolean) map.get("isActive");
        Boolean isOutOfStock=map.get("isOutOfStock")==null?null: (Boolean) map.get("isOutOfStock");
        Boolean isStockAlert=  map.get("isStockAlert")==null?null:(Boolean) map.get("isStockAlert");
        String noteId=map.get("noteId")==null?null:String.valueOf(map.get("noteId"));
        String erpFlag=map.get("erpFlag")==null?null:String.valueOf(map.get("erpFlag"));
        String searchProperty=map.get("searchProperty")==null?null:String.valueOf(map.get("searchProperty"));
        String searchValue=map.get("searchValue")==null?null:String.valueOf(map.get("searchValue"));
        if(searchProperty!=null){
            pageable.setSearchProperty(searchProperty);
            pageable.setSearchValue(searchValue);
        }

        List<Product> pages = productService.findPage(type, null, currentStore, productCategory,
                null, brand, productTag, storeProductTag,
                null, null, null, isMarketable, isList, isTop, isActive, isOutOfStock, isStockAlert, null,  pageable, noteId,erpFlag).getContent();

        for (Product  product : pages) {

                HSSFRow xssfRow = xssfSheet.createRow(row_num);

                HSSFRow xssfRow_tmp = xssfSheet.getRow(0);
                HSSFCellStyle rowStyle = xssfRow_tmp.getRowStyle();

                Iterator<Cell> iterator = xssfRow_tmp.cellIterator();
                int cell_number = 0;
                while (iterator.hasNext()) {
                    Cell cell_tmp = iterator.next();
                    Cell cell = xssfRow.createCell(cell_number);
                    cell.setCellStyle(cell_tmp.getCellStyle());
                    cell_number++;
                }

                xssfRow.getCell(0).setCellValue(product.getName());
                xssfRow.getCell(1).setCellValue(product.getCounterNo());
                xssfRow.getCell(2).setCellValue(product.getInternalNumber());
                xssfRow.getCell(3).setCellValue(product.getKeyword());
                xssfRow.getCell(4).setCellValue(product.getCaption());

                StringBuffer stringBuffer = new StringBuffer();

                List<SpecificationValue> specificationValueList = product.getDefaultSku().getSpecificationValues();
                for (SpecificationValue sp : specificationValueList) {
                    stringBuffer.append(sp.getValue()).append(",");
                }

                xssfRow.getCell(5).setCellValue(stringBuffer.toString());
                xssfRow.getCell(6).setCellValue(product.getDefaultSku().getStock());
                xssfRow.getCell(7).setCellValue(product.getUnit());
                xssfRow.getCell(8).setCellValue(String.valueOf(product.getDefaultSku().getMarketPrice()));
                xssfRow.getCell(9).setCellValue(String.valueOf(product.getDefaultSku().getPrice()));
                xssfRow.getCell(10).setCellValue(String.valueOf(product.getDefaultSku().getSkuNormalPrice0()));
                xssfRow.getCell(11).setCellValue(String.valueOf(product.getDefaultSku().getSkuNormalPrice1()));
                xssfRow.getCell(12).setCellValue(String.valueOf(product.getDefaultSku().getSkuNormalPrice2()));
                xssfRow.getCell(13).setCellValue(String.valueOf(product.getDefaultSku().getSkuNormalPrice3()));
                xssfRow.getCell(14).setCellValue(String.valueOf(product.getDefaultSku().getSkuPromotionPrice0()));
                xssfRow.getCell(15).setCellValue(String.valueOf(product.getDefaultSku().getSkuPromotionPrice1()));
                xssfRow.getCell(16).setCellValue(String.valueOf(product.getDefaultSku().getSkuPromotionPrice2()));
                xssfRow.getCell(17).setCellValue(String.valueOf(product.getDefaultSku().getSkuPromotionPrice3()));
                xssfRow.getCell(18).setCellValue(product.getDefaultSku().getIsPro()==true?"是":"否");
                xssfRow.getCell(19).setCellValue(formatter.format(product.getDefaultSku().getPromStartTime()));
                xssfRow.getCell(20).setCellValue(formatter.format(product.getDefaultSku().getPromEndTime()));

                String  s="";
                if(product.getLastModifiedDate()!=null){
                    s=formatter.format(product.getLastModifiedDate());
                }
                xssfRow.getCell(21).setCellValue(s);

                row_num++;

        }
        OutputStream out = null;
        String extfilename = "product.xls";
        try {
            String userAgent = request.getHeader("user-agent");

            if (userAgent != null && userAgent.indexOf("Firefox") >= 0 || userAgent.indexOf("Chrome") >= 0
                    || userAgent.indexOf("Safari") >= 0) {
                extfilename = new String((extfilename).getBytes(), "ISO8859-1");//IE浏览器
            } else {
                extfilename = URLEncoder.encode(extfilename, "UTF8"); //其他浏览器
            }

            response.setContentType("application/msexcel");
            response.setHeader("Content-disposition", "attachment;filename=\"" + extfilename + "\"");
            out = response.getOutputStream();
            wb.write(out);
            wb.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public ResponseEntity<?> delete(Long[] ids, @CurrentStore Store currentStore) {
        if (ids != null) {
            for (Long id : ids) {
                Product product = productService.find(id);
                if (product == null) {
                    return Results.UNPROCESSABLE_ENTITY;
                }
                if (!currentStore.equals(product.getStore())) {
                    return Results.UNPROCESSABLE_ENTITY;
                }
                productService.delete(product.getId());
                searchService.del(id);
            }
        }
        return Results.OK;
    }

    /**
     * 删除
     */
    @PostMapping("/delallnote")
    public ResponseEntity<?> delallnote(Long[] ids, @CurrentStore Store currentStore) {
        if (ids != null) {
            for (Long id : ids) {
                Product product = productService.find(id);
                if (product == null) {
                    return Results.UNPROCESSABLE_ENTITY;
                }
                if (!currentStore.equals(product.getStore())) {
                    return Results.UNPROCESSABLE_ENTITY;
                }

                product.setNoteIds("");
                productService.update(product);
            }
        }
        return Results.OK;
    }

    /**
     * 删除
     */
    @RequestMapping("/addnote")
    public ResponseEntity<?> addnote(String ids, Date beginDate, Date endDate, String pids, @CurrentStore Store currentStore) {

        if (null == pids || pids.equals(","))
            return Results.ok("此功能需要先从商品管理页面选择商品");

        if (null == ids || ids.equals(","))
            return Results.ok("未选择标签");

        if (null == beginDate || null == endDate)
            return Results.ok("未选择时间范围");

        pids = URLDecoder.decode(pids);
        if (ids != null && pids != null) {
            String[] strArrIds = pids.split(",");

            for (int i = 0; i < strArrIds.length; i++) {
                Long id = Long.parseLong(strArrIds[i]);
                Product product = productService.find(id);
                String strIds = product.getNoteIds();

                if (null == strIds || strIds.equals("")) {
                    product.setNoteIds(ids);
                    product.setBeginDate(beginDate);
                    product.setEndDate(endDate);
                    productService.update(product);
                } else {
//					ids += ",";
                    strIds += ",";


                    String[] strTmpIds = ids.split(",");
                    for (int j = 0; j < strTmpIds.length; j++) {
                        String tmpId = strTmpIds[j] + ",";
                        if (strIds.indexOf(tmpId) > -1) {
                            strIds = strIds.replace(tmpId, "");
                        }
                    }

                    strIds += ids;
                    product.setNoteIds(strIds);
                    product.setBeginDate(beginDate);
                    product.setEndDate(endDate);
                    productService.update(product);

                }
            }
            //			for (Long id : ids) {
//				Product product = productService.find(id);
//				if (product == null) {
//					return Results.UNPROCESSABLE_ENTITY;
//				}
//				if (!currentStore.equals(product.getStore())) {
//					return Results.UNPROCESSABLE_ENTITY;
//				}
//				productService.delete(product.getId());
//			}
        }
        return Results.OK;
    }

    @RequestMapping("/editproductnote")
    public ResponseEntity<?> editproductnote(String ids, Long productId, @CurrentStore Store currentStore) {
        if (null == productId) {
            return Results.ok("此功能需要先从商品管理页面选择商品");
        }

        if (null == ids || ids.equals(",")) {
            return Results.ok("未选择标签");
        }
        Product product = productService.find(productId);
        if (product == null) {
            return Results.ok("没有查询到对应的商品");
        }
        product.setNoteIds(ids);
        productService.update(product);
        return Results.OK;
    }

    /**
     * 上架商品
     */
    @PostMapping("/shelves")
    public ResponseEntity<?> shelves(Long[] ids, @CurrentStore Store currentStore) {
        if (ids != null) {
            for (Long id : ids) {
                Product product = productService.find(id);
                if (product == null || !currentStore.equals(product.getStore())) {
                    return Results.UNPROCESSABLE_ENTITY;
                }
                if (!storeService.productCategoryExists(product.getStore(), product.getProductCategory())) {
                    return Results.unprocessableEntity("business.product.marketableNotExistCategoryNotAllowed", product.getName());
                }

                searchService.shelves(product);
            }
            productService.shelves(ids);
        }
        return Results.OK;
    }

    /**
     * 下架商品
     */
    @PostMapping("/shelf")
    public ResponseEntity<?> shelf(Long[] ids, @CurrentStore final Store currentStore) {
        if (ids != null) {
            for (Long id : ids) {
                Product product = productService.find(id);
                if (product == null || !currentStore.equals(product.getStore())) {
                    return Results.UNPROCESSABLE_ENTITY;
                }
                searchService.del(id);
            }
            productService.shelf(ids);
        }
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

    /**
     * 更新商品ES
     */
    @GetMapping("/update_product_to_es3")
    public ResponseEntity<?> update_product_to_es3() {

        Set<Sku> skuSet = skuDao.findSet("price", 0);

        for (Product product : productService.findAll()) {
                searchService.add(productService.find(product.getId()));
        }

        return Results.OK;
    }

    /**
     * 获取允许发布商品分类上级分类
     *
     * @param store 店铺
     * @return 允许发布商品分类上级分类
     */
    private Set<ProductCategory> getAllowedProductCategoryParents(Store store) {
        Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

        Set<ProductCategory> result = new HashSet<>();
        List<ProductCategory> allowedProductCategories = productCategoryService.findList(store, null, null, null);
        for (ProductCategory allowedProductCategory : allowedProductCategories) {
            result.addAll(allowedProductCategory.getParents());
        }
        return result;
    }


    /**
     * 根据类型获取验证组
     *
     * @param type 类型
     * @return 验证组
     */
    private Class<?> getValidationGroup(Product.Type type) {
        Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

        switch (type) {
            case GENERAL:
                return Sku.General.class;
            case EXCHANGE:
                return Sku.Exchange.class;
            case GIFT:
                return Sku.Gift.class;
        }
        return null;
    }



    /**
     * 更改商品编辑人员
     */
    @PostMapping("/addYsProduct")
    public ResponseEntity<?> addYsProduct( Long[] ids,String ysDesc) {
        if (ids != null) {
            Business  toUser = businessService.findByUsername(ysDesc);
            if (toUser == null ) {
                ResponseEntity<Map<String, String>> UNPROCESSABLE_ENTITY = Results.unprocessableEntity("人员工号输入有误.");
                return UNPROCESSABLE_ENTITY;
            }
            if(toUser!=null){
                StringBuffer  buffer=new StringBuffer();
                for (Long id : ids) {
                    Product product = productService.find(id);
                    product.setBusinessId(toUser.getId());
                    product.setBusinessName(toUser.getName());
                    product.setLastModifiedDate(new Date());
                    productService.updateBusiness(product);
                    buffer.append(id).append(",");
                }
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                XmlUtils.appendMethodC(sf.format(new Date()) + "：用户："+toUser.getName()+"执行更换商品编辑人，更换商品ID："+buffer.toString(),"更换商品编辑人");
            }
        }
        return Results.OK;
    }

    /**
     * FormBean - SKU
     */
    public static class SkuForm {

        /**
         * SKU
         */
        private Sku sku;

        /**
         * 获取SKU
         *
         * @return SKU
         */
        public Sku getSku() {
            return sku;
        }

        /**
         * 设置SKU
         *
         * @param sku SKU
         */
        public void setSku(Sku sku) {
            this.sku = sku;
        }

    }

    /**
     * FormBean - SKU
     */
    public static class SkuListForm {

        /**
         * SKU
         */
        private List<Sku> skuList;

        /**
         * 获取SKU
         *
         * @return SKU
         */
        public List<Sku> getSkuList() {
            return skuList;
        }

        /**
         * 设置SKU
         *
         * @param skuList SKU
         */
        public void setSkuList(List<Sku> skuList) {
            this.skuList = skuList;
        }

    }

}