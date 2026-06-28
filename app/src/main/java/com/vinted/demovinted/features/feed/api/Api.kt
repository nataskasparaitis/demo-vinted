package com.vinted.demovinted.features.feed.api

import com.vinted.demovinted.features.feed.api.responses.CatalogItemListResponse // the response type
import retrofit2.http.GET // marks the HTTP GET call
import retrofit2.http.QueryMap // sends a map as URL query parameters

interface Api { // Retrofit generates the ral HTTP code form this interface

    @GET("items") // HTTP GET <base_url>/items
    suspend fun getItemsFeed( // suspend = async (must be called from a coroutine)
        @QueryMap params: Map<String, String> // query params, e.g. ?page=1per_page=20
    ): CatalogItemListResponse // JSON auto-parsed into this type

    companion object { // constants on the type itself
        const val KEY_PAGE = "page" // query key for the page number
        const val KEY_PER_PAGE = "per_page" // query key for the page size
        const val KEY_TIME = "time" // query key for a cache-busting timestamp
        const val STARTING_PAGE = 1 // the first page number
    }
}