package com.nastech.nia.ui.screens.vault

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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.nastech.nia.ui.theme.NeonPurple
import com.nastech.nia.ui.theme.NeonRed
import com.nastech.nia.ui.theme.SurfaceDark
import com.nastech.nia.ui.theme.SurfaceElevated
import com.nastech.nia.ui.theme.TextSecondary

@Composable
fun VaultScreen() {
    val vm: VaultViewModel = viewModel(factory = VaultViewModel.factory())
    var entryCode by remember { mutableStateOf("") }
    var unlocked by remember { mutableStateOf(false) }
    var unlockedOnce by remember { mutableStateOf(false) }
    var pinError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { vm.load() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
    ) {
        Text("Secure Vault", style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
        Text("Files are stored AES-GCM encrypted", style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary)
        Spacer(Modifier.height(16.dp))

        if (!vm.configured.value) {
            SetupCard(entryCode, { entryCode = it }, onConfirm = {
                if (entryCode.length == 4) {
                    vm.configure(entryCode)
                    entryCode = ""
                }
            })
        } else if (!unlocked && !vm.justConfigured.value) {
            UnlockCard(entryCode, { entryCode = it }, pinError, onUnlock = {
                vm.tryUnlock({ ok ->
                    unlocked = ok
                    unlockedOnce = true
                    pinError = !ok
                }, entryCode)
                if (entryCode.length == 4) entryCode = ""
            })
        } else {
            val showLock = unlockedOnce && !unlocked
            Content(vm, unlocked, showLock,
                onLock = {
                    unlocked = false
                    unlockedOnce = true
                })
        }
    }
}

@Composable
private fun SetupCard(pin: String, onChange: (String) -> Unit, onConfirm: () -> Unit) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceDark)) {
        Column(Modifier.padding(20.dp)) {
            Text("Create vault PIN", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
            Spacer(Modifier.height(4.dp))
            Text("4-digit PIN protects your encrypted vault.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = pin, onValueChange = { if (it.length <= 4) onChange(it) },
                singleLine = true, label = { Text("PIN") },
                modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            Button(onClick = onConfirm, enabled = pin.length == 4, modifier = Modifier.fillMaxWidth()) {
                Text("Enable Vault", fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun UnlockCard(pin: String, onChange: (String) -> Unit, error: Boolean, onUnlock: () -> Unit) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceDark)) {
        Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(Modifier.size(56.dp).clip(CircleShape).background(SurfaceElevated), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Lock, null, tint = NeonPurple, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text("Enter vault PIN", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = pin, onValueChange = { if (it.length <= 4) onChange(it) },
                singleLine = true, label = { Text("PIN") },
                isError = error, modifier = Modifier.fillMaxWidth())
            if (error) {
                Text("Wrong PIN", color = NeonRed, fontSize = 12.sp)
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onUnlock, modifier = Modifier.fillMaxWidth()) {
                Text("Unlock", fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun Content(vm: VaultViewModel, unlocked: Boolean, showLock: Boolean, onLock: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("My items", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.weight(1f))
        if (showLock) {
            Button(onClick = onLock, colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevated, contentColor = NeonRed)) {
                Text("Lock")
            }
        }
    }
    Spacer(Modifier.height(8.dp))
    if (vm.entries.value.isEmpty()) {
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = SurfaceDark)) {
            Text("Vault is empty. Import a file below.", color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(16.dp))
        }
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(vm.entries.value, key = { it.name }) { e ->
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(SurfaceDark).padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(e.name, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium)
                        Text(humanSize(e.sizeBytes), color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                    }
                    IconButton(onClick = { vm.remove(e.name) }) {
                        Icon(Icons.Filled.Delete, "Delete", tint = NeonRed)
                    }
                }
            }
        }
    }
    Spacer(Modifier.height(16.dp))
    Surface(shape = RoundedCornerShape(16.dp), color = SurfaceDark) {
        Text("Import a demo secret file", color = TextSecondary, style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp))
    }
    Spacer(Modifier.height(8.dp))
    Button(onClick = {
        vm.import("secret_${System.currentTimeMillis()}", "Demo sensitive data".toByteArray())
    }, colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevated, contentColor = NeonCyan), modifier = Modifier.fillMaxWidth()) {
        Text("Import demo file")
    }
}