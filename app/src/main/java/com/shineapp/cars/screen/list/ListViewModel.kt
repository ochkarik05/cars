package com.shineapp.cars.screen.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.github.musichin.reactivelivedata.switchMap
import com.shineapp.cars.data.Lce
import com.shineapp.cars.data.model.Data
import com.shineapp.cars.data.repository.CarsRepository
import com.shineapp.cars.system.AutoDisposableViewModel
import com.shineapp.cars.system.mutable
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

    var searchStringLiveData: LiveData<String> = MutableLiveData()

    val actualList: LiveData<PagedList<Data>>

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

        actualList = searchStringLiveData.switchMap {

            if(it.isEmpty()){
                list
            }else{
                when(listType){
                    ListType.MANUFACTURER -> carsRepository.getManufacturers(it)
                    ListType.MODEL -> carsRepository.getModels(manufacturer!!, it)
                    ListType.YEAR -> carsRepository.getYears(manufacturer!!, model!!, it)
                }.pageListState.pagedList
            }
        }

        searchStringLiveData.mutable.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        onDispose()
    }

    fun onSearch(searchString: String) {
        searchStringLiveData.mutable.value = searchString
    }


}

