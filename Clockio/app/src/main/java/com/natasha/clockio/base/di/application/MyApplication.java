package com.natasha.clockio.base.di.application;

import android.app.Application;
import android.util.Log;

import com.cloudinary.android.MediaManager;
import com.natasha.clockio.base.constant.UrlConst;
import com.natasha.clockio.base.di.component.ApplicationComponent;
import com.natasha.clockio.base.di.component.DaggerApplicationComponent;
import com.natasha.clockio.base.di.factory.DaggerWorkerFactory;

import androidx.work.Configuration;
import androidx.work.WorkManager;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

import javax.inject.Inject;

public class MyApplication extends Application implements HasAndroidInjector {

    private static final String TAG = MyApplication.class.getSimpleName();
    ApplicationComponent applicationComponent;

    @Inject
    DispatchingAndroidInjector<Object> dispatchingAndroidInjector;

//    @Inject
//    DaggerWorkerFactory factory;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .contextModule(this)
                .retrofitModule(UrlConst.BASE_URL)
                .build();

        MediaManager.init(this);

        DaggerWorkerFactory factory = applicationComponent.factory();
        WorkManager.initialize(this, new Configuration.Builder().setWorkerFactory(factory).build());

        applicationComponent.injectApplication(this);
        Log.d(TAG, "application component init on create");
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return dispatchingAndroidInjector;
    }
}
