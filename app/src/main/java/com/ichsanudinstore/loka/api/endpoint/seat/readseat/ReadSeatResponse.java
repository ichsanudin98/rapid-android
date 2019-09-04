package com.ichsanudinstore.loka.api.endpoint.seat.readseat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;
import com.ichsanudinstore.loka.api.endpoint.seat.Seat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 1:55 PM
 */
public class ReadSeatResponse extends BaseResponse {
    @SerializedName("LT")
    @Expose
    private List<Seat> list;

    {
        this.list = new ArrayList<>();
    }

    public List<Seat> getList() {
        return list;
    }

    public void setList(List<Seat> list) {
        this.list = list;
    }
}
