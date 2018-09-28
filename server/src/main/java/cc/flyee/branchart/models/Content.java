package cc.flyee.branchart.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Content extends BaseModel {

    private Integer version;
    private String articleId;
    private String content;
    private String referenceId;
    private String referenceContent;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public void setContent(String content) {
        this.content = content;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceContent() {
        return referenceContent;
    }

    public void setReferenceContent(String referenceContent) {
        this.referenceContent = referenceContent;
    }
}
