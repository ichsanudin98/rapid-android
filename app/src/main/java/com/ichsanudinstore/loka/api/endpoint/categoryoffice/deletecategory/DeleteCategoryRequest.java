package com.ichsanudinstore.loka.api.endpoint.categoryoffice.deletecategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.AuthorizationRequest;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 6:20 AM
 */
public class DeleteCategoryRequest extends AuthorizationRequest {
    @SerializedName("ID")
    @Expose
    private String category_id;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
