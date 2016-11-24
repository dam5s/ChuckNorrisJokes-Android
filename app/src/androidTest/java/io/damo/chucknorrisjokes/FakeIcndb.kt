package io.damo.chucknorrisjokes

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class FakeIcndb : Dispatcher() {

    val randomJokes = mutableListOf(
        response("""
            { "type": "success",
              "value": {
                "id": 502,
                "joke": "Chuck Norris insists on strongly-typed programming languages.",
                "categories": ["nerdy"]
            } }
        """),
        response("""
            { "type": "success",
              "value": {
                "id": 545,
                "joke": "Chuck Norris's brain waves are suspected to be harmful to cell phones.",
                "categories": []
            } }
        """)
    )

    override fun dispatch(request: RecordedRequest): MockResponse {
        when (request.path) {
            "/jokes/random?escape=javascript" -> return randomJokes.removeAt(0)
        }

        return notFound
    }

    private val notFound = MockResponse().apply { setResponseCode(404) }

    private fun response(body: String) = MockResponse().apply { setBody(body) }
}
