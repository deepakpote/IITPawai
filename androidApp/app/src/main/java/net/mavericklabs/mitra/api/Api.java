package net.mavericklabs.mitra.api;

import net.mavericklabs.mitra.api.model.Attend;
import net.mavericklabs.mitra.api.model.BaseModel;

import net.mavericklabs.mitra.api.model.EditPhoto;
import net.mavericklabs.mitra.api.model.EditUser;
import net.mavericklabs.mitra.api.model.LoginUser;

import net.mavericklabs.mitra.api.model.ContentDataResponse;
import net.mavericklabs.mitra.api.model.ContentDataRequest;

import net.mavericklabs.mitra.api.model.EventRequest;

import net.mavericklabs.mitra.api.model.MetaContent;
import net.mavericklabs.mitra.api.model.News;

import net.mavericklabs.mitra.api.model.SavedSelfLearningRequest;
import net.mavericklabs.mitra.api.model.SavedTeachingAidsRequest;
import net.mavericklabs.mitra.api.model.SelfLearningContentRequest;
import net.mavericklabs.mitra.api.model.TeachingAidsContentRequest;
import net.mavericklabs.mitra.api.model.LikeRequest;
import net.mavericklabs.mitra.api.model.Token;

import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.api.model.GenericListDataModel;
import net.mavericklabs.mitra.api.model.NewUser;
import net.mavericklabs.mitra.api.model.RegisterUser;
import net.mavericklabs.mitra.api.model.RegisterUserResponse;
import net.mavericklabs.mitra.api.model.VerifyUserOtp;
import net.mavericklabs.mitra.model.CommonCodeWrapper;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.Event;

import java.io.File;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by amoghpalnitkar on 18/11/16.
 */

public interface Api {

    @POST("content/searchTeachingAid/")
    Call<BaseModel<Content>> searchTeachingAids(@Body TeachingAidsContentRequest contentRequest);

    @POST("content/searchSelfLearning/")
    Call<BaseModel<Content>> searchSelfLearning(@Body SelfLearningContentRequest contentRequest);

    @POST("content/like/")
    Call<BaseModel<GenericListDataModel>> likeContent(@Body LikeRequest likeRequest);

    @POST("content/download/")
    Call<BaseModel<ContentDataResponse>> download(@Body ContentDataRequest request);

    @POST("content/share/")
    Call<BaseModel<ContentDataResponse>> share(@Body ContentDataRequest request);

    @POST("content/getContentResponse/")
    @FormUrlEncoded
    Call<BaseModel<MetaContent>> metaContent(@Field("contentID") String contentId);

    //---------------------------------------------------------------------------------

    @POST("user/requestOtp/")
    Call<BaseModel<GenericListDataModel>> requestOtp(@Body NewUser user);

    @POST("user/verifyOtp/")
    Call<BaseModel<Token>> verifyOtp(@Body VerifyUserOtp verifyUserOtp);

    @POST("user/register/")
    Call<BaseModel<RegisterUserResponse>> registerUser(@Body RegisterUser user);

    @POST("user/updateProfile/")
    Call<BaseModel<GenericListDataModel>> updateUser(@Body EditUser user);

    @GET("code/")
    Call<BaseModel<CommonCodeWrapper>> getCodeNameList(@Query("version") String version);

    @POST("user/contentSave/")
    @FormUrlEncoded
    Call<BaseModel<GenericListDataModel>> saveContent(@Field("userID") String userId,
                                                      @Field("contentID") String contentId,
                                                      @Field("saveContent") boolean value);

    @POST("user/saveLanguage/")
    @FormUrlEncoded
    Call<BaseModel<GenericListDataModel>> saveLanguage(@Field("userID") String userId,
                                                       @Field("preferredLanguageCodeID") Integer languageCode);

    @POST("user/detail/")
    @FormUrlEncoded
    Call<BaseModel<LoginUser>> getUserDetails(@Field("userID") String userId);

    @POST("events/attendEvent/")
    Call<BaseModel<GenericListDataModel>> attendEvent(@Body Attend attend);

    @POST("events/listEvents/")
    Call<BaseModel<Event>> listEvents(@Body EventRequest contentRequest);

    @POST("user/contentList/")
    Call<BaseModel<Content>> getSavedTeachingAids(@Body SavedTeachingAidsRequest contentRequest);

    @POST("user/contentList/")
    Call<BaseModel<Content>> getSavedSelfLearning(@Body SavedSelfLearningRequest contentRequest);

    @GET("news/")
    Call<BaseModel<News>> listNews();

    @POST("user/saveUserPhoto/")
    Call<BaseModel<GenericListDataModel>> savePhoto(@Body EditPhoto photo);
}
