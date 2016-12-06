package io.damo.chucknorrisjokes

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class FakeIcndb : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        when (request.path) {
            "/jokes/random/5?exclude=[explicit]&escape=javascript" -> return randomJokes.removeAt(0)
            "/jokes/random/3?limitTo=[nerdy]&escape=javascript" -> return nerdyJokes
            "/jokes/random/3?limitTo=[explicit]&escape=javascript" -> return explicitJokes
            "/jokes/random/3?exclude=[nerdy,explicit]&escape=javascript" -> return jokesWithoutCategory
            "/categories" -> return response("""
                { "type": "success", "value": [ "explicit", "nerdy" ] }
            """)
        }

        return notFound
    }


    //language=JSON
    val randomJokes = mutableListOf(
        response("""
            { "type": "success", "value": [
                { "id": 92, "joke": "Chuck Norris invented black. In fact, he invented the entire spectrum of visible light. Except pink. Tom Cruise invented pink.", "categories": [] },
                { "id": 255, "joke": "Chuck Norris does not have to answer the phone. His beard picks up the incoming electrical impulses and translates them into audible sound.", "categories": [] },
                { "id": 194, "joke": "The US did not boycott the 1980 Summer Olympics in Moscow due to political reasons: Chuck Norris killed the entire US team with a single round-house kick during TaeKwonDo practice.", "categories": [] }
            ] }
        """),
        response("""
            { "type": "success", "value": [
                { "id": 217, "joke": "Crime does not pay - unless you are an undertaker following Walker, Texas Ranger, on a routine patrol.", "categories": [] },
                { "id": 215, "joke": "Chuck Norris just says \"no\" to drugs. If he said \"yes\", it would collapse Colombia's infrastructure.", "categories": [] },
                { "id": 73, "joke": "Chuck Norris doesn't actually write books, the words assemble themselves out of fear.", "categories": [] }
            ] }
        """),
        response("""
            { "type": "success", "value": [
                { "id": 34, "joke": "The opening scene of the movie \"Saving Private Ryan\" is loosely based on games of dodgeball Chuck Norris played in second grade.", "categories": [] },
                { "id": 407, "joke": "Chuck Norris originally wrote the first dictionary. The definition for each word is as follows - A swift roundhouse kick to the face.", "categories": [] },
                { "id": 367, "joke": "Not everyone that Chuck Norris is mad at gets killed. Some get away. They are called astronauts.", "categories": [] }
            ] }
        """)
    )

    //language=JSON
    val jokesWithoutCategory = response("""
        { "type": "success", "value": [
            { "id": 73, "joke": "Chuck Norris doesn't actually write books, the words assemble themselves out of fear.", "categories": [] },
            { "id": 419, "joke": "Count from one to ten. That's how long it would take Chuck Norris to kill you...Fourty seven times.", "categories": [] },
            { "id": 120, "joke": "Chuck Norris played Russian Roulette with a fully loaded gun and won.", "categories": [] }
        ]  }
    """)

    //language=JSON
    val nerdyJokes = response("""
        { "type": "success", "value": [
            { "id": 477, "joke": "Chuck Norris can access private methods.", "categories": ["nerdy"] },
            { "id": 563, "joke": "Chuck Norris causes the Windows Blue Screen of Death.", "categories": ["nerdy"] },
            { "id": 462, "joke": "Project managers never ask Chuck Norris for estimations... ever.", "categories": ["nerdy"] }
        ]  }
    """)

    //language=JSON
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
