package com.shineapp.cars.data.repository.impl

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.shineapp.cars.data.model.Data
import com.shineapp.cars.data.repository.*
import com.shineapp.cars.data.repository.api.CarsApi
import javax.inject.Inject
import com.github.musichin.reactivelivedata.switchMap
import com.shineapp.cars.data.repository.api.toResponse

const val PAGE_SIZE = 15
class CarsRepositoryImpl @Inject constructor(val carsApi: CarsApi) : CarsRepository {

    override fun getManufacturers(): Listing<Data> {

        val sourceFactory = ManufacturersFactory(carsApi)

        return Listing(
            PagingListState(
                sourceFactory.toLiveData(PAGE_SIZE),
                networkState = sourceFactory.sourceLiveData.switchMap { it.networkState  },
                refreshState = sourceFactory.sourceLiveData.switchMap { it.initialLoad  }
            ),
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            dispose = {
                sourceFactory.sourceLiveData.value?.cancel()
            }
        )
    }

    override fun getModels(): Listing<Data> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getYears(): Listing<Data> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class ManufacturerSource(carsApi: CarsApi): PagedDataSource<Data, Int>(
    api = {key, pageSize ->
       carsApi.getManufacturers(key, pageSize).map { it.toResponse() }
    }
)

class ManufacturersFactory(
    val carsApi: CarsApi
): DataSource.Factory<Int, Data>() {


    val sourceLiveData = MutableLiveData<ManufacturerSource>()

    override fun create(): DataSource<Int, Data> {
        val source = ManufacturerSource(carsApi)
        sourceLiveData.postValue(source)
        return source
    }
}