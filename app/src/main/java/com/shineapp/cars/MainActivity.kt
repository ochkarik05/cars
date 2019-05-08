package com.shineapp.cars

import android.os.Bundle
import com.shineapp.cars.di.viewmodel.ViewModelFactory
import com.shineapp.cars.screen.ActivityViewModel
import com.shineapp.cars.system.withViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity: DaggerAppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_layout)
    }
}