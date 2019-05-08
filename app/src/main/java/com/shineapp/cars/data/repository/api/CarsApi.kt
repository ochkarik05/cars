package com.shineapp.cars.data.repository.api

import com.shineapp.cars.data.model.Data
import com.shineapp.cars.data.repository.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CarsApi{

    @GET("v1/car-types/manufacturer")
    fun getManufacturers(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Single<ServerResponse>


}

class ServerResponse(
    val wkda: Map<String, String>,
    val page: Int,
    val pageSize: Int,
    val totalPageCount:Int
)

fun ServerResponse.toResponse() :Response<Data> = Response(
    data = wkda.map { Data(it.key, it.value) },
    page = page,
    pageSize = pageSize,
    totalPageCount = totalPageCount
)
