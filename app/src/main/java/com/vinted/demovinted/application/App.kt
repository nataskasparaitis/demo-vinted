package com.vinted.demovinted.application // this file's package (matches its folder path)

import android.app.Application // Android's base Application class
import dagger.hilt.android.HiltAndroidApp // Hilt annotation that switches DI on

@HiltAndroidApp // generate the app-wide dependency-injection container
class App : Application() // our custom Application; created before any screen