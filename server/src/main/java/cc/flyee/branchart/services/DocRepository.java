package cc.flyee.branchart.services;

import cc.flyee.branchart.models.Doc;
import cc.flyee.branchart.models.PageModel;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface DocRepository extends ElasticsearchRepository<Doc, String> {

    public List<Doc> findByTitleOrDescriptionOrContentOrTags(String keyword1, String keyword2, String keyword3, String keyword4, PageModel pageModel);

    @Query(value = "{\"bool\" : {\"should\" : {\"title\" : \"?0\"}}}")
    public List<Doc> findByKeyword(String keyword, PageModel pageModel);

}
