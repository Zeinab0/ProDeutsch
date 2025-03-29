package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.viewmodel.FlashcardViewModel

@Composable
fun WordProgressPage(words: List<Word>, navController: NavController) {
    var selectedStatuses by remember { mutableStateOf(setOf<WordStatus>()) }
    val viewModel: FlashcardViewModel = viewModel()

    // ✅ لیست جدید از review_page (اگه باشه) یا لیست اصلی
    val updatedWords = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<List<Word>>("updated_words")

    val allWords = updatedWords ?: words


    val total = allWords.size
    val correctCount = allWords.count { it.status == WordStatus.CORRECT }
    val wrongCount = allWords.count { it.status == WordStatus.WRONG }
    val idkCount = allWords.count { it.status == WordStatus.IDK }
    val newCount = allWords.count { it.status == WordStatus.NEW }

    val filteredWords = if (selectedStatuses.isEmpty()) allWords
    else allWords.filter { it.status in selectedStatuses }

    val filteredPercentage =
        if (total > 0) (filteredWords.size.toFloat() / total * 100).toInt() else 0

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 🔼 هدر و تصویر بالا
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(235.dp)
                .background(Color(0xFF64DEF0))
        ) {
            Image(
                painter = painterResource(id = R.drawable.pic),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )

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
        }

        Text(
            text = "یادگیری کلمات در فرودگاه",
            fontSize = (screenWidth * 0.04f).value.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = iranSans,
            color = Color.Black,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🟢 بخش وضعیت
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(20.dp)
        ) {
            Text(
                text = "وضعیت",
                fontSize = (screenWidth * 0.035f).value.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = iranSans,
                color = Color.Black,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = "$filteredPercentage%",
                fontSize = (screenWidth * 0.035f).value.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = iranSans,
                color = Color.Black,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.Start)
                    .padding(horizontal = 16.dp)
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Divider(
                color = Color(0xFFABB7C2),
                thickness = 1.dp,
                modifier = Modifier
                    .width(350.dp)
                    .padding(vertical = 10.dp)
            )
        }

        // 📊 نمایش نمودار با Pager (وضعیت یا هفتگی)
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

        Spacer(modifier = Modifier.height(22.dp))

        // 🃏 عنوان کارت‌ها و درصد
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(20.dp)
                .clickable {
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("filter_statuses", selectedStatuses.toList())
                    navController.navigate("word_list_page")
                }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.backbtn),
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${filteredWords.size} / $total",
                        fontSize = (screenWidth * 0.035f).value.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = iranSans,
                        color = Color.Black
                    )
                }

                Text(
                    text = "کارت‌ها",
                    fontSize = (screenWidth * 0.035f).value.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = iranSans,
                    color = Color.Black,
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        WordCards(filteredWords)

        Spacer(modifier = Modifier.height(32.dp))

        // 🔘 دکمه مرور
        Button(
            onClick = {
                val reviewWords = if (selectedStatuses.isEmpty()) {
                    allWords // وقتی هیچ ستونی انتخاب نشده، همه‌ی کلمات رو مرور کن
                } else {
                    allWords.filter { it.status in selectedStatuses }
                }

                if (reviewWords.isNotEmpty()) {
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("review_words", reviewWords)
                    navController.navigate("review_page")
                } else {
                    // اگر هیچ کلمه‌ای برای مرور وجود نداشت (مثلاً فیلتر شده)، پیغام نشون بده
                    // یا می‌تونی از Toast هم استفاده کنی
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF90CECE)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(180.dp, 90.dp)
                .padding(10.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.review), contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("مرور (${filteredWords.size} کلمه)", fontFamily = iranSans)
        }


    }
}