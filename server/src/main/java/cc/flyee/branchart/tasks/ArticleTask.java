package cc.flyee.branchart.tasks;

import cc.flyee.branchart.models.*;
import cc.flyee.branchart.services.ArticleRepository;
import cc.flyee.branchart.services.DocRepository;
import cc.flyee.branchart.services.NewsRepository;
import cc.flyee.branchart.services.SocialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class ArticleTask {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    NewsRepository newsRepository;
    @Autowired
    SocialRepository socialRepository;
    @Autowired
    DocRepository docRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    private MessageSource messageSource;

    @Async
    public void createArticle(Article article, Content content) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage("text.some_one_publish", null, locale);
        List<Social> relations = socialRepository.findByIdolIdAndType(article.getOwnerId(), Social.TYPE_NORMAL);
        for (Social relation : relations) {
            News news = new News();
            news.setCreateTime(System.currentTimeMillis());
            news.setModifyTime(System.currentTimeMillis());
            news.setFansId(relation.getFansId());
            news.setWhoId(article.getOwnerId());
            news.setWhen(article.getCreateTime());
//            news.setType(News);
            news.setWhoName(article.getOwnerName());
            news.setWhat(message);
            newsRepository.save(news);
        }
        Doc doc = new Doc();
        doc.setOwnerId(article.getOwnerId());
        doc.setOwnerName(article.getOwnerName());
        doc.setTitle(article.getTitle());
        doc.setDescription(article.getDescription());
        doc.setContent(content.getContent());
        doc.setCreateTime(article.getCreateTime());
        doc.setModifyTime(article.getModifyTime());
        doc.setArticleId(article.getId());
        docRepository.save(doc);
        article.setSearchId(doc.getId());
        articleRepository.save(article);
    }

}
