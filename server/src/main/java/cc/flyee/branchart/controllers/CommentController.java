package cc.flyee.branchart.controllers;

import cc.flyee.branchart.exceptions.LogicException;
import cc.flyee.branchart.forms.PageQueryForm;
import cc.flyee.branchart.forms.SendCommentForm;
import cc.flyee.branchart.models.*;
import cc.flyee.branchart.services.CommentRepository;
import cc.flyee.branchart.services.UserExtraRepository;
import cc.flyee.branchart.services.UserRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
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
@RequestMapping(value = "/comment")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoTemplate mongoTemplate;


    /**
     * 发表评论   包括赞/踩/文本/举报
     * 更新Article的数据统计   和user的数据统计
     *
     * @param userId
     * @param form
     * @return
     */
    @PostMapping(value = "/send")
    public ResultEntry send(@RequestHeader(name = "ba_user_id") String userId,
                            @RequestBody SendCommentForm form) {
        User user = userRepository.findById(userId).get();
        Comment comment = new Comment();
        comment.setSenderId(user.getId());
        comment.setSenderName(user.getNickName());
        comment.setTarget(form.getTarget());
        comment.setTargetId(form.getTargetId());
        comment.setType(form.getType());
        comment.setContent(form.getContent());
        if (!Strings.isEmpty(form.getReplyId())) {
            Optional<Comment> replyOptional = commentRepository.findById(form.getReplyId());
            if (replyOptional.isPresent()) {
                Comment reply = replyOptional.get();
                comment.setReplyId(reply.getId());
                comment.setReplyContent(reply.getContent());
                comment.setReplyUserId(reply.getSenderId());
                comment.setReplyUserName(reply.getSenderName());
            }
        }
        comment.setCreateTime(System.currentTimeMillis());
        comment.setModifyTime(System.currentTimeMillis());
        commentRepository.save(comment);
        if (form.getTarget().intValue() == Comment.TAGRGET_ARTICLE) {
            if (form.getType().intValue() == Comment.TYPE_TEXT) {
                mongoTemplate.updateFirst(
                        new Query(new Criteria("userId").is(userId)),
                        new Update().inc("commentCount", 1),
                        UserExtra.class);
                mongoTemplate.updateFirst(
                        new Query(new Criteria("id").is(form.getTargetId())),
                        new Update().inc("commentCount", 1),
                        Article.class);
            } else if (form.getType().intValue() == Comment.TYPE_LIKE) {
                mongoTemplate.updateFirst(
                        new Query(new Criteria("userId").is(userId)),
                        new Update().inc("likeCount", 1),
                        UserExtra.class);
                mongoTemplate.updateFirst(
                        new Query(new Criteria("id").is(form.getTargetId())),
                        new Update().inc("likeCount", 1),
                        Article.class);
            } else if (form.getType().intValue() == Comment.TYPE_DISLIKE) {
                mongoTemplate.updateFirst(
                        new Query(new Criteria("userId").is(userId)),
                        new Update().inc("dislikeCount", 1),
                        UserExtra.class);
                mongoTemplate.updateFirst(
                        new Query(new Criteria("id").is(form.getTargetId())),
                        new Update().inc("dislikeCount", 1),
                        Article.class);
            }
        } else if (form.getTarget().intValue() == Comment.TAGRGET_USER) {
            UpdateResult ret = mongoTemplate.updateFirst(
                    new Query(new Criteria("userId").is(form.getTargetId())),
                    new Update().inc("commentCount", 1),
                    UserExtra.class);
            log.error(ret.getMatchedCount()+" -------------- "+ret.getModifiedCount());
        } else {

        }
        return new ResultEntry(ResultCode.SUCCESS, comment);
    }

    /**
     * 不多说
     *
     * @param targetId
     * @param target
     * @param type
     * @return
     */
    @GetMapping(value = "/list/{target}/{type}/{targetId}")
    public ResultEntry list(@PathVariable String targetId,
                            @PathVariable Integer target,
                            @PathVariable Integer type,
                            @ModelAttribute PageQueryForm pageForm) {
        List<Comment> comments = commentRepository.findByTargetAndTypeAndTargetId(target, type, targetId, pageForm.getPager());
        comments.stream().forEach(comment -> {
            if (comment.getDeleteFlag().intValue() == BaseModel.DELETE_FLAG_YES) {
                comment.setContent("************");
            }
        });
        return new ResultEntry(ResultCode.SUCCESS, comments);
    }

    /**
     * 删除评论   更新user和article
     *
     * @param userId
     * @param commentId
     * @return
     */
    @GetMapping(value = "/del/{commentId}")
    public ResultEntry delete(@RequestHeader(name = "ba_user_id") String userId,
                              @PathVariable String commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        commentOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        Comment comment = commentOptional.get();
        if (!comment.getSenderId().equals(userId)) {
            throw new LogicException(ResultCode.ERROR_NO_PERMISSION);
        }
        comment.setDeleteFlag(BaseModel.DELETE_FLAG_YES);
        commentRepository.save(comment);
        return new ResultEntry(ResultCode.SUCCESS);
    }
}
