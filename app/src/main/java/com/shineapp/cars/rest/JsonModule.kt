package com.shineapp.cars.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.shineapp.cars.system.createObjectMapper
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
class JsonModule {

    @Provides
    @Singleton
    fun mapper() = createObjectMapper()

    @Provides
    @Singleton
    fun converterFactory(mapper: ObjectMapper): Converter.Factory = JacksonConverterFactory.create(mapper)
}