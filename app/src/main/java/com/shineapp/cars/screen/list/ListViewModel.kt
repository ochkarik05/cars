package com.shineapp.cars.screen.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.shineapp.cars.data.Lce
import com.shineapp.cars.data.model.Data
import com.shineapp.cars.data.repository.CarsRepository
import com.shineapp.cars.system.AutoDisposableViewModel
import com.shineapp.cars.system.mutable
import javax.inject.Inject

class ListViewModel @Inject constructor(
    val carsRepository: CarsRepository
): AutoDisposableViewModel(){

    val list: LiveData<PagedList<Data>>

    val networkState: MutableLiveData<Lce<Data>>

    val refreshState: MutableLiveData<Lce<Int>>
    var retry: () -> Unit
    var refresh: () -> Unit
    var onDispose: () -> Unit


    init {

        val listing = carsRepository.getManufacturers()

        retry = listing.retry
        refresh = listing.refresh
        list = listing.pageListState.pagedList
        onDispose = listing.dispose
        networkState = listing.pageListState.networkState.mutable
        refreshState = listing.pageListState.refreshState.mutable
    }


}