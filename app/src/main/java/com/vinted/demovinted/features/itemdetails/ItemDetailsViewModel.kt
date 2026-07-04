package com.vinted.demovinted.features.itemdetails // package (folder path)

import androidx.lifecycle.SavedStateHandle // holds the fragment's arguments
import androidx.lifecycle.ViewModel // base class that holds screen state
import com.vinted.demovinted.features.feed.api.Api // the HTTP API
import com.vinted.demovinted.features.feed.api.Api.Companion.KEY_PAGE // "page" key
import com.vinted.demovinted.models.ItemBox // the UI item model
import androidx.lifecycle.viewModelScope // coroutine scope tied to this ViewModel
import com.vinted.demovinted.features.itemdetails.data.FavoriteItemRepositoryImpl // favorites store
import dagger.hilt.android.lifecycle.HiltViewModel // lets Hilt build this ViewModel
import kotlinx.coroutines.async // start concurrent work, get a handle
import kotlinx.coroutines.flow.MutableStateFlow // writable observable state
import kotlinx.coroutines.flow.asStateFlow // expose it read-only
import kotlinx.coroutines.flow.first // take the first value from a flow
import kotlinx.coroutines.flow.update // atomically replace the state
import kotlinx.coroutines.launch // start a coroutine
import javax.inject.Inject // marks a constructor Hilt can call

@HiltViewModel // Hilt provides this ViewModel and its dependencies
class ItemDetailsViewModel @Inject constructor( // Hilt injects the three parameters below
    private val api: Api, // the network API
    private val favoriteItemRepository: FavoriteItemRepositoryImpl, // favorites store
    private val savedStateHandle: SavedStateHandle, // the fragment's arguments
) : ViewModel() {

    private val _state = MutableStateFlow(ItemDetailsState(itemBox = itemBox)) // state seeded with the tapped item
    val state = _state.asStateFlow() // public read-only state

    private val itemBox get() = savedStateHandle.get<ItemBox>(ItemDetailsConstant.ARG_EXTRA_ITEM)!! // read the tapped item (must exist)

    fun init() { // called by the fragment to load suggestions
        runCatching { // try, capturing any error instead of crashing
            viewModelScope.launch { // start an async task
                _state.update { it.copy(isLoading = true) } // show the spinner

                val itemsFeed = async { // start the network call concurrently
                    api.getItemsFeed( // request items...
                        mutableMapOf<String, String>().apply { // ...with a params map
                            put(KEY_PAGE, INITIAL_PAGE.toString()) // page 0
                        },
                    )
                }

                setFavoriteItem(itemBox.itemId) // meanwhile, read the favorite status

                _state.update { // update state...
                    it.copy( // ...with a copy
                        isLoading = false, // hide the spinner
                        catalogItemListResponse = itemsFeed.await(), // wait for and store the suggestions
                    )
                }
            }
        }
    }

    private fun setFavoriteItem(itemId: String) { // read and store favorite status
        val isFavorite = favoriteItemRepository.isItemFavorite(itemId) // ask the store

        _state.update { it.copy(isFavorite = isFavorite) } // put it in state
    }

    fun onFavoriteClick() { // called when the heart is tapped
        viewModelScope.launch { // start an async task
            _state.update { it.copy(isLoading = true) } // show the spinner

            val isFavorite = favoriteItemRepository.toggleFavoriteItem(itemBox.itemId).first() // toggle, take the new state

            _state.update { // update state...
                it.copy( // ...with a copy
                    isLoading = false, // hide the spinner
                    isFavorite = isFavorite // store the new favorite state
                )
            }
        }
    }

    companion object {

        private const val INITIAL_PAGE = 0 // the page requested for suggestions
    }
}