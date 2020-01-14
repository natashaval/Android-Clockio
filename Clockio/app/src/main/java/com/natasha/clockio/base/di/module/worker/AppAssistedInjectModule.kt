package com.natasha.clockio.base.di.module.worker

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@Module(includes = [AssistedInject_AppAssistedInjectModule::class])
@AssistedModule
interface AppAssistedInjectModule

//https://medium.com/@neonankiti/how-to-use-dagger2-withworkmanager-bae3a5fb7dd3
//https://proandroiddev.com/dagger-2-setup-with-workmanager-a-complete-step-by-step-guild-bb9f474bde37
//https://arunkumar.dev/dagger-recipes-illustrative-step-by-step-guide-to-achieve-constructor-injection-in-workmanager/
//https://blog.stylingandroid.com/muselee-work-manager-part-2/
//https://android.jlelse.eu/injecting-into-workers-android-workmanager-and-dagger-948193c17684