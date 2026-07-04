package com.vinted.demovinted.features.feed // package (folder path)

import com.vinted.demovinted.models.FeedItem // raw item model
import com.vinted.demovinted.features.feed.api.Api // the API interface to fake
import com.vinted.demovinted.features.feed.api.responses.CatalogItemListResponse // the response type

class FakeApi( // a stand-in for the real API (no network)
    var exception: Exception? = null, // if set, the call throws this instead
) : Api {

    override suspend fun getItemsFeed(params: Map<String, String>): CatalogItemListResponse { // the faked call
        val immutableException = exception // copy to a local (so the null-check sticks)
        if (immutableException != null) throw immutableException // simulate a failure if asked

        return RESPONSE_ONE // otherwise return the canned response
    }
}

internal val RESPONSE_ONE = CatalogItemListResponse( // a fixed three-item response
    items = listOf( // the list of items
        FeedItem(id = 1), // item 1
        FeedItem(id = 2), // item 2
        FeedItem(id = 3) // item 3
    ),
)