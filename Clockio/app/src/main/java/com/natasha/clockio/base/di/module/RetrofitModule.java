package com.natasha.clockio.base.di.module;

import android.util.Log;
import com.natasha.clockio.base.di.constant.UrlConstantKt;
import com.natasha.clockio.base.di.qualifier.ApplicationContext;
import com.natasha.clockio.base.di.scope.ApplicationScope;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {
    private static final String TAG = RetrofitModule.class.getSimpleName();
    public String BASE_URL;
    private Retrofit retrofit;

    public RetrofitModule(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    @Provides
    @ApplicationScope
    Retrofit provideRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        Log.d(TAG, "Retrofit provideRetrofit()");
        return retrofit;
    }

//    @Provides
//    @ApplicationScope
//    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
//        Log.d(TAG, "Retrofit provideRetrofit()OKHTTP");
//        return new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(okHttpClient)
//                .build();
//    }

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
