package io.damo.chucknorrisjokes.favorites

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.damo.chucknorrisjokes.icndb.Joke
import io.damo.chucknorrisjokes.utils.Result
import io.damo.chucknorrisjokes.utils.Result.Success

class Favorites(val fileStorage: FileStorage) {

    companion object {
        fun load(fileStorage: FileStorage) =
            Favorites(fileStorage).apply { load() }
    }

    private var jokes: MutableList<Joke> = mutableListOf()

    private val jokesTypeRef = object : TypeReference<List<Joke>>() {}

    fun load() {
        jokes = mutableListOf()

        fileStorage
            .load("favorites.json")
            .then {
                jokes = mapper.readValue(it, jokesTypeRef)
            }
    }

    fun canAdd(joke: Joke) = !jokes.contains(joke)

    fun add(joke: Joke): Result<Int> {
        jokes.add(joke)
        jokes.sortBy(Joke::id)

        persist()

        return Success(jokes.indexOf(joke))
    }

    fun all(): List<Joke> = jokes


    private val mapper = ObjectMapper().apply { registerKotlinModule() }

    private fun persist() {
        fileStorage.save("favorites.json", mapper.writeValueAsString(jokes))
    }

    fun remove(joke: Joke) {
        jokes.remove(joke)
        persist()
    }
}
