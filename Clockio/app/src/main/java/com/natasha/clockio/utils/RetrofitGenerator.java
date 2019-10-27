package com.natasha.clockio.utils;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//https://futurestud.io/tutorials/retrofit-token-authentication-on-android

public class RetrofitGenerator {

    private static Retrofit retrofit;

//    private static final String BASE_URL = "http://192.168.1.12:8080";
    private static final String BASE_URL = "http://192.168.0.30:8080";


    //    https://stackoverflow.com/questions/45646188/how-can-i-debug-my-retrofit-api-call/45646202
    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(interceptor);

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <S> S createService(Class<S> serviceClass, String clientId, String clientSecret) {
        if (!TextUtils.isEmpty(clientId) && !TextUtils.isEmpty(clientSecret)) {
            final String basic = Credentials.basic(clientId, clientSecret);
            return createService(serviceClass, basic);
        }
        return createService(serviceClass, null, null);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
//            https://stackoverflow.com/questions/41078866/retrofit2-authorisation-with-bearer-token
//            Interceptor interceptor = new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request().newBuilder()
//                            .addHeader("Authorization", "Bearer " + authToken)
//                            .build();
//                    return chain.proceed(request);
//                }
//            };
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceBearer(Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
//            https://stackoverflow.com/questions/41078866/retrofit2-authorisation-with-bearer-token
            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + authToken)
                            .build();
                    return chain.proceed(request);
                }
            };

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }
}
