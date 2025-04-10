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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AudioProgressVisualizer(
    isPlaying: Boolean,
    isDisabled: Boolean, // ✅ پارامتر جدید برای حالت اتمام تکرار
    durationMs: Int = 10000,
    barCount: Int = 30,
    barColor: Color = Color(0xFFB7E5E4),
    barActiveColor: Color = Color(0xFF4DA3A3),
    barDisabledColor: Color = Color.Gray // ✅ رنگ جدید برای غیرفعال
) {
    var activeBars by remember { mutableStateOf(0) }
    val stepDelay = durationMs / barCount
    val heights = remember { List(barCount) { (16..48).random().dp } }

    LaunchedEffect(isPlaying) {
        if (isPlaying && !isDisabled) {
            activeBars = 0
            repeat(barCount) {
                delay(stepDelay.toLong())
                activeBars++
            }
        } else {
            activeBars = 0
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until barCount) {
            val animatedColor by animateColorAsState(
                targetValue = when {
                    isDisabled -> barDisabledColor         // ❗ حالت غیرفعال
                    i < activeBars -> barActiveColor       // فعال در حال پخش
                    else -> barColor                       // غیرفعال پیش‌فرض
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
