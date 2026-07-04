package com.vinted.demovinted.features.feed // package (folder path)

import app.cash.turbine.test // helper to assert on Flow emissions
import com.vinted.demovinted.core.currency.CurrencyFormatter // the formatter interface
import com.vinted.demovinted.DispatcherRule // swaps the Main dispatcher for tests
import com.vinted.demovinted.models.ItemBox // UI item model
import kotlinx.coroutines.test.runTest // runs coroutine tests
import org.junit.Assert.assertEquals // assert two values are equal
import org.junit.Assert.assertTrue // assert a condition is true
import org.junit.Rule // marks a JUnit rule
import org.junit.Test // marks a test method
import java.util.Locale // language/region
import kotlin.IllegalStateException // a standard exception type

class FeedViewModelTest { // unit tests for the feed ViewModel

    @get:Rule // attach this rule to every test
    val dispatcherRule = DispatcherRule() // make viewModelScope run on a test dispatcher

    private val fakeApi = FakeApi() // a fake API (no real network)
    private val inMemoryFormatter: CurrencyFormatter = CurrencyFormatter { // make a formatter from a lambda
        if (it == null) "" else "%.2f \u20AC".format(Locale.ENGLISH, it) // format the amount with a euro sign
    }
    private fun createFixture() = FeedViewModel(api = fakeApi, numberFormat = inMemoryFormatter) // build the ViewModel under test

    @Test // a test
    fun `initial requestMore invoke, emitted event is Loading with false and true`() = runTest { // error-path test
        val exception = IllegalStateException("Bummmm! :bomb") // the error the fake will throw
        fakeApi.exception = exception // make the fake API fail
        val fixture = createFixture() // create the ViewModel (its init triggers a load)

        fixture.feedEvent.test { // observe the event stream
            val eventReceived = expectMostRecentItem() // grab the latest event
            assertTrue(eventReceived is FeedViewModel.Event.Error) // it should be an Error event
            assertEquals((eventReceived as FeedViewModel.Event.Error).t, exception) // carrying our exception
            cancelAndIgnoreRemainingEvents() // stop observing
        }
    }

    @Test // a test
    fun `initial requestMore yields in a list of ItemBoxViewEntity with correct ids`() = runTest { // happy-path ids test
        val fixture = createFixture() // create the ViewModel
        val expected = listOf( // the ids we expect
            ItemBox(itemId = "1"), // item 1
            ItemBox(itemId = "2"), // item 2
            ItemBox(itemId = "3"), // item 3
        )

        fixture.feedState.test { // observe the state stream
            val content = expectMostRecentItem().content // the latest item list
            assertEquals(expected.map { it.itemId }, content.map { it.itemId }) // ids should match
        }
    }

    @Test // a test
    fun `requestMore yields in a list of ItemBoxViewEntity with price formatted`() = runTest { // price-format test
        val fixture = createFixture() // create the ViewModel
        val expected = listOf( // expected formatted prices
            ItemBox(itemId = "1", formattedPrice = "1.00 €"), // item 1 formatted
            ItemBox(itemId = "2", formattedPrice = "1.00 €"), // item 2 formatted
            ItemBox(itemId = "3", formattedPrice = "1.00 €"), // item 3 formatted
        )

        fixture.feedState.test { // observe the state stream
            val content = expectMostRecentItem().content // the latest item list
            assertEquals(expected.map { it.formattedPrice }, content.map { it.formattedPrice }) // prices should match
        }
    }
}