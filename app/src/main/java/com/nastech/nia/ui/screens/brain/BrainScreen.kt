package com.nastech.nia.ui.screens.brain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nastech.nia.ui.theme.AmoledBlack
import com.nastech.nia.ui.theme.AmbientMesh
import com.nastech.nia.ui.theme.NeonCyan
import com.nastech.nia.ui.theme.NeonPurple
import com.nastech.nia.ui.theme.glass
import com.nastech.nia.ui.theme.purpleGlow

@Composable
fun BrainScreen(
    onConfigure: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        AmbientMesh()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
        ) {
            Spacer(Modifier.size(8.dp))
            Text(
                text = "Brain AI",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Your AI security copilot",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.size(24.dp))
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxSize()
                    .glass()
                    .purpleGlow(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        Icons.Outlined.Psychology,
                        null,
                        tint = NeonCyan,
                        modifier = Modifier.size(48.dp),
                    )
                    Spacer(Modifier.size(12.dp))
                    Text(
                        text = "Copilot preparing…",
                        style = MaterialTheme.typography.titleLarge,
                        color = NeonCyan,
                    )
                    Text(
                        text = "AI/API servers, free models & nastech sync arrive in the next phase.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}