package com.shineapp.cars.screen.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shineapp.cars.MainActivity
import com.shineapp.cars.R
import com.shineapp.cars.di.viewmodel.ViewModelFactory
import com.shineapp.cars.di.viewmodel.ViewModelKey
import com.shineapp.cars.screen.ActivityViewModel
import com.shineapp.cars.system.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.android.support.DaggerFragment
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Named

class PagedListFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val activityViewModel by lazyActivityViewModel<ActivityViewModel> {
        viewModelFactory
    }

    val viewModel: ListViewModel by lazyViewModel { viewModelFactory }

    var adapter: ListAdapter = ListAdapter()

    val listType: ListType by argumentDelegate()
    val manufacturer: String? by argumentDelegate()
    val year: String? by argumentDelegate()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_paged_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Lg.i{
            "activityViewModel: ${activityViewModel.hashCode()}, $activityViewModel"
        }

        initRecyclerView(view)
        with(viewModel) {
            observe(list) {
                adapter.submitList(it)
            }
        }
    }

    private fun initRecyclerView(view: View) {

        val rv: RecyclerView = view.findViewById(R.id.recyclerView)

        rv.adapter = adapter

        rv.layoutManager = LinearLayoutManager(context!!)

    }

}

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

    @Named("year")
    @Provides
    fun providesYear(f: PagedListFragment): String? = f.year


}

@Module
interface ListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    fun bindViewModel(viewModel: ListViewModel): ViewModel
}
