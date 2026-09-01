package com.nastech.nia.ui.screens.passwords

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nastech.nia.core.security.PasswordStrengthAnalyzer.Level
import com.nastech.nia.ui.theme.NeonCyan
import com.nastech.nia.ui.theme.NeonGreen
import com.nastech.nia.ui.theme.NeonAmber
import com.nastech.nia.ui.theme.NeonRed
import com.nastech.nia.ui.theme.SurfaceDark
import com.nastech.nia.ui.theme.SurfaceElevated
import com.nastech.nia.ui.theme.TextSecondary

@Composable
fun PasswordCheckScreen() {
    val vm: PasswordCheckViewModel = viewModel(factory = PasswordCheckViewModel.factory())
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
    ) {
        Text("Password Checker", style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
        Text("Measure strength and breach status", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Spacer(Modifier.height(16.dp))

        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceDark)) {
            Column(Modifier.padding(20.dp)) {
                OutlinedTextField(value = password, onValueChange = {
                    password = it
                    vm.analyze(it)
                }, label = { Text("Password") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
                val color = when (vm.level.value) {
                    Level.WEAK -> NeonRed
                    Level.FAIR -> NeonAmber
                    Level.GOOD -> NeonCyan
                    Level.STRONG -> NeonGreen
                }
                Text("Strength: ${vm.level.value.label}", color = color, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(progress = { vm.scorePct() / 100f }, modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)), color = color, trackColor = SurfaceElevated)
                Spacer(Modifier.height(8.dp))
                Text("Est. entropy: ${vm.entropy.value} bits", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = { vm.checkBreach(password) }, modifier = Modifier.fillMaxWidth()) {
            Text("Check breach status", fontWeight = FontWeight.Medium)
        }
        if (vm.checked.value) {
            Spacer(Modifier.height(12.dp))
            Card(shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Security, null, tint = if (vm.breached.value) NeonRed else NeonGreen, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.size(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            if (vm.breached.value) "Found in known breaches" else "No known breach",
                            color = if (vm.breached.value) NeonRed else NeonGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(if (vm.breached.value) "Change this password immediately." else "Good — not in our breach set.",
                            color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}