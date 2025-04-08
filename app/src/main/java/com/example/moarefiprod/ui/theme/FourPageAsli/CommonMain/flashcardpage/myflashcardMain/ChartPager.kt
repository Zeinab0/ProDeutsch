package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.moarefiprod.iranSans
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp

@Composable
fun ChartPager(
    correct: Int,
    wrong: Int,
    idk: Int,
    new: Int,
    total: Int,
    selected: Set<WordStatus>,
    onStatusToggle: (WordStatus) -> Unit,
    weeklyData: List<Pair<String, Int>>
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val scope = rememberCoroutineScope()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> StatusChart(
                    correct = correct,
                    wrong = wrong,
                    idk = idk,
                    new = new,
                    total = total,
                    selected = selected,
                    onStatusToggle = onStatusToggle
                )
                1 -> WeeklyChart(data = weeklyData)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔘 Indicator سفارشی
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(2) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (isSelected) 12.dp else 8.dp)
                        .background(
                            color = if (isSelected) Color(0xFF4D869C) else Color.LightGray,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun WeeklyChart(data: List<Pair<String, Int>>) {
    val maxCount = data.maxOfOrNull { it.second } ?: 1

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        data.forEach { (day, count) ->
            val barHeight = (count.toFloat() / maxCount) * 120

            Column(
                modifier = Modifier.height(180.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Spacer(modifier = Modifier.height((120 - barHeight).dp))

                Text(
                    text = "$count",
                    fontSize = 12.sp,
                    fontFamily = iranSans,
                    color = Color(0xFF4D869C),
                )

                Box(
                    modifier = Modifier.run {
                        width(28.dp)
                                        .height(barHeight.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF7DB9B6))
                    }
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = day,
                    fontSize = 11.sp,
                    fontFamily = iranSans,
                    color = Color(0xFF78746D)
                )
            }
        }
    }
}
