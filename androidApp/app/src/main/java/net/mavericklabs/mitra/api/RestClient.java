package net.mavericklabs.mitra.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.mavericklabs.mitra.utils.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amoghpalnitkar on 18/11/16.
 */

public class RestClient {
    //private static final String BASE_URL = "http://maamitra.org.in:8000/";
    private static final String BASE_URL = "http://54.152.74.194/";
    private static OkHttpClient client;
    private static Gson gson;

    public static Api getApiService(final String authToken) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);


        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Logger.d("token is : " + authToken);
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("authToken",authToken)
                                .method(original.method(), original.body());
                        Request request = requestBuilder.build();
                        Logger.d("headers : " + request.headers().toString());
                        return chain.proceed(request);
                    }
                })
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .build();

        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = getRetrofitInstance();
        return retrofit.create(Api.class);
    }

    public static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
