package net.xiaoxiangshop.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import net.xiaoxiangshop.entity.Article;

@Component
public interface ArticleRepository extends ElasticsearchRepository<Article, Long> {

}
