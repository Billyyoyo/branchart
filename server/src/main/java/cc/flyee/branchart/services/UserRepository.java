package cc.flyee.branchart.services;


import cc.flyee.branchart.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByLoginName(String loginName);

}
