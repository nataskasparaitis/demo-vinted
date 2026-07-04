package com.vinted.demovinted.features.itemdetails.data // package (folder path)

import kotlinx.coroutines.flow.Flow // an async stream of values
import kotlinx.coroutines.flow.flow // builder for a flow (imported, unused here)
import kotlinx.coroutines.flow.flowOf // makes a flow that emits given values
import javax.inject.Inject // marks a constructor Hilt can call
import javax.inject.Singleton // one shared instance for the app

@Singleton // keep one instance (so favorites persist across screens)
class FavoriteItemRepositoryImpl @Inject constructor() : FavoriteItemRepository() { // in-memory implementation

    private val favoriteItems = mutableSetOf<String>() // the set of favorited item ids

    override fun toggleFavoriteItem(itemId: String): Flow<Boolean> { // flip favorite state
        val isNowFavorite = !favoriteItems.contains(itemId) // true if it was NOT a favorite

        if (isNowFavorite) { // becoming a favorite?
            favoriteItems.add(itemId) // add it
        } else {
            favoriteItems.remove(itemId) // otherwise remove it
        }

        return flowOf(isNowFavorite) // emit the new state as a one-value stream
    }

    override fun isItemFavorite(itemId: String): Boolean = favoriteItems.contains(itemId) // membership check
}