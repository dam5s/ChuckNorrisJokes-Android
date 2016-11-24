package io.damo.chucknorrisjokes.icndb

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.damo.chucknorrisjokes.icndb.Result.Error
import io.damo.chucknorrisjokes.icndb.Result.Success
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.reflect.KClass

class IcndbApi(val okHttp: OkHttpClient, val baseApiUrl: String) {

    val objectMapper = ObjectMapper().apply {
        registerKotlinModule()
        configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
    }


    fun fetchRandomJoke(): Result<Joke> = tryFetching("random joke") {
        val response = fetchJson("jokes/random?exclude=[explicit]&escape=javascript", JokeResponse::class)
        val joke = Joke(response.value.joke)

        Success(joke)
    }

    fun fetchCategories(): Result<List<Category>> = tryFetching("categories") {
        val response = fetchJson("categories", CategoriesResponse::class)
        val categories = response.value.map(::Category)

        Success(categories)
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


    data class JokeResponse(val value: JokeResponseValue)

    data class JokeResponseValue(val joke: String)

    data class CategoriesResponse(val value: List<String>)
}
