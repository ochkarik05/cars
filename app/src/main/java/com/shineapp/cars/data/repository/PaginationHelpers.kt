package com.shineapp.cars.data.repository

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.shineapp.cars.data.Lce
import com.shineapp.cars.data.repository.impl.PAGE_SIZE
import com.shineapp.cars.errors.toAppException
import com.shineapp.cars.system.AutoDisposable
import com.shineapp.cars.system.AutoDisposableImpl
import com.shineapp.cars.system.Lg
import io.reactivex.Single
import java.util.concurrent.Executor
import java.util.concurrent.Executors


const val DEFAULT_PAGE_VALUE = 0

data class Response<T>(
    val data: List<T>,
    val page: Int,
    val pageSize: Int,
    val totalPageCount: Int
)

open class PagedDataSource<T>(
    private val api: (key: Int, pageSize: Int) -> Single<out Response<T>>,
    private val retryExecutor: Executor = Executors.newSingleThreadExecutor()
) : PageKeyedDataSource<Int, T>(),
    AutoDisposable by AutoDisposableImpl() {

    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<Lce<T>>()
    val initialLoad = MutableLiveData<Lce<Int>>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        initialLoad.postValue(Lce.Loading)

        api(DEFAULT_PAGE_VALUE, PAGE_SIZE)
            .subscribe({
                val prevPage = if (it.page > 0) it.page - 1 else null
                val nextPage = if (it.page < it.totalPageCount - 1) it.page + 1 else null
                callback.onResult(it.data, prevPage, nextPage)
                retry = null
                initialLoad.postValue(Lce.Data(it.page))
            }, { t ->
                retry = {
                    loadInitial(params, callback)
                }
                Lg.e(t) { "Load Initial" }
                initialLoad.postValue(Lce.Error(t.toAppException()))

            }).autoDispose()
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {

        networkState.postValue(Lce.Loading)
        api(params.key, params.requestedLoadSize)
            .subscribe({
                val nextPage = if (it.page < it.totalPageCount - 1) it.page + 1 else null
                callback.onResult(it.data,  nextPage)
                retry = null
                networkState.postValue(Lce.Idle)
            }, { t ->
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(Lce.Error(t.toAppException()))
                Lg.i(t) { "Load After" }
            }).autoDispose()

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        api(params.key, params.requestedLoadSize)
            .subscribe({
                val prevPage = if (it.page > 0) it.page - 1 else null
                callback.onResult(it.data, prevPage)
                retry = null
            }, { t ->
                retry = {
                    loadBefore(params, callback)
                }
                Lg.i(t) { "Load Before" }
            }).autoDispose()
    }
}

@SuppressLint("RestrictedApi")
fun <Key, Value> DataSource.Factory<Key, Value>.toLiveData(
    pageSize: Int,
    initialLoadKey: Key? = null,
    boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
    fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
): LiveData<PagedList<Value>> {
    return LivePagedListBuilder(this, Config(pageSize))
        .setInitialLoadKey(initialLoadKey)
        .setBoundaryCallback(boundaryCallback)
        .setFetchExecutor(fetchExecutor)
        .build()
}

data class PagingListState<T, M>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<Lce<T>>,
    val refreshState: LiveData<Lce<M>>
)