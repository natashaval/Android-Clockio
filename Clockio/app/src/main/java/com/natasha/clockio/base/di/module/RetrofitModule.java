package com.natasha.clockio.base.di.module;

import android.util.Log;
import com.natasha.clockio.base.di.scope.ApplicationScope;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Named;

@Module
public class RetrofitModule {
    private static final String TAG = RetrofitModule.class.getSimpleName();

    @Provides
    @ApplicationScope
    Retrofit provideRetrofit(
            @Named("baseUrl") String baseUrl,
            OkHttpClient okHttpClient) {
        Log.d(TAG, "Retrofit provideRetrofit()");
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @ApplicationScope
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    @ApplicationScope
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
