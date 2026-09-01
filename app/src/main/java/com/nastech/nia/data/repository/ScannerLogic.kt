package com.nastech.nia.data.repository

import com.nastech.nia.data.model.ThreatLevel

object ScannerLogic {

    private val CRITICAL_TERMS = listOf("hack", "spy")
    private val LOW_TERMS = listOf("cleaner", "booster")

    fun classify(packageName: String, permissions: List<String>): ThreatLevel = when {
        packageName.contains("cyberguard") -> ThreatLevel.SAFE
        CRITICAL_TERMS.any { packageName.contains(it) } -> ThreatLevel.CRITICAL
        permissions.any {
            it.contains("android.permission.SEND_SMS") ||
                it.contains("android.permission.RECORD_AUDIO") ||
                it.contains("android.permission.READ_SMS")
        } -> ThreatLevel.MEDIUM
        LOW_TERMS.any { packageName.contains(it) } -> ThreatLevel.LOW
        else -> ThreatLevel.SAFE
    }

    fun describe(level: ThreatLevel): String = when (level) {
        ThreatLevel.SAFE -> "No known issues"
        ThreatLevel.LOW -> "Review requested permissions"
        ThreatLevel.MEDIUM -> "Sensitive permissions requested"
        ThreatLevel.HIGH -> "High-risk activity detected"
        ThreatLevel.CRITICAL -> "Potential malicious signature"
    }
}