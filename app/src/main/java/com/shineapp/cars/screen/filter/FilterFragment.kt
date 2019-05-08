package com.shineapp.cars.screen.filter


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.shineapp.cars.MainActivity
import com.shineapp.cars.R
import com.shineapp.cars.di.viewmodel.ViewModelFactory
import com.shineapp.cars.screen.ActivityViewModel
import com.shineapp.cars.screen.list.ListType
import com.shineapp.cars.system.Lg
import com.shineapp.cars.system.lazyActivityViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_filter.view.*
import javax.inject.Inject

class FilterFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val activityViewModel by lazyActivityViewModel<ActivityViewModel> {
        viewModelFactory
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initManufacturer(view)
        initModel(view)
        initYear(view)


        Lg.i {
            "activityViewModel: ${activityViewModel.hashCode()}"
        }


        Lg.i {
            "activityViewModel: $activityViewModel"
        }

    }

    private fun initManufacturer(view: View) {
        val layout = view.manufacturerLayout
        val hintId = R.string.manufacturer
        initLayout(layout, hintId, ListType.MANUFACTURER)
    }

    private fun navTo(arguments: FilterFragmentDirections.ActionShowList) = findNavController().navigate(arguments)

    private fun initModel(view: View) {
        val layout = view.modelLayout
        val hintId = R.string.model
        initLayout(layout, hintId, ListType.MODEL)
    }

    private fun initLayout(layout: View, hintId: Int, listType: ListType) {
        val hint = layout.findViewById<TextView>(R.id.hint)
        hint.setText(hintId)
        layout.setOnClickListener {
            navTo(FilterFragmentDirections.actionShowList().setListType(listType))
        }
    }

    private fun initYear(view: View) {
        val layout = view.yearLayout
        val hintId = R.string.year
        initLayout(layout, hintId, ListType.YEAR)
    }
}

