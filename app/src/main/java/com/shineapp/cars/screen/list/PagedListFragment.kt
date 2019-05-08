package com.shineapp.cars.screen.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.musichin.reactivelivedata.combineLatestWith
import com.shineapp.cars.R
import com.shineapp.cars.data.Lce
import com.shineapp.cars.di.viewmodel.ViewModelFactory
import com.shineapp.cars.screen.ActivityViewModel
import com.shineapp.cars.system.argumentDelegate
import com.shineapp.cars.system.lazyActivityViewModel
import com.shineapp.cars.system.lazyViewModel
import com.shineapp.cars.system.observe
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_paged_list.*
import javax.inject.Inject

class PagedListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val activityViewModel by lazyActivityViewModel<ActivityViewModel> {
        viewModelFactory
    }

    private val viewModel: ListViewModel by lazyViewModel { viewModelFactory }
    private val adapter: ListAdapter by lazy {

        ListAdapter { data ->

            val func = when (listType) {
                ListType.MANUFACTURER -> activityViewModel::setManufacturer
                ListType.MODEL -> activityViewModel::setModel
                ListType.YEAR -> activityViewModel::setYear
            }
            func(data)

            view?.postDelayed({
                findNavController().popBackStack()
            }, 300)
        }.apply {
            selectedItem = when (listType) {
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

        initRecyclerView(view)
        with(viewModel) {
            observe(list) {
                adapter.submitList(it)
            }

            observe(refreshState) {
                progress.isVisible = it !is Lce.Data
            }

            observe(networkState) { network ->
                val showNetworkState = network is Lce.Loading && progress.isVisible.not()
                linearProgressBar.isVisible = showNetworkState
            }

        }
    }

    private fun initRecyclerView(view: View) {
        view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            adapter = this@PagedListFragment.adapter
            layoutManager = LinearLayoutManager(context!!)
        }
    }

}

