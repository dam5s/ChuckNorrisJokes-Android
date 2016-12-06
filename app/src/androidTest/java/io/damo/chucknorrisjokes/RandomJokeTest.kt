package io.damo.chucknorrisjokes

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeLeft
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.allOf
import org.junit.Test

class RandomJokeTest : TestWithFakeIcndb() {

    @Test
    fun test() {
        setupServices()
        onView(withId(R.id.mainActivityButton)).perform(click())

        checkJokeAndSwipe("Chuck Norris invented black. In fact, he invented the entire spectrum of visible light. Except pink. Tom Cruise invented pink.")

        checkJokeAndSwipe("Chuck Norris does not have to answer the phone. His beard picks up the incoming electrical impulses and translates them into audible sound.")

        checkJokeAndSwipe("The US did not boycott the 1980 Summer Olympics in Moscow due to political reasons: Chuck Norris killed the entire US team with a single round-house kick during TaeKwonDo practice.")

        checkJokeAndSwipe("Crime does not pay - unless you are an undertaker following Walker, Texas Ranger, on a routine patrol.")

        checkJokeAndSwipe("Chuck Norris just says \"no\" to drugs. If he said \"yes\", it would collapse Colombia's infrastructure.")
    }


    private fun checkJokeAndSwipe(expectedJoke: String) {
        waitFor {
            onDisplayedRandomJoke()
                .check(matches(withText(expectedJoke)))
                .perform(swipeLeft())
        }
    }

    private fun onDisplayedRandomJoke() = onView(allOf(
        withId(R.id.randomJoke),
        isDisplayed()
    ))
}
