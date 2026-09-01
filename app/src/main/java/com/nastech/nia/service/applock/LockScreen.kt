package com.nastech.nia.service.applock

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nastech.nia.ui.theme.NeonCyan
import com.nastech.nia.ui.theme.NeonRed
import com.nastech.nia.ui.theme.SurfaceElevated
import com.nastech.nia.ui.theme.TextSecondary

@Composable
fun LockScreen(
    packageName: String,
    viewModel: LockViewModel,
    onUnlocked: () -> Unit,
    onDismiss: () -> Unit
) {
    val appName = rememberLockedAppName(packageName)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        Icon(
            imageVector = Icons.Filled.Lock,
            contentDescription = null,
            tint = NeonCyan,
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "App Locked",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = appName,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(Modifier.height(32.dp))
        PinDots(pinLength = viewModel.pin.length, hasError = viewModel.hasError)
        Spacer(Modifier.height(32.dp))

        PinPad(
            viewModel = viewModel,
            onUnlocked = {
                viewModel.verify(packageName) {
                    onUnlocked()
                }
            }
        )

        Spacer(Modifier.weight(1f))
        TextButton(onClick = onDismiss) {
            Text("Cancel", color = TextSecondary)
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun rememberLockedAppName(packageName: String): String {
    val context = AppLockAppContextHolder.get()
    return remember(context, packageName) {
        val pm = context.packageManager
        try {
            pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0)).toString()
        } catch (e: Exception) {
            packageName.substringAfterLast('.')
        }
    }
}

@Composable
private fun PinDots(pinLength: Int, hasError: Boolean) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        repeat(6) { index ->
            val active = index < pinLength
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            active -> NeonCyan
                            hasError -> NeonRed
                            else -> SurfaceElevated
                        }
                    )
            )
        }
    }
}

@Composable
private fun PinPad(viewModel: LockViewModel, onUnlocked: () -> Unit) {
    val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "C", "0", "⌫")
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        keys.chunked(3).forEach { rowKeys ->
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                rowKeys.forEach { key ->
                    PinKey(
                        label = key,
                        onKey = {
                            when (key) {
                                "⌫" -> viewModel.delete()
                                "C" -> viewModel.clear()
                                else -> {
                                    viewModel.append(key[0])
                                    if (viewModel.pin.length == 6) onUnlocked()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PinKey(label: String, onKey: () -> Unit) {
    IconButton(
        onClick = onKey,
        modifier = Modifier.size(72.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(SurfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            when (label) {
                "⌫" -> Icon(
                    imageVector = Icons.Filled.Backspace,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                "C" -> Text(
                    text = label,
                    fontSize = 18.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
                else -> Text(
                    text = label,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}