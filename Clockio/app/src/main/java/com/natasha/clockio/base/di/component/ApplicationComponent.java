package com.natasha.clockio.base.di.component;

import android.content.Context;
import com.natasha.clockio.MainActivity;
import com.natasha.clockio.base.di.application.MyApplication;
import com.natasha.clockio.base.di.module.ContextModule;
import com.natasha.clockio.base.di.module.RetrofitModule;
import com.natasha.clockio.base.di.qualifier.ApplicationContext;
import com.natasha.clockio.base.di.scope.ApplicationScope;
import dagger.Component;
import retrofit2.Retrofit;

@ApplicationScope
@Component(modules = {ContextModule.class, RetrofitModule.class})
public interface ApplicationComponent {

    Retrofit getRetrofit();

    @ApplicationContext
    Context getContext();

    void injectApplication(MyApplication myApplication);
}
