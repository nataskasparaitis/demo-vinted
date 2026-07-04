package com.vinted.demovinted.features.itemdetails // package (folder path)

import com.vinted.demovinted.features.feed.api.responses.CatalogItemListResponse // suggestions response
import com.vinted.demovinted.models.ItemBox // the UI item model

data class ItemDetailsState( // a snapshot of the details screen
    val itemBox: ItemBox = ItemBox(), // the item being shown
    val catalogItemListResponse: CatalogItemListResponse = CatalogItemListResponse(), // suggested items
    val isLoading: Boolean = false, // is a load in progress?
    val isFavorite: Boolean = false, // is this item favorited?
)