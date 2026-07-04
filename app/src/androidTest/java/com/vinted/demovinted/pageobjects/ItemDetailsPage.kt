package com.vinted.demovinted.pageobjects // package (folder path)

import androidx.test.espresso.Espresso.onView // find a view on screen
import androidx.test.espresso.action.ViewActions.click // tap action
import androidx.test.espresso.action.ViewActions.doubleClick // double-tap action
import androidx.test.espresso.assertion.ViewAssertions.matches // assert a view matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed // "is visible" matcher
import androidx.test.espresso.matcher.ViewMatchers.withId // find by id
import com.vinted.demovinted.R // generated resource ids
import com.vinted.demovinted.TestUtils.withDrawable // our "shows this image" matcher

class ItemDetailsPage { // page object wrapping the details screen

    fun assertDisplayed() = apply { // check the details screen is shown
        onView(withId(R.id.item_details_container)).check(matches(isDisplayed())) // container is visible
    }

    fun assertFavorited() = apply { // check the heart shows the filled icon
        onView(withId(R.id.button_favorite)).check(matches(withDrawable(R.drawable.ic_favorite))) // filled heart
    }

    fun assertNotFavorited() = apply { // check the heart shows the outline icon
        onView(withId(R.id.button_favorite)).check(matches(withDrawable(R.drawable.ic_unfavorite))) // outline heart
    }

    fun clickFavorite() = apply { // tap the heart once
        onView(withId(R.id.button_favorite)).perform(click()) // single tap
    }

    fun doubleClickFavorite() = apply { // tap the heart twice quickly
        onView(withId(R.id.button_favorite)).perform(doubleClick()) // double tap
    }
}