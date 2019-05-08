package com.shineapp.cars.screen.filter

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FilterModule {

    @ContributesAndroidInjector
    fun contributesModule(): FilterFragment

}



