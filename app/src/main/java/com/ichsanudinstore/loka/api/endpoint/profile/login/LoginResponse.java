package com.ichsanudinstore.loka.api.endpoint.profile.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 5:58 AM
 */
public class LoginResponse extends BaseResponse {
    @SerializedName("SD")
    @Expose
    private String session_id;

    @SerializedName("UD")
    @Expose
    private String user_id;

    @SerializedName("NM")
    @Expose
    private String user_name;

    @SerializedName("CD")
    @Expose
    private String company_id;

    @SerializedName("RN")
    @Expose
    private String role_name;

    @SerializedName("RD")
    @Expose
    private String role_id;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }
}
