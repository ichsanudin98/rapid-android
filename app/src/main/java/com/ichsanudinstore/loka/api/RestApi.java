package com.ichsanudinstore.loka.api;

import com.ichsanudinstore.loka.api.endpoint.AuthorizationRequest;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;
import com.ichsanudinstore.loka.api.endpoint.categoryoffice.createupdatecategory.CreateUpdateCategoryRequest;
import com.ichsanudinstore.loka.api.endpoint.categoryoffice.deletecategory.DeleteCategoryRequest;
import com.ichsanudinstore.loka.api.endpoint.categoryoffice.readcategory.ReadCategoryRequest;
import com.ichsanudinstore.loka.api.endpoint.categoryoffice.readcategory.ReadCategoryResponse;
import com.ichsanudinstore.loka.api.endpoint.office.createupdateoffice.CreateUpdateOfficeRequest;
import com.ichsanudinstore.loka.api.endpoint.office.deleteoffice.DeleteOfficeRequest;
import com.ichsanudinstore.loka.api.endpoint.office.readoffice.ReadOfficeResponse;
import com.ichsanudinstore.loka.api.endpoint.profile.createupdateprofile.CreateUpdateProfileRequest;
import com.ichsanudinstore.loka.api.endpoint.profile.deleteprofile.DeleteProfileRequest;
import com.ichsanudinstore.loka.api.endpoint.profile.forgetpassword.ForgetPasswordRequest;
import com.ichsanudinstore.loka.api.endpoint.profile.login.LoginRequest;
import com.ichsanudinstore.loka.api.endpoint.profile.login.LoginResponse;
import com.ichsanudinstore.loka.api.endpoint.profile.readprofile.ReadProfileRequest;
import com.ichsanudinstore.loka.api.endpoint.profile.readprofile.ReadProfileResponse;
import com.ichsanudinstore.loka.api.endpoint.profile.updatepassword.UpdatePasswordRequest;
import com.ichsanudinstore.loka.api.endpoint.profile.updateprofileactivation.UpdateProfileActivationRequest;
import com.ichsanudinstore.loka.api.endpoint.seat.createupdateseat.CreateUpdateSeatRequest;
import com.ichsanudinstore.loka.api.endpoint.seat.deleteseat.DeleteSeatRequest;
import com.ichsanudinstore.loka.api.endpoint.seat.readseat.ReadSeatRequest;
import com.ichsanudinstore.loka.api.endpoint.seat.readseat.ReadSeatResponse;

import retrofit2.Call;

/**
 * @author Ichsanudin_Chairin
 * @since Thursday 8/22/2019 10:19 PM
 */
public class RestApi {

    public static Call<LoginResponse> sendLogin(
            String timestamp,
            String security_code,
            String email,
            String password
    ) {
        LoginRequest data = new LoginRequest();
        data.setEmail(email);
        data.setPassword(password);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .sendLogin(data);

    }

    public static Call<BaseResponse> sendCreateUpdateProfile(
            String timestamp,
            String security_code,
            String session_id,
            String user_id,
            String email,
            String password,
            String name,
            String image,
            String address,
            Byte gender,
            String phone,
            String office_id,
            String business_name,
            String business_address,
            String business_phone,
            byte type
    ) {
        CreateUpdateProfileRequest data = new CreateUpdateProfileRequest();
        if (type == 0) {
            // TODO Create
            if (office_id != null) {
                data.setOffice_id(office_id);
                data.setSession(session_id);
            }
        } else {
            data.setUser_id(user_id);
            data.setSession(session_id);
            if (office_id != null) {
                data.setOffice_id(office_id);
            }
        }
        data.setTimestamp(timestamp);
        data.setSecurityCode(security_code);
        data.setEmail(email);
        if (password != null)
            data.setPassword(password);
        data.setName(name);
        if (image != null)
            data.setImage(image);
        data.setAddress(address);
        data.setGender(gender);
        data.setPhone(phone);
        if (business_name != null)
            data.setBusiness_name(business_name);
        if (business_address != null)
            data.setBusiness_address(business_address);
        if (business_phone != null)
            data.setBusiness_phone(business_phone);
        data.setType(type);


        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .sendCreateUpdateProfile(data);

    }

    public static Call<BaseResponse> sendUpdatePassword(
            String timestamp,
            String security_code,
            String session_id,
            String phone_number,
            String old_password,
            String new_password
    ) {
        UpdatePasswordRequest data = new UpdatePasswordRequest();
        data.setSession(session_id);
        data.setPhone(phone_number);
        data.setOld_password(old_password);
        data.setNew_password(new_password);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .sendUpdatePassword(data);

    }

    public static Call<BaseResponse> sendForgetPassword(
            String timestamp,
            String security_code,
            String email,
            String phone_number,
            String new_password
    ) {
        ForgetPasswordRequest data = new ForgetPasswordRequest();
        data.setEmail(email);
        data.setPhone(phone_number);
        data.setNew_password(new_password);
        data.setNew_password(new_password);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .sendForgetPassword(data);

    }

    public static Call<BaseResponse> sendDeleteProfile(
            String timestamp,
            String security_code,
            String session_id,
            String user_id
    ) {
        DeleteProfileRequest data = new DeleteProfileRequest();
        data.setSession(session_id);
        data.setUser_id(user_id);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .sendDeleteProfile(data);

    }

