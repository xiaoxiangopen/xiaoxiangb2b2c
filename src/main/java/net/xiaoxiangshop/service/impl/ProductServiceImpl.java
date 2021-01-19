package net.xiaoxiangshop.service.impl;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import javax.inject.Inject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.xiaoxiangshop.*;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.entity.api.sku.Data;
import net.xiaoxiangshop.entity.api.sku.SkuResponseBean;
import net.xiaoxiangshop.service.*;
import net.xiaoxiangshop.util.WebUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.elasticsearch.index.query.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.xiaoxiangshop.dao.AttributeDao;
import net.xiaoxiangshop.dao.BrandDao;
import net.xiaoxiangshop.dao.ProductCategoryDao;
import net.xiaoxiangshop.dao.ProductDao;
import net.xiaoxiangshop.dao.ProductTagDao;
import net.xiaoxiangshop.dao.SkuDao;
import net.xiaoxiangshop.dao.StockLogDao;
import net.xiaoxiangshop.dao.StoreDao;
import net.xiaoxiangshop.dao.StoreProductCategoryDao;
import net.xiaoxiangshop.dao.StoreProductTagDao;
import net.xiaoxiangshop.entity.Product.Type;
import net.xiaoxiangshop.repository.ProductRepository;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * Service - 商品
 */
@Service
public class ProductServiceImpl extends BaseServiceImpl<Product> implements ProductService {
    private static final Logger _logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    /**
     * ERP中台接口URL
     */
    @Value("${erp_basic_url}")
    private String erp_basic_url;

    @Inject
    private SearchService searchService;

    @Inject
    private SkuService skuService;

    @Inject
    private CacheManager cacheManager;
    @Inject
    private ProductDao productDao;
    @Inject
    private SkuDao skuDao;
    @Inject
    private SnService snService;
    @Inject
    private ProductCategoryDao productCategoryDao;
    @Inject
    private StoreProductCategoryDao storeProductCategoryDao;
    @Inject
    private BrandDao brandDao;
    @Inject
    private ProductTagDao productTagDao;
    @Inject
    private StoreProductTagDao storeProductTagDao;
    @Inject
    private AttributeDao attributeDao;
    @Inject
    private StockLogDao stockLogDao;
    @Inject
    private StoreDao storeDao;
    @Inject
    private SpecificationValueService specificationValueService;
    @Inject
    private ProductRepository productRepository;
    @Inject
    private ErpResultService erpResultService;


