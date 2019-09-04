package com.ichsanudinstore.loka.api.endpoint.profile.createupdateprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ichsanudinstore.loka.api.endpoint.AuthorizationRequest;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 6:00 AM
 */
public class CreateUpdateProfileRequest extends AuthorizationRequest {
    @SerializedName("ID")
    @Expose
    private String user_id;

    @SerializedName("EM")
    @Expose
    private String email;

    @SerializedName("PW")
    @Expose
    private String password;

    @SerializedName("NM")
    @Expose
    private String name;

    @SerializedName("IG")
    @Expose
    private String image;

    @SerializedName("AR")
    @Expose
    private String address;

    @SerializedName("GD")
    @Expose
    private Byte gender;

    @SerializedName("PN")
    @Expose
    private String phone;

    @SerializedName("OD")
    @Expose
    private String office_id;

    @SerializedName("BN")
    @Expose
    private String business_name;

    @SerializedName("BR")
    @Expose
    private String business_address;

    @SerializedName("BP")
    @Expose
    private String business_phone;

    @SerializedName("TY")
    @Expose
    private Byte type;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBusiness_address() {
        return business_address;
    }

    public void setBusiness_address(String business_address) {
        this.business_address = business_address;
    }

    public String getBusiness_phone() {
        return business_phone;
    }

    public void setBusiness_phone(String business_phone) {
        this.business_phone = business_phone;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
