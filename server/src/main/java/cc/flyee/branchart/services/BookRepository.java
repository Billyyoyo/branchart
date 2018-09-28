package cc.flyee.branchart.services;


import cc.flyee.branchart.models.Book;
import cc.flyee.branchart.models.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {

    List<Book> findByProfileIdAndType(String profileId, Integer type, Pageable pageable);

    List<Book> findByOwnerIdAndType(String userId, Integer type, Pageable pageable);

    List<Book> findByType(Integer type, Pageable pageable);

}
