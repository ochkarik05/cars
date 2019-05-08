package com.shineapp.cars.errors

fun Throwable.toAppException(message: String? = null): AppException = when(this){
    is AppException -> this
    else -> AppException(ErrorCode.UNKNOWN_ERROR, message, this)
}