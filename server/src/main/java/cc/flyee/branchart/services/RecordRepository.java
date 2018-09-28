package cc.flyee.branchart.services;


import cc.flyee.branchart.models.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecordRepository extends MongoRepository<Record, String> {

    List<Record> findByArticleId(String articleId, Pageable pageable);

}