    public static Call<ReadProfileResponse> getProfile(
            String timestamp,
            String security_code,
            String session_id,
            String company_id,
            String office_id,
            byte type
    ) {
        ReadProfileRequest data = new ReadProfileRequest();
        data.setSession(session_id);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);
        data.setCompany_id(company_id);
        data.setOffice_id(office_id);
        data.setType(type);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .getProfile(data);

    }

    public static Call<BaseResponse> sendActivationProfile(
            String timestamp,
            String security_code,
            String session_id,
            String user_id,
            Boolean activated
    ) {
        UpdateProfileActivationRequest data = new UpdateProfileActivationRequest();
        data.setSession(session_id);
        data.setUser_id(user_id);
        data.setActivated(activated);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .sendActivationProfile(data);

    }

    public static Call<ReadCategoryResponse> getCategory(
            String timestamp,
            String security_code,
            String session_id,
            String company_id
    ) {
        ReadCategoryRequest data = new ReadCategoryRequest();
        data.setSession(session_id);
        data.setCompany_id(company_id);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .getCategory(data);

    }

    public static Call<BaseResponse> sendDeleteCategory(
            String timestamp,
            String security_code,
            String session_id,
            String category_id
    ) {
        DeleteCategoryRequest data = new DeleteCategoryRequest();
        data.setSession(session_id);
        data.setCategory_id(category_id);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .sendDeleteCategory(data);

    }

    public static Call<BaseResponse> sendCreateUpdateCategory(
            String timestamp,
            String security_code,
            String session_id,
            String category_id,
            String category_name,
            byte type
    ) {
        CreateUpdateCategoryRequest data = new CreateUpdateCategoryRequest();
        if (type == 1)
            data.setCategory_id(category_id);

        data.setSession(session_id);
        data.setCategory_name(category_name);
        data.setType(type);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .sendCreateUpdateCategory(data);

    }

    public static Call<BaseResponse> sendCreateUpdateOffice(
            String timestamp,
            String security_code,
            String session_id,
            String office_id,
            String office_name,
            String office_image,
            String office_address,
            String office_phone,
            String office_latitude,
            String office_longitude,
            String total_seat,
            String category_id,
            byte type
    ) {
        CreateUpdateOfficeRequest data = new CreateUpdateOfficeRequest();
        if (type == 1)
            data.setOffice_id(office_id);

        data.setSession(session_id);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);
        data.setOffice_name(office_name);
        data.setOffice_image(office_image);
        data.setOffice_address(office_address);
        data.setOffice_phone(office_phone);
        data.setOffice_latitude(office_latitude);
        data.setOffice_latitude(office_longitude);
        data.setTotal_seat(total_seat);
        data.setCategory_id(category_id);
        data.setType(type);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .sendCreateUpdateOffice(data);

    }

    public static Call<ReadOfficeResponse> getOffice(
            String timestamp,
            String security_code,
            String session_id
    ) {
        AuthorizationRequest data = new AuthorizationRequest();
        data.setSession(session_id);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .getOffice(data);

    }

    public static Call<BaseResponse> sendDeleteOffice(
            String timestamp,
            String security_code,
            String session_id,
            String office_id
    ) {
        DeleteOfficeRequest data = new DeleteOfficeRequest();
        data.setSession(session_id);
        data.setOffice_id(office_id);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .sendDeleteOffice(data);

    }

    public static Call<BaseResponse> sendCreateUpdateSeat(
            String timestamp,
            String security_code,
            String session_id,
            String seat_id,
            String seat_name,
            String office_id,
            Boolean status,
            byte type
    ) {
        CreateUpdateSeatRequest data = new CreateUpdateSeatRequest();
        if (type == 1)
            data.setSeat_id(seat_id);

        data.setSession(session_id);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);
        data.setSeat_name(seat_name);
        data.setOffice_id(office_id);
        data.setStatus(status);
        data.setType(type);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .sendCreateUpdateSeat(data);

    }

    public static Call<ReadSeatResponse> getSeat(
            String timestamp,
            String security_code,
            String session_id
    ) {
        ReadSeatRequest data = new ReadSeatRequest();
        data.setSession(session_id);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);
        data.setType((byte) 0);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .getSeat(data);

    }

    public static Call<BaseResponse> sendDeleteSeat(
            String timestamp,
            String security_code,
            String session_id,
            String seat_id
    ) {
        DeleteSeatRequest data = new DeleteSeatRequest();
        data.setSession(session_id);
        data.setSeat_id(seat_id);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .sendDeleteSeat(data);

    }

    public static Call<BaseResponse> sendCreateUpdateRent(
            String timestamp,
            String security_code,
            String session_id,
            String seat_id,
            String seat_name,
            String office_id,
            Boolean status,
            String note,
            String keyphrase
    ) {
        CreateUpdateSeatRequest data = new CreateUpdateSeatRequest();
        data.setSeat_id(seat_id);
        data.setSession(session_id);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);
        data.setSeat_name(seat_name);
        data.setOffice_id(office_id);
        data.setStatus(status);
        data.setNote(note);
        data.setKeyphrase(keyphrase);
        data.setType((byte) 2);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .sendCreateUpdateSeat(data);

    }

    public static Call<ReadSeatResponse> getRent(
            String timestamp,
            String security_code,
            String session_id
    ) {
        ReadSeatRequest data = new ReadSeatRequest();
        data.setSession(session_id);
        data.setSecurityCode(security_code);
        data.setTimestamp(timestamp);
        data.setType((byte) 1);

        return RestManager
                .GET_RETROFIT((byte) 1)
                .create(RestParser.class)
                .getSeat(data);

    }
}
