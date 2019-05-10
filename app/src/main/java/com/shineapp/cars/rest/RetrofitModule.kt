package com.shineapp.cars.rest

import android.content.Context
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import javax.inject.Singleton

@Module(includes = [JsonModule::class])
class RetrofitModule{

    @Provides
    @Singleton
    fun retrofit(ctx: Context, converter: Converter.Factory) = createRetrofit(ctx, converter)

}