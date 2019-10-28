package com.natasha.clockio.base.di.component;

import com.natasha.clockio.MainActivity;
import com.natasha.clockio.base.di.scope.ActivityScope;
import dagger.Component;

@Component(dependencies = ApplicationComponent.class)
@ActivityScope
public interface ActivityComponent {
    void inject (MainActivity activity);
}
