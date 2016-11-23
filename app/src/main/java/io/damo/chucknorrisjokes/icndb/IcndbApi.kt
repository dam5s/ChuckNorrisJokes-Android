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


    fun fetchRandomJoke(): Result<Joke> {
        try {
            val response = fetchJson("jokes/random?escape=javascript", JokeResponse::class)
            val joke = Joke(response.value.joke)
            return Success(joke)

        } catch (e: IOException) {
            return Error("There was an error when fetching random joke.")
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
}
