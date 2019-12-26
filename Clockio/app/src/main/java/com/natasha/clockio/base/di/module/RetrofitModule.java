package com.natasha.clockio.base.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.natasha.clockio.base.di.scope.ApplicationScope;
import com.natasha.clockio.base.util.LiveDataCallAdapterFactory;
import com.natasha.clockio.base.util.RedirectInterceptor;
import com.natasha.clockio.base.util.RetrofitInterceptor;

import java.util.Date;
import java.util.UUID;

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
            OkHttpClient okHttpClient,
            Gson gson) {
        Log.d(TAG, "Retrofit provideRetrofit()");
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @ApplicationScope
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor,
                                     RetrofitInterceptor retrofitInterceptor,
                                     RedirectInterceptor redirectInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(redirectInterceptor)
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
    RetrofitInterceptor provideRetrofitInterceptor(SharedPreferences sharedPreferences) {
        return new RetrofitInterceptor(sharedPreferences);
    }

    @Provides
    @ApplicationScope
    RedirectInterceptor provideRedirectInterceptor(Context context) {
        return new RedirectInterceptor(context);
    }

//    https://stackoverflow.com/questions/41979086/how-to-serialize-date-to-long-using-gson
//    https://stackoverflow.com/questions/5671373/unparseable-date-1302828677828-trying-to-deserialize-with-gson-a-millisecond/8960644
    @Provides
    @ApplicationScope
    Gson provideGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOf, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
                .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, jsonSerializationContext) -> new JsonPrimitive(date.getTime()))
                .registerTypeAdapter(UUID.class, (JsonDeserializer<String>) (json, typeOf, context) -> json.getAsJsonPrimitive().getAsString())
                .create();
        return gson;
    }
}
