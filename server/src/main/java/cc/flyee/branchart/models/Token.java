package cc.flyee.branchart.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token implements Serializable {

    private static final long serialVersionUID = -1L;

    public final static int FORBID_FALSE = 0;
    public final static int FORBID_TRUE = 1;

    private String token;
    private String uuid;
    private String userId;
    private Long lastLoginTime;
    private Integer forbid = FORBID_FALSE;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getForbid() {
        return forbid;
    }

    public void setForbid(Integer forbid) {
        this.forbid = forbid;
    }
}
