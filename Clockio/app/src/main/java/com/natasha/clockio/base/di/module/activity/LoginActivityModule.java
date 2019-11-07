package com.natasha.clockio.base.di.module.activity;

import com.natasha.clockio.login.ui.LoginActivity;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class LoginActivityModule {
    @Binds
    abstract LoginActivity provideLoginActivity(LoginActivity loginActivity);
}
