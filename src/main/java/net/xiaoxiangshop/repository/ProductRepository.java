package net.xiaoxiangshop.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import net.xiaoxiangshop.entity.Product;

@Component
public interface ProductRepository extends ElasticsearchRepository<Product, Long> {

}
