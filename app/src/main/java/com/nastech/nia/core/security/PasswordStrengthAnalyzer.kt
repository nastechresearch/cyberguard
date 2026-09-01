package com.nastech.nia.core.security

/** Password strength analysis — pure logic, unit-testable. */
object PasswordStrengthAnalyzer {

    enum class Level(val label: String, val score: Int) {
        WEAK("Weak", 0),
        FAIR("Fair", 1),
        GOOD("Good", 2),
        STRONG("Strong", 3)
    }

    fun analyze(password: String): Level {
        if (password.isBlank()) return Level.WEAK
        var score = 0
        if (password.length >= 8) score++
        if (password.length >= 12) score++
        if (Regex("[A-Z]").containsMatchIn(password) &&
            Regex("[a-z]").containsMatchIn(password)) score++
        if (Regex("[0-9]").containsMatchIn(password)) score++
        if (Regex("[^A-Za-z0-9]").containsMatchIn(password)) score++
        if (isCommon(password)) return Level.WEAK
        return when {
            score <= 2 -> Level.WEAK
            score <= 3 -> Level.FAIR
            score == 4 -> Level.GOOD
            else -> Level.STRONG
        }
    }

    private val COMMON = setOf(
        "password", "123456", "12345678", "qwerty", "abc123", "111111",
        "123123", "admin", "letmein", "000000", "password1"
    )

    fun isCommon(password: String): Boolean = password.lowercase() in COMMON

    fun entropy(password: String): Int {
        val charsetSize = when {
            password.any { it in "!@#$%^&*().,;:-_+=" } -> 94
            password.any { it.isDigit() } && password.any { it.isLetter() } -> 62
            password.any { it.isDigit() } || password.any { it.isLetter() } -> 36
            else -> 10
        }
        return (password.length * Math.log(charsetSize.toDouble()) / Math.log(2.0)).toInt()
    }
}