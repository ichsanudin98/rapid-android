package com.ichsanudinstore.loka.api.endpoint.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 11:37 PM
 */
public class Profile {
    @SerializedName("ID")
    @Expose
    private String user_id;

    @SerializedName("EM")
    @Expose
    private String email;

    @SerializedName("NM")
    @Expose
    private String name;

    @SerializedName("IG")
    @Expose
    private String image;

    @SerializedName("AR")
    @Expose
    private String address;

    @SerializedName("PN")
    @Expose
    private String phone;

    @SerializedName("GD")
    @Expose
    private Byte gender;

    @SerializedName("OD")
    @Expose
    private String office_id;

    @SerializedName("ON")
    @Expose
    private String office_name;

    @SerializedName("AD")
    @Expose
    private Boolean activated;

    @SerializedName("CD")
    @Expose
    private String company_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

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

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }
}
