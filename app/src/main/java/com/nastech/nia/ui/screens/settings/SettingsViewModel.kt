package com.nastech.nia.ui.screens.settings

import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nastech.nia.CyberGuardApp
import com.nastech.nia.BuildConfig

class SettingsViewModel : ViewModel() {

    private val _version = mutableStateOf(BuildConfig.VERSION_NAME)
    val version: State<String> = _version

    private val _androidVersion = mutableStateOf(Build.VERSION.RELEASE)
    val androidVersion: State<String> = _androidVersion

    private val _securityScore = mutableStateOf(92)
    val securityScore: State<Int> = _securityScore

    private val _lastScan = mutableStateOf("Today, 22:00")
    val lastScan: State<String> = _lastScan

    fun refresh() {
        _lastScan.value = "Just now"
    }

    companion object {
        fun factory(): androidx.lifecycle.ViewModelProvider.Factory =
            object : androidx.lifecycle.ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : androidx.lifecycle.ViewModel> create(
                    modelClass: Class<T>
                ): T = SettingsViewModel() as T
            }
    }
}