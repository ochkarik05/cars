package com.shineapp.cars.data.repository.impl

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.github.musichin.reactivelivedata.switchMap
import com.shineapp.cars.data.model.Data
import com.shineapp.cars.data.repository.*
import com.shineapp.cars.data.repository.api.CarsApi
import com.shineapp.cars.data.repository.api.ServerResponse
import com.shineapp.cars.data.repository.api.toResponse
import io.reactivex.Single
import javax.inject.Inject

const val PAGE_SIZE = 15
class CarsRepositoryImpl @Inject constructor(val carsApi: CarsApi) : CarsRepository {

    override fun getManufacturers(filter: String?): Listing<Data> {

        val sourceFactory = CarRequestFactory {
            PagedDataSource(
                api = {key, pageSize ->
                    carsApi.getManufacturers(key, if(filter != null) null else pageSize)
                        .filterIfRequired(filter)
                }
            )
        }

        return createListing(sourceFactory)
    }

    private fun createListing(sourceFactory: CarRequestFactory<PagedDataSource<Data>>): Listing<Data> {
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

    override fun getModels(manufacturer: String, filter: String?): Listing<Data> {
        val sourceFactory = CarRequestFactory {
            PagedDataSource(
                api = {key, pageSize ->
                    carsApi.getModels(key, if(filter != null) null else pageSize, manufacturer).filterIfRequired(filter)

                }
            )
        }

        return createListing(sourceFactory)
    }

    override fun getYears(manufacturer: String, model: String, filter: String?): Listing<Data> {
        val sourceFactory = CarRequestFactory {
            PagedDataSource(
                api = {key, pageSize ->
                    carsApi.getYears(key, if(filter != null) null else pageSize, manufacturer, model).filterIfRequired(filter)
                }
            )
        }

        return createListing(sourceFactory)
    }
}

fun Single<ServerResponse>.filterIfRequired(filter: String?) = map { it.toResponse() }
.map { response ->
    response.copy( data = if(filter != null){
        response.data.filter { it.value.toLowerCase().contains(filter) }
    }else{
        response.data
    })
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