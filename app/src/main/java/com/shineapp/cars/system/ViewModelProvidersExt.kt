package com.shineapp.cars.system

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*

inline fun <reified VM : ViewModel> LifecycleOwner.lazyViewModel(
    crossinline factory: () -> ViewModelProvider.Factory
) = lazy { getViewModel<VM>(factory()) }


inline fun <reified VM: ViewModel> Fragment.lazyActivityViewModel(
    crossinline factory: () -> ViewModelProvider.Factory
) = lazy {
    ViewModelProviders.of(activity!!, factory()).get(VM::class.java)
//    activity!!.getViewModel<VM>(factory())
}


inline fun <reified T : ViewModel> LifecycleOwner.withViewModel(
    factory: ViewModelProvider.Factory,
    body: T.() -> Unit
): T {
    val vm = getViewModel<T>(factory)
    vm.body()
    return vm
}


inline fun <reified VM : ViewModel> LifecycleOwner.getViewModel(
    factory: ViewModelProvider.Factory? = null
): VM {
    return getViewModelProvider(factory).get(VM::class.java)
}

fun LifecycleOwner.getViewModelProvider(factory: ViewModelProvider.Factory? = null): ViewModelProvider {
    return when(this){
        is Fragment-> ViewModelProviders.of(this, factory)
        is FragmentActivity -> ViewModelProviders.of(this, factory)
        else -> throw IllegalArgumentException("Unsupported type: ${this::class.java}")
    }
}

