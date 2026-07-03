package com.vinted.demovinted.application // package (folder path)

import android.os.Bundle // carries saved screen state
import androidx.appcompat.app.AppCompatActivity // base class for a screen
import com.vinted.demovinted.R // generated resource ids
import com.vinted.demovinted.features.feed.FeedFragment // the feed screen
import dagger.hilt.android.AndroidEntryPoint // lets Hilt inject (and host Hilt fragments)

@AndroidEntryPoint // make this Activity part of the Hilt graph
class MainActivity : AppCompatActivity() { // the app's single screen/window

    override fun onCreate(savedInstanceState: Bundle?) { // Android calls this on screen creation
        super.onCreate(savedInstanceState) // base setup first
        setContentView(R.layout.main_activity) // show the container layout
        if (savedInstanceState == null) { // only on a fresh start (not after rotation)
            supportFragmentManager.beginTransaction() // start a fragment change
                .add(R.id.container, FeedFragment.newInstance()) // put the feed into the container
                .commit() // apply it
        }
    }
}