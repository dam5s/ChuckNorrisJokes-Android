package io.damo.chucknorrisjokes

import android.support.test.espresso.Espresso
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
abstract class TestWithFakeIcndb {

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

        Espresso.registerIdlingResources(OkHttp3IdlingResource.create("OkHttp", okHttp))
    }

    @After
    fun tearDown() {
        mockWebServer?.shutdown()
    }

    fun setupServices() {
        val baseApiUrl = mockWebServer!!.url("").toString()

        val filesDir = rule.activity.app.filesDir
        File(filesDir, "favorites.json").delete()

        rule.activity.app.apply {
            serviceLocator = ServiceLocator(this, okHttp, baseApiUrl)
        }
    }
}
