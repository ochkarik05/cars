package com.shineapp.cars.system

import androidx.lifecycle.ViewModel

abstract class AutoDisposableViewModel : ViewModel(), AutoDisposable by AutoDisposableImpl() {

    override fun onCleared() {
        dispose()
        super.onCleared()
    }

}