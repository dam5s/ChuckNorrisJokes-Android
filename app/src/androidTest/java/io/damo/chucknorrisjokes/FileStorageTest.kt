package io.damo.chucknorrisjokes

import android.content.Context
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import io.damo.chucknorrisjokes.favorites.AndroidFileStorage
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class FileStorageTest {

    @get:Rule
    val rule = ActivityTestRule(TestStarterActivity::class.java)

    lateinit var fileStorage: AndroidFileStorage
    lateinit var context: Context
    lateinit var testFile: File


    @Before
    fun setup() {
        context = rule.activity.app
        fileStorage = AndroidFileStorage(context)

        testFile = File(context.filesDir, "foo.txt")

        testFile.delete()
        assertThat(testFile.isFile, equalTo(false))
    }

    @Test
    fun testSave() {
        fileStorage.save("foo.txt", "Hello World!")

        val saved = testFile.readText()

        assertThat(saved, equalTo("Hello World!"))
    }

    @Test
    fun testLoad_WhenFound() {
        testFile.writeText("Oh hai!!")

        fileStorage
            .load("foo.txt")
            .then {
                assertThat(it, equalTo("Oh hai!!"))
            }
            .otherwise {
                fail("Expected to have success")
            }
    }

    @Test
    fun testLoad_WhenNotFound() {
        fileStorage
            .load("foo.txt")
            .then {
                fail("Expected to have error")
            }
            .otherwise {
                assertThat(it, not(isEmptyString()))
            }
    }
}
