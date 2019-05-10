package com.shineapp.cars.di

import com.shineapp.cars.App
import com.shineapp.cars.MainActivityModule
import com.shineapp.cars.data.repository.RepositoryModule
import com.shineapp.cars.rest.RetrofitModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        MainActivityModule::class,
        RetrofitModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }
}
