package com.nastech.nia.ui.screens.applock

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastech.nia.data.model.InstalledApp
import com.nastech.nia.data.repository.AppLockRepository
import com.nastech.nia.service.applock.AppLockService
import kotlinx.coroutines.launch

class AppLockViewModel(
    private val repository: AppLockRepository,
    private val appContext: android.content.Context
) : ViewModel() {

    companion object {
        fun factory(context: android.content.Context): androidx.lifecycle.ViewModelProvider.Factory {
            val appContext = context.applicationContext
            val repository = AppLockRepository(
                appContext,
                com.nastech.nia.data.local.prefs.AppLockPreferences(appContext)
            )
            return object : androidx.lifecycle.ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : androidx.lifecycle.ViewModel> create(
                    modelClass: Class<T>
                ): T = AppLockViewModel(repository, appContext) as T
            }
        }
    }

    private val settingsState = mutableStateOf(
        AppLockUiState(enabled = false, hasPin = false, timeoutMinutes = 1)
    )
    val settings: State<AppLockUiState> = settingsState

    private val appsState = mutableStateOf<List<InstalledApp>>(emptyList())
    val apps: State<List<InstalledApp>> = appsState

    private val lockedState = mutableStateOf<Set<String>>(emptySet())
    val lockedPackages: State<Set<String>> = lockedState

    val processRunning = mutableStateOf(false)

    var isSettingUpPin by mutableStateOf(false)
        private set
    var pinInput by mutableStateOf("")
        private set
    var pinSetupDonePulse by mutableStateOf(false)
        private set

    init {
        // Settings + locked packages reactive state
        viewModelScope.launch {
            repository.settings.collect { s ->
                settingsState.value = AppLockUiState(
                    enabled = s.enabled,
                    hasPin = s.hasPin,
                    timeoutMinutes = s.timeoutMinutes
                )
            }
        }
        viewModelScope.launch {
            repository.lockedPackages.collect { locked ->
                lockedState.value = locked
            }
        }
        refreshApps()
    }

    fun refreshApps() {
        viewModelScope.launch {
            appsState.value = repository.getInstalledApps()
        }
    }

    fun toggleLocked(packageName: String) {
        viewModelScope.launch {
            val currently = lockedState.value
            repository.setLocked(packageName, packageName !in currently)
        }
    }

    fun toggleEnabled() {
        viewModelScope.launch {
            val nowEnabled = !settingsState.value.enabled
            repository.setEnabled(nowEnabled)
            if (nowEnabled) {
                AppLockService.start(appContext)
                processRunning.value = true
            } else {
                AppLockService.stop(appContext)
                processRunning.value = false
            }
        }
    }

    fun changeTimeout(minutes: Int) {
        viewModelScope.launch { repository.changeTimeout(minutes) }
    }

    fun startPinSetup() {
        isSettingUpPin = true
        pinInput = ""
        pinSetupDonePulse = false
    }

    fun cancelPinSetup() {
        isSettingUpPin = false
        pinInput = ""
    }

    fun appendPin(digit: Char) {
        if (pinInput.length < 6) pinInput += digit
    }

    fun deletePin() {
        if (pinInput.isNotEmpty()) pinInput = pinInput.dropLast(1)
    }

    fun confirmPin() {
        if (pinInput.length == 6) {
            viewModelScope.launch {
                repository.savePin(pinInput)
                pinSetupDonePulse = true
            }
            isSettingUpPin = false
            pinInput = ""
        }
    }
}

data class AppLockUiState(
    val enabled: Boolean = false,
    val hasPin: Boolean = false,
    val timeoutMinutes: Int = 1
)