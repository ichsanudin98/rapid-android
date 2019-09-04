package com.ichsanudinstore.loka.api.endpoint.categoryoffice.createupdatecategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.AuthorizationRequest;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 6:20 AM
 */
public class CreateUpdateCategoryRequest extends AuthorizationRequest {
    @SerializedName("ID")
    @Expose
    private String category_id;

    @SerializedName("NM")
    @Expose
    private String category_name;

    @SerializedName("TY")
    @Expose
    private byte type;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
