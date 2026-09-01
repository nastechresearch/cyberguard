package com.nastech.nia.ui.screens.scanner

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastech.nia.data.model.ScanResult
import com.nastech.nia.data.repository.ScannerRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ScannerViewModel(
    private val repository: ScannerRepository
) : ViewModel() {

    companion object {
        fun factory(): androidx.lifecycle.ViewModelProvider.Factory {
            val ctx = com.nastech.nia.CyberGuardApp.context()
            val repository = ScannerRepository(ctx)
            return object : androidx.lifecycle.ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : androidx.lifecycle.ViewModel> create(
                    modelClass: Class<T>
                ): T = ScannerViewModel(repository) as T
            }
        }
    }

    var isScanning by mutableStateOf(false)
        private set

    var progress by mutableStateOf(0f)
        private set

    var scannedLabel by mutableStateOf("")
        private set

    var results by mutableStateOf<List<ScanResult>>(emptyList())
        private set

    var error by mutableStateOf<String?>(null)
        private set

    private var scanJob: Job? = null

    init {
        runScan()
    }

    fun runScan() {
        if (isScanning) return
        isScanning = true
        results = emptyList()
        progress = 0f
        error = null
        scanJob = viewModelScope.launch {
            repository.scanApps().collect { (current, total, currentResults) ->
                progress = if (total == 0) 1f else current.toFloat() / total
                scannedLabel = "$current / $total apps"
                results = currentResults
            }
            isScanning = false
        }
    }

    fun rescan() = runScan()

    override fun onCleared() {
        scanJob?.cancel()
        super.onCleared()
    }
}