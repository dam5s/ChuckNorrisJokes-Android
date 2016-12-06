package io.damo.chucknorrisjokes

import android.support.annotation.StringRes
import android.support.design.internal.BottomNavigationItemView
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.Toolbar
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Test


class FavoritesTest : TestWithFakeIcndb() {

    @Test
    fun test() {
        setupServices()
        onView(withId(R.id.mainActivityButton)).perform(click())


        waitFor {
            onView(allOf(withId(R.id.randomJoke), isDisplayed()))
                .check(matches(withText("Chuck Norris invented black. In fact, he invented the entire spectrum of visible light. Except pink. Tom Cruise invented pink.")))
        }

        val favoriteJokeText = "Chuck Norris does not have to answer the phone. His beard picks up the incoming electrical impulses and translates them into audible sound."

        // check empty favorites
        clickBottomTab(R.string.favorites)
        checkTitle(R.string.favorites)

        checkNothingInFavorites()

        // add random joke to favorites
        clickBottomTab(R.string.random)

        onView(allOf(withId(R.id.randomJoke), isDisplayed()))
            .perform(swipeLeft())

        onView(allOf(withId(R.id.addToFavorites), isDisplayed()))
            .perform(click())

        onView(withId(R.id.removeFromFavorites))
            .perform(click())
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.addToFavorites))
            .perform(click())
            .check(matches(not(isDisplayed())))

        // check we have the favorite then remove it
        clickBottomTab(R.string.favorites)

        onView(withText(favoriteJokeText))
            .check(matches(isDisplayed()))
            .perform(swipeRight())

        checkNothingInFavorites()


        // undo, check and remove again
        waitFor {
            onView(withText(R.string.undo))
                .check(matches(isCompletelyDisplayed()))
        }

        onView(withText(R.string.undo)).perform(click())

        onView(withText(R.string.nothing_in_favorites))
            .check(matches(not(isDisplayed())))

        onView(withText(favoriteJokeText))
            .check(matches(isDisplayed()))
            .perform(swipeRight())
            .check(matches(not(isDisplayed())))

        checkNothingInFavorites()


        // check it's persisted
        clickBottomTab(R.string.categories)
        clickBottomTab(R.string.favorites)

        checkNothingInFavorites()

        // add to favorites from categories tab

        clickBottomTab(R.string.categories)

        onView(allOf(
            isDescendantOfA(withId(R.id.joke1)), isDisplayed(),
            withId(R.id.add)
        )).perform(click())

        // check it's persisted
        clickBottomTab(R.string.favorites)

        onView(withText("Chuck Norris doesn't actually write books, the words assemble themselves out of fear."))
            .check(matches(isDisplayed()))

        // remove it from categories tab add another one
        clickBottomTab(R.string.categories)

        onView(allOf(
            isDescendantOfA(withId(R.id.joke1)), isDisplayed(),
            withId(R.id.remove)
        )).perform(click())

        onView(allOf(
            isDescendantOfA(withId(R.id.joke2)), isDisplayed(),
            withId(R.id.add)
        )).perform(click())

        // check it's persisted
        clickBottomTab(R.string.favorites)

        onView(withText("Count from one to ten. That's how long it would take Chuck Norris to kill you...Fourty seven times."))
            .check(matches(isDisplayed()))
            .perform(swipeRight())

        checkNothingInFavorites()

        // check it's reflected from categories fragment
        clickBottomTab(R.string.categories)

        onView(allOf(
            isDescendantOfA(withId(R.id.joke2)), isDisplayed(),
            withId(R.id.add)
        )).check(matches(isDisplayed()))
    }


    private fun checkNothingInFavorites()
        = onView(withText(R.string.nothing_in_favorites)).check(matches(isDisplayed()))


    private fun clickBottomTab(@StringRes resId: Int) {
        onView(allOf(
            withText(resId),
            isDescendantOfA(isAssignableFrom(BottomNavigationItemView::class.java)),
            isDisplayed()
        ))
            .perform(click())
    }

    private fun checkTitle(@StringRes resId: Int) {
        onView(allOf(
            isDescendantOfA(isAssignableFrom(Toolbar::class.java)),
            isDisplayed()
        ))
            .check(matches(withText(resId)))
    }
}
