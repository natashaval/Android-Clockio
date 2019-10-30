package com.natasha.clockio.base.di.module;

import android.app.Application;
import android.content.Context;
import com.natasha.clockio.base.di.qualifier.ApplicationContext;
import com.natasha.clockio.base.di.scope.ApplicationScope;
import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    @Provides
    @ApplicationScope
    @ApplicationContext
    public Context provideContext(Application application) {
        return application;
    }
}
