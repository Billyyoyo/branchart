package cc.flyee.branchart.services;


import cc.flyee.branchart.models.News;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NewsRepository extends MongoRepository<News, String> {

    List<News> findByFansId(String fansId, Pageable pageable);

}
