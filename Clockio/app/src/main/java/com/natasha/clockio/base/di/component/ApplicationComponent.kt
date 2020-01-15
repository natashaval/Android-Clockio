package com.natasha.clockio.base.di.component

import com.natasha.clockio.base.di.application.MyApplication
import com.natasha.clockio.base.di.module.ApiModule
import com.natasha.clockio.base.di.module.BuilderActivityModule
import com.natasha.clockio.base.di.module.ContextModule
import com.natasha.clockio.base.di.module.ViewModelFactoryModule
import com.natasha.clockio.base.di.module.worker.WorkerBindingModule
import com.natasha.clockio.base.di.scope.ApplicationScope
import com.natasha.clockio.base.di.factory.DaggerWorkerFactory
import com.natasha.clockio.base.di.module.worker.WorkerFactoryModule

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

import javax.inject.Named

@ApplicationScope
@Component(modules = [
  AndroidInjectionModule::class,
  ContextModule::class,
  ViewModelFactoryModule::class,
  BuilderActivityModule::class,
  ApiModule::class,
  WorkerFactoryModule::class,
  WorkerBindingModule::class])
interface ApplicationComponent : AndroidInjector<MyApplication> {

  //    https://codeday.me/es/qa/20190614/880268.html
  // to throw baseUrl name from MyApplication
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun contextModule(application: MyApplication): Builder

    @BindsInstance
    fun retrofitModule(@Named("baseUrl") baseUrl: String): Builder

    fun build(): ApplicationComponent
  }

  fun injectApplication(myApplication: MyApplication)
  fun factory(): DaggerWorkerFactory
}
