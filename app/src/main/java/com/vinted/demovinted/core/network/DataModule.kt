package com.vinted.demovinted.core.network

import com.squareup.moshi.Moshi // the JSON engine
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory // lets Moshi read Kotlin classes
import com.vinted.demovinted.core.network.interceptor.BigDecimalAdapter // our BigDecimal converter
import dagger.Module // Hilt module marker
import dagger.Provides // Hilt: build something with custom code
import dagger.hilt.InstallIn // which scope this module lives in
import dagger.hilt.components.SingletonComponent // app-wide scope

@Module // Hilt module
@InstallIn(SingletonComponent::class) // app-wide recipes
class DataModule {

    @Provides // recipe that builds a Moshi
    fun provideMoshi(): Moshi {
        return Moshi.Builder() // start building Moshi
            .add(BigDecimalAdapter) // register our BigDecimal converter
            .add(KotlinJsonAdapterFactory()) // enable reading/writing Kotlin classes
            .build() // produce the Moshi instance
    }
}