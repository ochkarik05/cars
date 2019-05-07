package com.shineapp.cars.system

import android.annotation.SuppressLint
import android.util.Log
import com.github.ajalt.timberkt.Timber
import io.reactivex.*
import java.util.concurrent.atomic.AtomicReference

typealias Lg = Timber

fun <T> Observable<T>.debug(description: String): Observable<T> {

    val nextOffset = createAtomicRef()

    return doOnEach { notification ->
        doOnEach(notification, description, nextOffset)
    }.doOnSubscribe {
        printSubscribe(description)
    }.doOnDispose {
        printDispose(description)
    }
}

fun createAtomicRef() = AtomicReference<String>("-")

fun Completable.debug(description: String): Completable {
    return doOnEvent { throwable ->
        if (throwable == null) {
            printComplete(description)
        } else {
            printError(description, throwable)
        }
    }.doOnSubscribe {
        printSubscribe(description)
    }.doFinally {
        printDispose(description)
    }
}

fun <T> Single<T>.debug(description: String): Single<T> {
    return doOnEvent { result, error ->
        if (result != null) {
            printValue(description, result)
        } else if (error != null) {
            printError(description, error)
        }
    }.doOnSubscribe {
        printSubscribe(description)
    }.doFinally {
        printDispose(description)
    }
}

fun <T> Maybe<T>.debug(description: String): Maybe<T> {
    return doOnEvent { result, error ->
        if (result != null) {
            printValue(description, result)
        } else if (error != null) {
            printError(description, error)
        } else {
            printComplete(description)
        }
    }.doOnSubscribe {
        printSubscribe(description)
    }.doOnDispose {
        printDispose(description)
    }
}

fun <T> Flowable<T>.debug(description: String): Flowable<T> {

    val nextOffset = createAtomicRef()

    return doOnEach { notification ->
        doOnEach<T>(notification, description, nextOffset)
    }.doOnSubscribe {
        printSubscribe(description)
    }.doOnCancel {
        printDispose(description)
    }
}

private fun <T> doOnEach(notification: Notification<T>, description: String, nextOffset: AtomicReference<String>) {
    when {
        notification.isOnNext -> printValue(description, notification.value, nextOffset.get())
        notification.isOnError -> printError(description, notification.error, nextOffset.get())
        notification.isOnComplete -> printComplete(description, nextOffset.get())
    }
    nextOffset.getAndUpdateCompat { p -> "-$p" }
}

private const val DEBUG_TAG = "RxDebug"

@SuppressLint("LogNotTimber")
fun printDispose(description: String) {
    Log.v(DEBUG_TAG, "${Thread.currentThread().name}|$description: disposed")
}

@SuppressLint("LogNotTimber")
fun printSubscribe(description: String) {
    Log.v(DEBUG_TAG, "${Thread.currentThread().name}|$description: subsribed")
}

@SuppressLint("LogNotTimber")
private fun printComplete(description: String, offset: String = "-") {
    Log.w(DEBUG_TAG, "${Thread.currentThread().name}|$description: $offset|")
}

@SuppressLint("LogNotTimber")
private fun printError(description: String, error: Throwable?, offset: String = "-") {
    Log.e(DEBUG_TAG, "${Thread.currentThread().name}|$description: ${offset}X $error")
}

@SuppressLint("LogNotTimber")
private fun <T> printValue(description: String, value: T?, offset: String = "-") {
    Log.w(DEBUG_TAG, "${Thread.currentThread().name}|$description: $offset> $value")
}

fun <V> AtomicReference<V>.getAndUpdateCompat(f: (V) -> V): V {
    var prev: V
    var next: V
    do {
        prev = get()
        next = f.invoke(prev)
    } while (!compareAndSet(prev, next))
    return prev
}
