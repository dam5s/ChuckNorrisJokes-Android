package io.damo.chucknorrisjokes

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class FakeIcndb : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        when (request.path) {
            "/jokes/random?exclude=[explicit]&escape=javascript" -> return randomJokes.removeAt(0)
            "/jokes/random/3?limitTo=[nerdy]&escape=javascript" -> return nerdyJokes
            "/jokes/random/3?limitTo=[explicit]&escape=javascript" -> return explicitJokes
            "/jokes/random/3?exclude=[nerdy,explicit]&escape=javascript" -> return jokesWithoutCategory
            "/categories" -> return response("""
                { "type": "success", "value": [ "explicit", "nerdy" ] }
            """)
        }

        return notFound
    }


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

    val jokesWithoutCategory = response("""
        { "type": "success", "value": [
            { "id": 73, "joke": "Chuck Norris doesn't actually write books, the words assemble themselves out of fear.", "categories": [] },
            { "id": 419, "joke": "Count from one to ten. That's how long it would take Chuck Norris to kill you...Fourty seven times.", "categories": [] },
            { "id": 120, "joke": "Chuck Norris played Russian Roulette with a fully loaded gun and won.", "categories": [] }
        ]  }
    """)

    val nerdyJokes = response("""
        { "type": "success", "value": [
            { "id": 477, "joke": "Chuck Norris can access private methods.", "categories": ["nerdy"] },
            { "id": 563, "joke": "Chuck Norris causes the Windows Blue Screen of Death.", "categories": ["nerdy"] },
            { "id": 462, "joke": "Project managers never ask Chuck Norris for estimations... ever.", "categories": ["nerdy"] }
        ]  }
    """)

    val explicitJokes = response("""
        { "type": "success", "value": [
            { "id": 5, "joke": "Chuck Norris lost his virginity before his dad did.", "categories": ["explicit"] },
            { "id": 46, "joke": "Chuck Norris once ate three 72 oz. steaks in one hour. He spent the first 45 minutes having sex with his waitress.", "categories": ["explicit"] },
            { "id": 229, "joke": "One day Chuck Norris walked down the street with a massive erection. There were no survivors.", "categories": ["explicit"] }
        ]  }
    """)

    private val notFound = MockResponse().apply { setResponseCode(404) }

    private fun response(body: String) = MockResponse().apply { setBody(body) }
}
