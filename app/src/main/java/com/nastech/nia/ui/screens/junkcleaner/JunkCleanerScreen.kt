package com.nastech.nia.ui.screens.junkcleaner

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nastech.nia.core.security.JunkFileLogic.humanSize
import com.nastech.nia.ui.theme.NeonCyan
import com.nastech.nia.ui.theme.NeonGreen
import com.nastech.nia.ui.theme.NeonPurple
import com.nastech.nia.ui.theme.NeonRed
import com.nastech.nia.ui.theme.SurfaceDark
import com.nastech.nia.ui.theme.SurfaceElevated
import com.nastech.nia.ui.theme.TextSecondary

@Composable
fun JunkCleanerScreen() {
    val vm: JunkCleanerViewModel = viewModel(factory = JunkCleanerViewModel.factory())
    LaunchedEffect(Unit) { vm.scan() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
    ) {
        Text("Junk Cleaner", style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
        Text("Reclaim space from cache and temp files", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Spacer(Modifier.height(16.dp))

        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceDark)) {
            Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(56.dp).clip(CircleShape).background(SurfaceElevated), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Delete, null, tint = NeonPurple, modifier = Modifier.size(28.dp))
                }
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text("Scan result", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                    Text(
                        if (vm.hasScanned.value) humanSize(vm.totalBytes) + " reclaimable · ${vm.items.value.size} files"
                        else "No scan yet",
                        color = if (vm.totalBytes > 0) NeonGreen else TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Button(onClick = { vm.scan() }, colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevated, contentColor = NeonCyan), shape = RoundedCornerShape(10.dp)) {
                    Text("Scan")
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        if (vm.items.value.isNotEmpty()) {
            Button(onClick = { vm.clean() }, colors = ButtonDefaults.buttonColors(containerColor = NeonRed, contentColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Text("Clean ${humanSize(vm.totalBytes)}", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
        }
        if (vm.freedBytes.value > 0) {
            Text("Freed ${humanSize(vm.freedBytes.value)}", color = NeonGreen, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (vm.items.value.isEmpty() && !vm.scanning.value) {
                item {
                    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = SurfaceDark)) {
                        Text("No junk files in app cache.", color = TextSecondary, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(16.dp))
                    }
                }
            } else {
                items(vm.items.value, key = { it.path }) { item ->
                    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(SurfaceDark).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(item.label, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium)
                            Text(item.path, color = TextSecondary, style = MaterialTheme.typography.labelSmall, maxLines = 1)
                        }
                        Text(item.size, color = TextSecondary, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}