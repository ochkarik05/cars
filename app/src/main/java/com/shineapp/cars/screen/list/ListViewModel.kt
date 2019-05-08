package com.shineapp.cars.screen.list

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.shineapp.cars.data.Lce
import com.shineapp.cars.data.model.Data
import com.shineapp.cars.data.repository.CarsRepository
import com.shineapp.cars.system.AutoDisposableViewModel
import javax.inject.Inject
import javax.inject.Named

class ListViewModel @Inject constructor(
    carsRepository: CarsRepository,
    listType: ListType,
    @Named("manufacturer") manufacturer: String?,
    @Named("model") model: String?
): AutoDisposableViewModel(){

    val list: LiveData<PagedList<Data>>
    val networkState: LiveData<Lce<Data>>
    val refreshState: LiveData<Lce<Int>>

    var retry: () -> Unit
    var refresh: () -> Unit
    var onDispose: () -> Unit


    init {

        val listing = when(listType){
            ListType.MANUFACTURER -> carsRepository.getManufacturers()
            ListType.MODEL -> carsRepository.getModels(manufacturer!!)
            ListType.YEAR -> carsRepository.getYears(manufacturer!!, model!!)
        }

        retry = listing.retry
        refresh = listing.refresh
        list = listing.pageListState.pagedList
        onDispose = listing.dispose
        networkState = listing.pageListState.networkState
        refreshState = listing.pageListState.refreshState
    }

    override fun onCleared() {
        super.onCleared()
        onDispose()
    }

}