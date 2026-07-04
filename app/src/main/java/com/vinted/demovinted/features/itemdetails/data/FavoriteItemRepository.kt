package com.vinted.demovinted.features.itemdetails.data // package (folder path)

import kotlinx.coroutines.flow.Flow // an async stream of values

abstract class FavoriteItemRepository { // the contract for storing favorites (like a Python ABC)
    abstract fun toggleFavoriteItem(itemId: String) : Flow<Boolean> // flip favorite; stream the new state

    abstract fun isItemFavorite(itemId: String): Boolean // is this item currently favorited?
}