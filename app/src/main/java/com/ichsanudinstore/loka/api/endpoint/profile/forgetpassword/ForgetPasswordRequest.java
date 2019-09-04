package com.ichsanudinstore.loka.api.endpoint.profile.forgetpassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.BaseRequest;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 6:07 AM
 */
public class ForgetPasswordRequest extends BaseRequest {
    @SerializedName("EM")
    @Expose
    private String email;

    @SerializedName("PN")
    @Expose
    private String phone;

    @SerializedName("NP")
    @Expose
    private String new_password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
