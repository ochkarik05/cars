package com.shineapp.cars.data.repository

data class Listing<T>(
    val pageListState: PagingListState<T, Int>,
    val refresh: () -> Unit,
    val retry: () -> Unit,
    val dispose: () -> Unit
)
