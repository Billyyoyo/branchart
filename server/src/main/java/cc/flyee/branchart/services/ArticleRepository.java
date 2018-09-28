package cc.flyee.branchart.services;


import cc.flyee.branchart.models.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleRepository extends MongoRepository<Article, String> {

}
