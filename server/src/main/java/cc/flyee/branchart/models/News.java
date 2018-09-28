package cc.flyee.branchart.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class News extends BaseModel {

    private String fansId;
    private String whoId;
    private String whoName;
    private String what;
    private Long when;
    private Integer type;   //忘了啥意思了

    public String getFansId() {
        return fansId;
    }

    public void setFansId(String fansId) {
        this.fansId = fansId;
    }

    public String getWhoId() {
        return whoId;
    }

    public void setWhoId(String whoId) {
        this.whoId = whoId;
    }

    public String getWhoName() {
        return whoName;
    }

    public void setWhoName(String whoName) {
        this.whoName = whoName;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public Long getWhen() {
        return when;
    }

    public void setWhen(Long when) {
        this.when = when;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
