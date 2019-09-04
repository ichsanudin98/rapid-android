package com.ichsanudinstore.loka.api.endpoint.categoryoffice.readcategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;
import com.ichsanudinstore.loka.api.endpoint.categoryoffice.CategoryOffice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 6:17 AM
 */
public class ReadCategoryResponse extends BaseResponse {
    @SerializedName("LT")
    @Expose
    private List<CategoryOffice> list;

    {
        this.list = new ArrayList<>();
    }

    public List<CategoryOffice> getList() {
        return list;
    }

    public void setList(List<CategoryOffice> list) {
        this.list = list;
    }
}
