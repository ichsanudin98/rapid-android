package com.ichsanudinstore.loka.api.endpoint.profile.readprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.AuthorizationRequest;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 6:13 AM
 */
public class ReadProfileRequest extends AuthorizationRequest {
    @SerializedName("CD")
    @Expose
    private String company_id;

    @SerializedName("OD")
    @Expose
    private String office_id;

    @SerializedName("TY")
    @Expose
    private byte type;

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
