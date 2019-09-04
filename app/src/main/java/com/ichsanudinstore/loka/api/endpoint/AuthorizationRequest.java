package com.ichsanudinstore.loka.api.endpoint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 5:52 AM
 */
public class AuthorizationRequest extends BaseRequest {
    @SerializedName("SD")
    @Expose
    private String session;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
