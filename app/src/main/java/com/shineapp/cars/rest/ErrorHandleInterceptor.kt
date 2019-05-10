package com.shineapp.cars.rest

import android.content.Context
import com.shineapp.cars.errors.AppException
import com.shineapp.cars.errors.ErrorCode
import com.shineapp.cars.system.isOffline
import okhttp3.Interceptor
import okhttp3.Response

fun getErrorHandleInterceptor(
    ctx: Context
): Interceptor {

    return Interceptor { chain ->

        if (isOffline(ctx)) {
            throw AppException(ErrorCode.NETWORK_ERROR)
        }

        val request = chain.request()
        val response = chain.proceed(request)
        throwServerErrorIfAny(response)
        response
    }
}

private fun throwServerErrorIfAny(
    response: Response
) {

    when (response.code()) {
        in 400..599 -> throw  AppException(ErrorCode.SERVER_ERROR)
    }
}

