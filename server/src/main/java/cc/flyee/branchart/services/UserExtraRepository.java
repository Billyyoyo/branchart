package cc.flyee.branchart.services;


import cc.flyee.branchart.models.User;
import cc.flyee.branchart.models.UserExtra;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserExtraRepository extends MongoRepository<UserExtra, String> {

}
