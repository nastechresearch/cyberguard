package com.nastech.nia.ui.screens.onboard

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nastech.nia.ui.theme.NeonCyan
import com.nastech.nia.ui.theme.NeonPurple

private val cyberGradient = Brush.horizontalGradient(
    listOf(NeonCyan, NeonPurple)
)

@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val screens = listOf(
        OnboardPage(Icons.Filled.Shield, "Welcome to CyberGuard", "Your phone. Protected. All-in-one security suite."),
        OnboardPage(Icons.Filled.Security, "Antivirus Scanner", "Scan installed apps, detect threats, and get a security score."),
        OnboardPage(Icons.Filled.Lock, "Anti-Theft & App Lock", "Protect sensitive apps and your device if it's lost or stolen.")
    )
    var page by androidx.compose.runtime.remember {
        mutableStateOf(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        Icon(
            imageVector = screens[page].icon,
            contentDescription = null,
            tint = NeonCyan,
            modifier = Modifier.size(120.dp)
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = screens[page].title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = screens[page].subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )
        Spacer(Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(screens.size) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == page) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(if (index == page) NeonCyan else Color(0xFF3A3A3A))
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {
                if (page < screens.lastIndex) page++ else onComplete()
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonCyan,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = if (page < screens.lastIndex) "Next" else "Get Started",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onComplete) {
            Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(24.dp))
    }
}

private data class OnboardPage(
    val icon: ImageVector,
    val title: String,
    val subtitle: String
)