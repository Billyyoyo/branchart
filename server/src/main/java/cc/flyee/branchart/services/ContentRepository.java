package cc.flyee.branchart.services;


import cc.flyee.branchart.models.Content;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContentRepository extends MongoRepository<Content, String> {


}
