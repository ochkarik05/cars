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
import java.util.*
import javax.inject.Inject

const val PAGE_SIZE = 15
class CarsRepositoryImpl @Inject constructor(private val carsApi: CarsApi) : CarsRepository {

    private val cachedManufacturers = carsApi.getManufacturers(0,  null).cache()
    private val cachedModels = mutableMapOf<String, Single<ServerResponse>>()
    private val cachedYears = mutableMapOf<Pair<String, String>, Single<ServerResponse>>()

    override fun getManufacturers(filter: String?): Listing<Data> {

        val sourceFactory = CarRequestFactory {
            PagedDataSource(
                api = {key, pageSize ->
                    if(filter == null){
                        carsApi.getManufacturers(key, pageSize).map { it.toResponse() }
                    }else{
                        cachedManufacturers.filterValues(filter)
                    }
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

        val cachedModel = cachedModels.getOrPut(manufacturer) {
            carsApi.getModels(0, null, manufacturer).cache()
        }

        val sourceFactory = CarRequestFactory {
            PagedDataSource(
                api = {key, pageSize ->

                    if(filter == null){
                        carsApi.getModels(key,  pageSize, manufacturer).map { it.toResponse() }
                    }else{
                        cachedModel.filterValues(filter)
                    }
                }
            )
        }

        return createListing(sourceFactory)
    }

    override fun getYears(manufacturer: String, model: String, filter: String?): Listing<Data> {


        val cachedYear = cachedYears.getOrPut(Pair(manufacturer, model)) {
            carsApi.getYears(0, null, manufacturer, model).cache()
        }

        val sourceFactory = CarRequestFactory {
            PagedDataSource(
                api = {key, pageSize ->
                    if(filter == null){
                        carsApi.getYears(key, pageSize, manufacturer, model).map { it.toResponse() }
                    }else{
                        cachedYear.filterValues(filter)
                    }
                }
            )
        }

        return createListing(sourceFactory)
    }
}

fun Single<ServerResponse>.filterValues(filter: String) = map { it.toResponse() }
.map { response ->
    response.copy( data = response.data.filter { it.value.toLowerCase(Locale.getDefault()).contains(filter) })
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