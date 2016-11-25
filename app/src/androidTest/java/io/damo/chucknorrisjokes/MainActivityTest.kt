package io.damo.chucknorrisjokes

import android.support.annotation.StringRes
import android.support.design.internal.BottomNavigationItemView
import android.support.design.widget.TabLayout
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.registerIdlingResources
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.Toolbar
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


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
        setupServiceLocatorWithFakeApi()
        onView(withId(R.id.mainActivityButton)).perform(click())


        waitFor {
            onView(withId(R.id.randomJoke))
                .check(matches(withText("Chuck Norris insists on strongly-typed programming languages.")))
        }

        testCategories()

        clickBottomTab(R.string.random)
        waitFor {
            onView(withId(R.id.randomJoke))
                .check(matches(withText("Chuck Norris's brain waves are suspected to be harmful to cell phones.")))
        }

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

    private fun onTopTab(title: String): ViewInteraction {
        return onView(allOf(
            withText(title),
            isDescendantOfA(isAssignableFrom(TabLayout::class.java))
        ))
    }

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

    private fun setupServiceLocatorWithFakeApi() {
        val baseApiUrl = mockWebServer!!.url("").toString()

        rule.activity.app.apply {
            serviceLocator = ServiceLocator(this, okHttp, baseApiUrl)
        }
    }
}
