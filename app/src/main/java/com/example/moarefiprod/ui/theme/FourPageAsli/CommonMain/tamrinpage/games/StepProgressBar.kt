package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StepProgressBar(
    currentStep: Int,
    totalSteps: Int = 6,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.wrapContentWidth(), // ⬅️ فقط به اندازه محتوا
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalSteps) { index ->
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(
                        if (index == currentStep)
                            Color(0xFF90CECE)
                        else
                            Color(0xFFE0F2F1)
                    )
            )
        }
    }
}
