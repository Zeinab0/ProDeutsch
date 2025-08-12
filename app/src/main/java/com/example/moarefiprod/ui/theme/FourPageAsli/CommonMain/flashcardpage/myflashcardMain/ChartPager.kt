package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.WordStatus
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@Composable
fun ChartPager(
    correct: Int,
    wrong: Int,
    idk: Int,
    new: Int,
    total: Int,
    selected: Set<WordStatus>,
    onStatusToggle: (WordStatus) -> Unit,
    weeklyData: List<Pair<String, Int>> // مثلاً [("شنبه", 20), ("یکشنبه", 14), ...]
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            count = 2,
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

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = Color(0xFF4D869C),
            inactiveColor = Color.LightGray,
            modifier = Modifier.padding(4.dp)
        )
    }
}
@Composable
fun WeeklyChart(data: List<Pair<String, Int>>) {
    val maxCount = data.maxOfOrNull { it.second } ?: 1

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        data.forEach { (day, count) ->
            val barHeight = (count.toFloat() / maxCount) * 100 // درصدی

            Column(
                modifier = Modifier.height(164.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom // ⬅️ این باعث میشه ستون از پایین رشد کنه
            ) {
                // باکس سفید خالی بالا برای هم‌تراز کردن عدد
                Spacer(modifier = Modifier.height((100 - barHeight).dp))

                // عدد بالا
                Text("$count", fontSize = 12.sp, fontFamily = iranSans)

                // ستون رنگی
                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .height(barHeight.dp)
                        .background(Color(0xFF7DB9B6), RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.height(4.dp))

                // روز هفته
                Text(day, fontSize = 10.sp, fontFamily = iranSans)
            }
        }
    }
}

