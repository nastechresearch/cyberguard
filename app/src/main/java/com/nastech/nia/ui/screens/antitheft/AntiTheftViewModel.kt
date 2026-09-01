package com.nastech.nia.ui.screens.antitheft

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastech.nia.data.repository.AntiTheftRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AntiTheftViewModel(
    private val repository: AntiTheftRepository
) : ViewModel() {

    private val _armed = mutableStateOf(false)
    val armed: State<Boolean> = _armed

    private val _location = mutableStateOf<String?>(null)
    val location: State<String?> = _location

    private val _simAlertEnabled = mutableStateOf(false)
    val simAlertEnabled: State<Boolean> = _simAlertEnabled

    private val _smsCommandsEnabled = mutableStateOf(false)
    val smsCommandsEnabled: State<Boolean> = _smsCommandsEnabled

    private val _busy = mutableStateOf(false)
    val busy: State<Boolean> = _busy

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> = _message

    private var locationJob: Job? = null

    init {
        viewModelScope.launch {
            repository.armed.collectLatest { _armed.value = it }
        }
        viewModelScope.launch {
            repository.lastKnownLocation.collectLatest { _location.value = it }
        }
        viewModelScope.launch {
            repository.simAlertEnabled.collectLatest { _simAlertEnabled.value = it }
        }
        viewModelScope.launch {
            repository.smsCommandsEnabled.collectLatest { _smsCommandsEnabled.value = it }
        }
    }

    fun toggleArmed() = viewModelScope.launch { repository.setArmed(!_armed.value) }

    fun toggleSimAlert() = viewModelScope.launch { repository.setSimAlertEnabled(!_simAlertEnabled.value) }

    fun toggleSmsCommands() = viewModelScope.launch { repository.setSmsCommandsEnabled(!_smsCommandsEnabled.value) }

    fun locate() {
        if (_busy.value) return
        _busy.value = true
        _message.value = null
        locationJob = viewModelScope.launch {
            _location.value = repository.locate()
            _message.value = "Device located"
            _busy.value = false
        }
    }

    fun triggerAlarm() {
        _message.value = "Alarm triggered (audible) — simulate in real device"
    }

    fun lockDevice() {
        _message.value = "Device lock requested"
        viewModelScope.launch {
            repository.remoteLock()
        }
    }

    companion object {
        fun factory(): androidx.lifecycle.ViewModelProvider.Factory {
            val repo = AntiTheftRepository(com.nastech.nia.CyberGuardApp.context())
            return object : androidx.lifecycle.ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : androidx.lifecycle.ViewModel> create(
                    modelClass: Class<T>
                ): T = AntiTheftViewModel(repo) as T
            }
        }
    }
}