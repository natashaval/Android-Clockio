package com.natasha.clockio.base.di.module;

import android.content.Context;
import com.natasha.clockio.MainActivity;
import com.natasha.clockio.base.di.qualifier.ActivityContext;
import com.natasha.clockio.base.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityContextModule {
    private MainActivity mainActivity;

    public Context context;

    public MainActivityContextModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        context = mainActivity;
    }

    @Provides
    @ActivityScope
    public MainActivity providesMainActivity() {
        return mainActivity;
    }

    @Provides
    @ActivityScope
    @ActivityContext
    public Context provideContext() {
        return context;
    }
}
