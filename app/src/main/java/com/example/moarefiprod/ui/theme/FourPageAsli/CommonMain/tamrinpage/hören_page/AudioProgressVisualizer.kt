package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AudioProgressVisualizerr(
    isPlaying: Boolean,
    isDisabled: Boolean,
    progress: Float, // ⬅️ مقدار بین 0.0 تا 1.0
    barCount: Int = 28,
//    barWidth: Dp = 2.dp,           // ← قابل تغییر
//    barHeight: Dp = 12.dp,         // ← حداکثر ارتفاع
//    spaceBetween: Dp = 1.dp,
    barColor: Color = Color(0xFFB7E5E4),
    barActiveColor: Color = Color(0xFF4DA3A3),
    barDisabledColor: Color = Color.Gray
) {
    val heights = remember { List(barCount) { (16..48).random().dp } }

    val activeBars = (progress * barCount).toInt().coerceIn(0, barCount)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until barCount) {
            val animatedColor by animateColorAsState(
                targetValue = when {
                    isDisabled -> barDisabledColor
                    i < activeBars -> barActiveColor
                    else -> barColor
                },
                animationSpec = tween(durationMillis = 500)
            )

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(heights[i])
                    .clip(RoundedCornerShape(2.dp))
                    .background(animatedColor)
            )
        }
    }
}
