package cc.flyee.branchart.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Social extends BaseModel {

    public final static int TYPE_NONE = -1;
    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_BLACK = 1;

    private Integer type;
    private String idolId;
    private String idolName;
    private String fansId;
    private String fansName;

    public Integer getType() {
        return type;
    }

    public String getIdolName() {
        return idolName;
    }

    public void setIdolName(String idolName) {
        this.idolName = idolName;
    }

    public String getFansName() {
        return fansName;
    }

    public void setFansName(String fansName) {
        this.fansName = fansName;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIdolId() {
        return idolId;
    }

    public void setIdolId(String idolId) {
        this.idolId = idolId;
    }

    public String getFansId() {
        return fansId;
    }

    public void setFansId(String fansId) {
        this.fansId = fansId;
    }

    public static Social createNone(){
        Social social = new Social();
        social.setType(TYPE_NONE);
        return social;
    }
}
