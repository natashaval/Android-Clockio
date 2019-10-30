package com.natasha.clockio.base.di.module.activity;

import com.natasha.clockio.MainActivity;
import com.natasha.clockio.base.di.scope.ActivityScope;
import dagger.Binds;
import dagger.Module;

//https://stackoverflow.com/questions/48081881/dagger-2-not-injecting-sharedpreference
@Module
public abstract class MainActivityModule {
    @Binds
    @ActivityScope
    abstract MainActivity provideMainActivity(MainActivity mainActivity);
}
