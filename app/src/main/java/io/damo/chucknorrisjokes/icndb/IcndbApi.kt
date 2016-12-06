package io.damo.chucknorrisjokes.icndb

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.damo.chucknorrisjokes.utils.Result
import io.damo.chucknorrisjokes.utils.Result.Error
import io.damo.chucknorrisjokes.utils.Result.Success
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.reflect.KClass

class IcndbApi(val okHttp: OkHttpClient, val baseApiUrl: String) {

    val objectMapper = ObjectMapper().apply {
        registerKotlinModule()
        configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
    }


    fun fetchRandomJokes(): Result<List<Joke>> = tryFetching("random jokes") {
        val response = fetchJson("jokes/random/5?exclude=[explicit]&escape=javascript", JokeListResponse::class)

        Success(response.mapJokes())
    }

    fun fetchCategories(): Result<List<Category>> = tryFetching("categories") {
        val response = fetchJson("categories", CategoriesResponse::class)

        Success(response.mapCategories())
    }

    fun fetchCategoryJokes(name: String): Result<List<Joke>> = tryFetching("category $name jokes") {
        val response = fetchJson(categoryPath(name), JokeListResponse::class)

        Success(response.mapJokes())
    }

    private fun categoryPath(name: String): String {
        if (name == "none") {
            return "jokes/random/3?exclude=[nerdy,explicit]&escape=javascript"
        }

        return "jokes/random/3?limitTo=[$name]&escape=javascript"
    }


    private fun <T> tryFetching(name: String, fetch: () -> Success<T>): Result<T> {
        try {
            return fetch()

        } catch (e: IOException) {
            return Error("There was an error when fetching $name.")
        }
    }

    private fun <T : Any> fetchJson(path: String, kClass: KClass<T>): T {
        val httpRequest = Request.Builder()
            .url("$baseApiUrl$path")
            .build()

        val httpResponse = okHttp.newCall(httpRequest).execute()
        val body = httpResponse.body().byteStream()

        return objectMapper.readValue(body, kClass.java)
    }


    data class SingleJokeResponse(val value: JokeJson) {
        fun mapJoke() = value.mapJoke()
    }

    data class JokeListResponse(val value: List<JokeJson>) {
        fun mapJokes() = value.map { it.mapJoke() }
    }

    data class JokeJson(val id: Int, val joke: String) {
        fun mapJoke() = Joke(id, joke)
    }

    data class CategoriesResponse(val value: List<String>) {
        fun mapCategories() = value.map(::Category)
    }
}
