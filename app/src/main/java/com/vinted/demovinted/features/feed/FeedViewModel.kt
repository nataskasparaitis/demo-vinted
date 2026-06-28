package com.vinted.demovinted.features.feed

import androidx.lifecycle.ViewModel // base class that holds screen state
import androidx.lifecycle.viewModelScope // coroutine scope tied to this ViewModel's life
import com.vinted.demovinted.core.currency.CurrencyFormatter // formats prices
import com.vinted.demovinted.features.feed.api.Api // the HTTP API
import com.vinted.demovinted.features.feed.api.Api.Companion.KEY_PAGE // "page" key
import com.vinted.demovinted.features.feed.api.Api.Companion.KEY_PER_PAGE // "per_page" key
import com.vinted.demovinted.features.feed.api.Api.Companion.KEY_TIME // "time" key
import com.vinted.demovinted.models.FeedItem // raw item model
import com.vinted.demovinted.models.ItemBox // UI item model
import dagger.hilt.android.lifecycle.HiltViewModel // lets Hilt build this ViewModel
import kotlinx.coroutines.channels.Channel // one-shot event pipe
import kotlinx.coroutines.flow.MutableStateFlow // writable observable state holder
import kotlinx.coroutines.flow.asStateFlow // expose it read-only
import kotlinx.coroutines.flow.receiveAsFlow // expose the channel as a stream
import kotlinx.coroutines.flow.update // atomically replace the state
import kotlinx.coroutines.launch // start a coroutine
import javax.inject.Inject // marks a constructor Hilt can call

@HiltViewModel // Hilt provides this ViewModel and its dependencies
class FeedViewModel @Inject constructor( // Hilt injects the two parameters below
    private val api: Api, // the network Api
    private val numberFormat: CurrencyFormatter, // the price formatter
) : ViewModel() { // it is an Android ViewModel

    private val _feedState = MutableStateFlow(State()) // private writable state, starts at default
    val feedState = _feedState.asStateFlow() // public read-only view of the state

    private val _feedEvent = Channel<Event>() // private one-shot event pipe
    val feedEvent = _feedEvent.receiveAsFlow() // public stream of events

    private val parameters = createParameters() // build the query params once

    init {
        requestItems() // load the feed immediately
    }

    private fun requestItems() { // fetch items from the network
        _feedState.update { state -> state.copy(status = State.Status.Loading) } // flip state to Loading
        viewModelScope.launch { // start a coroutine (async) task tied to this ViewModel
            try { // network calls can fail
                val result = api.getItemsFeed(parameters) // await the HTTP response
                _feedState.update { state -> // update the state...
                    state.copy( // ...by copying it with changes
                        status = State.Status.Success, // mark success
                        content = result.items.mapToUniqueItemBox()) // convert + deduplicate the items
                }
            } catch (e: Exception) { // if the request failed
                _feedEvent.send(Event.Error(e)) // emit a one-shot error event
            }
        }
    }

    private fun List<FeedItem>.mapToUniqueItemBox(): List<ItemBox> { // extension: convert a list of raw items
        val currentItems = feedState.value.content // items already shown
        val result = this.map { feedItem -> // for each raw item...
            ItemBox.fromFeedItem(feedItem, numberFormat) // ...make a UI item (formatting the price)
        }
        val itemIds = currentItems.map { it.itemId } // ids we already have
        return currentItems + result.filterNot { itemIds.contains(it.itemId) } // append only new ids (no duplicates)
    }

    private fun createParameters(): Map<String, String> { // build the query parameters
        val params = HashMap<String, String>() // an empty map
        params[KEY_PAGE] = "1" // request page 1
        params[KEY_PER_PAGE] = "20" // 20 items per page
        params[KEY_TIME] = "${System.currentTimeMillis()}" // current time, to bust caches
        return params // return the map
    }

    data class State( // a snapshot of the screen
        val status: Status = Status.Loading, // loading or success
        val content: List<ItemBox> = emptyList() // the items to show
    ) {

        sealed class Status { // a fixed set of statuses
            data object Loading : Status() // the loading case (singleton)
            data object Success : Status() // the success case (singleton)
        }
    }

    sealed class Event { // a fixed set of one-shot events
        data class Error(val t: Throwable) : Event() // an error carrying the exception
    }
}