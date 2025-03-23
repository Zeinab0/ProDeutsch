package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.moarefiprod.R

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val iconSize = screenWidth * 0.06f
    val selectedIconSize = screenWidth * 0.07f
    val itemBoxSize = screenWidth * 0.15f
    val navHeight = screenHeight * 0.08f

    val items = listOf("خانه", "دوره‌ها", "افزودن", "پروفایل")

    val icons = listOf(
        R.drawable.home,
        R.drawable.course,
        R.drawable.group,
        R.drawable.flashcard
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(navHeight)
            .shadow(
                elevation = 40.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                ambientColor = Color.Black,
                spotColor = Color.Black,
                clip = false // ✅ جلوگیری از حذف سایه
            )
            .background(Color(0xFFCDE8E5), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                Column(
                    modifier = Modifier
                        .clickable { onItemSelected(index) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(itemBoxSize),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedIndex == index) {
                            Box(
                                modifier = Modifier
                                    .size(itemBoxSize)
                                    .background(Color(0xFF4D869C), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = icons[index]),
                                    contentDescription = item,
                                    modifier = Modifier.size(selectedIconSize),
                                    tint = Color.White
                                )
                            }
                        } else {
                            Icon(
                                painter = painterResource(id = icons[index]),
                                contentDescription = item,
                                modifier = Modifier.size(iconSize),
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}