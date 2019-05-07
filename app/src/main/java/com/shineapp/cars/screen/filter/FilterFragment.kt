package com.shineapp.cars.screen.filter


import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.shineapp.cars.R
import com.shineapp.cars.system.hasKitKat
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_filter.view.*

class FilterFragment : DaggerFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val listener: (View) -> Unit = {
            if (hasKitKat()) {
                TransitionManager.beginDelayedTransition(it as ViewGroup)
            }
            val txt = it.findViewById<View>(R.id.text)
            txt.isVisible = !txt.isVisible
        }

        view.manufacturerLayout.setOnClickListener(listener)
        view.modelLayout.setOnClickListener(listener)
        view.yearLayout.setOnClickListener(listener)

    }

}

@Module
interface FilterModule {

    @ContributesAndroidInjector
    fun contributesModule(): FilterFragment

}

