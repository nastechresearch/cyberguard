package com.nastech.nia

import com.nastech.nia.core.security.BreachChecker
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BreachCheckerTest {

    @Test
    fun knownPassword_isBreached() {
        assertTrue(BreachChecker.isPasswordBreached("password"))
    }

    @Test
    fun sha1Hex_matchesKnownVector() {
        // SHA-1 of "abc"
        assertEquals(
            "A9993E364706816ABA3E25717850C26C9CD0D89D",
            BreachChecker.sha1Hex("abc")
        )
    }

    @Test
    fun uniquePassword_notBreached() {
        assertEquals(false, BreachChecker.isPasswordBreached("Xq!9#fLm$2z@"))
    }

    @Test
    fun breachCount_onlyCountsMatches() {
        assertEquals(0, BreachChecker.breachCountMatch("Xq!9#fLm$2z@"))
    }
}