package com.shineapp.cars.screen.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shineapp.cars.R
import com.shineapp.cars.di.viewmodel.ViewModelFactory
import com.shineapp.cars.di.viewmodel.ViewModelKey
import com.shineapp.cars.system.Lg
import com.shineapp.cars.system.argumentDelegate
import com.shineapp.cars.system.lazyViewModel
import com.shineapp.cars.system.observe
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.DaggerFragment
import dagger.multibindings.IntoMap
import javax.inject.Inject

class PagedListFragment: DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    val viewModel: ListViewModel by lazyViewModel { viewModelFactory  }

    var adapter: ListAdapter = ListAdapter()

    val listType: ListType by argumentDelegate()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_paged_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Lg.i{
            "listType: $listType"
        }

        initRecyclerView(view)
        with(viewModel){
            observe(list){
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

    @ContributesAndroidInjector(modules = [
        ListViewModelModule::class
    ])
    fun contributesModule(): PagedListFragment

}
@Module
interface ListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    fun bindViewModel(viewModel: ListViewModel): ViewModel
}
