package com.shineapp.cars.rest

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.shineapp.cars.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import okhttp3.HttpUrl



internal fun createRetrofit(
    ctx: Context,
    converterFactory: Converter.Factory
): Retrofit {
    val clientBuilder = OkHttpClient.Builder()
    clientBuilder.connectTimeout(15, TimeUnit.SECONDS)
    clientBuilder.writeTimeout(30, TimeUnit.SECONDS)
    clientBuilder.readTimeout(30, TimeUnit.SECONDS)

//    val converterFactory = JacksonConverterFactory.create(createObjectMapper())
    clientBuilder.addInterceptor(getErrorHandleInterceptor(ctx))
    clientBuilder.addInterceptor(getQueryInterceptor())
//    clientBuilder.addNetworkInterceptor(getNetworkInterceptor())
    if (BuildConfig.DEBUG) clientBuilder.addInterceptor(getLoggingInterceptor())
    val okHttpClient = clientBuilder.build()

    return Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(converterFactory)
        .client(okHttpClient)
        .build()
}

fun getQueryInterceptor(): Interceptor = Interceptor { chain ->
    val original = chain.request()
    val originalHttpUrl = original.url()

    val url = originalHttpUrl.newBuilder()
        .addEncodedQueryParameter("wa_key", "coding-puzzle-client-449cc9d")
        .build()

    val requestBuilder = original.newBuilder()
        .url(url)

    val request = requestBuilder.build()

    chain.proceed(request)
}


private fun getLoggingInterceptor(): Interceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
}


