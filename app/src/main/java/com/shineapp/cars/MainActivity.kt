package com.shineapp.cars

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main_layout.*


class MainActivity: DaggerAppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_layout)
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.main_host_fragment)
        setupWithNavController(toolbar, navController)
    }
}