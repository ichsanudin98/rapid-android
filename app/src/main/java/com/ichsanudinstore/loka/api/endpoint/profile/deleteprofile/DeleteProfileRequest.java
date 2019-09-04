package com.ichsanudinstore.loka.api.endpoint.profile.deleteprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.AuthorizationRequest;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 6:08 AM
 */
public class DeleteProfileRequest extends AuthorizationRequest {
    @SerializedName("ID")
    @Expose
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
