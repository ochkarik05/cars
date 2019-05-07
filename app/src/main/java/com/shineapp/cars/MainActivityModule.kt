package com.shineapp.cars

import com.shineapp.cars.screen.filter.FilterModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainActivityModule {

    @ContributesAndroidInjector(modules = [
        FilterModule::class
    ])
    fun contributesMainActivity(): MainActivity

}
