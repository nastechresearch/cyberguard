package com.nastech.nia.core.security

import java.security.MessageDigest

/**
 * Checks whether an email / password hash appears in known data-breach
 * dumps. Uses a local bloom-filter-like set (demo) matching the
 * "Have I Been Pwned" style SHA-1 prefixes. Production would query an API.
 */
object BreachChecker {

    private val KNOWN_BREACHED_SHA1 = setOf(
        "5BAA61E4C9B93F3F0682250B6CF8331B7EE68FD8", // "password"
        "7C4A8D09CA3762AF61E59520943DC26494F8941B"  // "123456"
    )

    fun sha1Hex(value: String): String =
        MessageDigest.getInstance("SHA-1")
            .digest(value.toByteArray())
            .joinToString("") { "%02X".format(it) }

    fun isPasswordBreached(password: String): Boolean =
        sha1Hex(password) in KNOWN_BREACHED_SHA1

    fun breachCountMatch(password: String): Int = if (isPasswordBreached(password)) 1 else 0
}