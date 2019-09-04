package com.ichsanudinstore.loka.api.endpoint.profile.readprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;
import com.ichsanudinstore.loka.api.endpoint.profile.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 6:09 AM
 */
public class ReadProfileResponse extends BaseResponse {
    @SerializedName("LT")
    @Expose
    private List<Profile> list;

    {
        this.list = new ArrayList<>();
    }

    public List<Profile> getList() {
        return list;
    }

    public void setList(List<Profile> list) {
        this.list = list;
    }
}
