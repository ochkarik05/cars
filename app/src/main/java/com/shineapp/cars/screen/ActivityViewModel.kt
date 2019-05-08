package com.shineapp.cars.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shineapp.cars.data.model.Data
import com.shineapp.cars.system.AutoDisposableViewModel
import com.shineapp.cars.system.mutable
import javax.inject.Inject

val EMPTY_DATA = Data("", "")

class ActivityViewModel @Inject constructor(): AutoDisposableViewModel() {

    val manufacturerLiveData: LiveData<Data> = MutableLiveData()
    val modelLiveData: LiveData<Data> = MutableLiveData()
    val yearLiveData: LiveData<Data> = MutableLiveData()

    init {
        setLiveData(manufacturerLiveData, EMPTY_DATA)
        setLiveData(modelLiveData, EMPTY_DATA)
        setLiveData(yearLiveData, EMPTY_DATA)
    }

    fun setManufacturer(data: Data){
        setLiveData(manufacturerLiveData, data)
        setLiveData(modelLiveData, EMPTY_DATA)
        setLiveData(yearLiveData, EMPTY_DATA)
    }

    fun setModel(data: Data){
        setLiveData(modelLiveData, data)
        setLiveData(yearLiveData, EMPTY_DATA)
    }

    fun setYear(data: Data){
        setLiveData(yearLiveData, data)
    }

    private fun setLiveData(liveData:  LiveData<Data>, data: Data) {
        liveData.mutable.value  = data
    }

}

