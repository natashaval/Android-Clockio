package com.natasha.clockio.base.di.component;

import android.content.Context;
import com.natasha.clockio.MainActivity;
import com.natasha.clockio.base.di.module.MainActivityContextModule;
import com.natasha.clockio.base.di.qualifier.ActivityContext;
import com.natasha.clockio.base.di.scope.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(modules = {MainActivityContextModule.class}, dependencies = ApplicationComponent.class)
public interface MainActivityComponent {

    @ActivityContext
    Context getContext();

    void injectMainActivity(MainActivity mainActivity);
}
