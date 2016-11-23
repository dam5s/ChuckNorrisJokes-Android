package io.damo.chucknorrisjokes

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class FakeIcndb : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        when (request.path) {
            "/jokes/random?escape=javascript" -> return response(
                """
                    { "type": "success",
                      "value": {
                        "id": 502,
                        "joke": "Chuck Norris insists on strongly-typed programming languages.",
                        "categories": ["nerdy"]
                    } }
                """)
        }

        return notFound
    }

    private val notFound = MockResponse().apply { setResponseCode(404) }

    private fun response(body: String) = MockResponse().apply { setBody(body) }
}
