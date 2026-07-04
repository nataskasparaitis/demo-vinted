package com.vinted.demovinted // package (folder path)

import android.view.View // a UI element
import android.widget.ImageButton // an image button (the favorite heart)
import androidx.core.content.ContextCompat // safe resource access
import androidx.recyclerview.widget.LinearLayoutManager // list arrangement info
import androidx.recyclerview.widget.RecyclerView // the list/grid widget
import androidx.test.espresso.UiController // drives the UI thread in tests
import androidx.test.espresso.ViewAction // a custom test action
import androidx.test.espresso.matcher.BoundedMatcher // a matcher limited to one view type
import androidx.test.espresso.matcher.ViewMatchers // built-in view matchers
import org.hamcrest.Description // text describing a matcher
import org.hamcrest.Matcher // a reusable predicate over a view
import org.hamcrest.TypeSafeMatcher // a type-checked matcher
import java.util.concurrent.TimeoutException // thrown when waiting times out

object TestUtils { // shared test helper functions

    fun hasItemCount(count: Int): Matcher<View> = object : BoundedMatcher<View, RecyclerView>( // matches a list with exactly N items
        RecyclerView::class.java) {
        override fun describeTo(description: Description?) { // describe this matcher
            description?.appendText("RecyclerView with item count: $count") // text shown on failure
        }

        override fun matchesSafely(view: RecyclerView?): Boolean { // the actual check
            return view?.adapter?.itemCount == count // item count equals N?
        }
    }

    fun hasItemCountAtLeast(count: Int): Matcher<View> = object : BoundedMatcher<View, RecyclerView>( // matches at least N items
        RecyclerView::class.java) {
        override fun describeTo(description: Description?) { // describe this matcher
            description?.appendText("RecyclerView with at least $count items") // failure text
        }

        override fun matchesSafely(view: RecyclerView?): Boolean { // the actual check
            return (view?.adapter?.itemCount ?: 0) >= count // count (or 0) is at least N?
        }
    }

    fun isAtBottom(): Matcher<View> { // matches when the list is scrolled to the end
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) { // describe this matcher
                description?.appendText("is at bottom") // failure text
            }

            override fun matchesSafely(view: View?): Boolean { // the actual check
                if (view !is RecyclerView) return false // only applies to a RecyclerView
                val layoutManager = view.layoutManager as LinearLayoutManager // get its layout manager
                val itemCount = layoutManager.itemCount // total items
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition() // last visible index
                return lastVisibleItemPosition == itemCount - 1 // is the last item visible?
            }
        }
    }

    fun waitForCondition(viewCondition: Matcher<View>): ViewAction { // wait until a condition holds
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> { // which views this action accepts
                return ViewMatchers.isAssignableFrom(RecyclerView::class.java) // only RecyclerViews
            }

            override fun perform(uiController: UiController, view: View) { // run the action
                val endTime = System.currentTimeMillis() + 20000 // give up after 20 seconds
                do {
                    if (viewCondition.matches(view)) return // done if the condition holds
                    uiController.loopMainThreadForAtLeast(50) // else let the UI run 50ms and retry
                } while (System.currentTimeMillis() < endTime) // until the timeout
                throw TimeoutException() // timed out
            }

            override fun getDescription(): String { // describe this action
                return "wait for a specific view state" // shown in logs
            }
        }
    }

    fun withDrawable(drawableId: Int): BoundedMatcher<View, ImageButton> { // matches a button showing a given image
        return object : BoundedMatcher<View, ImageButton>(ImageButton::class.java) {
            override fun describeTo(description: Description) { // describe this matcher
                description.appendText("has drawable resource $drawableId") // failure text
            }

            override fun matchesSafely(view: ImageButton): Boolean { // the actual check
                val expectedDrawable = ContextCompat.getDrawable(view.context, drawableId) // the expected image
                val backgroundDrawable = view.background // the button's current background
                return expectedDrawable?.constantState == backgroundDrawable?.constantState // same image?
            }
        }
    }
}