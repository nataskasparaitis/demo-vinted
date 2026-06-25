package com.vinted.demovinted.application

import android.os.Bundle // type that carries saved screen state
import androidx.appcompat.app.AppCompatActivity // base class for a screen
import com.vinted.demovinted.R // generated ids for resources (layouts, strings, ...)
import dagger.hilt.android.AndroidEntryPoint // lets Hilt inject into this screen

@AndroidEntryPoint // make this Activity part of the Hilt graph
class MainActivity : AppCompatActivity() { // our single screen/window
    override fun onCreate(savedInstanceState: Bundle?) { // Android calls this when the screen is created
        super.onCreate(savedInstanceState) // run the base class setup first
        setContentView(R.layout.main_activity) // display the empty container layout
    }
}