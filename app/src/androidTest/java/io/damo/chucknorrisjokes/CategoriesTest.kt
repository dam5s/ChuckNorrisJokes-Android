package io.damo.chucknorrisjokes

import android.support.annotation.StringRes
import android.support.design.internal.BottomNavigationItemView
import android.support.design.widget.TabLayout
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.Toolbar
import org.hamcrest.Matchers.allOf
import org.junit.Test


class CategoriesTest : TestWithFakeIcndb() {

    @Test
    fun test() {
        setupServices()
        onView(withId(R.id.mainActivityButton)).perform(click())

        testCategories()

        clickBottomTab(R.string.favorites)

        testCategories()
    }

    private fun testCategories() {
        clickBottomTab(R.string.categories)
        checkTitle(R.string.categories)

        waitFor {
            checkTopTabWithTitle("none")
            checkTopTabWithTitle("nerdy")
            checkTopTabWithTitle("explicit")
        }

        checkJokeText(R.id.joke1, "Chuck Norris doesn't actually write books, the words assemble themselves out of fear.")
        checkJokeText(R.id.joke2, "Count from one to ten. That's how long it would take Chuck Norris to kill you...Fourty seven times.")
        checkJokeText(R.id.joke3, "Chuck Norris played Russian Roulette with a fully loaded gun and won.")

        clickTopTab("explicit")

        checkJokeText(R.id.joke1, "Chuck Norris lost his virginity before his dad did.")
        checkJokeText(R.id.joke2, "Chuck Norris once ate three 72 oz. steaks in one hour. He spent the first 45 minutes having sex with his waitress.")
        checkJokeText(R.id.joke3, "One day Chuck Norris walked down the street with a massive erection. There were no survivors.")

        clickTopTab("nerdy")

        checkJokeText(R.id.joke1, "Chuck Norris can access private methods.")
        checkJokeText(R.id.joke2, "Chuck Norris causes the Windows Blue Screen of Death.")
        checkJokeText(R.id.joke3, "Project managers never ask Chuck Norris for estimations... ever.")

        clickTopTab("none")
    }

    private fun checkJokeText(id: Int, text: String)
        = onView(allOf(
        isDescendantOfA(withId(id)),
        withId(R.id.text),
        isDisplayed()
    ))
        .check(matches(withText(text)))

    private fun clickTopTab(title: String)
        = onTopTab(title).perform(click())

    private fun checkTopTabWithTitle(title: String)
        = onTopTab(title).check(matches(isDisplayed()))

    private fun onTopTab(title: String)
        = onView(allOf(
        withText(title),
        isDescendantOfA(isAssignableFrom(TabLayout::class.java))
    ))

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
