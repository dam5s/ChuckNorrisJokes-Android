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

        val result = fileStorage.load("favorites.json")
        when (result) {
            is Success -> {
                jokes = mapper.readValue(result.value, jokesTypeRef)
            }
        }
    }

    fun canAdd(joke: Joke) = !jokes.contains(joke)

    fun add(joke: Joke): Result<Boolean> {
        jokes.add(joke)
        persist()

        return Success(true)
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
