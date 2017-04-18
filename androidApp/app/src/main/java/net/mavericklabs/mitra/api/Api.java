package net.mavericklabs.mitra.api;

import net.mavericklabs.mitra.model.Chapter;
import net.mavericklabs.mitra.model.api.Attend;
import net.mavericklabs.mitra.model.api.BaseModel;

import net.mavericklabs.mitra.model.api.EditPhoto;
import net.mavericklabs.mitra.model.api.EditUser;
import net.mavericklabs.mitra.model.api.LoginUser;

import net.mavericklabs.mitra.model.api.ContentDataResponse;
import net.mavericklabs.mitra.model.api.ContentDataRequest;

import net.mavericklabs.mitra.model.api.EventRequest;

import net.mavericklabs.mitra.model.api.MetaContent;
import net.mavericklabs.mitra.model.News;

import net.mavericklabs.mitra.model.api.SavedSelfLearningRequest;
import net.mavericklabs.mitra.model.api.SavedTeachingAidsRequest;
import net.mavericklabs.mitra.model.api.SelfLearningContentRequest;
import net.mavericklabs.mitra.model.api.TeachingAidsContentRequest;
import net.mavericklabs.mitra.model.api.LikeRequest;
import net.mavericklabs.mitra.model.api.Token;

import net.mavericklabs.mitra.model.api.GenericListDataModel;
import net.mavericklabs.mitra.model.api.NewUser;
import net.mavericklabs.mitra.model.api.RegisterUser;
import net.mavericklabs.mitra.model.api.RegisterUserResponse;
import net.mavericklabs.mitra.model.api.VerifyUserOtp;
import net.mavericklabs.mitra.model.CommonCodeWrapper;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.Event;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by amoghpalnitkar on 18/11/16.
 */

public interface Api {

    //Authenticated calls.

    @POST("content/searchTeachingAid/")
    Call<BaseModel<Content>> searchTeachingAids(@Header("appLanguageCodeID") int appLanguageCodeID,
                                                @Body TeachingAidsContentRequest contentRequest);

    @POST("content/searchSelfLearning/")
    Call<BaseModel<Content>> searchSelfLearning(@Header("appLanguageCodeID") int appLanguageCodeID,
                                                @Body SelfLearningContentRequest contentRequest);

    @POST("content/like/")
    Call<BaseModel<GenericListDataModel>> likeContent(@Body LikeRequest likeRequest);

    @POST("content/download/")
    Call<BaseModel<ContentDataResponse>> download(@Body ContentDataRequest request);

    @POST("content/share/")
    Call<BaseModel<ContentDataResponse>> share(@Body ContentDataRequest request);

    @POST("content/getContentResponse/")
    @FormUrlEncoded
    Call<BaseModel<MetaContent>> metaContent(@Field("contentID") String contentId);

    @POST("events/attendEvent/")
    Call<BaseModel<GenericListDataModel>> attendEvent(@Body Attend attend);

    @POST("user/updateProfile/")
    Call<BaseModel<GenericListDataModel>> updateUser(@Body EditUser user);

    @POST("user/detail/")
    Call<BaseModel<LoginUser>> getUserDetails();

    @POST("user/contentSave/")
    @FormUrlEncoded
    Call<BaseModel<GenericListDataModel>> saveContent(@Field("contentID") String contentId,
                                                      @Field("saveContent") boolean value);

    @POST("user/contentList/")
    Call<BaseModel<Content>> getSavedTeachingAids(@Header("appLanguageCodeID") int appLanguageCodeID,
                                                  @Body SavedTeachingAidsRequest contentRequest);

    @POST("user/contentList/")
    Call<BaseModel<Content>> getSavedSelfLearning(@Header("appLanguageCodeID") int appLanguageCodeID,
                                                  @Body SavedSelfLearningRequest contentRequest);

    @POST("user/saveLanguage/")
    @FormUrlEncoded
    Call<BaseModel<GenericListDataModel>> saveLanguage(@Field("preferredLanguageCodeID") Integer languageCode);

    @POST("user/saveUserPhoto/")
    Call<BaseModel<GenericListDataModel>> savePhoto(@Body EditPhoto photo);

    //Un-authenticated calls.

    @POST("user/requestOtp/")
    Call<BaseModel<GenericListDataModel>> requestOtp(@Body NewUser user);

    @POST("user/verifyOtp/")
    Call<BaseModel<Token>> verifyOtp(@Body VerifyUserOtp verifyUserOtp);

    @POST("user/register/")
    Call<BaseModel<RegisterUserResponse>> registerUser(@Body RegisterUser user);

    @GET("code/")
    Call<BaseModel<CommonCodeWrapper>> getCodeNameList(@Query("version") String version);


    @POST("events/listEvents/")
    Call<BaseModel<Event>> listEvents(@Body EventRequest contentRequest);

    @POST("news/newsList/")
    Call<BaseModel<News>> listNews(@Header("appLanguageCodeID") int appLanguageCodeID);

    @POST("content/chapterList/")
    @FormUrlEncoded
    Call<BaseModel<Chapter>> getChapters(@Field("subjectCodeID") String subjectCodeID,
                                         @Field("gradeCodeID") String gradeCodeID);

}
