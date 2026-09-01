package com.nastech.nia

import com.nastech.nia.core.security.PasswordStrengthAnalyzer
import org.junit.Assert.assertEquals
import org.junit.Test

class PasswordStrengthAnalyzerTest {

    @Test
    fun commonPassword_isWeak() {
        assertEquals(
            PasswordStrengthAnalyzer.Level.WEAK,
            PasswordStrengthAnalyzer.analyze("password")
        )
        assertEquals(
            PasswordStrengthAnalyzer.Level.WEAK,
            PasswordStrengthAnalyzer.analyze("123456")
        )
    }

    @Test
    fun blank_isWeak() {
        assertEquals(
            PasswordStrengthAnalyzer.Level.WEAK,
            PasswordStrengthAnalyzer.analyze("")
        )
    }

    @Test
    fun strongPassword_isStrong() {
        assertEquals(
            PasswordStrengthAnalyzer.Level.STRONG,
            PasswordStrengthAnalyzer.analyze("CorrectHorse_Batt3ry!")
        )
    }

    @Test
    fun goodsPassword_scoresGood() {
        assertEquals(
            PasswordStrengthAnalyzer.Level.GOOD,
            PasswordStrengthAnalyzer.analyze("Passw0rdNic3x")
        )
    }

    @Test
    fun entropy_growsWithLength() {
        val weak = PasswordStrengthAnalyzer.entropy("abc")
        val strong = PasswordStrengthAnalyzer.entropy("CorrectHorse_Batt3ry!9")
        assertEquals(true, strong > weak)
    }
}