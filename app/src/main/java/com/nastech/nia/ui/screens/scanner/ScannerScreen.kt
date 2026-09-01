package com.nastech.nia.ui.screens.scanner

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nastech.nia.data.model.ScanResult
import com.nastech.nia.data.model.ThreatLevel
import com.nastech.nia.ui.theme.NeonCyan
import com.nastech.nia.ui.theme.NeonGreen
import com.nastech.nia.ui.theme.NeonAmber
import com.nastech.nia.ui.theme.NeonRed
import com.nastech.nia.ui.theme.SurfaceDark
import com.nastech.nia.ui.theme.SurfaceElevated
import com.nastech.nia.ui.theme.TextSecondary

@Composable
fun ScannerScreen() {
    val vm: ScannerViewModel = viewModel(factory = ScannerViewModel.factory())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
    ) {
        Text(
            text = "Device Scanner",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Scan installed apps for threats",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(Modifier.height(16.dp))
        ScanHeroCard(vm = vm)
        Spacer(Modifier.height(16.dp))
        if (vm.isScanning) {
            Text(
                text = vm.scannedLabel.ifEmpty { "Preparing scan..." },
                style = MaterialTheme.typography.bodyMedium,
                color = NeonCyan
            )
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { vm.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = NeonCyan,
                trackColor = SurfaceElevated
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Results",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = { vm.rescan() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceElevated,
                        contentColor = NeonCyan
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Re-scan", fontWeight = FontWeight.Medium)
                }
            }
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (vm.results.isEmpty()) {
                    item { NoResultsCard(vm.error) }
                } else {
                    items(vm.results, key = { it.packageName }) { r ->
                        ResultCard(r)
                    }
                }
            }
        }
    }
}

@Composable
private fun ScanHeroCard(vm: ScannerViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(SurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Security,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = if (vm.isScanning) "Scanning device..." else "Threat scan complete",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = vm.results.count { it.threat == ThreatLevel.SAFE }.toString() +
                        " safe · " +
                        vm.results.count { it.threat != ThreatLevel.SAFE }.toString() +
                        " flagged",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (vm.results.any { it.threat != ThreatLevel.SAFE }) {
                        NeonAmber
                    } else {
                        NeonGreen
                    }
                )
            }
        }
    }
}

@Composable
private fun ResultCard(result: ScanResult) {
    val color = when (result.threat) {
        ThreatLevel.SAFE -> NeonGreen
        ThreatLevel.LOW -> NeonCyan
        ThreatLevel.MEDIUM -> NeonAmber
        ThreatLevel.HIGH, ThreatLevel.CRITICAL -> NeonRed
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = result.appName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${result.packageName} · ${result.description}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    maxLines = 1
                )
            }
            Text(
                text = result.threat.name,
                fontSize = 11.sp,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun NoResultsCard(error: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Text(
            text = error ?: "No apps found to scan.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(16.dp)
        )
    }
}