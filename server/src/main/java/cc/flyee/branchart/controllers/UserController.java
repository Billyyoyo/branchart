package cc.flyee.branchart.controllers;

import cc.flyee.branchart.exceptions.LogicException;
import cc.flyee.branchart.forms.ChangePasswordForm;
import cc.flyee.branchart.forms.EditUserForm;
import cc.flyee.branchart.forms.PageQueryForm;
import cc.flyee.branchart.models.*;
import cc.flyee.branchart.services.SocialRepository;
import cc.flyee.branchart.services.UserExtraRepository;
import cc.flyee.branchart.services.UserRepository;
import cc.flyee.branchart.tasks.UserTask;
import cc.flyee.branchart.util.CodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.mongodb.core.query.*;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserExtraRepository userExtraRepository;
    @Autowired
    SocialRepository socialRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    UserTask userTask;

    /**
     * 返回用户所有用户相关数据   user   userextra
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/{userId}")
    public ResultEntry getUserInfo(@RequestHeader(name = "ba_user_id") String selfId,
                                   @PathVariable String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ACCOUNT_NOT_EXIST));
        User user = userOptional.get();
        Optional<UserExtra> extraOptional = userExtraRepository.findById(user.getExtraId());
        UserExtra extra = extraOptional.get();
        ResultData data = ResultData.build().put("user", user).put("extra", extra);
        if(!selfId.equals(userId)) {
            Optional<Social> relationOptional = socialRepository.findByIdolIdAndFansId(userId, selfId);
            if (relationOptional.isPresent()) {
                data.put("relation", relationOptional.get());
            } else {
                data.put("relation", Social.createNone());
            }
        }
        return new ResultEntry(ResultCode.SUCCESS, data.getData());
    }

    /**
     * 本人权限
     * 修改用户字段   如果修改了昵称   将开启异步任务修改所有集合的用户昵称
     *
     * @param userId
     * @param form
     * @return
     */
    @PostMapping(value = "/edit")
    public ResultEntry editUserInfo(
            @RequestHeader(name = "ba_user_id") String userId,
            @RequestBody EditUserForm form) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();
        Optional<UserExtra> extraOptional = userExtraRepository.findById(user.getExtraId());
        UserExtra extra = extraOptional.get();
        if (!Strings.isEmpty(form.getNickName())) {
            user.setNickName(form.getNickName());
            extra.setUserName(form.getNickName());
            userTask.editUserNickName(userId, user.getNickName());
        }
        if (form.getGender() != null) {
            user.setGender(form.getGender());
        }
        user.setModifyTime(System.currentTimeMillis());
        userRepository.save(user);
        extra.setEmail(form.getEmail());
        extra.setAddress(form.getAddress());
        extra.setBio(form.getBio());
        extra.setPhoneNumber(form.getPhone());
        extra.setModifyTime(System.currentTimeMillis());
        userExtraRepository.save(extra);
        return new ResultEntry(ResultCode.SUCCESS, ResultData.build().put("user", user).put("extra", extra).getData());
    }

    /**
     * 本人权限
     * 修改密码
     *
     * @param userId
     * @param form
     * @return
     */
    @PostMapping(value = "/changepwd")
    public ResultEntry changePassword(
            @RequestHeader(name = "ba_user_id") String userId,
            @RequestBody ChangePasswordForm form) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();
        if (!user.getPassword().equals(CodeUtil.sigature(form.getOldPwd()))) {
            throw new LogicException(ResultCode.ERROR_PASSWORD);
        } else {
            user.setPassword(CodeUtil.sigature(form.getNewPwd()));
            user.setModifyTime(System.currentTimeMillis());
            userRepository.save(user);
            return new ResultEntry(ResultCode.SUCCESS);
        }
    }

    /**
     * 查询用户
     *
     * @param sort 1.articleCount
     *             2.commentCount
     *             3.likeCount
     *             4.dislikeCount
     *             5.fansCount
     * @return
     */
    @GetMapping(value = "/list/{sort}")
    public ResultEntry list(@PathVariable Integer sort,
                            @ModelAttribute PageQueryForm queryForm) {
        Criteria criteria = new Criteria();
        criteria = criteria.andOperator(
                new Criteria("deleteFlag").is(BaseModel.DELETE_FLAG_NO),
                new Criteria("role").gte(User.ROLE_NORMAL));
        Query query = new Query(criteria);
        PageModel pager = queryForm.getPager();
        switch (sort.intValue()) {
            case 1:
                pager.setSort(new Sort(Sort.Direction.DESC, "articleCount"));
                break;
            case 2:
                pager.setSort(new Sort(Sort.Direction.DESC, "commentCount"));
                break;
            case 3:
                pager.setSort(new Sort(Sort.Direction.DESC, "likeCount"));
                break;
            case 4:
                pager.setSort(new Sort(Sort.Direction.DESC, "dislikeCount"));
                break;
            case 5:
                pager.setSort(new Sort(Sort.Direction.DESC, "fansCount"));
                break;
        }
        query.with(pager);
        List<UserExtra> extras = mongoTemplate.find(query, UserExtra.class);
        return new ResultEntry(ResultCode.SUCCESS, extras);
    }

}
