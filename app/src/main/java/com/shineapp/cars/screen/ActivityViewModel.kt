package com.shineapp.cars.screen

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shineapp.cars.data.model.Data
import com.shineapp.cars.screen.filter.FilterFragmentDirections
import com.shineapp.cars.screen.list.ListType
import com.shineapp.cars.system.AutoDisposableViewModel
import com.shineapp.cars.system.SingleLiveEvent
import com.shineapp.cars.system.mutable
import javax.inject.Inject

val EMPTY_DATA = Data("", "")

class ActivityViewModel @Inject constructor() : AutoDisposableViewModel() {

    val manufacturerLiveData: LiveData<Data> = MutableLiveData()
    val modelLiveData: LiveData<Data> = MutableLiveData()
    val yearLiveData: LiveData<Data> = MutableLiveData()

    val openUri: LiveData<Uri> = SingleLiveEvent()

    val navWithArguments: LiveData<FilterFragmentDirections.ActionShowList> = SingleLiveEvent()


    init {
        setLiveData(manufacturerLiveData, EMPTY_DATA)
        setLiveData(modelLiveData, EMPTY_DATA)
        setLiveData(yearLiveData, EMPTY_DATA)
    }

    fun setManufacturer(data: Data) {
        setLiveData(manufacturerLiveData, data)
        setLiveData(modelLiveData, EMPTY_DATA)
        setLiveData(yearLiveData, EMPTY_DATA)
    }

    fun setModel(data: Data) {
        setLiveData(modelLiveData, data)
        setLiveData(yearLiveData, EMPTY_DATA)
    }

    fun setYear(data: Data) {
        setLiveData(yearLiveData, data)
    }

    private fun setLiveData(liveData: LiveData<Data>, data: Data) {
        liveData.mutable.value = data
    }

    fun submitPressed() {
        val manufacturer = getManufacturer().value
        val model = getModel().value
        val year = getYear().value
        openUri.mutable.value =
            Uri.parse("https://www.google.com.ua/search?q=Used+$manufacturer+$model+$year+for+sale+Berlin")
    }

    private fun getYear() = requireNotNull(yearLiveData.value)
    private fun getModel() = requireNotNull(modelLiveData.value)
    private fun getManufacturer() = requireNotNull(manufacturerLiveData.value)

    fun layoutPressed(listType: ListType) {

        navWithArguments.mutable.value = FilterFragmentDirections
            .actionShowList()
            .setManufacturer(getManufacturer().id)
            .setModel(getModel().id)
            .setYear(getYear().id)
            .setListType(listType)

    }

}

