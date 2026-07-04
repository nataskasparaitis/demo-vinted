package com.vinted.demovinted // package (folder path)

import androidx.test.ext.junit.rules.ActivityScenarioRule // launches an Activity per test
import androidx.test.ext.junit.runners.AndroidJUnit4 // the Android test runner
import androidx.test.filters.LargeTest // marks a slow/large test
import com.vinted.demovinted.FeedFragmentInstrumentedTest.Companion.DEFAULT_PAGE_SIZE // reuse the page-size constant
import com.vinted.demovinted.application.MainActivity // the screen to launch
import com.vinted.demovinted.pageobjects.FeedPage // the feed page object
import com.vinted.demovinted.pageobjects.ItemDetailsPage // the details page object
import org.junit.Rule // marks a JUnit rule
import org.junit.Test // marks a test method
import org.junit.runner.RunWith // selects the test runner

@RunWith(AndroidJUnit4::class) // run on a device/emulator
@LargeTest // size category
class ItemDetailsFragmentInstrumentedTest { // end-to-end tests for item details

    @get:Rule // attach to every test
    var activityRule: ActivityScenarioRule<MainActivity> = // launch MainActivity...
        ActivityScenarioRule(MainActivity::class.java) // ...before each test

    @Test // a test
    fun clickFeedItem_navigateToItemDetailFragment() { // tapping an item opens details
        FeedPage() // on the feed...
            .assertDisplayed() // it is shown
            .waitForItems(DEFAULT_PAGE_SIZE) // wait for 20 items
            .openItem(0) // tap the first item

        ItemDetailsPage().assertDisplayed() // the details screen is shown
    }

    @Test // a test
    fun clickFavoriteButton_favoriteItem() { // the heart toggles correctly
        FeedPage() // on the feed...
            .assertDisplayed() // it is shown
            .waitForItems(DEFAULT_PAGE_SIZE) // wait for 20 items
            .openItem(0) // open the first item

        ItemDetailsPage() // on the details screen...
            .assertNotFavorited() // starts not favorited
            .doubleClickFavorite() // toggle on then off
            .assertNotFavorited() // back to not favorited
            .clickFavorite() // toggle on
            .assertFavorited() // now favorited
    }
}