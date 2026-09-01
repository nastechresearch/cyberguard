package com.nastech.nia.core.security

/**
 * Wi-Fi network risk classification. Pure logic on SSID/security type names.
 * Secure = WPA2/WPA3/WEP (any encryption). Open = none -> high risk.
 */
object WifiSecurityLogic {

    enum class Security { OPEN, WEP, WPA2, WPA3, UNKNOWN }
    enum class Risk { SAFE, WARNING, DANGEROUS }

    fun parseSecurity(capabilities: String?): Security {
        val caps = capabilities ?: return Security.UNKNOWN
        return when {
            caps.contains("WPA3") -> Security.WPA3
            caps.contains("WPA2") || caps.contains("WPA") -> Security.WPA2
            caps.contains("WEP") -> Security.WEP
            caps.isEmpty() || caps.equals("none", ignoreCase = true) -> Security.OPEN
            else -> Security.OPEN
        }
    }

    fun assess(ssid: String?, capabilities: String?): Risk {
        val security = parseSecurity(capabilities)
        return when (security) {
            Security.OPEN -> Risk.DANGEROUS
            Security.WEP -> Risk.WARNING
            Security.WPA2, Security.WPA3 -> Risk.SAFE
            Security.UNKNOWN -> if (ssid.isNullOrBlank()) Risk.DANGEROUS else Risk.WARNING
        }
    }
}