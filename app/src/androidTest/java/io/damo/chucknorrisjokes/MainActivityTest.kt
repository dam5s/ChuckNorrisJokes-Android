package io.damo.chucknorrisjokes

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.registerIdlingResources
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
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

        onView(withId(R.id.main_activity_button))
            .perform(click())

        waitFor {
            onView(withId(R.id.random_joke))
                .check(matches(withText("Chuck Norris insists on strongly-typed programming languages.")))
        }
    }


    private fun setupServiceLocatorWithFakeApi() {
        val baseApiUrl = mockWebServer!!.url("").toString()

        rule.activity.app.apply {
            serviceLocator = ServiceLocator(this, okHttp, baseApiUrl)
        }
    }
}
