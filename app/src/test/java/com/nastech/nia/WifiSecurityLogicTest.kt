package com.nastech.nia

import com.nastech.nia.core.security.WifiSecurityLogic
import com.nastech.nia.core.security.WifiSecurityLogic.Risk
import com.nastech.nia.core.security.WifiSecurityLogic.Security
import org.junit.Assert.assertEquals
import org.junit.Test

class WifiSecurityLogicTest {

    @Test
    fun openNetwork_isDangerous() {
        assertEquals(Risk.DANGEROUS, WifiSecurityLogic.assess("FreeWiFi", ""))
        assertEquals(Risk.DANGEROUS, WifiSecurityLogic.assess("FreeWiFi", "none"))
    }

    @Test
    fun wpa2_isSafe() {
        assertEquals(Risk.SAFE, WifiSecurityLogic.assess("Home", "[WPA2-PSK-CCMP][ESS]"))
    }

    @Test
    fun wpa3_isSafe() {
        assertEquals(Risk.SAFE, WifiSecurityLogic.assess("Home", "[WPA3-SAE][ESS]"))
    }

    @Test
    fun wep_isWarning() {
        assertEquals(Risk.WARNING, WifiSecurityLogic.assess("OldRouter", "[WEP][ESS]"))
    }

    @Test
    fun parseSecurity_detectsFormats() {
        assertEquals(Security.OPEN, WifiSecurityLogic.parseSecurity(""))
        assertEquals(Security.WPA2, WifiSecurityLogic.parseSecurity("[WPA2-PSK][ESS]"))
        assertEquals(Security.WPA3, WifiSecurityLogic.parseSecurity("[WPA3-SAE][ESS]"))
        assertEquals(Security.WEP, WifiSecurityLogic.parseSecurity("[WEP][ESS]"))
    }
}