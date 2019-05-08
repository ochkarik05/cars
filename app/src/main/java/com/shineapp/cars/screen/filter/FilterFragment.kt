package com.shineapp.cars.screen.filter


import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.shineapp.cars.R
import com.shineapp.cars.di.viewmodel.ViewModelFactory
import com.shineapp.cars.screen.list.ListViewModel
import com.shineapp.cars.system.hasKitKat
import com.shineapp.cars.system.lazyViewModel
import com.shineapp.cars.system.observe
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_filter.view.*
import javax.inject.Inject

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

        initManufacturer(view)
        initModel(view, listener)
        initYear(view, listener)


    }
    private fun initManufacturer(view: View) {
        view.manufacturerLayout.setOnClickListener{
            navTo(R.id.actionShowList)
        }
    }

    private fun navTo(actionId: Int) {


        findNavController().navigate(actionId)
    }

    private fun initModel(view: View, listener: (View) -> Unit) {
        view.modelLayout.setOnClickListener(listener)
    }

    private fun initYear(view: View, listener: (View) -> Unit) {
        view.yearLayout.setOnClickListener(listener)
    }

}

