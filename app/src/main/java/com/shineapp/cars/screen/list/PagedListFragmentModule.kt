package com.shineapp.cars.screen.list

import androidx.lifecycle.ViewModel
import com.shineapp.cars.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
interface PagedListModule {

    @ContributesAndroidInjector(
        modules = [
            ListViewModelModule::class,
            PagedListFragmentModule::class
        ]
    )
    fun contributesModule(): PagedListFragment

}

@Module
class PagedListFragmentModule {

    @Provides
    fun providesListType(f: PagedListFragment) = f.listType


    @Named("manufacturer")
    @Provides
    fun providesManufacturer(f: PagedListFragment): String? = f.manufacturer

    @Named("model")
    @Provides
    fun providesYear(f: PagedListFragment): String? = f.model


}

@Module
interface ListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    fun bindViewModel(viewModel: ListViewModel): ViewModel
}