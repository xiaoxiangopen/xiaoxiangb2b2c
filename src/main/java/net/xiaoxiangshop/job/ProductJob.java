package net.xiaoxiangshop.job;
import net.xiaoxiangshop.service.ProductService;
import net.xiaoxiangshop.service.SearchService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Job - 店铺
 */
@Lazy(false)
@Component
@EnableScheduling
public class ProductJob {

    @Inject
    private ProductService productService;
    @Inject
    private SearchService searchService;

    /**
     * 商品同步处理
     */
//    @Scheduled(cron = "${job.product_sync.cron}")
    public void evictExpired() {
//        productService.syncProducts(null);
        //ES全量实始化
//        searchService.index(Article.class);
//        searchService.index(Product.class);
//        searchService.index(Store.class);
    }

}