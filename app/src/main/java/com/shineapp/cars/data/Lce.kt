package com.shineapp.cars.data

import com.shineapp.cars.errors.AppException


sealed class Lce<out T>  {
    object Idle : Lce<Nothing>()
    object Loading : Lce<Nothing>()
    data class Error<T>(val error: AppException) : Lce<T>()
    data class Data<T>(val data: T) : Lce<T>()
}

fun <T> Lce<T>.doOn(
    loading: () -> Unit = {},
    data: (T) -> Unit = {},
    error: (AppException) -> Unit = {},
    idle: () -> Unit = {}
) = when (this) {
    Lce.Idle -> idle()
    Lce.Loading -> loading()
    is Lce.Data -> data(this.data)
    is Lce.Error -> error(this.error)
}


