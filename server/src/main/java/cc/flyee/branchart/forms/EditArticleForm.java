package cc.flyee.branchart.forms;

public class EditArticleForm {

    private String rootId;
    private String upperId;
    private String lowerId;

    private String title;
    private String description;
    private String content;
    private String referenceId;
    private String referenceContent;

    private String tags;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getUpperId() {
        return upperId;
    }

    public void setUpperId(String upperId) {
        this.upperId = upperId;
    }

    public String getLowerId() {
        return lowerId;
    }

    public void setLowerId(String lowerId) {
        this.lowerId = lowerId;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
