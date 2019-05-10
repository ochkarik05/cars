package com.shineapp.cars.screen

import androidx.lifecycle.ViewModel
import com.shineapp.cars.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ActivityModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ActivityViewModel::class)
    fun bindViewModel(viewModel: ActivityViewModel): ViewModel
}