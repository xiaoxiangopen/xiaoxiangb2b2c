package net.xiaoxiangshop.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import net.xiaoxiangshop.entity.Article;
import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.entity.Sku;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.repository.ArticleRepository;
import net.xiaoxiangshop.repository.ProductRepository;
import net.xiaoxiangshop.repository.StoreRepository;
import net.xiaoxiangshop.service.ArticleService;
import net.xiaoxiangshop.service.ProductService;
import net.xiaoxiangshop.service.SearchService;
import net.xiaoxiangshop.service.SkuService;
import net.xiaoxiangshop.service.StoreService;

/**
 * Service - 搜索
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class SearchServiceImpl implements SearchService {

    @Inject
    private ArticleRepository articleRepository;
    @Inject
    private StoreRepository storeRepository;
    @Inject
    private ProductRepository productRepository;
    @Inject
    private ArticleService articleService;
    @Inject
    private ProductService productService;
    @Inject
    private StoreService storeService;
    @Inject
    private SkuService skuService;

    @Override
    public void index(Class<?> type) {
        index(type, true);
    }

    @Override
    public void index(Class<?> type, boolean purgeAll) {
        // 文章
        if (type.isAssignableFrom(Article.class)) {
            QueryWrapper<Article> queryArticle = new QueryWrapper<>();
            queryArticle.eq("is_publication", true);
            List<Article> articles = articleService.list(queryArticle);
            articleRepository.saveAll(articles);
        }

        // 商品
        if (type.isAssignableFrom(Product.class)) {
            QueryWrapper<Product> queryProduct = new QueryWrapper<>();
            queryProduct.eq("is_active", true);
            queryProduct.eq("is_list", true);
            queryProduct.eq("is_marketable", true);
            List<Product> products = productService.list(queryProduct);
            for (Product product : products) {
                Product persistant = productService.find(product.getId());

                Store store = storeService.getById(persistant.getStore().getId());
                product.setStore(store);

                QueryWrapper<Sku> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("product_id", product.getId());
                Set<Sku> skus = new HashSet<>(skuService.list(queryWrapper));

                for(Sku sku : skus)
                {
                    sku.setOutOfStock();
                }
                product.setSkus(skus);
                product.setProductImages(persistant.getProductImages());
                 //库存显示标识
                product.setStockFlag(persistant.getIsOutOfStock());

//                productRepository.save(product);

            }
        }

        // 店铺
        if (type.isAssignableFrom(Store.class)) {
            QueryWrapper<Store> queryStore = new QueryWrapper<>();
            queryStore.eq("status", Store.Status.SUCCESS.ordinal());
            queryStore.eq("is_enabled", true);
            List<Store> stores = storeService.findAll();
            storeRepository.saveAll(stores);
        }

    }


    @Override
    public void add(Product product1) {

        Product product=productService.find(product1.getId());

        Product product_es = new Product();
        Store store = storeService.getById(product.getStore().getId());
        product_es.setStore(store);
        product_es.setId(product.getId());
        product_es.setName(product.getName());
        product_es.setKeyword(product.getKeyword());
        product_es.setIsTop(product.getIsTop());
        product_es.setIsActive(product.getIsActive());
        product_es.setIsMarketable(product.getIsMarketable());
        product_es.setIsList(product.getIsList());
        product_es.setProductImages(product.getProductImages());
        product_es.setMarketPrice(product.getMarketPrice());
        product_es.setPrice(product.getPrice());
        product_es.setParameterValues(product.getParameterValues());
        product_es.setCost(product.getCost());
        product_es.setType(product.getType());
        product_es.setIsPro(product.getIsPro());

        QueryWrapper<Sku> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", product.getId());
        Set<Sku> skus = new HashSet<>(skuService.list(queryWrapper));
        for(Sku sku : skus)
        {
            sku.setOutOfStock();
        }
        product_es.setSkus(skus);


        //库存显示标识
        product_es.setStockFlag(product.getIsOutOfStock());

//        productRepository.save(product_es);
    }

    @Override
    public void del(Long productId) {
//        productRepository.deleteById(productId);
    }

    @Override
    public void shelves(Product product1) {
        Product product=productService.find(product1.getId());
        Product product_es = new Product();
        Store store = storeService.getById(product.getStore().getId());
        product_es.setStore(store);
        product_es.setId(product.getId());
        product_es.setName(product.getName());
        product_es.setKeyword(product.getKeyword());
        product_es.setIsTop(product.getIsTop());
        product_es.setIsActive(product.getIsActive());
        product_es.setIsMarketable(true);
        product_es.setIsList(product.getIsList());
        product_es.setProductImages(product.getProductImages());
        product_es.setMarketPrice(product.getMarketPrice());
        product_es.setPrice(product.getPrice());
        product_es.setParameterValues(product.getParameterValues());
        product_es.setCost(product.getCost());
        product_es.setType(product.getType());
        product_es.setIsPro(product.getIsPro());
        QueryWrapper<Sku> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", product.getId());
        Set<Sku> skus = new HashSet<>(skuService.list(queryWrapper));
        for(Sku sku : skus)
        {
            sku.setOutOfStock();
        }
        product_es.setSkus(skus);

        //库存显示标识
        product_es.setStockFlag(product.getIsOutOfStock());

//        productRepository.save(product_es);
    }

    @Override
    public void shelf(Product product) {
        Product product_es = new Product();
        product_es.setId(product.getId());
        product_es.setIsMarketable(false);
//        productRepository.save(product_es);
    }
}