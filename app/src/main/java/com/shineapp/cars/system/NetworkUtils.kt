package com.shineapp.cars.system

import android.content.Context
import android.net.ConnectivityManager

private val Context.connectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

private val Context.activeNetworkInfo get() = connectivityManager.activeNetworkInfo

fun isOnline(ctx: Context): Boolean = ctx.activeNetworkInfo?.isConnected ?: false

fun isOffline(ctx: Context): Boolean = !isOnline(ctx)
