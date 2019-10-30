package com.natasha.clockio.base.di.module;

import android.util.Log;
import com.natasha.clockio.base.di.scope.ApplicationScope;
import com.natasha.clockio.base.util.RetrofitInterceptor;
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

//    https://stackoverflow.com/questions/35949128/dagger2-providing-retrofit-instances-with-different-urls/35949468
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
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor, RetrofitInterceptor retrofitInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(retrofitInterceptor)
                .build();
    }

    @Provides
    @ApplicationScope
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

//    https://stackoverflow.com/questions/54159012/dagger-2-get-old-token-when-token-is-refreshed
    @Provides
    @ApplicationScope
    RetrofitInterceptor provideRetrofitInterceptor() {
        return new RetrofitInterceptor();
    }
}
