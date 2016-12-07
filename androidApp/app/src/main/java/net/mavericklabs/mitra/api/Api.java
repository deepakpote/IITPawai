package net.mavericklabs.mitra.api;

import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.ContentRequest;
import net.mavericklabs.mitra.api.model.LikeRequest;
import net.mavericklabs.mitra.api.model.Token;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.api.model.GenericListDataModel;
import net.mavericklabs.mitra.api.model.NewUser;
import net.mavericklabs.mitra.api.model.RegisterUser;
import net.mavericklabs.mitra.api.model.RegisterUserResponse;
import net.mavericklabs.mitra.api.model.VerifyUserOtp;
import net.mavericklabs.mitra.model.Content;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by amoghpalnitkar on 18/11/16.
 */

public interface Api {

    @POST("user/requestOtp/")
    Call<BaseModel<GenericListDataModel>> requestOtp(@Body NewUser user);

    @POST("user/verifyOtp/")
    Call<BaseModel<Token>> verifyOtp(@Body VerifyUserOtp verifyUserOtp);

    @POST("user/register/")
    Call<BaseModel<RegisterUserResponse>> registerUser(@Body RegisterUser user);

    @POST("user/login/")
    Call<BaseModel<GenericListDataModel>> loginUser(@Field("phoneNumber") String phoneNumber,
                                                    @Field("token") String token);

    @GET("code/")
    Call<BaseModel<CommonCode>> getCodeNameList();

    @POST("content/searchTeachingAid/")
    Call<BaseModel<Content>> searchTeachingAids(@Body ContentRequest contentRequest);

    @POST("content/like/")
    Call<BaseModel<GenericListDataModel>> likeContent(@Body LikeRequest likeRequest);
}
