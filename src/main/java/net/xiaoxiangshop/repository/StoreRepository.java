package net.xiaoxiangshop.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import net.xiaoxiangshop.entity.Store;

@Component
public interface StoreRepository extends ElasticsearchRepository<Store, Long> {

}
