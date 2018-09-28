package cc.flyee.branchart.controllers;

import cc.flyee.branchart.exceptions.LogicException;
import cc.flyee.branchart.forms.EditArticleForm;
import cc.flyee.branchart.forms.PageQueryForm;
import cc.flyee.branchart.forms.QueryArticleForm;
import cc.flyee.branchart.models.*;
import cc.flyee.branchart.services.*;
import cc.flyee.branchart.tasks.ArticleTask;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping(value = "/article")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ArticleTask articleTask;

    @GetMapping(value = "/search")
    public ResultEntry search() {
        throw new LogicException(ResultCode.ERROR_INTERNAL);
    }

    /**
     * 查询
     *
     * @param form
     * @return
     */
    //todo 未测试
    @GetMapping(value = "/list")
    public ResultEntry list(@ModelAttribute QueryArticleForm form) {
        Criteria criteria = new Criteria();
        List<Criteria> clist = new ArrayList<>();
        if (!Strings.isEmpty(form.getTag())) {
            clist.add(new Criteria("tags").is(form.getTag()));
        }
        if (!Strings.isEmpty(form.getUpperId())) {
            clist.add(new Criteria("upperIds").is(form.getUpperId()));
        }
        if (!Strings.isEmpty(form.getOwnerId())) {
            clist.add(new Criteria("ownerId").is(form.getOwnerId()));
        }
        if (!Strings.isEmpty(form.getLowerId())) {
            Optional<Article> lowerOptional = articleRepository.findById(form.getLowerId());
            if (lowerOptional.isPresent()) {
                Article lower = lowerOptional.get();
                clist.add(new Criteria("id").in(lower.getUpperIds()));
            }
        }
        if (clist.size() > 0) {
            criteria.andOperator(clist.toArray(new Criteria[clist.size()]));
        }
        Query query = new Query(criteria);
        Sort sort = null;
        if (form.getCreateTimeSort() != null) {
            sort = new Sort(form.getCreateTimeSort() > 0 ? Sort.Direction.DESC : Sort.Direction.ASC, "createTime");
        } else if (form.getModifyTimeSort() != null) {
            sort = new Sort(form.getModifyTimeSort() > 0 ? Sort.Direction.DESC : Sort.Direction.ASC, "modifyTime");
        } else if (form.getLikeSort() != null) {
            sort = new Sort(form.getLikeSort() > 0 ? Sort.Direction.DESC : Sort.Direction.ASC, "likeCount");
        } else if (form.getDislikeSort() != null) {
            sort = new Sort(form.getDislikeSort() > 0 ? Sort.Direction.DESC : Sort.Direction.ASC, "dislikeCount");
        } else if (form.getCommentSort() != null) {
            sort = new Sort(form.getCommentSort() > 0 ? Sort.Direction.DESC : Sort.Direction.ASC, "commentCount");
        } else if (form.getReadSort() != null) {
            sort = new Sort(form.getReadSort() > 0 ? Sort.Direction.DESC : Sort.Direction.ASC, "readCount");
        } else {
            sort = new Sort(Sort.Direction.DESC, "createTime");
        }
        query.with(new PageModel(form.getPageNumber(), form.getPageSize(), sort));
        List<Article> articles = mongoTemplate.find(query, Article.class);
        return new ResultEntry(ResultCode.SUCCESS, articles);
    }

    /**
     * 同时保存article和content   version设置为1 更新user文章数
     * 如果有填写tag   将tag保存到Tag集合  如果tag已存在   tagCount++
     * 保存record
     *
     * @param userId
     * @param form
     * @return
     */
    @PostMapping(value = "/create")
    public ResultEntry create(@RequestHeader(name = "ba_user_id") String userId,
                              @RequestBody EditArticleForm form) {
        User user = userRepository.findById(userId).get();
        Article article = new Article();
        if (!Strings.isEmpty(form.getRootId())) {
            Optional<Article> rootOptional = articleRepository.findById(form.getRootId());
            if (rootOptional.isPresent()) {
                Article root = rootOptional.get();
                article.setTitle(root.getTitle());
                article.setRootId(root.getId());
            } else {
                article.setRootId(Article.DEFAULT_ROOT_ID);
                article.setTitle(form.getTitle());
            }
        } else {
            article.setRootId(Article.DEFAULT_ROOT_ID);
            article.setTitle(form.getTitle());
        }
        article.setDescription(form.getDescription());
        article.setOwnerId(user.getId());
        article.setOwnerName(user.getNickName());
        if (!Strings.isEmpty(form.getUpperId())) {
            article.setUpperIds(new String[]{form.getUpperId()});
        }
        if (!Strings.isEmpty(form.getTags())) {
            String[] tagArray = form.getTags().split(",");
            article.setTags(tagArray);
            for (String tagString : tagArray) {
                Optional<Tag> tagOp = tagRepository.findByTag(tagString);
                Tag tag = null;
                if (tagOp.isPresent()) {
                    tag = tagOp.get();
                } else {
                    tag = new Tag();
                    tag.setTag(tagString);
                }
                tag.setCount(tag.getCount() + 1);
                tagRepository.save(tag);

            }
        }
        article.setVersion(1);
        article.setCreateTime(System.currentTimeMillis());
        article.setModifyTime(System.currentTimeMillis());
        Content content = new Content();
        content.setContent(form.getContent());
        content.setReferenceId(form.getReferenceId());
        content.setReferenceContent(form.getReferenceContent());
        content.setVersion(1);
        content.setCreateTime(article.getCreateTime());
        article.setModifyTime(article.getModifyTime());
        Record record = new Record();
        record.setCreateTime(article.getCreateTime());
        record.setModifyTime(article.getCreateTime());
        record.setDescription(article.getDescription());
        record.setVersion(1);
        contentRepository.save(content);
        article.setContentId(content.getId());
        articleRepository.save(article);
        record.setArticleId(article.getId());
        record.setContentId(content.getId());
        recordRepository.save(record);
        content.setArticleId(article.getId());
        contentRepository.save(content);
        if (!Strings.isEmpty(form.getLowerId())) {
            mongoTemplate.updateFirst(new Query(new Criteria("id").is(form.getLowerId())), new Update().addToSet("upperIds", form.getLowerId()), Article.class);
        }
        mongoTemplate.updateFirst(new Query(new Criteria("userId").is(userId)), new Update().inc("articleCount", 1), UserExtra.class);
        articleTask.createArticle(article);
        return new ResultEntry(ResultCode.SUCCESS, ResultData.build().put("article", article).put("content", content).getData());
    }

    /**
     * 作者权限
     * 修改  先修改 article集合和content集合
     * 如果内容有修改  version++  并且将老的数据保存到record
     * 如果title或description有修改   更新到record，favorite，book集合中
     *
     * @param userId
     * @param articleId
     * @param form
     * @return
     */
    @PostMapping(value = "/update/{articleId}")
    public ResultEntry update(@RequestHeader(name = "ba_user_id") String userId,
                              @PathVariable String articleId,
                              @RequestBody EditArticleForm form) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        articleOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        Article article = articleOptional.get();
        if (!article.getOwnerId().equals(userId)) {
            throw new LogicException(ResultCode.ERROR_NO_PERMISSION);
        }
        if (!Strings.isEmpty(form.getTitle())) {
            article.setTitle(form.getTitle());
        }
        if (!Strings.isEmpty(form.getDescription())) {
            article.setDescription(form.getDescription());
        }
        Content content = contentRepository.findById(article.getContentId()).get();
        if (!Strings.isEmpty(form.getContent())) {
            content = new Content();
            content.setContent(form.getContent());
            content.setVersion(article.getVersion() + 1);
            content.setArticleId(article.getId());
            content.setCreateTime(System.currentTimeMillis());
            content.setModifyTime(System.currentTimeMillis());
            article.setVersion(article.getVersion() + 1);
            contentRepository.save(content);
            article.setContentId(content.getId());
            Record record = new Record();
            record.setContentId(content.getId());
            record.setArticleId(article.getId());
            record.setVersion(content.getVersion());
            record.setDescription(article.getDescription());
            record.setCreateTime(System.currentTimeMillis());
            record.setModifyTime(System.currentTimeMillis());
            recordRepository.save(record);
        }
        articleRepository.save(article);
        return new ResultEntry(ResultCode.SUCCESS, ResultData.build().put("article", article).put("content", content).getData());
    }

    @PostMapping(value = "/linkupper/{flag}/{articleId}/{upperId}")
    public ResultEntry linkUpper(@RequestHeader(name = "ba_user_id") String userId,
                                 @PathVariable Integer flag,
                                 @PathVariable String articleId,
                                 @PathVariable String upperId) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        articleOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        Article article = articleOptional.get();
        if (!article.getOwnerId().equals(userId)) {
            throw new LogicException(ResultCode.ERROR_NO_PERMISSION);
        }
        Update update;
        if (flag.intValue() == 1) {
            update = new Update().addToSet("upperIds", upperId);
        } else {
            update = new Update().pull("upperIds", upperId);
        }
        UpdateResult ret = mongoTemplate.updateFirst(new Query(new Criteria("id").is(articleId)), update, Article.class);
        log.error("update count: " + ret.getModifiedCount());
        article.setModifyTime(System.currentTimeMillis());
        articleRepository.save(article);
        return new ResultEntry(ResultCode.SUCCESS);
    }

    /**
     * 获取article所有内容，如果被标识为删除，将内容部分省略后返回
     *
     * @param articleId
     * @return
     */
    @GetMapping(value = "/{articleId}")
    public ResultEntry get(@PathVariable String articleId) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        articleOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        Article article = articleOptional.get();
        Content content = contentRepository.findById(article.getContentId()).get();
        mongoTemplate.updateFirst(new Query(new Criteria("id").is(article.getId())), new Update().inc("readCount", 1), Article.class);
        if (article.getDeleteFlag().intValue() == BaseModel.DELETE_FLAG_YES) {
            content.setContent("******************************************************");
        }
        return new ResultEntry(ResultCode.SUCCESS, ResultData.build().put("article", article).put("content", content).getData());
    }

    /**
     * 设置删除标识  作者或管理员权限
     *
     * @param articleId
     * @return
     */
    @GetMapping(value = "/delete/{articleId}")
    public ResultEntry delete(@RequestHeader(name = "ba_user_id") String userId,
                              @PathVariable String articleId) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        articleOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        Article article = articleOptional.get();
        if (!article.getOwnerId().equals(userId)) {
            throw new LogicException(ResultCode.ERROR_NO_PERMISSION);
        }
        article.setDeleteFlag(BaseModel.DELETE_FLAG_YES);
        articleRepository.save(article);
        mongoTemplate.updateFirst(new Query(new Criteria("id").is(userId)), new Update().inc("articleCount", -1), UserExtra.class);
        return new ResultEntry(ResultCode.SUCCESS);
    }

    /**
     * 添加/删除标签    并更新Tag集合
     *
     * @param userId
     * @param articleId
     * @param tag
     * @return
     */
    @GetMapping(value = "/optag/{flag}/{articleId}/{tag}")
    public ResultEntry addTag(@RequestHeader(name = "ba_user_id") String userId,
                              @PathVariable Integer flag,
                              @PathVariable String articleId,
                              @PathVariable String tag) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        articleOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        Article article = articleOptional.get();
        if (!article.getOwnerId().equals(userId)) {
            throw new LogicException(ResultCode.ERROR_NO_PERMISSION);
        }
        if (flag == 1) {
            mongoTemplate.updateFirst(new Query(new Criteria("id").is(articleId)), new Update().addToSet("tags", tag), Article.class);
        } else {
            mongoTemplate.updateFirst(new Query(new Criteria("id").is(articleId)), new Update().pull("tags", tag), Article.class);
        }
        Optional<Tag> tagOptional = tagRepository.findByTag(tag);
        if (tagOptional.isPresent()) {
            Tag t = tagOptional.get();
            mongoTemplate.updateFirst(new Query(new Criteria("id").is(t.getId())), new Update().inc("tags", flag == 1 ? 1 : -1), Tag.class);
        } else {
            if (flag == 1) {
                Tag t = new Tag();
                t.setTag(tag);
                t.setCount(1);
                t.setCreateTime(System.currentTimeMillis());
                t.setModifyTime(System.currentTimeMillis());
                tagRepository.save(t);
            }
        }
        return new ResultEntry(ResultCode.SUCCESS);
    }

    @GetMapping(value = "/tags")
    public ResultEntry getTags(@ModelAttribute PageQueryForm pageForm) {
        PageModel pager = pageForm.getPager();
        pager.setSort(new Sort(Sort.Direction.DESC, "count"));
        List<Tag> tags = tagRepository.findByDeleteFlag(BaseModel.DELETE_FLAG_NO, pager);
        return new ResultEntry(ResultCode.SUCCESS, tags);
    }

    /**
     * 获取article的所有历史版本
     *
     * @param articleId
     * @return
     */
    @GetMapping(value = "/versions/{articleId}")
    public ResultEntry getVersions(@PathVariable String articleId,
                                   @ModelAttribute PageQueryForm pageForm) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        articleOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        List<Record> records = recordRepository.findByArticleId(articleId, new PageModel(pageForm.getPageNumber(), pageForm.getPageSize()));
        return new ResultEntry(ResultCode.SUCCESS, records);
    }

    /**
     * 获取某一历史版本的内容
     *
     * @param articleId
     * @param recordId
     * @return
     */
    @GetMapping(value = "/version/{articleId}/{recordId}")
    public ResultEntry getVersion(@PathVariable String articleId, @PathVariable String recordId) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        articleOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        Optional<Record> recordOptional = recordRepository.findById(recordId);
        recordOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        Article article = articleOptional.get();
        Record record = recordOptional.get();
        Optional<Content> contentOptional = contentRepository.findById(record.getContentId());
        recordOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        Content content = contentOptional.get();
        return new ResultEntry(ResultCode.SUCCESS,
                ResultData.build()
                        .put("article", article)
                        .put("record", record)
                        .put("content", content)
                        .getData());
    }

}
