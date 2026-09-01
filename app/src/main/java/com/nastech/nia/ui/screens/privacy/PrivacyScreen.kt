package com.nastech.nia.ui.screens.privacy

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
import com.nastech.nia.core.security.PermissionAuditLogic.PrivacyLevel
import com.nastech.nia.ui.theme.NeonCyan
import com.nastech.nia.ui.theme.NeonGreen
import com.nastech.nia.ui.theme.NeonAmber
import com.nastech.nia.ui.theme.NeonRed
import com.nastech.nia.ui.theme.SurfaceDark
import com.nastech.nia.ui.theme.SurfaceElevated
import com.nastech.nia.ui.theme.TextSecondary

@Composable
fun PrivacyScreen() {
    val vm: PrivacyViewModel = viewModel(factory = PrivacyViewModel.factory())
    LaunchedEffect(Unit) { vm.audit() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
    ) {
        Text("Privacy Advisor", style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
        Text("Audit app permissions for risk", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Spacer(Modifier.height(16.dp))

        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceDark)) {
            Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(56.dp).clip(CircleShape).background(SurfaceElevated), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Security, null, tint = NeonCyan, modifier = Modifier.size(28.dp))
                }
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text("Permission audit", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                    Text(
                        "${vm.apps.value.size} apps · ${vm.highRiskCount} high risk",
                        color = if (vm.highRiskCount > 0) NeonAmber else NeonGreen,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Button(onClick = { vm.audit() }, colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevated, contentColor = NeonCyan), shape = RoundedCornerShape(10.dp)) {
                    Text("Re-audit")
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(vm.apps.value, key = { it.packageName }) { app ->
                appCard(app, onClick = { vm.select(app) })
            }
        }
    }
}

@Composable
private fun appCard(app: AppPermissionAudit, onClick: () -> Unit) {
    val color = when (app.level) {
        PrivacyLevel.LOW -> NeonGreen
        PrivacyLevel.MEDIUM -> NeonAmber
        PrivacyLevel.HIGH -> NeonRed
    }
    Card(onClick = onClick, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = SurfaceDark)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(12.dp).clip(CircleShape).background(color))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(app.appName, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium)
                Text(app.riskFlags.joinToString(", ").ifEmpty { "No risky permissions" },
                    style = MaterialTheme.typography.labelSmall, color = TextSecondary, maxLines = 1)
            }
            Text(app.level.label, fontSize = 11.sp, color = color, fontWeight = FontWeight.Bold)
        }
    }
}