    @Override
    @Transactional(readOnly = true)
    public boolean snExists(String sn) {
        return productDao.exists("sn", sn);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean internalNumberExists(String internalNumber) {
        Boolean isExists = false;
        isExists = skuDao.exists("internal_number", internalNumber);

        if (isExists) {
            Sku sku = skuDao.findByAttribute("internal_number", internalNumber);
            Boolean erpFlag = sku.getProduct().getErpFlag();

            if (null == erpFlag || !erpFlag)
                isExists = !isExists;
        }

        return isExists;
    }

    @Override
    @Transactional(readOnly = true)
    public Product findBySn(String sn) {
        return productDao.findByAttribute("sn", sn);
    }



    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "product", condition = "#useCache")
    public List<Product> findList(Product.Type type, Long storeId, Long productCategoryId, Long storeProductCategoryId, Long brandId,  Long productTagId, Long storeProductTagId, Map<Long, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable,
                                  Boolean isList, Boolean isTop, Boolean isActive, Boolean isOutOfStock, Boolean isStockAlert,  Product.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
        Store store = storeDao.find(storeId);
        if (storeId != null && store == null) {
            return Collections.emptyList();
        }
        ProductCategory productCategory = productCategoryDao.find(productCategoryId);
        if (productCategoryId != null && productCategory == null) {
            return Collections.emptyList();
        }
        StoreProductCategory storeProductCategory = storeProductCategoryDao.find(storeProductCategoryId);
        if (storeProductCategoryId != null && storeProductCategory == null) {
            return Collections.emptyList();
        }
        Brand brand = brandDao.find(brandId);
        if (brandId != null && brand == null) {
            return Collections.emptyList();
        }

        ProductTag productTag = productTagDao.find(productTagId);
        if (productTagId != null && productTag == null) {
            return Collections.emptyList();
        }
        StoreProductTag storeProductTag = storeProductTagDao.find(storeProductTagId);
        if (storeProductTagId != null && storeProductTag == null) {
            return Collections.emptyList();
        }
        Map<Attribute, String> map = new HashMap<>();
        if (attributeValueMap != null) {
            for (Map.Entry<Long, String> entry : attributeValueMap.entrySet()) {
                Attribute attribute = attributeDao.find(entry.getKey());
                if (attribute != null) {
                    map.put(attribute, entry.getValue());
                }
            }
        }
        if (MapUtils.isNotEmpty(attributeValueMap) && MapUtils.isEmpty(map)) {
            return Collections.emptyList();
        }
        QueryWrapper<Product> queryWrapper = createQueryWrapper(null, count, filters, orders);
        return productDao.findByWrapperList(queryWrapper, type, store, productCategory, storeProductCategory, brand, productTag, storeProductTag, map, startPrice, endPrice, isMarketable, isList, isTop, isActive, isOutOfStock, isStockAlert,  orderType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findList(Product.RankingType rankingType, Store store, Integer count) {
        return productDao.findByRankingTypeList(rankingType, store, count);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findPage(Product.Type type, Store.Type storeType, Store store, ProductCategory productCategory, StoreProductCategory storeProductCategory, Brand brand,  ProductTag productTag, StoreProductTag storeProductTag, Map<Attribute, String> attributeValueMap,
                                  BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isActive, Boolean isOutOfStock, Boolean isStockAlert,  Product.OrderType orderType, Pageable pageable, String noteId,String erpFlag) {
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        IPage<Product> iPage = getPluginsPage(pageable);
        iPage.setRecords(productDao.findPage(iPage, getPageable(pageable), type, storeType, store, productCategory, storeProductCategory, brand, productTag, storeProductTag, attributeValueMap, startPrice, endPrice, isMarketable, isList, isTop, isActive, isOutOfStock, isStockAlert,  orderType, noteId,erpFlag));
        return super.findPage(iPage, pageable);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Page<Product> search(String keyword, Product.Type type, Store.Type storeType, Store store, Boolean isOutOfStock, Boolean isStockAlert, BigDecimal startPrice, BigDecimal endPrice, Product.OrderType orderType, Pageable pageable) {
        if (StringUtils.isEmpty(keyword)) {
            return Page.emptyPage(pageable);
        }

        if (pageable == null) {
            pageable = new Pageable();
        }

        // es原因page从0开始不是从1开始
        Integer pageNumber = 0;
        if (pageable.getPageNumber() >= 1) {
            pageNumber = pageable.getPageNumber() - 1;
        }

        BoolQueryBuilder boolQueryBuilder =null;
        Sort sort = null;
        org.springframework.data.domain.Page<Product> products = productRepository.search(boolQueryBuilder, PageRequest.of(pageNumber, pageable.getPageSize(), sort));

        IPage<Product> iPage = getPluginsPage(pageable);
        iPage.setRecords(products.getContent());
        iPage.setTotal(products.getTotalElements());
        iPage.setPages(products.getTotalPages());
        return super.findPage(iPage, pageable);

    }

    @Override
    @Transactional(readOnly = true)
    public Long count(Product.Type type, Store store, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isActive, Boolean isOutOfStock, Boolean isStockAlert) {
        return productDao.count(type, store, isMarketable, isList, isTop, isActive, isOutOfStock, isStockAlert);
    }

    @Override
    public Long count(Type type, Long storeId, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isActive, Boolean isOutOfStock, Boolean isStockAlert) {
        Store store = storeDao.find(storeId);
        if (storeId != null && store == null) {
            return 0L;
        }
        return productDao.count(type, store, isMarketable, isList, isTop, isActive, isOutOfStock, isStockAlert);
    }
    @Override
    public Long selectCount(Type type, Long storeId, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isActive, Boolean isOutOfStock, Boolean isStockAlert) {
        Store store = storeDao.find(storeId);
        if (storeId != null && store == null) {
            return 0L;
        }
        return productDao.selectCount(type, store, isMarketable, isList, isTop, isActive, isOutOfStock, isStockAlert);
    }
    @Override
    public long viewHits(Long id) {
        Assert.notNull(id, "[Assertion failed] - id is required; it must not be null");

        Ehcache cache = cacheManager.getEhcache(Product.HITS_CACHE_NAME);
        cache.acquireWriteLockOnKey(id);
        try {
            Element element = cache.get(id);
            Long hits;
            if (element != null) {
                hits = (Long) element.getObjectValue() + 1;
            } else {
                Product product = productDao.find(id);
                if (product == null) {
                    return 0L;
                }
                hits = product.getHits() + 1;
            }
            cache.put(new Element(id, hits));
            return hits;
        } finally {
            cache.releaseWriteLockOnKey(id);
        }
    }

    @Override
    public void addHits(Product product, long amount) {
        Assert.notNull(product, "[Assertion failed] - product is required; it must not be null");
        Assert.state(amount >= 0, "[Assertion failed] - amount must be equal or greater than 0");

        if (amount == 0) {
            return;
        }

        Calendar nowCalendar = Calendar.getInstance();
        Calendar weekHitsCalendar = DateUtils.toCalendar(product.getWeekHitsDate());
        Calendar monthHitsCalendar = DateUtils.toCalendar(product.getMonthHitsDate());
        if (nowCalendar.get(Calendar.YEAR) > weekHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekHitsCalendar.get(Calendar.WEEK_OF_YEAR)) {
            product.setWeekHits(amount);
        } else {
            product.setWeekHits(product.getWeekHits() + amount);
        }
        if (nowCalendar.get(Calendar.YEAR) > monthHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthHitsCalendar.get(Calendar.MONTH)) {
            product.setMonthHits(amount);
        } else {
            product.setMonthHits(product.getMonthHits() + amount);
        }
        product.setHits(product.getHits() + amount);
        product.setWeekHitsDate(new Date());
        product.setMonthHitsDate(new Date());
        productDao.update(product);
    }

    @Override
    public void addSales(Product product, long amount) {
        Assert.notNull(product, "[Assertion failed] - product is required; it must not be null");
        Assert.state(amount >= 0, "[Assertion failed] - amount must be equal or greater than 0");

        if (amount == 0) {
            return;
        }

        Calendar nowCalendar = Calendar.getInstance();
        Calendar weekSalesCalendar = DateUtils.toCalendar(product.getWeekSalesDate());
        Calendar monthSalesCalendar = DateUtils.toCalendar(product.getMonthSalesDate());
        if (nowCalendar.get(Calendar.YEAR) > weekSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekSalesCalendar.get(Calendar.WEEK_OF_YEAR)) {
            product.setWeekSales(amount);
        } else {
            product.setWeekSales(product.getWeekSales() + amount);
        }
        if (nowCalendar.get(Calendar.YEAR) > monthSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthSalesCalendar.get(Calendar.MONTH)) {
            product.setMonthSales(amount);
        } else {
            product.setMonthSales(product.getMonthSales() + amount);
        }
        product.setSales(product.getSales() + amount);
        product.setWeekSalesDate(new Date());
        product.setMonthSalesDate(new Date());
        productDao.update(product);
    }

    @Override
    @CacheEvict(value = {"product", "productCategory"}, allEntries = true)
    public Product create(Product product, Sku sku) {
        Assert.notNull(product, "[Assertion failed] - product is required; it must not be null");
        Assert.isTrue(product.isNew(), "[Assertion failed] - product must be new");
        Assert.notNull(product.getType(), "[Assertion failed] - product type is required; it must not be null");
        Assert.isTrue(!product.hasSpecification(), "[Assertion failed] - product must not have specification");
        Assert.notNull(sku, "[Assertion failed] - sku is required; it must not be null");
        Assert.isTrue(sku.isNew(), "[Assertion failed] - sku must be new");
        Assert.state(!sku.hasSpecification(), "[Assertion failed] - sku must not have specification");

        switch (product.getType()) {
            case GENERAL:
                sku.setExchangePoint(0L);
                break;
            case EXCHANGE:
                sku.setPrice(BigDecimal.ZERO);
                sku.setMaxCommission(BigDecimal.ZERO);
                sku.setRewardPoint(0L);
                break;
            case GIFT:
                sku.setPrice(BigDecimal.ZERO);
                sku.setMaxCommission(BigDecimal.ZERO);
                sku.setRewardPoint(0L);
                sku.setExchangePoint(0L);
                break;
        }
        if (sku.getMarketPrice() == null) {
            sku.setMarketPrice(calculateDefaultMarketPrice(sku.getPrice()));
        }
        if (sku.getRewardPoint() == null) {
            sku.setRewardPoint(calculateDefaultRewardPoint(sku.getPrice()));
        } else {
            long maxRewardPoint = calculateMaxRewardPoint(sku.getPrice());
            sku.setRewardPoint(sku.getRewardPoint() > maxRewardPoint ? maxRewardPoint : sku.getRewardPoint());
        }
        sku.setAllocatedStock(0);
        sku.setIsDefault(true);
        sku.setProduct(product);
        sku.setSpecificationValues(null);
        sku.setCartItems(null);
        sku.setOrderItems(null);
        sku.setOrderShippingItems(null);
        sku.setProductNotifies(null);
        sku.setStockLogs(null);

        product.setPrice(sku.getPrice());
        product.setCost(sku.getCost());
        product.setMarketPrice(sku.getMarketPrice());
        product.setMaxCommission(BigDecimal.ZERO);
        product.setIsActive(true);
        product.setScore(0F);
        product.setTotalScore(0L);
        product.setScoreCount(0L);
        product.setHits(0L);
        product.setWeekHits(0L);
        product.setMonthHits(0L);
        product.setSales(0L);
        product.setWeekSales(0L);
        product.setMonthSales(0L);
        product.setWeekHitsDate(new Date());
        product.setMonthHitsDate(new Date());
        product.setWeekSalesDate(new Date());
        product.setMonthSalesDate(new Date());
        product.setSpecificationItems(null);
        product.setReviews(null);
        product.setConsultations(null);
        product.setProductFavorites(null);
        product.setSkus(null);
        setValue(product);
        product.setSn(StringUtils.lowerCase(product.getSn()));
        if (CollectionUtils.isNotEmpty(product.getProductImages())) {
            Collections.sort(product.getProductImages());
        }
        super.save(product);

        if (sku.getCost() == null) {
            sku.setCost(BigDecimal.ZERO);
        }
        if (sku.getMaxCommission() == null) {
            sku.setMaxCommission(BigDecimal.ZERO);
        }
        //默认0
        sku.setIsPro(product.getIsPro());
        sku.setInternalNumber(product.getInternalNumber());
        if (sku.getPromStartTime() == null) {
            sku.setPromStartTime(new Date());
        }

        if (sku.getPromEndTime() == null) {
            sku.setPromEndTime(new Date());
        }

        setValue(sku);
        skuDao.save(sku);
        stockIn(sku);

        return product;
    }

    @Override
    @CacheEvict(value = {"product", "productCategory"}, allEntries = true)
    public Product create(Product product, List<Sku> skus) {
        Assert.notNull(product, "[Assertion failed] - product is required; it must not be null");
        Assert.isTrue(product.isNew(), "[Assertion failed] - product must be new");
        Assert.notNull(product.getType(), "[Assertion failed] - product type is required; it must not be null");
        Assert.isTrue(product.hasSpecification(), "[Assertion failed] - product must have specification");
        Assert.notEmpty(skus, "[Assertion failed] - skus must not be empty: it must contain at least 1 element");

        final List<SpecificationItem> specificationItems = product.getSpecificationItems();
        if (CollectionUtils.exists(skus, new Predicate() {
            private Set<List<Integer>> set = new HashSet<>();

            public boolean evaluate(Object object) {
                Sku sku = (Sku) object;
                return sku == null || !sku.isNew() || !sku.hasSpecification() || !set.add(sku.getSpecificationValueIds()) || !specificationValueService.isValid(specificationItems, sku.getSpecificationValues());
            }
        })) {
            throw new IllegalArgumentException();
        }

        Sku defaultSku = (Sku) CollectionUtils.find(skus, new Predicate() {
            public boolean evaluate(Object object) {
                Sku sku = (Sku) object;
                return sku != null && sku.getIsDefault();
            }
        });
        if (defaultSku == null) {
            defaultSku = skus.get(0);
            defaultSku.setIsDefault(true);
        }

        for (Sku sku : skus) {
            switch (product.getType()) {
                case GENERAL:
                    sku.setExchangePoint(0L);
                    break;
                case EXCHANGE:
                    sku.setPrice(BigDecimal.ZERO);
                    sku.setMaxCommission(BigDecimal.ZERO);
                    sku.setRewardPoint(0L);
                    break;
                case GIFT:
                    sku.setPrice(BigDecimal.ZERO);
                    sku.setMaxCommission(BigDecimal.ZERO);
                    sku.setRewardPoint(0L);
                    sku.setExchangePoint(0L);
                    break;
            }
            if (sku.getMarketPrice() == null) {
                sku.setMarketPrice(calculateDefaultMarketPrice(sku.getPrice()));
            }
            if (sku.getRewardPoint() == null) {
                sku.setRewardPoint(calculateDefaultRewardPoint(sku.getPrice()));
            } else {
                long maxRewardPoint = calculateMaxRewardPoint(sku.getPrice());
                sku.setRewardPoint(sku.getRewardPoint() > maxRewardPoint ? maxRewardPoint : sku.getRewardPoint());
            }
            if (sku != defaultSku) {
                sku.setIsDefault(false);
            }
            sku.setAllocatedStock(0);
            sku.setProduct(product);
            sku.setCartItems(null);
            sku.setOrderItems(null);
            sku.setOrderShippingItems(null);
            sku.setProductNotifies(null);
            sku.setStockLogs(null);
        }

        product.setPrice(defaultSku.getPrice());
        product.setCost(defaultSku.getCost());
        product.setMarketPrice(defaultSku.getMarketPrice());
        product.setMaxCommission(BigDecimal.ZERO);
        product.setIsActive(true);
        product.setScore(0F);
        product.setTotalScore(0L);
        product.setScoreCount(0L);
        product.setHits(0L);
        product.setWeekHits(0L);
        product.setMonthHits(0L);
        product.setSales(0L);
        product.setWeekSales(0L);
        product.setMonthSales(0L);
        product.setWeekHitsDate(new Date());
        product.setMonthHitsDate(new Date());
        product.setWeekSalesDate(new Date());
        product.setMonthSalesDate(new Date());
        product.setReviews(null);
        product.setConsultations(null);
        product.setProductFavorites(null);
        product.setSkus(null);
        setValue(product);
        product.setSn(StringUtils.lowerCase(product.getSn()));
        if (CollectionUtils.isNotEmpty(product.getProductImages())) {
            Collections.sort(product.getProductImages());
        }

        if (product.getIsPro() == false) {
            for (Sku sku : skus) {
                if (sku.getIsPro() == true) {
                    product.setIsPro(true);
                }
            }
        }

        super.save(product);

        for (Sku sku : skus) {

            if (sku.getCost() == null) {
                sku.setCost(BigDecimal.ZERO);
            }
            if (sku.getMaxCommission() == null) {
                sku.setMaxCommission(BigDecimal.ZERO);
            }

            if (sku.getIsPro() == null) {
                sku.setIsPro(false);
            }
            if (sku.getPromStartTime() == null) {
                sku.setPromStartTime(new Date());
            }
            if (sku.getPromEndTime() == null) {
                sku.setPromEndTime(new Date());
            }
            setValue(sku);
            skuDao.save(sku);
            stockIn(sku);
        }

        return product;
    }

    @Override
    @CacheEvict(value = {"product", "productCategory"}, allEntries = true)
    public Product modify(Product product, Sku sku) {
        Assert.notNull(product, "[Assertion failed] - product is required; it must not be null");
        Assert.isTrue(!product.isNew(), "[Assertion failed] - product must not be new");
        Assert.isTrue(!product.hasSpecification(), "[Assertion failed] - product must not have specification");
        Assert.notNull(sku, "[Assertion failed] - sku is required; it must not be null");
        Assert.isTrue(sku.isNew(), "[Assertion failed] - sku must be new");
        Assert.state(!sku.hasSpecification(), "[Assertion failed] - sku must not have specification");

        Product pProduct = productDao.find(product.getId());
        switch (pProduct.getType()) {
            case GENERAL:
                sku.setExchangePoint(0L);
                break;
            case EXCHANGE:
                sku.setPrice(BigDecimal.ZERO);
                sku.setMaxCommission(BigDecimal.ZERO);
                sku.setRewardPoint(0L);
                break;
            case GIFT:
                sku.setPrice(BigDecimal.ZERO);
                sku.setMaxCommission(BigDecimal.ZERO);
                sku.setRewardPoint(0L);
                sku.setExchangePoint(0L);
                break;
        }
        if (sku.getMarketPrice() == null) {
            sku.setMarketPrice(calculateDefaultMarketPrice(sku.getPrice()));
        }
        if (sku.getRewardPoint() == null) {
            sku.setRewardPoint(calculateDefaultRewardPoint(sku.getPrice()));
        } else {
            long maxRewardPoint = calculateMaxRewardPoint(sku.getPrice());
            sku.setRewardPoint(sku.getRewardPoint() > maxRewardPoint ? maxRewardPoint : sku.getRewardPoint());
        }
        sku.setAllocatedStock(0);
        sku.setIsDefault(true);
        sku.setProduct(pProduct);
        sku.setSpecificationValues(null);
        sku.setCartItems(null);
        sku.setOrderItems(null);
        sku.setOrderShippingItems(null);
        sku.setProductNotifies(null);
        sku.setStockLogs(null);
        sku.setMaxCommission(BigDecimal.ZERO);
        sku.setInternalNumber(product.getInternalNumber());

        if (sku.getPromStartTime() == null) {
            sku.setPromStartTime(new Date());
        }

        if (sku.getPromEndTime() == null) {
            sku.setPromEndTime(new Date());
        }
        sku.setIsPro(product.getIsPro());
        if (pProduct.hasSpecification()) {
            for (Sku pSku : pProduct.getSkus()) {
                skuDao.remove(pSku);
            }
            if (sku.getStock() == null) {
                throw new IllegalArgumentException();
            }
            setValue(sku);
            skuDao.save(sku);
            stockIn(sku);
        } else {
            Sku defaultSku = pProduct.getDefaultSku();
            defaultSku.setCounterNo(sku.getCounterNo());
            defaultSku.setPrice(sku.getPrice());
            defaultSku.setCost(sku.getCost());
            defaultSku.setMarketPrice(sku.getMarketPrice());
            defaultSku.setMaxCommission(BigDecimal.ZERO);
            defaultSku.setRewardPoint(sku.getRewardPoint());
            defaultSku.setExchangePoint(sku.getExchangePoint());
            defaultSku.setLastModifiedDate(new Date());
            defaultSku.setInternalNumber(sku.getInternalNumber());

            defaultSku.setSkuNormalPrice0(sku.getSkuNormalPrice0());
            defaultSku.setSkuNormalPrice1(sku.getSkuNormalPrice1());
            defaultSku.setSkuNormalPrice2(sku.getSkuNormalPrice2());
            defaultSku.setSkuNormalPrice3(sku.getSkuNormalPrice3());

            defaultSku.setSkuPromotionPrice0(sku.getSkuPromotionPrice0());
            defaultSku.setSkuPromotionPrice1(sku.getSkuPromotionPrice1());
            defaultSku.setSkuPromotionPrice2(sku.getSkuPromotionPrice2());
            defaultSku.setSkuPromotionPrice3(sku.getSkuPromotionPrice3());
            defaultSku.setPromStartTime(sku.getPromStartTime());
            defaultSku.setPromEndTime(sku.getPromEndTime());
            defaultSku.setIsPro(product.getIsPro());
            skuDao.update(defaultSku);
        }
        product.setPrice(sku.getPrice());
        product.setCost(sku.getCost());
        product.setMarketPrice(sku.getMarketPrice());
//		product.setMaxCommission(sku.getMaxCommission().compareTo(sku.getPrice()) > 0 ? BigDecimal.ZERO : sku.getMaxCommission());
        product.setMaxCommission(BigDecimal.ZERO);
        setValue(product);
        BeanUtils.copyProperties(product, pProduct, "sn", "type", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "reviews", "consultations", "productFavorites", "skus", "store", "erpFlag");
        pProduct.setErpFlag(product.getErpFlag());
        pProduct.setIsPro(product.getIsPro());
        pProduct.setInternalNumber(product.getInternalNumber());
        pProduct.setKeyword(product.getKeyword());
        if (pProduct.getTotalScore() != null && pProduct.getScoreCount() != null && pProduct.getScoreCount() > 0) {
            pProduct.setScore((float) product.getTotalScore() / product.getScoreCount());
        } else {
            pProduct.setScore(0F);
        }
        if (CollectionUtils.isNotEmpty(pProduct.getProductImages())) {
            Collections.sort(pProduct.getProductImages());
        }
        super.update(pProduct);

        return pProduct;
    }

    @Override
    @CacheEvict(value = {"product", "productCategory"}, allEntries = true)
    public Product modify(Product product, List<Sku> skus) {
        Assert.notNull(product, "[Assertion failed] - product is required; it must not be null");
        Assert.isTrue(!product.isNew(), "[Assertion failed] - product must not be new");
        Assert.isTrue(product.hasSpecification(), "[Assertion failed] - product must have specification");
        Assert.notEmpty(skus, "[Assertion failed] - skus must not be empty: it must contain at least 1 element");

        final List<SpecificationItem> specificationItems = product.getSpecificationItems();
        if (CollectionUtils.exists(skus, new Predicate() {
            private Set<List<Integer>> set = new HashSet<>();

            public boolean evaluate(Object object) {
                Sku sku = (Sku) object;
                return sku == null || !sku.isNew() || !sku.hasSpecification() || !set.add(sku.getSpecificationValueIds()) || !specificationValueService.isValid(specificationItems, sku.getSpecificationValues());
            }
        })) {
            throw new IllegalArgumentException();
        }

        Sku defaultSku = (Sku) CollectionUtils.find(skus, new Predicate() {
            public boolean evaluate(Object object) {
                Sku sku = (Sku) object;
                return sku != null && sku.getIsDefault();
            }
        });
        if (defaultSku == null) {
            defaultSku = skus.get(0);
            defaultSku.setIsDefault(true);
        }

        Product pProduct = productDao.find(product.getId());
        for (Sku sku : skus) {
            switch (pProduct.getType()) {
                case GENERAL:
                    sku.setExchangePoint(0L);
                    break;
                case EXCHANGE:
                    sku.setPrice(BigDecimal.ZERO);
                    sku.setMaxCommission(BigDecimal.ZERO);
                    sku.setRewardPoint(0L);
                    break;
                case GIFT:
                    sku.setPrice(BigDecimal.ZERO);
                    sku.setMaxCommission(BigDecimal.ZERO);
                    sku.setRewardPoint(0L);
                    sku.setExchangePoint(0L);
                    break;
            }
            if (sku.getMarketPrice() == null) {
                sku.setMarketPrice(calculateDefaultMarketPrice(sku.getPrice()));
            }
            if (sku.getRewardPoint() == null) {
                sku.setRewardPoint(calculateDefaultRewardPoint(sku.getPrice()));
            } else {
                long maxRewardPoint = calculateMaxRewardPoint(sku.getPrice());
                sku.setRewardPoint(sku.getRewardPoint() > maxRewardPoint ? maxRewardPoint : sku.getRewardPoint());
            }
            if (sku != defaultSku) {
                sku.setIsDefault(false);
            }
            sku.setMaxCommission(BigDecimal.ZERO);
            sku.setAllocatedStock(0);
            sku.setProduct(pProduct);
            sku.setCartItems(null);
            sku.setOrderItems(null);
            sku.setOrderShippingItems(null);
            sku.setProductNotifies(null);
            sku.setStockLogs(null);

            if (sku.getPromStartTime() == null) {
                sku.setPromStartTime(new Date());
            }
            if (sku.getPromEndTime() == null) {
                sku.setPromEndTime(new Date());
            }
        }

        if (pProduct.hasSpecification()) {
            for (Sku pSku : pProduct.getSkus()) {
                if (!exists(skus, pSku.getSpecificationValueIds())) {
                    skuDao.remove(pSku);
                }
            }
            for (Sku sku : skus) {
                Sku pSku = find(pProduct.getSkus(), sku.getSpecificationValueIds());
                if (pSku != null) {
                    pSku.setPrice(sku.getPrice());
                    pSku.setCost(sku.getCost());
                    pSku.setMarketPrice(sku.getMarketPrice());
                    pSku.setMaxCommission(BigDecimal.ZERO);
                    pSku.setRewardPoint(sku.getRewardPoint());
                    pSku.setExchangePoint(sku.getExchangePoint());
                    pSku.setIsDefault(sku.getIsDefault());
                    pSku.setSpecificationValues(sku.getSpecificationValues());
                    pSku.setLastModifiedDate(new Date());
                    pSku.setSkuNormalPrice0(sku.getSkuNormalPrice0());
                    pSku.setSkuNormalPrice1(sku.getSkuNormalPrice1());
                    pSku.setSkuNormalPrice2(sku.getSkuNormalPrice2());
                    pSku.setSkuNormalPrice3(sku.getSkuNormalPrice3());
                    pSku.setSkuPromotionPrice0(sku.getSkuPromotionPrice0());
                    pSku.setSkuPromotionPrice1(sku.getSkuPromotionPrice1());
                    pSku.setSkuPromotionPrice2(sku.getSkuPromotionPrice2());
                    pSku.setSkuPromotionPrice3(sku.getSkuPromotionPrice3());
                    pSku.setPromEndTime(sku.getPromEndTime());
                    pSku.setPromStartTime(sku.getPromStartTime());
                    pSku.setCounterNo(sku.getCounterNo());
                    pSku.setCounterName(sku.getCounterName());
                    pSku.setIsPro(sku.getIsPro());
                    pSku.setInternalNumber(sku.getInternalNumber());
                    skuDao.update(pSku);
                } else {
                    if (sku.getStock() == null) {
                        throw new IllegalArgumentException();
                    }
                    setValue(sku);
                    skuDao.save(sku);
                    stockIn(sku);
                }
            }
        } else {
            skuDao.remove(pProduct.getDefaultSku());
            for (Sku sku : skus) {
                if (sku.getStock() == null) {
                    throw new IllegalArgumentException();
                }
                setValue(sku);
                skuDao.save(sku);
                stockIn(sku);
            }
        }

        product.setPrice(defaultSku.getPrice());
        product.setCost(defaultSku.getCost());
        product.setMarketPrice(defaultSku.getMarketPrice());
//		product.setMaxCommission(defaultSku.getMaxCommission().compareTo(defaultSku.getPrice()) > 0 ? BigDecimal.ZERO : defaultSku.getMaxCommission());
        product.setMaxCommission(BigDecimal.ZERO);
        setValue(product);
        BeanUtils.copyProperties(product, pProduct, "sn", "type", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "reviews", "consultations", "productFavorites", "skus", "store", "isPro", "erpFlag");
        pProduct.setErpFlag(product.getErpFlag());
        pProduct.setInternalNumber(product.getInternalNumber());
        pProduct.setKeyword(product.getKeyword());
        if (pProduct.getTotalScore() != null && pProduct.getScoreCount() != null && pProduct.getScoreCount() > 0) {
            pProduct.setScore((float) pProduct.getTotalScore() / pProduct.getScoreCount());
        } else {
            pProduct.setScore(0F);
        }
        if (CollectionUtils.isNotEmpty(pProduct.getProductImages())) {
            Collections.sort(pProduct.getProductImages());
        }

        if (product.getIsPro() == null) {
            for (Sku sku : skus) {
                if (sku.getIsPro() == true) {
                    pProduct.setIsPro(true);
                    break;
                }
            }
        }
        pProduct.setIsPro(product.getIsPro());
        super.update(pProduct);
        return pProduct;
    }

    @Override
    @CacheEvict(value = {"product", "productCategory"}, allEntries = true)
    public void refreshExpiredStoreProductActive() {
        productDao.refreshExpiredStoreProductActive();
//        searchService.index(Product.class);
    }

    @Override
    @CacheEvict(value = {"product", "productCategory"}, allEntries = true)
    public void refreshActive(Store store) {
        Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");
        productDao.refreshActive(store);
//        searchService.index(Product.class);
    }

    @Override
    @CacheEvict(value = {"product", "productCategory"}, allEntries = true)
    public void shelves(Long[] ids) {
        Assert.notEmpty(ids, "[Assertion failed] - ids must not be empty: it must contain at least 1 element");

        productDao.shelves(ids);
//        searchService.index(Product.class);
    }

    @Override
    @CacheEvict(value = {"product", "productCategory"}, allEntries = true)
    public void shelf(Long[] ids) {
        Assert.notEmpty(ids, "[Assertion failed] - ids must not be empty: it must contain at least 1 element");

        productDao.shelf(ids);
//        searchService.index(Product.class);
    }


    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory"}, allEntries = true)
    public boolean save(Product product) {
        return super.save(product);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory"}, allEntries = true)
    public Product update(Product product) {
        return super.update(product);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory"}, allEntries = true)
    public Product update(Product product, String... ignoreProperties) {
        return super.update(product, ignoreProperties);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory"}, allEntries = true)
    public void delete(Long id) {
        productDao.delete(Arrays.asList(id));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory"}, allEntries = true)
    public void delete(Long... ids) {
        productDao.delete(Arrays.asList(ids));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory"}, allEntries = true)
    public void delete(Product product) {
        productDao.delete(Arrays.asList(product.getId()));
    }



    /**
     * 函数式接口 T -> bollean
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> java.util.function.Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        ConcurrentHashMap<Object, Boolean> map = new ConcurrentHashMap<>(16);
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 设置商品值
     *
     * @param product 商品
     */
    private void setValue(Product product) {
        if (product == null) {
            return;
        }

        if (product.isNew()) {
            if (StringUtils.isEmpty(product.getSn())) {
                String sn;
                do {
                    sn = snService.generate(Sn.Type.PRODUCT);
                } while (snExists(sn));
                product.setSn(sn);
            }
        }
    }

    /**
     * 设置SKU值
     *
     * @param sku SKU
     */
    private void setValue(Sku sku) {
        if (sku == null) {
            return;
        }

        if (sku.isNew()) {
            Product product = sku.getProduct();
            if (product != null && StringUtils.isNotEmpty(product.getSn())) {
                String sn;
                int i = sku.hasSpecification() ? 1 : 0;
                do {
                    sn = product.getSn() + (i == 0 ? StringUtils.EMPTY : "_" + i);
                    i++;
                } while (skuDao.exists("sn", sn));
                sku.setSn(sn);
                sku.setId(IdWorker.getId());
                sku.setCreatedDate(new Date());
                sku.setVersion(0L);
            }
        }
    }

    /**
     * 计算默认市场价
     *
     * @param price 价格
     * @return 默认市场价
     */
    private BigDecimal calculateDefaultMarketPrice(BigDecimal price) {
        Assert.notNull(price, "[Assertion failed] - price is required; it must not be null");

        Setting setting = SystemUtils.getSetting();
        Double defaultMarketPriceScale = setting.getDefaultMarketPriceScale();
        return defaultMarketPriceScale != null ? setting.setScale(price.multiply(new BigDecimal(String.valueOf(defaultMarketPriceScale)))) : BigDecimal.ZERO;
    }

    /**
     * 计算默认赠送积分
     *
     * @param price 价格
     * @return 默认赠送积分
     */
    private long calculateDefaultRewardPoint(BigDecimal price) {
        Assert.notNull(price, "[Assertion failed] - price is required; it must not be null");

        Setting setting = SystemUtils.getSetting();
        Double defaultPointScale = setting.getDefaultPointScale();
        return defaultPointScale != null ? price.multiply(new BigDecimal(String.valueOf(defaultPointScale))).longValue() : 0L;
    }

    /**
     * 计算最大赠送积分
     *
     * @param price 价格
     * @return 最大赠送积分
     */
    private long calculateMaxRewardPoint(BigDecimal price) {
        Assert.notNull(price, "[Assertion failed] - price is required; it must not be null");

        Setting setting = SystemUtils.getSetting();
        Double maxPointScale = setting.getMaxPointScale();
        return maxPointScale != null ? price.multiply(new BigDecimal(String.valueOf(maxPointScale))).longValue() : 0L;
    }

    /**
     * 根据规格值ID查找SKU
     *
     * @param skus                  SKU
     * @param specificationValueIds 规格值ID
     * @return SKU
     */
    private Sku find(Collection<Sku> skus, final List<Integer> specificationValueIds) {
        if (CollectionUtils.isEmpty(skus) || CollectionUtils.isEmpty(specificationValueIds)) {
            return null;
        }

        return (Sku) CollectionUtils.find(skus, new Predicate() {
            public boolean evaluate(Object object) {
                Sku sku = (Sku) object;
                return sku != null && sku.getSpecificationValueIds() != null && sku.getSpecificationValueIds().equals(specificationValueIds);
            }
        });
    }

    /**
     * 根据规格值ID判断SKU是否存在
     *
     * @param skus                  SKU
     * @param specificationValueIds 规格值ID
     * @return SKU是否存在
     */
    private boolean exists(Collection<Sku> skus, final List<Integer> specificationValueIds) {
        return find(skus, specificationValueIds) != null;
    }

    /**
     * 入库
     *
     * @param sku SKU
     */
    private void stockIn(Sku sku) {
        if (sku == null || sku.getStock() == null || sku.getStock() <= 0) {
            return;
        }

        StockLog stockLog = new StockLog();
        stockLog.setType(StockLog.Type.STOCK_IN);
        stockLog.setInQuantity(sku.getStock());
        stockLog.setOutQuantity(0);
        stockLog.setStock(sku.getStock());
        stockLog.setMemo(null);
        stockLog.setSku(sku);
        stockLog.setId(IdWorker.getId());
        stockLog.setCreatedDate(new Date());
        stockLog.setVersion(0L);
        stockLogDao.save(stockLog);
    }

    /**
     * 同步商品
     */
    @Override
    public void syncProducts(Product sync_product, Long productCategoryId) {

        _logger.info("SyncProductEnd--{}", new Date());
    }


    /**
     * 同步商品
     */
    @Override
    public void syncProducts(Product sync_product) {
        _logger.info("SyncProductBegin--{}", new Date());
        //每批数量
        int batch_no = 1;

        try {

            Set<net.xiaoxiangshop.entity.Product> lstProduct = new HashSet<>();
            if (null == sync_product) {
                lstProduct = productDao.findSet("erp_flag", true);

                batch_no = 10;
            } else {
                lstProduct.add(sync_product);
            }
            int batchNum = 0;
            List<String> lstInterNumber = new ArrayList<>();
            Set<String> lstProductId = new HashSet<String>();
            Date date = new Date();

            for (Product product : lstProduct) {
                Product product_sync = productDao.find(product.getId());

                Date sync_time = product_sync.getSyncTime();

                if (null != sync_time) {

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sync_time);
                    cal.add(Calendar.HOUR_OF_DAY, 1);
                    Date dateStart = cal.getTime();

                    if (dateStart.after(date)) {
                        continue;
                    }
                }

                Store store = product_sync.getStore();
                String store_no = store.getStoreNo();
                if (null == store_no || store_no.equals(""))
                    continue;
                Set<Sku> skuSet = skuDao.findSet("product_id", product.getId());

                for (Sku item : skuSet) {
                    String internal_number = item.getInternalNumber();
                    if (null != internal_number && !internal_number.equals("")) {
                        lstProductId.add(store_no + "|" + internal_number);
                    }
                }

            }

            String[] snString = (String[]) lstProductId.toArray(new String[0]);
            List<String> lstTmp = new ArrayList<String>(Arrays.asList(snString));
            HashMap hashMap = new HashMap();

            _logger.info("Total Skus:{}", lstTmp.size());

            Integer processNum = 0;

            for (int i = 0; i < lstTmp.size(); i++) {
                String[] tmp = lstTmp.get(i).split("\\|");
                String store_no = tmp[0];
                Boolean blnInt = isInteger(tmp[1]);
                List<String> lstSend = new ArrayList<String>();
                if (blnInt) {
                    if (hashMap.containsKey(store_no)) {
                        lstSend = (List<String>) hashMap.get(store_no);
                    }
                    lstSend.add(tmp[1]);
                    hashMap.put(store_no, lstSend);
                }
                batchNum++;

                if (batchNum > batch_no) {
                    processNum += batchNum;
                    _logger.info("processNum:{}", processNum);
                    syncErp(hashMap);
                    hashMap.clear();
                    batchNum = 0;

                    try {
                        Thread.sleep(3000l);
                    } catch (Exception ex) {
                        _logger.info("sync_product_exception,{}", ex.getMessage());
                    }
                }
            }

            if (hashMap.size() > 0) {
                processNum += hashMap.size();
                _logger.info("processNum:{}", processNum);
                syncErp(hashMap);
                hashMap.clear();
                batchNum = 0;

            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        _logger.info("SyncProductEnd--{}", new Date());
    }

    public boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 同步处理
     */
    @Override
    public void syncErp(HashMap hashMap_product) {
        SkuRequestBean skuRequestBean = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat formatter_log = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        skuRequestBean = new SkuRequestBean();
        Boolean isPro = false;
        String product_price = "";

        String retV = "";
        Iterator iter = hashMap_product.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            List<String> val = (List<String>) entry.getValue();

            try {
                skuRequestBean.setMethod(ErpInterfaceMethod.GET_GOODS_LIST);
                skuRequestBean.setOrgid(key);
                skuRequestBean.setProductbarcodes(val);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                Integer sub_code = 1;
                SkuResponseBean skuResponseBean = null;

                try {
                    String send = JSONObject.toJSONString(skuRequestBean);
                    _logger.info(send);
                    HashMap hashMap = new HashMap();
                    hashMap.put("erpType", "ProductService.syncErp");
                    hashMap.put("sendTime", formatter_log.format(new Date()));
                    hashMap.put("sendText", send);
                    hashMap.put("type", "1");

                    retV = WebUtils.sendPost(erp_basic_url, send);
                    _logger.info(retV);
                    hashMap.put("resultTime", formatter_log.format(new Date()));
                    hashMap.put("resultText", retV);
                    JSONObject obj = JSONObject.parseObject(retV);
                    skuResponseBean = JSON.toJavaObject(obj, SkuResponseBean.class);
                    sub_code = skuResponseBean.getSub_code();
                    hashMap.put("resultCode", sub_code);

                    erpResultService.add(hashMap);
                    hashMap.clear();

                } catch (Exception ex) {
                    _logger.info("sync_product_exception,{}", ex.getMessage());
                    ex.printStackTrace();
                }

                if (sub_code == 0) {
                    List<Data> lstData = skuResponseBean.getData();

                    if (lstData.size() > 0) {
                        for (int i = 0; i < lstData.size(); i++) {
                            Data data = lstData.get(i);

                            String mddm = data.getMddm();

                            try {
                                //如果att不等于2，中台此条数据不完整，忽略
                                if (data.getAtt() != 2){
                                    _logger.info("sync_product_exception,{}", "att !=2 报文不完整忽略");
                                    continue;
                                }
                                if (!mddm.equals(key)){
                                    _logger.info("sync_product_exception,{}", "门店编码不一致，mddm:"+mddm+";key:"+key);
                                    continue;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                continue;
                            }
                            String counter_no = data.getZgdm();
                            String counter_name = data.getZgmc();
                            String internal_number = data.getProductbarcode();


                            String stock = data.getKcsl();
                            String weight = data.getWeight();
                            String ispro = data.getIspro();
                            String start_time = data.getSdate() + " " + data.getStime();
                            String end_time = data.getEdate() + " " + data.getEtime();
                            String iszsz = data.getIszsz();
                            String price = data.getPrice();

                            try {
                                //如果中台价格为空或 <=0不同步
                                if (null == price || Float.valueOf(price) <= 0){
                                    _logger.info("sync_product_exception,{}", "价格不正常忽略");
                                    continue;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                continue;
                            }
                            String vipprice1 = data.getVipprice1();
                            String vipprice2 = data.getVipprice2();
                            String vipprice3 = data.getVipprice3();
                            String proprice = data.getProprice();
                            String provipprice1 = data.getProvarprice1();
                            String provipprice2 = data.getProvarprice2();
                            String provipprice3 = data.getProvarprice3();
//                            Set<Sku> skuSet = skuDao.findSet("internal_number", internal_number);

                            Set<Sku> skuSet = skuDao.findErpSet(internal_number,mddm);

                            Product product = null;

                            for (Sku item : skuSet) {

                                        product = item.getProduct();

                                        // 判断未上架商品，不执行更新操作
//                                        if (!product.getIsMarketable())
//                                            continue;

                                        item.setCounterNo(counter_no);
                                        item.setCounterName(counter_name);

                                        //从商品顶级分类取核减库存数，减除
                                        Product product_sku = item.getProduct();
                                        ProductCategory productCategory = product_sku.getProductCategory();

                                        Integer product_stock = Integer.parseInt(stock);
                                        try {
                                            if (null == productCategory) {
                                                item.setStock(product_stock);
                                            } else {

                                                Integer deduct_stock = productCategory.getDeductStock();


                                                if (null == deduct_stock)
                                                    deduct_stock = 0;

                                                if (product_stock >= deduct_stock)
                                                    product_stock = product_stock - deduct_stock;

                                                item.setStock(product_stock);
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }


                                        if (ispro.equals("1"))
                                            isPro = true;

                                        item.setIsPro(isPro);

                                        if (isPro) {
                                            try {
                                                item.setPromStartTime(formatter.parse(start_time));
                                                item.setPromEndTime(formatter.parse(end_time));
                                            } catch (Exception ex) {
                                                _logger.info("sync_product_exception,{}", ex.getMessage());
                                            }
                                        }

                                        if (iszsz.equals("1"))
                                            item.setIsZsz(true);
                                        else
                                            item.setIsZsz(false);

                                        item.setPrice(BigDecimal.valueOf(Double.parseDouble(price)));
                                        item.setMarketPrice(BigDecimal.valueOf(Double.parseDouble(price)));
                                        item.setSkuNormalPrice0(BigDecimal.valueOf(Double.parseDouble(price)));
                                        item.setSkuNormalPrice1(BigDecimal.valueOf(Double.parseDouble(vipprice1)));
                                        item.setSkuNormalPrice2(BigDecimal.valueOf(Double.parseDouble(vipprice2)));
                                        item.setSkuNormalPrice3(BigDecimal.valueOf(Double.parseDouble(vipprice3)));
                                        item.setSkuPromotionPrice0(BigDecimal.valueOf(Double.parseDouble(proprice)));
                                        item.setSkuPromotionPrice1(BigDecimal.valueOf(Double.parseDouble(provipprice1)));
                                        item.setSkuPromotionPrice2(BigDecimal.valueOf(Double.parseDouble(provipprice2)));
                                        item.setSkuPromotionPrice3(BigDecimal.valueOf(Double.parseDouble(provipprice3)));
                                        item.setSyncTime(new Date());
                                        item.setSyncFlag(true);
                                         _logger.info("更新sku ID:"+item.getId()+"; sku name:"+ product_sku.getName()+"; store_no:"+mddm);
                                        Sku sku = skuService.update(item);
                                        try {
                                            Thread.sleep(100l);
                                        } catch (Exception ex) {
                                            _logger.info("sync_product_exception,{}", ex.getMessage());
                                        }
                                        if (null == sku)
                                            continue;

                                        if (item.getIsDefault()) {
                                            product_price = price;
                                        }
//                                    }
//                                }

                                try {
                                    if (null != product) {
                                        if (!product_price.equals(""))
                                            product.setPrice(BigDecimal.valueOf(Double.parseDouble(product_price)));
                                        product.setIsPro(isPro);
                                        product.setSyncTime(new Date());

                                        product.setCounterNo(counter_no);
                                        product.setCounterName(counter_name);

                                        if (stock.trim().equals("0")) {
                                            product.setIsMarketable(false);
                                        } else
                                            product.setIsMarketable(true);

                                        product.setSyncFlag(true);
                                        Boolean sucess = productDao.update(product);
                                        if (sucess)
                                            searchService.add(product);
                                        _logger.info("Updated product");
                                        try {
                                            Thread.sleep(100l);
                                        } catch (Exception ex) {
                                            _logger.info("sync_product_exception,{}", ex.getMessage());
                                        }
                                    }
                                } catch (Exception ex) {
                                    _logger.info("sync_product_exception,{}", ex.getMessage());
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }

            } catch (Exception ex) {
                _logger.info("sync_product_exception,{}", ex.getMessage());
                ex.printStackTrace();
            }
        }

    }

    @Override
    public void updateBusiness(Product product) {
        productDao.updateBusiness(product);
    }

}