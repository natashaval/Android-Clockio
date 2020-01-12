package com.natasha.clockio.base.di.module

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.natasha.clockio.base.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {
    @Provides
    @ApplicationScope
    fun provideFirebaseInstance(): FirebaseInstanceId {
        return FirebaseInstanceId.getInstance()
    }

    @Provides
    @ApplicationScope
    fun provideFirebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }
}