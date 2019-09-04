package com.ichsanudinstore.loka.api.endpoint.seat.deleteseat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.AuthorizationRequest;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 1:56 PM
 */
public class DeleteSeatRequest extends AuthorizationRequest {
    @SerializedName("ID")
    @Expose
    private String seat_id;

    public String getSeat_id() {
        return seat_id;
    }

    public void setSeat_id(String seat_id) {
        this.seat_id = seat_id;
    }
}
