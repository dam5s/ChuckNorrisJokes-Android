package io.damo.chucknorrisjokes.favorites

import io.damo.chucknorrisjokes.icndb.Joke
import io.damo.chucknorrisjokes.utils.Result
import io.damo.chucknorrisjokes.utils.Result.Error
import io.damo.chucknorrisjokes.utils.Result.Success
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FavoritesTest {

    lateinit var fileStorage: FakeFileStorage
    lateinit var favorites: Favorites

    @Before
    fun setup() {
        fileStorage = FakeFileStorage()
        favorites = Favorites(fileStorage)
    }

    @Test
    fun testCanAdd() {
        val joke = Joke(1, "Oh hello")

        assertThat(favorites.canAdd(joke), equalTo(true))

        fileStorage.save("favorites.json", """[{"id":1,"text":"Oh hello"}]""")
        favorites.load()

        assertThat(favorites.canAdd(joke), equalTo(false))
    }

    @Test
    fun testAdd_ReturnsResultWithIndexOfJoke() {
        favorites.add(Joke(1, "Oh hello"))
            .then { assertThat(it, equalTo(0)) }
            .otherwise { fail("Expected success") }

        assertThat(fileStorage.files["favorites.json"], equalTo("""[{"id":1,"text":"Oh hello"}]"""))


        favorites.add(Joke(3, "Hello World!"))
            .then { assertThat(it, equalTo(1)) }
            .otherwise { fail("Expected success") }

        assertThat(fileStorage.files["favorites.json"], equalTo("""[{"id":1,"text":"Oh hello"},{"id":3,"text":"Hello World!"}]"""))


        favorites.add(Joke(2, "What's up!"))
            .then { assertThat(it, equalTo(1)) }
            .otherwise { fail("Expected success") }

        assertThat(fileStorage.files["favorites.json"], equalTo("""[{"id":1,"text":"Oh hello"},{"id":2,"text":"What's up!"},{"id":3,"text":"Hello World!"}]"""))
    }

    @Test
    fun testAll() {
        fileStorage.save("favorites.json", """[{"id":1,"text":"Oh hello"},{"id":2,"text":"Hello World!"}]""")
        favorites.load()

        assertThat(favorites.all(), equalTo(listOf(
            Joke(1, "Oh hello"),
            Joke(2, "Hello World!")
        )))
    }

    @Test
    fun testLoad_WhenNoFile() {
        favorites.load()

        assertThat(favorites.all(), equalTo(emptyList()))
    }
}

class FakeFileStorage : FileStorage {

    val files = hashMapOf<String, String>()

    override fun save(name: String, content: String) {
        files.put(name, content)
    }

    override fun load(name: String): Result<String> {
        val content = files[name]

        if (content == null) {
            return Error("Oops")
        } else {
            return Success(content)
        }
    }
}
