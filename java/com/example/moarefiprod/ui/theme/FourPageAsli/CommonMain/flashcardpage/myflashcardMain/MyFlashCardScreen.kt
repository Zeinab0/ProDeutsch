package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Cards
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.NewLabel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.flashCard
import androidx.compose.foundation.lazy.items


@Composable
fun MyFlashCardScreen(navController: NavController, words: List<Word>) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val total = words.size
    val correctCount = words.count { it.status == WordStatus.CORRECT }
    val wrongCount = words.count { it.status == WordStatus.WRONG }
    val idkCount = words.count { it.status == WordStatus.IDK }
    val newCount = words.count { it.status == WordStatus.NEW }

    var selectedStatuses by remember { mutableStateOf(setOf<WordStatus>()) }

    val purchasedCourses = listOf(
        Cards("A1 آموزش آلمانی سطح", "آشنایی با پایه‌ها", "۱۰ ساعت", "۱۲ جلسه", -1, R.drawable.cours1),
        Cards("A2 آموزش آلمانی سطح", "سطح پیشرفته‌تر", "۹ ساعت", "۱۰ جلسه", -1, R.drawable.cours1),
        Cards("A1 آموزش آلمانی سطح", "آشنایی با پایه‌ها", "۱۰ ساعت", "۱۲ جلسه", -1, R.drawable.cours1),
        Cards("A2 آموزش آلمانی سطح", "سطح پیشرفته‌تر", "۹ ساعت", "۱۰ جلسه", -1, R.drawable.cours1),
        Cards("B1 آموزش آلمانی سطح", "شروع مکالمات روان", "۱۱ ساعت", "۱۴ جلسه", -1, R.drawable.cours1),
        Cards("B1 آموزش آلمانی سطح", "شروع مکالمات روان", "۱۱ ساعت", "۱۴ جلسه", -1, R.drawable.cours1),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(
                        start = screenWidth * 0.03f,
                        top = screenHeight * 0.05f
                    )
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.backbtn),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(screenWidth * 0.09f)
                )
            }

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(
                        end = screenWidth * 0.03f,
                        top = screenHeight * 0.05f
                    )
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(screenWidth * 0.055f)
                )
            }
        }
        Text(
            text = "وضعیت",
            fontSize = (screenWidth * 0.04f).value.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = iranSans,
            color = Color.Black,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp)
        )

        ChartPager(
            correct = correctCount,
            wrong = wrongCount,
            idk = idkCount,
            new = newCount,
            total = total,
            selected = selectedStatuses,
            onStatusToggle = { status ->
                selectedStatuses = if (selectedStatuses.contains(status)) {
                    selectedStatuses - status
                } else {
                    selectedStatuses + status
                }
            },
            weeklyData = listOf(
                "جمعه" to 25,
                "پنج شنبه" to 10,
                "چهارشنبه" to 18,
                "سه‌شنبه" to 32,
                "دوشنبه" to 22,
                "یکشنبه" to 12,
                "شنبه" to 5
            )
        )
        Text(
            text = "کلمات من",
            fontSize = (screenWidth * 0.04f).value.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = iranSans,
            color = Color.Black,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(purchasedCourses) { cards ->
                Box {
                    flashCard(cards = cards, navController = navController)
                    if (cards.isNew) {
                        NewLabel()
                    }
                }
            }
        }



    }

}

