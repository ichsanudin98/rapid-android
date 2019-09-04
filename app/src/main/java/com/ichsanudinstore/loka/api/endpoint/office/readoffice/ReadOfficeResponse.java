package com.ichsanudinstore.loka.api.endpoint.office.readoffice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;
import com.ichsanudinstore.loka.api.endpoint.office.Office;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 6:24 AM
 */
public class ReadOfficeResponse extends BaseResponse {
    @SerializedName("LT")
    @Expose
    private List<Office> list;

    {
        this.list = new ArrayList<>();
    }

    public List<Office> getList() {
        return list;
    }

    public void setList(List<Office> list) {
        this.list = list;
    }
}
