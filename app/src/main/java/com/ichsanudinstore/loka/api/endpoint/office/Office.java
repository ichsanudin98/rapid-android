package com.ichsanudinstore.loka.api.endpoint.office;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 6:24 AM
 */
public class Office {
    @SerializedName("ID")
    @Expose
    private String office_id;

    @SerializedName("NM")
    @Expose
    private String office_name;

    @SerializedName("IG")
    @Expose
    private String office_image;

    @SerializedName("OD")
    @Expose
    private String office_address;

    @SerializedName("ON")
    @Expose
    private String office_phone;

    @SerializedName("OT")
    @Expose
    private String office_latitude;

    @SerializedName("OG")
    @Expose
    private String office_longitude;

    @SerializedName("LS")
    @Expose
    private String total_seat;

    @SerializedName("CD")
    @Expose
    private String category_id;

    @SerializedName("CN")
    @Expose
    private String category_name;

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getOffice_name() {
        return office_name;
    }

    public void setOffice_name(String office_name) {
        this.office_name = office_name;
    }

    public String getOffice_image() {
        return office_image;
    }

    public void setOffice_image(String office_image) {
        this.office_image = office_image;
    }

    public String getOffice_address() {
        return office_address;
    }

    public void setOffice_address(String office_address) {
        this.office_address = office_address;
    }

    public String getOffice_phone() {
        return office_phone;
    }

    public void setOffice_phone(String office_phone) {
        this.office_phone = office_phone;
    }

    public String getOffice_latitude() {
        return office_latitude;
    }

    public void setOffice_latitude(String office_latitude) {
        this.office_latitude = office_latitude;
    }

    public String getOffice_longitude() {
        return office_longitude;
    }

    public void setOffice_longitude(String office_longitude) {
        this.office_longitude = office_longitude;
    }

    public String getTotal_seat() {
        return total_seat;
    }

    public void setTotal_seat(String total_seat) {
        this.total_seat = total_seat;
    }

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
}
