package cc.flyee.branchart.services;


import cc.flyee.branchart.models.Social;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SocialRepository extends MongoRepository<Social, String> {

    Optional<Social> findByIdolIdAndFansId(String idolId, String fansId);

    List<Social> findByIdolIdAndType(String idolId, Integer type);

    List<Social> findByIdolIdAndType(String idolId, Integer type, Pageable pageable);

    List<Social> findByFansIdAndType(String fansId, Integer type, Pageable pageable);

}
