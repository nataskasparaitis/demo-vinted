package com.vinted.demovinted // package (folder path)

import androidx.test.ext.junit.rules.ActivityScenarioRule // launches an Activity per test
import androidx.test.ext.junit.runners.AndroidJUnit4 // the Android test runner
import androidx.test.filters.LargeTest // marks a slow/large test
import com.vinted.demovinted.application.MainActivity // the screen to launch
import com.vinted.demovinted.pageobjects.FeedPage // the feed page object
import org.junit.Rule // marks a JUnit rule
import org.junit.Test // marks a test method
import org.junit.runner.RunWith // selects the test runner

@RunWith(AndroidJUnit4::class) // run on a device/emulator
@LargeTest // size category
class FeedFragmentInstrumentedTest { // end-to-end test for the feed

    @get:Rule // attach to every test
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java) // launch MainActivity

    @Test // a test
    fun onFragmentDisplayed_checkItemCount_scrollToBottom() { // load, then scroll to the end
        FeedPage() // on the feed...
            .assertDisplayed() // it is shown
            .waitForItems(DEFAULT_PAGE_SIZE) // wait for 20 items
            .scrollToBottom() // scroll to the last item
            .assertAtBottom() // we are at the bottom
    }

    companion object {

        const val DEFAULT_PAGE_SIZE = 20 // the API returns 20 items per page
    }
}