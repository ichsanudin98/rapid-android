package com.ichsanudinstore.loka.api.endpoint.profile.updatepassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.AuthorizationRequest;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 6:06 AM
 */
public class UpdatePasswordRequest extends AuthorizationRequest {
    @SerializedName("PN")
    @Expose
    private String phone;

    @SerializedName("OP")
    @Expose
    private String old_password;

    @SerializedName("NP")
    @Expose
    private String new_password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
