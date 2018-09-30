package cc.flyee.branchart.controllers;

import cc.flyee.branchart.exceptions.LogicException;
import cc.flyee.branchart.forms.SearchForm;
import cc.flyee.branchart.models.Doc;
import cc.flyee.branchart.models.ResultCode;
import cc.flyee.branchart.models.ResultEntry;
import cc.flyee.branchart.services.ArticleRepository;
import cc.flyee.branchart.services.DocRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.lucene.search.function.FiltersFunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/search")
public class SearchController {

    final static float MIN_SCORE = 10.0F;

    @Autowired
    private DocRepository docRepository;

    @GetMapping
    public ResultEntry search(SearchForm searchForm) {
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
        Page<Doc> pager = docRepository.search(queryBuilder.build());
        return new ResultEntry(ResultCode.SUCCESS, pager.getContent());
    }

}
