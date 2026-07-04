package com.vinted.demovinted.pageobjects // package (folder path)

import androidx.recyclerview.widget.RecyclerView // the list/grid widget
import androidx.test.espresso.Espresso.onView // find a view on screen
import androidx.test.espresso.action.ViewActions.click // tap action
import androidx.test.espresso.assertion.ViewAssertions.matches // assert a view matches
import androidx.test.espresso.contrib.RecyclerViewActions // list scroll/tap actions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition // act on a list item
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed // "is visible" matcher
import androidx.test.espresso.matcher.ViewMatchers.withId // find by id
import com.vinted.demovinted.R // generated resource ids
import com.vinted.demovinted.TestUtils.hasItemCountAtLeast // our "at least N items" matcher
import com.vinted.demovinted.TestUtils.isAtBottom // our "scrolled to end" matcher
import com.vinted.demovinted.TestUtils.waitForCondition // our wait helper

class FeedPage { // page object wrapping the feed screen

    fun assertDisplayed() = apply { // check the feed is shown (returns this for chaining)
        onView(withId(R.id.container)).check(matches(isDisplayed())) // the container is visible
    }

    fun waitForItems(minCount: Int) = apply { // wait until enough items load
        onView(withId(R.id.feed_list)).perform(waitForCondition(hasItemCountAtLeast(minCount))) // poll until >= minCount
    }

    fun openItem(position: Int) = apply { // tap an item to open it
        onView(withId(R.id.feed_list)).perform( // on the grid...
            actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()) // ...click item at position
        )
    }

    fun scrollToBottom() = apply { // scroll to the last item
        onView(withId(R.id.feed_list)) // on the grid...
            .perform(RecyclerViewActions.scrollToLastPosition<RecyclerView.ViewHolder>()) // ...scroll to the end
    }

    fun assertAtBottom() = apply { // check we are at the end
        onView(withId(R.id.feed_list)).check(matches(isAtBottom())) // the list is at the bottom
    }
}