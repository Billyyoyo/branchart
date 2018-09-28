package cc.flyee.branchart.controllers;

import cc.flyee.branchart.exceptions.LogicException;
import cc.flyee.branchart.forms.PageQueryForm;
import cc.flyee.branchart.models.*;
import cc.flyee.branchart.services.NewsRepository;
import cc.flyee.branchart.services.SocialRepository;
import cc.flyee.branchart.services.UserExtraRepository;
import cc.flyee.branchart.services.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping(value = "/social")
public class SocialController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserExtraRepository userExtraRepository;
    @Autowired
    SocialRepository socialRepository;
    @Autowired
    NewsRepository newsRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 关注或取消关注   后更新统计数
     *
     * @param userId
     * @param flag   关注或取消
     * @param type   关注或黑名单
     * @param idolId 关注谁
     * @return
     */
    @GetMapping(value = "/follow/{flag}/{type}/{idolId}")
    public ResultEntry follow(@RequestHeader(name = "ba_user_id") String userId,
                              @PathVariable Integer flag,
                              @PathVariable Integer type,
                              @PathVariable String idolId) {
        Optional<User> idolOptional = userRepository.findById(idolId);
        idolOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ACCOUNT_NOT_EXIST));
        User idol = idolOptional.get();
        Optional<User> fansOptional = userRepository.findById(userId);
        fansOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ACCOUNT_NOT_EXIST));
        User fans = fansOptional.get();

        Optional<Social> relationOptional = socialRepository.findByIdolIdAndFansId(idolId, userId);
        if (relationOptional.isPresent()) {
            Social relation = relationOptional.get();
            if (flag == 1) {
                if (relation.getType().intValue() != type.intValue()) {
                    relation.setModifyTime(System.currentTimeMillis());
                    relation.setType(type);
                    socialRepository.save(relation);
                    mongoTemplate.updateFirst(new Query(new Criteria("id").is(idol.getExtraId())), new Update().inc("fansCount", (type.intValue() == Social.TYPE_NORMAL) ? 1 : -1), UserExtra.class);
                    mongoTemplate.updateFirst(new Query(new Criteria("id").is(fans.getExtraId())), new Update().inc("followCount", (type.intValue() == Social.TYPE_NORMAL) ? 1 : -1), UserExtra.class);
                }
            } else {
                socialRepository.delete(relation);
                if (type.intValue() == Social.TYPE_NORMAL) {
                    mongoTemplate.updateFirst(new Query(new Criteria("id").is(idol.getExtraId())), new Update().inc("fansCount", -1), UserExtra.class);
                    mongoTemplate.updateFirst(new Query(new Criteria("id").is(fans.getExtraId())), new Update().inc("followCount", -1), UserExtra.class);
                }
            }
        } else {
            if (flag == 1) {
                Social relation = new Social();
                relation.setIdolId(idol.getId());
                relation.setIdolName(idol.getNickName());
                relation.setFansId(fans.getId());
                relation.setFansName(fans.getNickName());
                relation.setCreateTime(System.currentTimeMillis());
                relation.setModifyTime(System.currentTimeMillis());
                relation.setType(type);
                socialRepository.save(relation);
                if (type.intValue() == Social.TYPE_NORMAL) {
                    mongoTemplate.updateFirst(new Query(new Criteria("id").is(idol.getExtraId())), new Update().inc("fansCount", 1), UserExtra.class);
                    mongoTemplate.updateFirst(new Query(new Criteria("id").is(fans.getExtraId())), new Update().inc("followCount", 1), UserExtra.class);
                }
            }
        }
        return new ResultEntry(ResultCode.SUCCESS);
    }

    @GetMapping(value = "/relationlist/{flag}/{type}/{userId}")
    public ResultEntry relationList(@PathVariable Integer flag,
                                    @PathVariable Integer type,
                                    @PathVariable String userId,
                                    @ModelAttribute PageQueryForm pageForm) {
        List<Social> list = null;
        if (flag == 1) {
            list = socialRepository.findByIdolIdAndType(userId, type, pageForm.getPager());
        } else {
            list = socialRepository.findByFansIdAndType(userId, type, pageForm.getPager());
        }
        return new ResultEntry(ResultCode.SUCCESS, list);
    }

    @GetMapping(value = "/check/{idolId}")
    public ResultEntry check(@RequestHeader(name = "ba_user_id") String userId,
                             @PathVariable String idolId){
        Social social;
        Optional<Social> optional = socialRepository.findByIdolIdAndFansId(idolId, userId);
        if(optional.isPresent()){
            social = optional.get();
        } else {
            social = Social.createNone();
        }
        return new ResultEntry(ResultCode.SUCCESS, social);
    }

    /**
     * @param userId
     * @return
     */
    @GetMapping(value = "/news")
    public ResultEntry news(@RequestHeader(name = "ba_user_id") String userId,
                            @ModelAttribute PageQueryForm pageForm) {
        List<News> list = newsRepository.findByFansId(userId, pageForm.getPager());
        return new ResultEntry(ResultCode.SUCCESS, list);
    }

}
