package com.natasha.clockio.base.di.component;

import com.natasha.clockio.base.di.application.MyApplication;
import com.natasha.clockio.base.di.module.ApiModule;
import com.natasha.clockio.base.di.module.BuilderActivityModule;
import com.natasha.clockio.base.di.module.ContextModule;
import com.natasha.clockio.base.di.module.ViewModelFactoryModule;
import com.natasha.clockio.base.di.scope.ApplicationScope;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

import javax.inject.Named;

@ApplicationScope
@Component(modules = {
        AndroidInjectionModule.class,
        ContextModule.class,
        ViewModelFactoryModule.class,
        BuilderActivityModule.class,
        ApiModule.class
})
public interface ApplicationComponent extends AndroidInjector<MyApplication> {

//    https://codeday.me/es/qa/20190614/880268.html
    // to throw baseUrl name from MyApplication
    @Component.Builder
    interface Builder {
        @BindsInstance Builder contextModule(MyApplication application);
        @BindsInstance Builder retrofitModule(@Named("baseUrl") String baseUrl);
        ApplicationComponent build();
    }

    void injectApplication(MyApplication myApplication);
}
