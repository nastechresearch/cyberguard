package com.nastech.nia.ui.screens.home

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.ui.unit.sp
import com.nastech.nia.R
import com.nastech.nia.ui.theme.AmoledBlack
import com.nastech.nia.ui.theme.NeonCyan
import com.nastech.nia.ui.theme.NeonGreen
import com.nastech.nia.ui.theme.NeonPurple
import com.nastech.nia.ui.theme.SurfaceDark

@Composable
fun HomeScreen(
    onQuickScan: () -> Unit = {},
    onFullScan: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(24.dp))
            DashboardTitle()
            Spacer(Modifier.height(20.dp))
            SecurityScoreCard(score = 92)
            Spacer(Modifier.height(20.dp))
            ScanActions(
                onQuickScan = onQuickScan,
                onFullScan = onFullScan
            )
            Spacer(Modifier.height(24.dp))
            ProtectionStatusRow()
        }
    }
}

@Composable
private fun DashboardTitle() {
    Column {
        Text(
            text = stringResource(R.string.dashboard_title),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.dashboard_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SecurityScoreCard(score: Int) {
    val gradient = Brush.horizontalGradient(listOf(NeonCyan, NeonPurple))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.score_label),
                style = MaterialTheme.typography.bodyMedium,
                color = androidx.compose.ui.graphics.Color.White
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$score",
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color.White
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = "/ 100",
                    style = MaterialTheme.typography.titleLarge,
                    color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            StatusBadge(status = "Protected")
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(NeonGreen)
        )
        Text(
            text = stringResource(R.string.status_protected),
            style = MaterialTheme.typography.labelMedium,
            color = androidx.compose.ui.graphics.Color.White
        )
    }
}

@Composable
private fun ScanActions(
    onQuickScan: () -> Unit,
    onFullScan: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onQuickScan,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonCyan,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Outlined.Security, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.size(8.dp))
            Text(stringResource(R.string.btn_quick_scan), fontWeight = FontWeight.Medium)
        }
        Button(
            onClick = onFullScan,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SurfaceDark,
                contentColor = NeonCyan
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Outlined.Shield, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.size(8.dp))
            Text(stringResource(R.string.btn_full_scan), fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ProtectionStatusRow() {
    Text(
        text = stringResource(R.string.status_protected),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(Modifier.height(4.dp))
    Text(
        text = "Real-time protection active. Last scan: today 18:40",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}