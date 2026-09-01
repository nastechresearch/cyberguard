package com.nastech.nia.core.security

/** Privacy audit — classifies requested app permissions into risk buckets. */
object PermissionAuditLogic {

    enum class PrivacyLevel(val label: String) {
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High")
    }

    private val HIGH_RISK = setOf(
        "SMS", "READ_SMS", "SEND_SMS", "RECEIVE_SMS",
        "READ_CONTACTS", "RECORD_AUDIO", "CAMERA",
        "READ_PHONE_STATE", "ACCESS_FINE_LOCATION", "READ_CALL_LOG"
    )

    private val MEDIUM_RISK = setOf(
        "READ_EXTERNAL_STORAGE", "WRITE_EXTERNAL_STORAGE",
        "ACCESS_COARSE_LOCATION", "READ_PHONE_NUMBERS", "WIFI"
    )

    fun assess(permissions: List<String>): PrivacyLevel {
        if (permissions.any { p -> HIGH_RISK.any { p.contains(it) } }) return PrivacyLevel.HIGH
        if (permissions.any { p -> MEDIUM_RISK.any { p.contains(it) } }) return PrivacyLevel.MEDIUM
        return PrivacyLevel.LOW
    }

    fun riskFlags(permissions: List<String>): List<String> =
        permissions.filter { p -> HIGH_RISK.any { p.contains(it) } || MEDIUM_RISK.any { p.contains(it) } }
}