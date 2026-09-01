package com.nastech.nia

import com.nastech.nia.core.security.PermissionAuditLogic
import com.nastech.nia.core.security.PermissionAuditLogic.PrivacyLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PermissionAuditLogicTest {

    @Test
    fun smsPermission_isHighRisk() {
        assertEquals(
            PrivacyLevel.HIGH,
            PermissionAuditLogic.assess(listOf("android.permission.READ_SMS"))
        )
    }

    @Test
    fun storageOnly_isMedium() {
        assertEquals(
            PrivacyLevel.MEDIUM,
            PermissionAuditLogic.assess(listOf("android.permission.READ_EXTERNAL_STORAGE"))
        )
    }

    @Test
    fun basicPermissions_isLow() {
        assertEquals(
            PrivacyLevel.LOW,
            PermissionAuditLogic.assess(listOf("android.permission.INTERNET"))
        )
    }

    @Test
    fun riskFlags_onlyListsRisky() {
        val flags = PermissionAuditLogic.riskFlags(
            listOf("android.permission.INTERNET", "android.permission.CAMERA", "android.permission.ACCESS_FINE_LOCATION")
        )
        assertTrue(flags.any { it.contains("CAMERA") })
        assertTrue(flags.any { it.contains("LOCATION") })
        assertEquals(2, flags.size)
    }
}