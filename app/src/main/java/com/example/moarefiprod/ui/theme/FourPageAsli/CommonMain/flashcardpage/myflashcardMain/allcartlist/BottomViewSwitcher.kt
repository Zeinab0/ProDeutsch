package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.allcartlist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.moarefiprod.R

import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun BottomViewSwitcher(current: WordViewType, onSelect: (WordViewType) -> Unit) {
    val activeColor = Color(0xFFFFFFFF)
    val inactiveColor = Color(0xFF3B8686)
    val selectedBackground = Color(0xFF6ABBBB)
    val unselectedBackground = Color(0xFF90CECE)

    Surface(
        shadowElevation = 6.dp,
        shape = RectangleShape,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            // 🔵 نمای کارتی
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (current == WordViewType.CARD) selectedBackground else unselectedBackground)
                    .border(1.dp, Color.Black)
                    .clickable { onSelect(WordViewType.CARD) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.iconcard),
                    contentDescription = "نمای کارتی",
                    tint = if (current == WordViewType.CARD) activeColor else inactiveColor
                )
            }

            // 📋 نمای لیستی
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (current == WordViewType.LIST) selectedBackground else unselectedBackground)
                    .border(1.dp, Color.Black)
                    .clickable { onSelect(WordViewType.LIST) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.iconlist),
                    contentDescription = "نمای لیستی",
                    tint = if (current == WordViewType.LIST) activeColor else inactiveColor
                )
            }
        }
    }
}
