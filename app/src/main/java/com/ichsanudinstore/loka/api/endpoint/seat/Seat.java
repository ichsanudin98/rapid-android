package com.ichsanudinstore.loka.api.endpoint.seat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 6:28 AM
 */
public class Seat {
    @SerializedName("ID")
    @Expose
    private String seat_id;

    @SerializedName("NM")
    @Expose
    private String seat_name;

    @SerializedName("OD")
    @Expose
    private String office_id;

    @SerializedName("ST")
    @Expose
    private Boolean status;

    @SerializedName("NT")
    @Expose
    private String note;

    @SerializedName("KP")
    @Expose
    private String keyphrase;

    @SerializedName("RD")
    @Expose
    private String rent_id;

    @SerializedName("CD")
    @Expose
    private String create_date;

    public String getSeat_id() {
        return seat_id;
    }

    public void setSeat_id(String seat_id) {
        this.seat_id = seat_id;
    }

    public String getSeat_name() {
        return seat_name;
    }

    public void setSeat_name(String seat_name) {
        this.seat_name = seat_name;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getKeyphrase() {
        return keyphrase;
    }

    public void setKeyphrase(String keyphrase) {
        this.keyphrase = keyphrase;
    }

    public String getRent_id() {
        return rent_id;
    }

    public void setRent_id(String rent_id) {
        this.rent_id = rent_id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
}
