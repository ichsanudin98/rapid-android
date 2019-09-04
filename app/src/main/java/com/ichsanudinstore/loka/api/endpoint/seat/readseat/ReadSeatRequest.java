package com.ichsanudinstore.loka.api.endpoint.seat.readseat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.AuthorizationRequest;

/**
 * @author Ichsanudin_Chairin
 * @since Saturday 8/31/2019 1:35 AM
 */
public class ReadSeatRequest extends AuthorizationRequest {
    @SerializedName("TY")
    @Expose
    private byte type;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
