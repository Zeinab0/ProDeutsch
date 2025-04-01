package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.allcartlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.WordStatus

@Composable
fun WordStatusFilters(
    selected: Set<WordStatus>,
    onToggle: (WordStatus) -> Unit
) {
    val statusOptions = listOf(
        WordStatus.CORRECT to "درست",
        WordStatus.WRONG to "غلط",
        WordStatus.IDK to "نمی‌دانم",
        WordStatus.NEW to "جدید"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp),
        horizontalArrangement = Arrangement.End
    ) {
        statusOptions.forEach { (status, label) ->
            val isSelected = selected.contains(status)
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .background(
                        if (isSelected) Color(0xFF4D869C) else Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onToggle(status) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = label,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = iranSans
                )
            }
        }
    }
}
