package com.nastech.nia.ui.screens.applock

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nastech.nia.data.model.InstalledApp
import com.nastech.nia.service.applock.AppLockAppContextHolder
import com.nastech.nia.ui.theme.NeonCyan
import com.nastech.nia.ui.theme.SurfaceDark
import com.nastech.nia.ui.theme.SurfaceElevated
import com.nastech.nia.ui.theme.TextSecondary

@Composable
fun AppLockScreen() {
    val context = LocalContext.current
    if (AppLockAppContextHolder.context == null) {
        AppLockAppContextHolder.context = context.applicationContext
    }
    val vm: AppLockViewModel = viewModel(factory = AppLockViewModel.factory(context))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "App Lock",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Protect your sensitive apps with PIN or biometric",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Spacer(Modifier.height(8.dp))
                MasterToggleCard(
                    enabled = vm.settings.value.enabled,
                    hasPin = vm.settings.value.hasPin,
                    onToggleMaster = { vm.toggleEnabled() },
                    onSetupPin = { vm.startPinSetup() },
                    isSettingUpPin = vm.isSettingUpPin,
                    pin = vm.pinInput,
                    onAppendPin = { vm.appendPin(it) },
                    onDeletePin = { vm.deletePin() },
                    onConfirmPin = { vm.confirmPin() },
                    onCancelPinSetup = { vm.cancelPinSetup() },
                    pinSetupDone = vm.pinSetupDonePulse
                )
            }
            item {
                TimeoutCard(
                    minutes = vm.settings.value.timeoutMinutes,
                    onChange = { vm.changeTimeout(it) }
                )
            }
            item {
                Column {
                    Text(
                        text = "Select apps to protect",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${vm.apps.value.size} apps available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
            if (vm.apps.value.isEmpty()) {
                item {
                    EmptyAppsCard()
                }
            } else {
                items(vm.apps.value, key = { it.packageName }) { app ->
                    AppLockRow(
                        app = app,
                        isLocked = app.packageName in vm.lockedPackages.value,
                        onToggle = { vm.toggleLocked(app.packageName) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MasterToggleCard(
    enabled: Boolean,
    hasPin: Boolean,
    onToggleMaster: () -> Unit,
    onSetupPin: () -> Unit,
    isSettingUpPin: Boolean,
    pin: String,
    onAppendPin: (Char) -> Unit,
    onDeletePin: () -> Unit,
    onConfirmPin: () -> Unit,
    onCancelPinSetup: () -> Unit,
    pinSetupDone: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    tint = NeonCyan
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Master Lock",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (enabled) "Protection active" else "Protection off",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (enabled) NeonCyan else TextSecondary
                    )
                }
                Switch(
                    checked = enabled,
                    onCheckedChange = { onToggleMaster() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = NeonCyan,
                        checkedTrackColor = NeonCyan.copy(alpha = 0.4f)
                    )
                )
            }

            Spacer(Modifier.height(16.dp))

            if (!hasPin) {
                Text(
                    text = "Set a PIN to lock your apps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onSetupPin,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonCyan,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(Icons.Filled.Key, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Set PIN", fontWeight = FontWeight.Medium)
                }
            } else if (hasPin && !isSettingUpPin) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LockClock, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "PIN configured",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            if (isSettingUpPin) {
                PinSetupSection(
                    pin = pin,
                    onAppend = onAppendPin,
                    onDelete = onDeletePin,
                    onConfirm = onConfirmPin,
                    onCancel = onCancelPinSetup,
                    saved = pinSetupDone
                )
            }
        }
    }
}

@Composable
private fun PinSetupSection(
    pin: String,
    onAppend: (Char) -> Unit,
    onDelete: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    saved: Boolean
) {
    Spacer(Modifier.height(16.dp))
    Text(
        text = "Enter a 6-digit PIN",
        style = MaterialTheme.typography.bodyMedium,
        color = TextSecondary
    )
    Spacer(Modifier.height(12.dp))
    val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "⌫")
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(6) { index ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (index < pin.length) NeonCyan else SurfaceElevated)
            )
        }
    }
    Spacer(Modifier.height(12.dp))
    Row(Modifier.fillMaxWidth()) {
        Button(
            onClick = onCancel,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevated)
        ) {
            Text("Cancel", color = TextSecondary)
        }
        Spacer(Modifier.width(12.dp))
        Button(
            onClick = onConfirm,
            modifier = Modifier.weight(1f),
            enabled = pin.length == 6,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (pin.length == 6) NeonCyan else SurfaceElevated,
                contentColor = Color.Black
            )
        ) {
            Text(if (saved) "Saved" else "Save PIN", fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun TimeoutCard(minutes: Int, onChange: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Auto-unlock duration",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Keep apps unlocked after verification",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(1, 2, 5, 10).forEach { m ->
                    TimeoutPill(
                        label = "${m}m",
                        selected = minutes == m,
                        onClick = { onChange(m) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeoutPill(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) NeonCyan else SurfaceElevated)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) Color.Black else TextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun AppLockRow(app: InstalledApp, isLocked: Boolean, onToggle: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                val icon = androidx.compose.runtime.remember(app.packageName) {
                    com.nastech.nia.service.applock.AppLockAppContextHolder.get()
                        .let { ctx ->
                            com.nastech.nia.data.repository.IconHelper.load(ctx, app.packageName)
                        }
                }
                icon?.let { bmp ->
                    androidx.compose.foundation.Image(
                        bitmap = bmp,
                        contentDescription = app.appName,
                        modifier = Modifier.size(36.dp).clip(CircleShape)
                    )
                } ?: Text(
                    text = app.appName.take(1).uppercase(),
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = app.appName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = app.packageName,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    maxLines = 1
                )
            }
            Switch(
                checked = isLocked,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = NeonCyan,
                    checkedTrackColor = NeonCyan.copy(alpha = 0.4f)
                )
            )
        }
    }
}

@Composable
private fun EmptyAppsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Warning, contentDescription = null, tint = TextSecondary)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = "No apps found",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Make sure Usage Access is enabled in System Settings",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }
}