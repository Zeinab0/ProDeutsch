package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatusChart(
    correct: Int,
    wrong: Int,
    idk: Int,
    new: Int,
    total: Int,
    selected: Set<WordStatus>,
    onStatusToggle: (WordStatus) -> Unit
) {
    val barModifier = Modifier
        .width(50.dp)
        .height(120.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 60.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        StatusBar("درست", correct, total, Color.Green, WordStatus.CORRECT, selected, onStatusToggle, barModifier)
        StatusBar("غلط", wrong, total, Color.Red, WordStatus.WRONG, selected, onStatusToggle, barModifier)
        StatusBar("نمیدانم", idk, total, Color(0xFFFFA500), WordStatus.IDK, selected, onStatusToggle, barModifier)
        StatusBar("جدید", new, total, Color.Cyan, WordStatus.NEW, selected, onStatusToggle, barModifier)
    }
}
