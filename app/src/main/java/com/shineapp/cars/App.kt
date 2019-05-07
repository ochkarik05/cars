package com.shineapp.cars

import com.github.ajalt.timberkt.Timber
import com.halfhp.rxtracer.RxTracer
import com.shineapp.cars.di.AppComponent
import com.shineapp.cars.di.AppModule
import com.shineapp.cars.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins

class App : DaggerApplication() {
    lateinit var appComponent: AppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = appComponent

    override fun onCreate() {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .appModule(AppModule(this))
            .build()
        super.onCreate()


        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }


        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace)

        RxTracer.setMode(RxTracer.Mode.APPEND)
        RxTracer.enable()
    }
}