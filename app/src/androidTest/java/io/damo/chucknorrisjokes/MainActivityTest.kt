package io.damo.chucknorrisjokes

import android.support.annotation.StringRes
import android.support.design.internal.BottomNavigationItemView
import android.support.design.widget.TabLayout
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.registerIdlingResources
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.Toolbar
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    val okHttp = OkHttpClient()
    var mockWebServer: MockWebServer? = null

    @get:Rule
    val rule = ActivityTestRule(TestStarterActivity::class.java)

    @Before
    fun setup() {
        mockWebServer = MockWebServer().apply {
            setDispatcher(FakeIcndb())
            start()
        }

        registerIdlingResources(OkHttp3IdlingResource.create("OkHttp", okHttp))
    }

    @After
    fun tearDown() {
        mockWebServer?.shutdown()
    }

    @Test
    fun test() {
        setupServices()
        onView(withId(R.id.mainActivityButton)).perform(click())


        waitFor {
            onView(withId(R.id.randomJoke))
                .check(matches(withText("Chuck Norris insists on strongly-typed programming languages.")))
        }

        testFavorites()

        testCategories()

        clickBottomTab(R.string.random)
        waitFor {
            onView(withId(R.id.randomJoke))
                .check(matches(withText("In an act of great philanthropy, Chuck made a generous donation to the American Cancer Society. He donated 6,000 dead bodies for scientific research.")))
        }

        onView(withId(R.id.randomJoke)).perform(swipeDown())
        waitFor {
            onView(withId(R.id.randomJoke))
                .check(matches(withText("A study showed the leading causes of death in the United States are: 1. Heart disease, 2. Chuck Norris, 3. Cancer")))
        }

        testCategories()
    }

    private fun testFavorites() {
        val favoriteJokeText = "Chuck Norris's brain waves are suspected to be harmful to cell phones."


        // check empty favorites
        clickBottomTab(R.string.favorites)
        checkTitle(R.string.favorites)

        onView(withText(R.string.nothing_in_favorites))
            .check(matches(isDisplayed()))

        // add random joke to favorites
        clickBottomTab(R.string.random)

        waitFor {
            onView(withId(R.id.randomJoke)).check(matches(withText(favoriteJokeText)))
        }

        onView(withId(R.id.addToFavorites))
            .perform(click())
            .check(matches(not(isDisplayed())))

        // check we have the favorite then remove it
        clickBottomTab(R.string.favorites)

        onView(withText(favoriteJokeText))
            .check(matches(isDisplayed()))
            .perform(swipeRight())
            .check(matches(not(isDisplayed())))

        onView(withText(R.string.nothing_in_favorites))
            .check(matches(isDisplayed()))


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

        onView(withText(R.string.nothing_in_favorites))
            .check(matches(isDisplayed()))


        // check it's persisted
        clickBottomTab(R.string.categories)
        clickBottomTab(R.string.favorites)

        onView(withText(R.string.nothing_in_favorites)).check(matches(isDisplayed()))
    }

    private fun testCategories() {
        clickBottomTab(R.string.categories)
        checkTitle(R.string.categories)

        waitFor {
            checkTopTabWithTitle("none")
            checkTopTabWithTitle("nerdy")
            checkTopTabWithTitle("explicit")
        }

        onView(allOf(withId(R.id.joke1), isDisplayed()))
            .check(matches(withText("Chuck Norris doesn't actually write books, the words assemble themselves out of fear.")))
        onView(allOf(withId(R.id.joke2), isDisplayed()))
            .check(matches(withText("Count from one to ten. That's how long it would take Chuck Norris to kill you...Fourty seven times.")))
        onView(allOf(withId(R.id.joke3), isDisplayed()))
            .check(matches(withText("Chuck Norris played Russian Roulette with a fully loaded gun and won.")))

        clickTopTab("explicit")

        onView(allOf(withId(R.id.joke1), isDisplayed()))
            .check(matches(withText("Chuck Norris lost his virginity before his dad did.")))
        onView(allOf(withId(R.id.joke2), isDisplayed()))
            .check(matches(withText("Chuck Norris once ate three 72 oz. steaks in one hour. He spent the first 45 minutes having sex with his waitress.")))
        onView(allOf(withId(R.id.joke3), isDisplayed()))
            .check(matches(withText("One day Chuck Norris walked down the street with a massive erection. There were no survivors.")))

        clickTopTab("nerdy")

        onView(allOf(withId(R.id.joke1), isDisplayed()))
            .check(matches(withText("Chuck Norris can access private methods.")))
        onView(allOf(withId(R.id.joke2), isDisplayed()))
            .check(matches(withText("Chuck Norris causes the Windows Blue Screen of Death.")))
        onView(allOf(withId(R.id.joke3), isDisplayed()))
            .check(matches(withText("Project managers never ask Chuck Norris for estimations... ever.")))

        clickTopTab("none")
    }


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


    private fun setupServices() {
        val baseApiUrl = mockWebServer!!.url("").toString()

        val filesDir = rule.activity.app.filesDir
        File(filesDir, "favorites.json").delete()

        rule.activity.app.apply {
            serviceLocator = ServiceLocator(this, okHttp, baseApiUrl)
        }
    }
}
