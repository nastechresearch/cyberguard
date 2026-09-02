package com.nastech.nia.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nastech.nia.R
import com.nastech.nia.ui.theme.AmoledBlack
import com.nastech.nia.ui.theme.AmbientMesh
import com.nastech.nia.ui.theme.GlassTile
import com.nastech.nia.ui.theme.GlassTheme
import com.nastech.nia.ui.theme.NeonCyan
import com.nastech.nia.ui.theme.NeonGreen
import com.nastech.nia.ui.theme.NeonPurple
import com.nastech.nia.ui.theme.cyanGlow
import com.nastech.nia.ui.theme.glass
import com.nastech.nia.ui.theme.glow

private data class ToolItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
)

@Composable
fun HomeScreen(
    onQuickScan: () -> Unit = {},
    onFullScan: () -> Unit = {},
    onOpenTool: (String) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        AmbientMesh()
        val tools = toolItems()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            DashboardHeader()
            HeroScoreCard(score = 92, modifier = Modifier.padding(horizontal = 16.dp))
            QuickScanRow(
                onQuickScan = onQuickScan,
                onFullScan = onFullScan,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            ToolsGrid(
                items = tools,
                onOpenTool = onOpenTool,
                modifier = Modifier.weight(1f)
            )
            FooterAttribution()
        }
    }
}

@Composable
private fun toolItems(): List<ToolItem> = listOf(
    ToolItem("brain", Icons.Outlined.Psychology, "Brain AI"),
    ToolItem("vault", Icons.Outlined.Lock, "Vault"),
    ToolItem("wifi", Icons.Outlined.Wifi, "Wi-Fi Scan"),
    ToolItem("passwords", Icons.Outlined.Password, "Passwords"),
    ToolItem("privacy", Icons.Outlined.PrivacyTip, "Privacy"),
    ToolItem("junk_cleaner", Icons.Outlined.Delete, "Junk Cleaner"),
)

@Composable
private fun DashboardHeader() {
    Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        Text(
            text = stringResource(R.string.dashboard_title),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(R.string.dashboard_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun HeroScoreCard(score: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .glass(elevation = 0.dp)
            .cyanGlow()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.score_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f),
                )
                Spacer(Modifier.size(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$score",
                        fontSize = 54.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    Text(
                        text = " / 100",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Spacer(Modifier.size(12.dp))
                StatusBadge()
            }
            ScoreRing(score = score)
        }
    }
}

@Composable
private fun ScoreRing(score: Int) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(NeonPurple.copy(alpha = 0.35f))
            .glow(NeonPurple, blurDp = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$score",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = NeonCyan,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun StatusBadge() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .clip(CircleShape)
                .background(NeonGreen)
                .glow(NeonGreen, blurDp = 6.dp)
        )
        Text(
            text = stringResource(R.string.status_protected),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
        )
    }
}

@Composable
private fun QuickScanRow(
    onQuickScan: () -> Unit,
    onFullScan: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = GlassTheme.Shape
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        androidx.compose.material3.Button(
            onClick = onQuickScan,
            modifier = Modifier.weight(1f),
            shape = shape.copy(
                topEnd = CornerSize(8.dp),
                bottomEnd = CornerSize(8.dp),
            ),
        ) {
            Icon(Icons.Outlined.Lock, null, Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.btn_quick_scan), fontWeight = FontWeight.Medium)
        }
        androidx.compose.material3.OutlinedButton(
            onClick = onFullScan,
            modifier = Modifier.weight(1f),
            shape = shape.copy(
                topStart = CornerSize(8.dp),
                bottomStart = CornerSize(8.dp),
            ),
        ) {
            Text(stringResource(R.string.btn_full_scan), fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ToolsGrid(
    items: List<ToolItem>,
    onOpenTool: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(108.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 16.dp, vertical = 12.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = items,
            key = { it.route },
        ) { tool ->
            GlassTile(
                onClick = { onOpenTool(tool.route) }
            ) {
                Icon(tool.icon, null, tint = NeonCyan, modifier = Modifier.size(26.dp))
                Spacer(Modifier.size(10.dp))
                Text(
                    text = tool.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun FooterAttribution() {
    Text(
        text = stringResource(R.string.copyright),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
    )
}