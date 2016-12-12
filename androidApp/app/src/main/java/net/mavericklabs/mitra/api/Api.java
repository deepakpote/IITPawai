package net.mavericklabs.mitra.api;

import net.mavericklabs.mitra.api.model.Attend;
import net.mavericklabs.mitra.api.model.BaseModel;

import net.mavericklabs.mitra.api.model.EditUser;
import net.mavericklabs.mitra.api.model.LoginUser;

import net.mavericklabs.mitra.api.model.ContentDataResponse;
import net.mavericklabs.mitra.api.model.ContentDataRequest;

import net.mavericklabs.mitra.api.model.EventRequest;
import net.mavericklabs.mitra.api.model.SavedContentRequest;
import net.mavericklabs.mitra.api.model.SelfLearningContentRequest;
import net.mavericklabs.mitra.api.model.TeachingAidsContentRequest;
import net.mavericklabs.mitra.api.model.LikeRequest;
import net.mavericklabs.mitra.api.model.Token;

import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.api.model.GenericListDataModel;
import net.mavericklabs.mitra.api.model.NewUser;
import net.mavericklabs.mitra.api.model.RegisterUser;
import net.mavericklabs.mitra.api.model.RegisterUserResponse;
import net.mavericklabs.mitra.api.model.VerifyUserOtp;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.Event;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    @POST("user/updateProfile/")
    Call<BaseModel<GenericListDataModel>> updateUser(@Body EditUser user);

    @GET("code/")
    Call<BaseModel<CommonCode>> getCodeNameList();

    @POST("content/searchTeachingAid/")
    Call<BaseModel<Content>> searchTeachingAids(@Body TeachingAidsContentRequest contentRequest);

    @POST("content/searchSelfLearning/")
    Call<BaseModel<Content>> searchSelfLearning(@Body SelfLearningContentRequest contentRequest);

    @POST("content/like/")
    Call<BaseModel<GenericListDataModel>> likeContent(@Body LikeRequest likeRequest);

    @POST("user/contentSave/")
    @FormUrlEncoded
    Call<BaseModel<GenericListDataModel>> saveContent(@Field("userID") String userId,
                                                      @Field("contentID") String contentId);

    @POST("user/savelanguage/")
    @FormUrlEncoded
    Call<BaseModel<GenericListDataModel>> saveLanguage(@Field("userID") String userId,
                                                       @Field("preferredLanguage") String languageCode);

    @POST("user/detail/")
    @FormUrlEncoded
    Call<BaseModel<LoginUser>> getUserDetails(@Field("userID") String userId);

    @POST("events/attendEvent/")
    Call<BaseModel<GenericListDataModel>> attendEvent(@Body Attend attend);

    @POST("events/listEvents/")
    Call<BaseModel<Event>> listEvents(@Body EventRequest contentRequest);


    @POST("user/contentList/")
    Call<BaseModel<Content>> getSavedContent(@Body SavedContentRequest contentRequest);

    @POST("content/download/")
    Call<BaseModel<ContentDataResponse>> download(@Body ContentDataRequest request);

    @POST("content/share/")
    Call<BaseModel<ContentDataResponse>> share(@Body ContentDataRequest request);
}
