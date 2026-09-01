package com.nastech.nia.ui.screens.junkcleaner

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nastech.nia.CyberGuardApp
import com.nastech.nia.data.repository.JunkCleanerRepository
import com.nastech.nia.data.repository.JunkItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JunkCleanerViewModel : ViewModel() {

    private val _items = mutableStateOf<List<JunkItem>>(emptyList())
    val items: State<List<JunkItem>> = _items

    private val _scanning = mutableStateOf(false)
    val scanning: State<Boolean> = _scanning

    private val _freedBytes = mutableStateOf(0L)
    val freedBytes: State<Long> = _freedBytes

    private val _hasScanned = mutableStateOf(false)
    val hasScanned: State<Boolean> = _hasScanned

    fun scan() {
        _scanning.value = true
        _hasScanned.value = false
        viewModelScope.launch(Dispatchers.IO) {
            val found = JunkCleanerRepository.scan(CyberGuardApp.context())
            _items.value = found
            _hasScanned.value = true
            _scanning.value = false
        }
    }

    fun clean() {
        viewModelScope.launch(Dispatchers.IO) {
            val freed = JunkCleanerRepository.delete(_items.value)
            _freedBytes.value = freed.toLong()
            _items.value = emptyList()
        }
    }

    val totalBytes: Long get() = JunkCleanerRepository.totalBytes(_items.value)

    companion object {
        fun factory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T = JunkCleanerViewModel() as T
            }
    }
}