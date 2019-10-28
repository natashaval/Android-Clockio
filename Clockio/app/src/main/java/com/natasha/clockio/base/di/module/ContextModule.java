package com.natasha.clockio.base.di.module;

import android.content.Context;
import com.natasha.clockio.base.di.qualifier.ActivityContext;
import com.natasha.clockio.base.di.qualifier.ApplicationContext;
import com.natasha.clockio.base.di.scope.ApplicationScope;
import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {
    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @ApplicationScope
    @ApplicationContext
    public Context provideContext() {
        return context;
    }
}
