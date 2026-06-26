package com.vinted.demovinted.core.network

import android.app.Application // the app object (used to read string resources)
import com.squareup.moshi.Moshi // JSON engine (injected here)
import com.vinted.demovinted.R
import dagger.Module // Hilt module marker
import dagger.Provides // Hilt: build something with custom code
import dagger.hilt.InstallIn // scope selector
import dagger.hilt.components.SingletonComponent // app-wide scope
import retrofit2.Retrofit // the HTTP client
import retrofit2.converter.moshi.MoshiConverterFactory // connects Moshi to Retrofit
import javax.inject.Qualifier // lets us tag a specific dependency
import javax.inject.Singleton // one shared instance for the app

@Qualifier // define a custom tag...
@Retention(AnnotationRetention.BINARY) // ...kept in the compiled code
annotation class BaseApi // the tag meaning "the base API endpoint"

@Module // Hilt module
@InstallIn(SingletonComponent::class) // app-wide recipes
class NetworkingModule {

    @Provides // recipe for the endpoint
    @BaseApi // tagged as the base endpoint
    fun provideBaseApiEndpoint(application: Application): VintedEndpoint { // Hilt passes in the Application
        return VintedEndpoint(application.getString(R.string.base_vinted_endpoint_url)) // wrap the URL string response
    }

    @Provides // recipe for Retrofit
    @Singleton // build it only one for the whole app
    fun providesRetrofit(
        @BaseApi baseVintedEndpoint: VintedEndpoint, // the base endpoint (selected by tag)
        moshi: Moshi // the JSON engine
    ): Retrofit {
        return Retrofit.Builder() // start building Retrofit
            .baseUrl(baseVintedEndpoint.url) // set the base URL
            .addConverterFactory(MoshiConverterFactory.create(moshi)) // auto-convert JSON using Moshi
            .build() // produce the Retrofit instance
    }
}