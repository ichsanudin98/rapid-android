package com.ichsanudinstore.loka.api.endpoint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Ichsanudin_Chairin
 * @since Thursday 8/22/2019 10:24 PM
 */
public class BaseRequest {
    @SerializedName("TS")
    @Expose
    private String timestamp;

    @SerializedName("SC")
    @Expose
    private String securityCode;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}
