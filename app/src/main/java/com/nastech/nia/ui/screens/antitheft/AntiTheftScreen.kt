package com.nastech.nia.ui.screens.antitheft

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RingVolume
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nastech.nia.ui.theme.NeonCyan
import com.nastech.nia.ui.theme.NeonGreen
import com.nastech.nia.ui.theme.NeonRed
import com.nastech.nia.ui.theme.SurfaceDark
import com.nastech.nia.ui.theme.SurfaceElevated
import com.nastech.nia.ui.theme.TextSecondary

@Composable
fun AntiTheftScreen() {
    val vm: AntiTheftViewModel = viewModel(factory = AntiTheftViewModel.factory())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
    ) {
        Text(
            text = "Anti-Theft",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Protect your device if it's lost or stolen",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(Modifier.height(16.dp))
        StatusBanner(armed = vm.armed.value)
        Spacer(Modifier.height(16.dp))
        ArmCard(armed = vm.armed.value, onToggle = { vm.toggleArmed() })
        Spacer(Modifier.height(16.dp))
        ActionGrid(vm = vm)
        Spacer(Modifier.height(16.dp))
        vm.message.value?.let { msg ->
            Text(
                text = msg,
                style = MaterialTheme.typography.bodyMedium,
                color = NeonCyan,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun StatusBanner(armed: Boolean) {
    val gradient = if (armed) {
        Brush.horizontalGradient(listOf(NeonGreen, NeonCyan))
    } else {
        Brush.horizontalGradient(listOf(NeonRed, Color(0xFF8E0000)))
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(gradient)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.Shield,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = if (armed) "Protection armed" else "Protection disarmed",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (armed) {
                        "You'll be alerted on suspicious activity"
                    } else {
                        "Arm protection to activate remote commands"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
private fun ArmCard(armed: Boolean, onToggle: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Remote protection",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Requires network + location",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
            Switch(
                checked = armed,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = NeonGreen,
                    checkedTrackColor = NeonGreen.copy(alpha = 0.4f)
                )
            )
        }
    }
}

@Composable
private fun ActionGrid(vm: AntiTheftViewModel) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        TheftAction(
            icon = Icons.Filled.LocationOn,
            label = "Locate",
            enabled = vm.armed.value && !vm.busy.value,
            modifier = Modifier.weight(1f),
            onClick = { vm.locate() }
        )
        TheftAction(
            icon = Icons.Filled.RingVolume,
            label = "Alarm",
            enabled = vm.armed.value,
            modifier = Modifier.weight(1f),
            onClick = { vm.triggerAlarm() }
        )
    }
    Spacer(Modifier.height(12.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        TheftAction(
            icon = Icons.Filled.Lock,
            label = "Lock",
            enabled = vm.armed.value,
            modifier = Modifier.weight(1f),
            onClick = { vm.lockDevice() }
        )
        Spacer(Modifier.weight(1f))
        Spacer(Modifier.weight(1f))
    }
    Spacer(Modifier.height(12.dp))
    vm.location.value?.let { loc ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Text(
                text = "Last known location: $loc",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun TheftAction(
    icon: ImageVector,
    label: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (enabled) Modifier.clickable(onClick = onClick) else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) SurfaceDark else SurfaceElevated
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (enabled) NeonCyan else TextSecondary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                color = if (enabled) MaterialTheme.colorScheme.onBackground else TextSecondary
            )
        }
    }
}