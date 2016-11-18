package net.mavericklabs.mitra.api;

import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.GenericListDataModel;
import net.mavericklabs.mitra.api.model.NewUser;
import net.mavericklabs.mitra.api.model.RegisterUser;
import net.mavericklabs.mitra.api.model.RegisterUserResponse;
import net.mavericklabs.mitra.api.model.VerifyUserOtp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by root on 18/11/16.
 */

public interface Api {

    @POST("users/requestotp/")
    Call<BaseModel<GenericListDataModel>> requestOtp(@Body NewUser user);

    @POST("users/verifyotp/")
    Call<BaseModel<GenericListDataModel>> verifyOtp(@Body VerifyUserOtp verifyUserOtp);

    @POST("users/register/")
    Call<BaseModel<RegisterUserResponse>> registerUser(@Body RegisterUser user);
}
