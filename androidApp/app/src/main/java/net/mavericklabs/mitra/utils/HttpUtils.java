/*
 *
 *  * ************************************************************************
 *  *
 *  *  MAVERICK LABS CONFIDENTIAL
 *  *  __________________
 *  *
 *  *   [2015] Maverick Labs
 *  *   All Rights Reserved.
 *  *
 *  *  NOTICE:  All information contained herein is, and remains
 *  *  the property of Maverick Labs and its suppliers,
 *  *  if any.  The intellectual and technical concepts contained
 *  *  herein are proprietary to Maverick Labs
 *  *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  *  patents in process, and are protected by trade secret or copyright law.
 *  *  Dissemination of this information or reproduction of this material
 *  *  is strictly forbidden unless prior written permission is obtained
 *  *  from Maverick Labs.
 *  * /
 *
 */

package net.mavericklabs.mitra.utils;

import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.News;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.Content;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by vishakha on 02/12/16.
 */

public class HttpUtils {

    public static Integer getErrorMessage(Response<BaseModel<Content>> response) {
        Converter<ResponseBody, BaseModel> errorConverter =
                RestClient.getRetrofitInstance().responseBodyConverter(BaseModel.class, new Annotation[0]);
        try {
            BaseModel error = errorConverter.convert(response.errorBody());
            Logger.d(" " + error.getResponseMessage());
            return error.getResponseMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Integer getErrorMessageForNews(Response<BaseModel<News>> response) {
        Converter<ResponseBody, BaseModel> errorConverter =
                RestClient.getRetrofitInstance().responseBodyConverter(BaseModel.class, new Annotation[0]);
        try {
            BaseModel error = errorConverter.convert(response.errorBody());
            Logger.d(" " + error.getResponseMessage());
            return error.getResponseMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
