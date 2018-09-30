package cc.flyee.branchart;

import cc.flyee.branchart.controllers.SearchController;
import cc.flyee.branchart.forms.SearchForm;
import cc.flyee.branchart.models.*;
import cc.flyee.branchart.services.*;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.common.lucene.search.function.FiltersFunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
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

    final static float MIN_SCORE = 10.0F;

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
    ContentRepository contentRepository;
    @Autowired
    SocialRepository socialRepository;

    @Autowired
    DocRepository docRepository;

    @Autowired
    SearchController searchController;

    @Test
    public void contextLoads() {
//        saveDocs();
        searchDoc();
    }

    private void searchDoc() {
        SearchForm searchForm = new SearchForm();
        searchForm.setKeyword("盗墓");
        searchForm.setPageNumber(1);
        searchForm.setPageSize(3);

        QueryBuilder titleBuilder = QueryBuilders.matchQuery("title", searchForm.getKeyword());
        QueryBuilder descriptionBuilder = QueryBuilders.matchQuery("description", searchForm.getKeyword());
        QueryBuilder contentBuilder = QueryBuilders.matchQuery("content", searchForm.getKeyword());
        QueryBuilder tagBuilder = QueryBuilders.matchQuery("tags", searchForm.getKeyword());
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(titleBuilder);
        boolQueryBuilder.should(descriptionBuilder);
        boolQueryBuilder.should(contentBuilder);
        boolQueryBuilder.should(tagBuilder);

        FetchSourceFilterBuilder sourceFilter = new FetchSourceFilterBuilder();
        sourceFilter.withExcludes("content");

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withPageable(searchForm.getPager())
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withSourceFilter(sourceFilter.build())
                .withQuery(boolQueryBuilder);

        log.info(SortBuilders.fieldSort("createTime").order(SortOrder.DESC).toString());
        Page<Doc> pager = docRepository.search(queryBuilder.build());
        for (Doc doc : pager.getContent()) {
            log.info("::::: " + doc.getTitle()
                    + " ----- " + doc.getDescription().substring(0, 10)
                    + " ----- " + ((doc.getCreateTime()-1537000000000l)/1000)
            );
        }
    }

    private void saveDocs() {
        List<Article> articles = articleRepository.findAll();
        for (Article article : articles) {
            Content content = contentRepository.findById(article.getContentId()).get();
            if (content != null) {
                Doc doc = new Doc();
                doc.setOwnerId(article.getOwnerId());
                doc.setOwnerName(article.getOwnerName());
                doc.setTitle(article.getTitle());
                doc.setTags(article.getTags());
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
    }

}
