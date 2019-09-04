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
import com.ichsanudinstore.loka.config.Constant;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 3:28 PM
 */
public interface RestParser {
    @POST(Constant.URL.LOGIN)
    Call<LoginResponse> sendLogin(@Body LoginRequest data);

    @POST(Constant.URL.CREATE_UPDATE_PROFILE)
    Call<BaseResponse> sendCreateUpdateProfile(@Body CreateUpdateProfileRequest data);

    @POST(Constant.URL.UPDATE_PASSWORD)
    Call<BaseResponse> sendUpdatePassword(@Body UpdatePasswordRequest data);

    @POST(Constant.URL.FORGET_PASSWORD)
    Call<BaseResponse> sendForgetPassword(@Body ForgetPasswordRequest data);

    @POST(Constant.URL.DELETE_PROFILE)
    Call<BaseResponse> sendDeleteProfile(@Body DeleteProfileRequest data);

    @POST(Constant.URL.READ_PROFILE)
    Call<ReadProfileResponse> getProfile(@Body ReadProfileRequest data);

    @POST(Constant.URL.ACTIVATION_PROFILE)
    Call<BaseResponse> sendActivationProfile(@Body UpdateProfileActivationRequest data);

    @POST(Constant.URL.READ_CATEGORY)
    Call<ReadCategoryResponse> getCategory(@Body ReadCategoryRequest data);

    @POST(Constant.URL.DELETE_CATEGORY)
    Call<BaseResponse> sendDeleteCategory(@Body DeleteCategoryRequest data);

    @POST(Constant.URL.CREATE_UPDATE_CATEGORY)
    Call<BaseResponse> sendCreateUpdateCategory(@Body CreateUpdateCategoryRequest data);

    @POST(Constant.URL.CREATE_UPDATE_OFFICE)
    Call<BaseResponse> sendCreateUpdateOffice(@Body CreateUpdateOfficeRequest data);

    @POST(Constant.URL.READ_OFFICE)
    Call<ReadOfficeResponse> getOffice(@Body AuthorizationRequest data);

    @POST(Constant.URL.DELETE_OFFICE)
    Call<BaseResponse> sendDeleteOffice(@Body DeleteOfficeRequest data);

    @POST(Constant.URL.CREATE_UPDATE_SEAT)
    Call<BaseResponse> sendCreateUpdateSeat(@Body CreateUpdateSeatRequest data);

    @POST(Constant.URL.READ_SEAT)
    Call<ReadSeatResponse> getSeat(@Body ReadSeatRequest data);

    @POST(Constant.URL.DELETE_SEAT)
    Call<BaseResponse> sendDeleteSeat(@Body DeleteSeatRequest data);
}
