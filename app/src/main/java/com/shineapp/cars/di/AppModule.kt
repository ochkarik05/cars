package com.shineapp.cars.di

import android.content.Context
import com.shineapp.cars.App
import dagger.Module
import dagger.Provides

@Module
class AppModule(val app: App) {

    @Provides
    fun context(): Context = app
}