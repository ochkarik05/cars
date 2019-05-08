package com.shineapp.cars.screen.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
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

    val adapter: ListAdapter by lazy{

        ListAdapter{ data ->

            val func = when(listType){
                ListType.MANUFACTURER -> activityViewModel::setManufacturer
                ListType.MODEL -> activityViewModel::setModel
                ListType.YEAR -> activityViewModel::setYear
            }
            func(data)

            view?.postDelayed({
                findNavController().popBackStack()
            }, 300)
        }.apply {
            selectedItem = when(listType){
                ListType.MANUFACTURER -> manufacturer
                ListType.MODEL -> model
                ListType.YEAR -> year
            }
        }

    }

    val listType: ListType by argumentDelegate()
    val manufacturer: String? by argumentDelegate()
    val year: String? by argumentDelegate()
    val model: String? by argumentDelegate()

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
