package cc.flyee.branchart.services;


import cc.flyee.branchart.models.Content;
import cc.flyee.branchart.models.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends MongoRepository<Tag, String> {

    Optional<Tag> findByTag(String tag);

    List<Tag> findByDeleteFlag(Integer deleteFlag, Pageable pageable);

}
