package com.natasha.clockio.base.di.module;

import com.natasha.clockio.MainActivity;
import com.natasha.clockio.base.di.module.activity.MainActivityModule;
import com.natasha.clockio.base.di.scope.ActivityScope;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BuilderModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = {MainActivityModule.class})
    abstract MainActivity bindMainActivity();
}
