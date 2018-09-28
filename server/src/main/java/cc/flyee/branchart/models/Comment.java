package cc.flyee.branchart.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment extends BaseModel {

    public final static int TYPE_TEXT = 0;
    public final static int TYPE_LIKE = 1;
    public final static int TYPE_DISLIKE = 2;
    public final static int TYPE_COMPLAINT = 3;

    public final static int TAGRGET_USER = 0;
    public final static int TAGRGET_ARTICLE = 1;
    public final static int TAGRGET_COMMENT = 2;
    public final static int TAGRGET_BOOK = 3;

    private String senderId;
    private String senderName;
    private Integer type;
    private Integer target;
    private String content;
    private String targetId;
    private String replyId;
    private String replyUserId;
    private String replyUserName;
    private String replyContent;

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyUserName() {
        return replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }
}
