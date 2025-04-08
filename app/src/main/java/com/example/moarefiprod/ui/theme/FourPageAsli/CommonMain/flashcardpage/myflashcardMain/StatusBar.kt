package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color // ✅ امپورت درست رنگ
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans

@Composable
fun StatusBar(
    label: String,
    count: Int,
    total: Int,
    color: Color,
    status: WordStatus,
    selected: Set<WordStatus>,
    onStatusToggle: (WordStatus) -> Unit,
    modifier: Modifier
) {
    val percentage = if (total > 0) count.toFloat() / total else 0f
    val heightDp = (percentage * 120).dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onStatusToggle(status) }
    ) {
        Box(
            modifier = modifier
                .drawBehind {
                    drawRoundRect(
                        color = if (selected.contains(status)) Color.Magenta else Color.Transparent,
                        style = Stroke(
                            width = 4f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        ),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx())
                    )
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            // 📦 ستون رنگی با متن وسط
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(heightDp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(color),
                contentAlignment = Alignment.Center // ✅ وسط ستون (عمودی و افقی)
            ) {
                Text(
                    text = count.toString(),
                    fontSize = 12.sp,
                    fontFamily = iranSans,
                    color = Color.Black // یا مشکی، بسته به رنگ ستون
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            fontFamily = iranSans
        )
    }
}
