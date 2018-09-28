package cc.flyee.branchart.models;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class BaseModel implements Serializable {
    private static final long serialVersionUID = -1L;

    public final static int DELETE_FLAG_YES = 1;
    public final static int DELETE_FLAG_NO = 0;

    @Id
    protected String id;
    protected Integer deleteFlag = DELETE_FLAG_NO;
    protected Long createTime;
    protected Long modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
