package com.shineapp.cars

import com.shineapp.cars.screen.ActivityModelModule
import com.shineapp.cars.screen.filter.FilterModule
import com.shineapp.cars.screen.list.PagedListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainActivityModule {

    @ContributesAndroidInjector(
        modules = [
            FilterModule::class,
            PagedListModule::class,
            ActivityModelModule::class
        ]
    )
    fun contributesMainActivity(): MainActivity

}
