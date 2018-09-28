package cc.flyee.branchart;

import cc.flyee.branchart.models.*;
import cc.flyee.branchart.services.*;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BranchartApplicationTests {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserExtraRepository userExtraRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    SocialRepository socialRepository;

    @Test
    public void contextLoads() {
        Optional<Social> optional = socialRepository.findByIdolIdAndFansId("5b879b4d98f77941ff2e6c09","5b9f787a98f779388a348d55");
        if(optional.isPresent()){
            log.error(optional.get().getType()+"");
        }else {
            log.error("------------------------------");
        }
    }

}
