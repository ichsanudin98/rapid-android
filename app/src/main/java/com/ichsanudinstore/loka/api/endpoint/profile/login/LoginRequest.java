package com.ichsanudinstore.loka.api.endpoint.profile.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.BaseRequest;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 5:57 AM
 */
public class LoginRequest extends BaseRequest {
    @SerializedName("EM")
    @Expose
    private String email;

    @SerializedName("PW")
    @Expose
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
