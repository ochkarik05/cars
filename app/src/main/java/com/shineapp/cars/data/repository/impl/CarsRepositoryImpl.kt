package com.shineapp.cars.data.repository.impl

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.shineapp.cars.data.model.Data
import com.shineapp.cars.data.repository.*
import com.shineapp.cars.data.repository.api.CarsApi
import javax.inject.Inject
import com.github.musichin.reactivelivedata.switchMap
import com.github.musichin.reactivelivedata.toLiveData
import com.shineapp.cars.data.repository.api.toResponse

const val PAGE_SIZE = 15
class CarsRepositoryImpl @Inject constructor(val carsApi: CarsApi) : CarsRepository {

    override fun getManufacturers(): Listing<Data> {

        val sourceFactory = CarRequestFactory {
            PagedDataSource<Data, Int>(
                api = {key, pageSize ->
                    carsApi.getManufacturers(key, pageSize).map { it.toResponse() }
                }
            )
        }

        return createListing(sourceFactory)
    }

    private fun createListing(sourceFactory: CarRequestFactory<PagedDataSource<Data, Int>>): Listing<Data> {
        return Listing(
            PagingListState(
                sourceFactory.toLiveData(PAGE_SIZE),
                networkState = sourceFactory.sourceLiveData.switchMap { it.networkState },
                refreshState = sourceFactory.sourceLiveData.switchMap { it.initialLoad }
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

    override fun getModels(manufacturer: String): Listing<Data> {
        val sourceFactory = CarRequestFactory {
            PagedDataSource<Data, Int>(
                api = {key, pageSize ->
                    carsApi.getModels(key, pageSize, manufacturer).map { it.toResponse() }
                }
            )
        }

        return createListing(sourceFactory)
    }

    override fun getYears(manufacturer: String, model: String): Listing<Data> {
        val sourceFactory = CarRequestFactory {
            PagedDataSource<Data, Int>(
                api = {key, pageSize ->
                    carsApi.getYears(key, pageSize, manufacturer, model).map { it.toResponse() }
                }
            )
        }

        return createListing(sourceFactory)
    }
}

open class CarRequestFactory<T: DataSource<Int, Data>>(
    private val dataSource: () -> T
): DataSource.Factory<Int, Data>(){
    val sourceLiveData = MutableLiveData<T>()
    override fun create(): DataSource<Int, Data> {
        val source = dataSource()
        sourceLiveData.postValue(source)
        return source
    }
}