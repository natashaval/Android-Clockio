package com.natasha.clockio.base.util;

import android.text.TextUtils;
import android.util.Log;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

//https://jakewharton.com/making-retrofit-work-for-you/
@Singleton
public class RetrofitInterceptor implements Interceptor {
    private static final String TAG = RetrofitInterceptor.class.getSimpleName();

    private String token = "";
    private String clientId, clientSecret;

    @Inject public RetrofitInterceptor() {
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setBasic(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        if (request.url().encodedPath().equals("/oauth/token")) {
            request = request.newBuilder()
                    .header("Authorization", Credentials.basic(clientId, clientSecret)).build();
            Log.d(TAG, "retrofit add header basic");
        } else if (!TextUtils.isEmpty(token)){
            Log.d(TAG, "retrofit add header token");
            request = request.newBuilder()
                    .addHeader("Authorization", "Bearer "  +token).build();
        } else {
            Log.d(TAG, "retrofit add header NOTHING!");
        }
        return chain.proceed(request);
    }
}
