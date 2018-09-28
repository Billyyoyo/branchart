package cc.flyee.branchart.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends BaseModel {

    public final static int MALE = 1;
    public final static int FEMALE = 2;

    public final static int ROLE_ADMIN = -2;
    public final static int ROLE_BLACK = -1;
    public final static int ROLE_NORMAL = 0;
    public final static int ROLE_VIP = 1;

    public final static int STATUS_ACTIVE = 1;
    public final static int STATUS_INACTIVE = 0;

    private String loginName;
    private String nickName;
    private String password;
    private Integer gender;
    private Integer role = ROLE_NORMAL;
    private Integer status;
    private String extraId;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExtraId() {
        return extraId;
    }

    public void setExtraId(String extraId) {
        this.extraId = extraId;
    }
}
