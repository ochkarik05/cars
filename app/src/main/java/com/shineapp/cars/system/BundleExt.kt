package com.shineapp.cars.system

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


typealias ParcelableMapper<T> = (Parcelable) -> T

inline fun <reified T> Activity.argumentDelegate(
    noinline mapper: ParcelableMapper<T>? = { it as T }
): ReadOnlyProperty<Activity, T> {
    return ArgumentDelegate({ it.intent.extras!! }, mapper, T::class.java)
}

inline fun <reified T> Fragment.argumentDelegate(
    noinline mapper: ParcelableMapper<T>? = { it as T }
): ReadOnlyProperty<Fragment, T> {
    val getArguments = { fragment: Fragment ->
        fragment.arguments ?: throw RuntimeException("$fragment's arguments must to be not null")
    }
    return ArgumentDelegate(getArguments, mapper, T::class.java)
}

@Suppress("UNCHECKED_CAST")
class ArgumentDelegate<in F, T>(
    val getArguments: (F) -> Bundle,
    val mapper: ParcelableMapper<T>? = null,
    val clazz: Class<T>
) : ReadOnlyProperty<F, T> {

    var initialized = false
    var value: T? = null

    override operator fun getValue(thisRef: F, property: KProperty<*>): T {
        if (!initialized) {
            value = getArgument(getArguments(thisRef), property, mapper, clazz)
            initialized = true
        }
        return value as T
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T> getArgument(
    args: Bundle?,
    property: KProperty<*>,
    mapper: ParcelableMapper<T>?,
    clazz: Class<T>
): T {
    args ?: throw RuntimeException("Property ${property.name} can't be read if arguments have not been set")
    val value = args.get(property.name)
    return when {
        value == null -> null as T
        clazz.isAssignableFrom(value::class.java) -> value as T
        mapper != null && value is Parcelable -> mapper.invoke(value)
        else -> throw RuntimeException("Property ${property.name} can't be mapped")
    }
}

//fun <T> Bundle.unwrap(key: String): T {
//    return get(key) as T
//}
//
//fun Bundle.wrap(key: String, any: Any?): Bundle {
//    putParcelable(key, Parcels.wrap(any))
//    return this
//}
//
//fun <T> Bundle.wrap(key: String, any: T?, type: Class<T>): Bundle {
//    putParcelable(key, Parcels.wrap(type, any))
//    return this
//}


