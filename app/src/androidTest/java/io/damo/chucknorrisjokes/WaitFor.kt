package io.damo.chucknorrisjokes

import org.junit.Assert
import java.util.*

fun waitFor(atMost: Int = 2.seconds, assertion: () -> Unit) {
    val start = Date().time
    var failing = true

    while (failing) {
        try {
            assertion()
            failing = false
        } catch (e: AssertionError) {
            verifyTimeout(atMost, start)
        } catch (e: RuntimeException) {
            verifyTimeout(atMost, start)
        }
    }
}

private fun verifyTimeout(timeout: Int, start: Long) {
    val now = Date().time

    if (now - start > timeout) {
        Assert.fail("Timed out waiting for assertion")
    }

    Thread.sleep(100)
}

val Int.seconds: Int
    get() = this * 1000
