package com.nastech.nia.ui.screens.privacy

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nastech.nia.core.security.PermissionAuditLogic
import com.nastech.nia.core.security.PermissionAuditLogic.PrivacyLevel

data class AppPermissionAudit(
    val appName: String,
    val packageName: String,
    val permissions: List<String>,
    val level: PrivacyLevel,
    val riskFlags: List<String>
)

class PrivacyViewModel : ViewModel() {

    private val _apps = mutableStateOf<List<AppPermissionAudit>>(emptyList())
    val apps: State<List<AppPermissionAudit>> = _apps

    private val _selected = mutableStateOf<AppPermissionAudit?>(null)
    val selected: State<AppPermissionAudit?> = _selected

    fun audit(sample: List<Pair<String, List<String>>> = demo()) {
        _apps.value = sample.map { (pkg, perms) ->
            AppPermissionAudit(
                appName = pkg.substringAfterLast('.'),
                packageName = pkg,
                permissions = perms,
                level = PermissionAuditLogic.assess(perms),
                riskFlags = PermissionAuditLogic.riskFlags(perms)
            )
        }
    }

    fun select(app: AppPermissionAudit) {
        _selected.value = app
    }

    val highRiskCount: Int get() = _apps.value.count { it.level == PrivacyLevel.HIGH }

    private fun demo(): List<Pair<String, List<String>>> = listOf(
        "com.some.messenger" to listOf("android.permission.READ_SMS", "android.permission.CAMERA"),
        "com.example.photo" to listOf("android.permission.READ_EXTERNAL_STORAGE", "android.permission.INTERNET"),
        "com.your.app" to listOf("android.permission.INTERNET")
    )

    companion object {
        fun factory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T = PrivacyViewModel() as T
            }
    }
}