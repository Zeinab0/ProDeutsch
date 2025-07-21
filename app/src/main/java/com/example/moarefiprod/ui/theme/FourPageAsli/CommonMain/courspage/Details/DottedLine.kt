package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color // ✅ استفاده از Compose Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DottedLine(
    width: Dp,
    color: Color = Color(0xFFABB7C2),
    dotSpacing: Dp = 4.dp,
    dotRadius: Dp = 0.8.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(width)
            .height(1.dp)
            .drawBehind {
                val spacingPx = dotSpacing.toPx()
                val radiusPx = dotRadius.toPx()
                var x = 0f
                while (x < size.width) {
                    drawCircle(
                        color = color,
                        radius = radiusPx,
                        center = Offset(x, size.height / 2)
                    )
                    x += spacingPx
                }
            }
    )
}

