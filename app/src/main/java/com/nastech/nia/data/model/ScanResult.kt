package com.nastech.nia.data.model

enum class ThreatLevel { SAFE, LOW, MEDIUM, HIGH, CRITICAL }

data class ScanResult(
    val packageName: String,
    val appName: String,
    val threat: ThreatLevel,
    val description: String
)