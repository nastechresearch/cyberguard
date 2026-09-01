package com.nastech.nia.ui.screens.settings

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nastech.nia.ui.theme.NeonCyan
import com.nastech.nia.ui.theme.SurfaceDark
import com.nastech.nia.ui.theme.SurfaceElevated
import com.nastech.nia.ui.theme.TextSecondary

@Composable
fun SettingsScreen() {
    val vm: SettingsViewModel = viewModel(factory = SettingsViewModel.factory())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "View app and security details",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(Modifier.height(16.dp))
        SecuritySummaryCard(
            score = vm.securityScore.value,
            lastScan = vm.lastScan.value
        )
        Spacer(Modifier.height(16.dp))
        ToggleGroup()
        Spacer(Modifier.height(16.dp))
        AboutCard(version = vm.version.value, androidVersion = vm.androidVersion.value)
    }
}

@Composable
private fun SecuritySummaryCard(score: Int, lastScan: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        StatTile(
            icon = Icons.Filled.Shield,
            value = "$score",
            label = "Security score",
            modifier = Modifier.weight(1f)
        )
        StatTile(
            icon = Icons.Filled.Schedule,
            value = lastScan,
            label = "Last scan",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatTile(icon: ImageVector, value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = NeonCyan)
            Spacer(Modifier.height(10.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun ToggleGroup() {
    ToggleRow(
        icon = Icons.Filled.Tune,
        title = "Auto-scan on app install",
        subtitle = "Scan new apps when installed",
        checked = true,
        onCheckedChange = {}
    )
    Spacer(Modifier.height(12.dp))
    ToggleRow(
        icon = Icons.Filled.Info,
        title = "In-app notifications",
        subtitle = "Alerts for scan results",
        checked = true,
        onCheckedChange = {}
    )
}

@Composable
private fun ToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = NeonCyan)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = NeonCyan,
                    checkedTrackColor = NeonCyan.copy(alpha = 0.4f)
                )
            )
        }
    }
}

@Composable
private fun AboutCard(version: String, androidVersion: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(Modifier.padding(16.dp)) {
            InfoRow(Icons.Filled.Build, "App version", version)
            Spacer(Modifier.height(8.dp))
            InfoRow(Icons.Filled.Android, "Android", androidVersion)
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, title: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Text(text = title, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Spacer(Modifier.weight(1f))
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
    }
}