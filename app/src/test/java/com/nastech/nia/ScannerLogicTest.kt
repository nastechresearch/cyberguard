package com.nastech.nia

import com.nastech.nia.data.model.ThreatLevel
import com.nastech.nia.data.repository.ScannerLogic
import org.junit.Assert.assertEquals
import org.junit.Test

class ScannerLogicTest {

    @Test
    fun cleanApp_isSafe() {
        assertEquals(
            ThreatLevel.SAFE,
            ScannerLogic.classify("com.example.gallery", listOf("android.permission.INTERNET"))
        )
    }

    @Test
    fun cgApp_alwaysSafe() {
        assertEquals(
            ThreatLevel.SAFE,
            ScannerLogic.classify(
                "com.nastech.nia.cleaner",
                listOf("android.permission.SEND_SMS")
            )
        )
    }

    @Test
    fun sensitivePermissions_isMedium() {
        assertEquals(
            ThreatLevel.MEDIUM,
            ScannerLogic.classify(
                "com.example.messenger",
                listOf("android.permission.READ_SMS")
            )
        )
    }

    @Test
    fun spyPackage_isCritical() {
        assertEquals(
            ThreatLevel.CRITICAL,
            ScannerLogic.classify(
                "com.evil.spyware",
                emptyList()
            )
        )
    }

    @Test
    fun describe_mapsEveryLevel() {
        assertEquals("No known issues", ScannerLogic.describe(ThreatLevel.SAFE))
        assertEquals("Review requested permissions", ScannerLogic.describe(ThreatLevel.LOW))
        assertEquals("Sensitive permissions requested", ScannerLogic.describe(ThreatLevel.MEDIUM))
        assertEquals("Potential malicious signature", ScannerLogic.describe(ThreatLevel.CRITICAL))
    }
}