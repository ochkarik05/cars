package com.shineapp.cars.screen

import androidx.lifecycle.ViewModel
import com.shineapp.cars.di.viewmodel.ViewModelKey
import com.shineapp.cars.system.AutoDisposableViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Singleton

class ActivityViewModel @Inject constructor(): AutoDisposableViewModel() {

}

@Module
interface ActivityModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ActivityViewModel::class)
    fun bindViewModel(viewModel: ActivityViewModel): ViewModel
}
