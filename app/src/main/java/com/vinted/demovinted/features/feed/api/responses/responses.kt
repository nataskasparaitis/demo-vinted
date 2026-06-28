package com.vinted.demovinted.features.feed.api.responses

import com.vinted.demovinted.models.FeedItem // the item model

open class CatalogItemListResponse ( // models the JSON envelope { "items": [...] }; open = subclassable
    val items: List<FeedItem> = emptyList(), // the list of items; default empty
)