package com.vinted.demovinted.features.feed.api

import dagger.Module // Hilt module maker
import dagger.Provides // Hilt: build with custom code
import dagger.hilt.InstallIn // scope selector
import dagger.hilt.components.SingletonComponent // app-wide scope
import retrofit2.Retrofit // the HTTP client (injected here)

@Module // Hilt module
@InstallIn(SingletonComponent::class) // app-wide recipes
object FeedApiModule { // object = single shared instance

    @Provides // recipe that builds the API
    fun providesFeedApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java) // ask Retrofit to implement the Api interface
}