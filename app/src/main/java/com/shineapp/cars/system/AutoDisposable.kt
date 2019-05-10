package com.shineapp.cars.system

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer

interface AutoDisposable :  DisposableContainer {
    val disp: CompositeDisposable

    fun Disposable.autoDispose() {
        disp.add(this)
    }

    fun cancel()
    fun dispose()

}

class AutoDisposableImpl : AutoDisposable {

    override val disp = CompositeDisposable()

    override fun dispose() {
        disp.dispose()
    }

    override fun cancel() {
        disp.clear()
    }

    override fun add(d: Disposable): Boolean {
        return disp.add(d)
    }

    override fun remove(d: Disposable): Boolean {
        return disp.remove(d)
    }

    override fun delete(d: Disposable): Boolean {
        return disp.delete(d)
    }
}
