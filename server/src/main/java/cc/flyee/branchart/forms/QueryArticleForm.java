package cc.flyee.branchart.forms;


public class QueryArticleForm extends PageQueryForm{

    private String tag;
    private String upperId;
    private String lowerId;
    private String ownerId;

    private Integer readSort;
    private Integer commentSort;
    private Integer likeSort;
    private Integer createTimeSort;
    private Integer modifyTimeSort;
    private Integer dislikeSort;

    public String getLowerId() {
        return lowerId;
    }

    public void setLowerId(String lowerId) {
        this.lowerId = lowerId;
    }

    public Integer getModifyTimeSort() {
        return modifyTimeSort;
    }

    public void setModifyTimeSort(Integer modifyTimeSort) {
        this.modifyTimeSort = modifyTimeSort;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUpperId() {
        return upperId;
    }

    public void setUpperId(String upperId) {
        this.upperId = upperId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getReadSort() {
        return readSort;
    }

    public void setReadSort(Integer readSort) {
        this.readSort = readSort;
    }

    public Integer getCommentSort() {
        return commentSort;
    }

    public void setCommentSort(Integer commentSort) {
        this.commentSort = commentSort;
    }

    public Integer getLikeSort() {
        return likeSort;
    }

    public void setLikeSort(Integer likeSort) {
        this.likeSort = likeSort;
    }

    public Integer getCreateTimeSort() {
        return createTimeSort;
    }

    public void setCreateTimeSort(Integer createTimeSort) {
        this.createTimeSort = createTimeSort;
    }

    public Integer getDislikeSort() {
        return dislikeSort;
    }

    public void setDislikeSort(Integer dislikeSort) {
        this.dislikeSort = dislikeSort;
    }

}
