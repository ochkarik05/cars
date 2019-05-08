package com.shineapp.cars.screen.filter

import androidx.lifecycle.ViewModel
import com.shineapp.cars.di.viewmodel.ViewModelKey
import com.shineapp.cars.screen.list.ListViewModel
import com.shineapp.cars.screen.list.ListViewModelModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface FilterModule {

    @ContributesAndroidInjector
    fun contributesModule(): FilterFragment

}



