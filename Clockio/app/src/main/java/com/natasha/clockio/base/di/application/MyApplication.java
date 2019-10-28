package com.natasha.clockio.base.di.application;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import com.natasha.clockio.base.di.component.ApplicationComponent;
import com.natasha.clockio.base.di.component.DaggerApplicationComponent;
import com.natasha.clockio.base.di.constant.UrlConstantKt;
import com.natasha.clockio.base.di.module.ContextModule;
import com.natasha.clockio.base.di.module.RetrofitModule;

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();
    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .contextModule(new ContextModule(this))
                .retrofitModule(new RetrofitModule(UrlConstantKt.BASE_URL))
                .build();

        applicationComponent.injectApplication(this);
        Log.d(TAG, "application component init on create");
    }

    public static MyApplication get(Activity activity) {
        return (MyApplication) activity.getApplication();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
