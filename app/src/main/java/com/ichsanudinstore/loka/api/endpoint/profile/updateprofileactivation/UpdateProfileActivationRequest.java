package com.ichsanudinstore.loka.api.endpoint.profile.updateprofileactivation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.AuthorizationRequest;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 6:14 AM
 */
public class UpdateProfileActivationRequest extends AuthorizationRequest {
    @SerializedName("ID")
    @Expose
    private String user_id;

    @SerializedName("AD")
    @Expose
    private Boolean activated;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }
}
