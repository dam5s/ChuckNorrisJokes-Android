package io.damo.chucknorrisjokes

import org.junit.Assert
import java.util.*

fun waitFor(assertion: () -> Unit) {
    val start = Date().time
    var failing = true

    while (failing) {
        try {
            assertion()
            failing = false
        } catch (e: AssertionError) {
            val now = Date().time

            if (now - start > 2000) {
                Assert.fail("Timed out waiting for assertion")
            }

            Thread.sleep(100)
        }
    }
}
