package com.nastech.nia.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Compute responsive grid columns from the window width bucket. */
fun columnsForWidth(maxWidth: Dp, dense: Boolean): Int = when {
    maxWidth < 360.dp -> 2
    maxWidth < 600.dp -> if (dense) 3 else 2
    maxWidth < 960.dp -> if (dense) 4 else 3
    else -> if (dense) 5 else 4
}

/**
 * A responsive glass tile grid. Items render as [GlassTile] cards laid out
 * on a consistent gutter system over the AMOLED base.
 */
@Composable
fun <T> GlassGrid(
    items: List<T>,
    key: (T) -> Any,
    modifier: Modifier = Modifier,
    maxWidth: Dp,
    dense: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    gutter: Dp = 12.dp,
    tileContent: @Composable (T) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnsForWidth(maxWidth, dense)),
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(gutter),
        verticalArrangement = Arrangement.spacedBy(gutter),
    ) {
        items(items = items, key = key) { item ->
            GlassTile {
                tileContent(item)
            }
        }
    }
}

/** A clickable glass card used as a grid tile. */
@Composable
fun GlassTile(
    modifier: Modifier = Modifier,
    shape: androidx.compose.foundation.shape.RoundedCornerShape = GlassTheme.Shape,
    contentPadding: PaddingValues = PaddingValues(14.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    if (onClick != null) {
        androidx.compose.material3.Surface(
            onClick = onClick,
            modifier = modifier.glass(shape = shape),
            shape = shape,
            contentColor = androidx.compose.ui.graphics.Color.White,
        ) {
            Column(Modifier.padding(contentPadding)) { content() }
        }
    } else {
        androidx.compose.material3.Surface(
            modifier = modifier.glass(shape = shape),
            shape = shape,
            color = androidx.compose.ui.graphics.Color.Transparent,
            contentColor = androidx.compose.ui.graphics.Color.White,
        ) {
            Column(Modifier.padding(contentPadding)) { content() }
        }
    }
}