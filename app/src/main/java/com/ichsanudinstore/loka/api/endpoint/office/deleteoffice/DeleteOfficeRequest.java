package com.ichsanudinstore.loka.api.endpoint.office.deleteoffice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.AuthorizationRequest;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 6:28 AM
 */
public class DeleteOfficeRequest extends AuthorizationRequest {
    @SerializedName("ID")
    @Expose
    private String office_id;

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }
}
