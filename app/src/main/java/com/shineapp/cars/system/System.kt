package com.shineapp.cars.system

import android.os.Build

fun hasOreo() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
fun hasKitKat() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
fun hasMarshmallow() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
fun hasNougat() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
