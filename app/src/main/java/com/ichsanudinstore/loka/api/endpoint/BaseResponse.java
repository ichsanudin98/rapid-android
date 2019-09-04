package com.ichsanudinstore.loka.api.endpoint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Ichsanudin_Chairin
 * @since Thursday 8/22/2019 10:23 PM
 */
public class BaseResponse {
    @SerializedName("RC")
    @Expose
    private Integer responseCode;

    @SerializedName("RM")
    @Expose
    private String responseMessage;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
