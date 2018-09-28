package cc.flyee.branchart.services;


import cc.flyee.branchart.models.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByTargetAndTypeAndTargetId(Integer target, Integer type, String targetId, Pageable pageable);

}
