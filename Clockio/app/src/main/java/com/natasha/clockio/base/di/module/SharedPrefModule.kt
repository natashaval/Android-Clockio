package com.natasha.clockio.base.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.natasha.clockio.base.di.application.MyApplication
import com.natasha.clockio.base.di.scope.ApplicationScope
import dagger.Binds
import dagger.Module
import dagger.Provides

import javax.inject.Inject

//https://stackoverflow.com/questions/48081881/dagger-2-not-injecting-sharedpreference
@Module
abstract class SharedPrefModule {

    @Binds
    abstract fun provideContext(application: MyApplication) : Context

    @Module
    companion object {
        @JvmStatic
        @Provides
        @ApplicationScope
        fun provideSharedPreferences(context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }
    }

}
