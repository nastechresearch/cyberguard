package com.nastech.nia.ui.screens.wifi

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nastech.nia.core.security.WifiSecurityLogic
import com.nastech.nia.core.security.WifiSecurityLogic.Risk

data class WifiNetworkUi(
    val ssid: String,
    val capabilities: String,
    val risk: Risk
) {
    val security: WifiSecurityLogic.Security = WifiSecurityLogic.parseSecurity(capabilities)
}

class WifiScanViewModel : ViewModel() {

    private val _networks = mutableStateOf<List<WifiNetworkUi>>(emptyList())
    val networks: State<List<WifiNetworkUi>> = _networks

    private val _scanning = mutableStateOf(false)
    val scanning: State<Boolean> = _scanning

    private val _lastScan = mutableStateOf("Not scanned yet")
    val lastScan: State<String> = _lastScan

    fun scan(sample: List<Pair<String, String>> = demo()) {
        _scanning.value = true
        _networks.value = sample.map { (ssid, caps) ->
            WifiNetworkUi(ssid, caps, WifiSecurityLogic.assess(ssid, caps))
        }
        _lastScan.value = "Just now"
        _scanning.value = false
    }

    val openCount: Int get() = _networks.value.count { it.risk == Risk.DANGEROUS }

    fun clear() {
        _networks.value = emptyList()
        _lastScan.value = "Not scanned yet"
    }

    private fun demo(): List<Pair<String, String>> = listOf(
        "Home-WiFi" to "[WPA2-PSK-CCMP][ESS]",
        "CoffeeShop_Free" to "",
        "Office 5G" to "[WPA3-SAE][ESS]",
        "LegacyRouter" to "[WEP][ESS]"
    )

    companion object {
        fun factory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T = WifiScanViewModel() as T
            }
    }
}