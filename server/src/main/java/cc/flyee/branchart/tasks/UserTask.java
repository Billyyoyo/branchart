package cc.flyee.branchart.tasks;

import cc.flyee.branchart.models.*;
import cc.flyee.branchart.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserTask {

    @Autowired
    MongoTemplate mongoTemplate;

    @Async
    public void editUserNickName(String userId, String nickName) {
        mongoTemplate.updateMulti(new Query(new Criteria("userId").is(userId)), new Update().set("userName", nickName), UserExtra.class);
        mongoTemplate.updateMulti(new Query(new Criteria("ownerId").is(userId)), new Update().set("ownerName", nickName), Article.class);
        mongoTemplate.updateMulti(new Query(new Criteria("ownerId").is(userId)), new Update().set("ownerName", nickName), Book.class);
        mongoTemplate.updateMulti(new Query(new Criteria("senderId").is(userId)), new Update().set("senderName", nickName), Comment.class);
        mongoTemplate.updateMulti(new Query(new Criteria("ownerId").is(userId)), new Update().set("ownerName", nickName), Favorite.class);
        mongoTemplate.updateMulti(new Query(new Criteria("whoId").is(userId)), new Update().set("whoName", nickName), News.class);
    }

}
