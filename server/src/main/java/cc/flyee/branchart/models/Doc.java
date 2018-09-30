package cc.flyee.branchart.models;


import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Document(indexName = "docindex", type = "doc")
public class Doc implements Serializable {

    private String id;
    private String ownerId;
    private String ownerName;
    @Field(type = FieldType.Text, fielddata = true, searchAnalyzer = "ik_max_word", analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text, fielddata = true, searchAnalyzer = "ik_max_word", analyzer = "ik_max_word")
    private String description;
    @Field(type = FieldType.Text, fielddata = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
    private String content;
    @Field(type = FieldType.Text, fielddata = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
    private String[] tags;
    private Long createTime;
    private Long modifyTime;
    private String articleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
