package com.shineapp.cars.data.repository

import com.shineapp.cars.data.repository.api.CarsApi
import com.shineapp.cars.data.repository.impl.CarsRepositoryImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class RepositoryModule{

    @Provides
    fun providesCarsRepo(r: CarsRepositoryImpl): CarsRepository = r

    @Provides
    fun providesCarsApi(retrofit: Retrofit): CarsApi = retrofit.create(CarsApi::class.java)

}