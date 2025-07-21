package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Cards
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.NewLabel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.flashCard
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Course
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseCard

@Composable
fun courspage(onShowDialog: () -> Unit, navController: NavController){
    var showDialog by remember { mutableStateOf(false) }

    val allCourses = listOf(
        Course("A1 آموزش آلمانی سطح", "با این دوره، می‌توانید به راحتی آلمانی را یاد بگیرید!", "بدون پیش‌نیاز", "۱۰ ساعت و ۳۰ دقیقه", "۱۲ جلسه + ۲۴ آزمون", 120, R.drawable.cours1, true),
        Course("A2 آموزش آلمانی سطح", "ادامه مسیر یادگیری آلمانی با نکات بیشتر", "نیازمند A1", "۹ ساعت", "۱۰ جلسه + تمرین", 0, R.drawable.cours1, true)
    )

    val allCards = listOf(
        Cards("A1 آموزش آلمانی سطح", "با این دوره، می‌توانید به راحتی آلمانی را یاد بگیرید!", "۱۰ ساعت و ۳۰ دقیقه", "۱۲ جلسه + ۲۴ آزمون", 120, R.drawable.cours1, true),
        Cards("A2 آموزش آلمانی سطح", "ادامه مسیر یادگیری آلمانی با نکات بیشتر", "۹ ساعت", "۱۰ جلسه + تمرین", 0, R.drawable.cours1, true),
        Cards("B1 آموزش آلمانی سطح", "آمادگی برای مکالمه‌های روزمره و آزمون‌ها", "۱۱ ساعت", "۱۴ جلسه + پروژه", 200, R.drawable.cours1, true)
    )

    val newCourses = allCourses.filter { it.isNew }
    val newCards = allCards.filter { it.isNew }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            ExercisesSection(
                items = exerciseList,
                onItemClick = { selected ->
                    when (selected.title) {
                        "پادکست" -> onShowDialog()
                        "گرامر" -> navController.navigate("grammar_page")
                        "مهارت شنیداری" -> navController.navigate("hören_page") // 👈 اضافه شد

                    }
                }
            )


            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "جدیدترین ها",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = iranSans,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.End)
                    .padding(bottom = 18.dp, end = 30.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(newCourses) { course ->
                    Box {
                      // CourseCard(course = course)
                        CourseCard(course = course, navController = navController)
                        if (course.isNew) {
                            NewLabel()
                        }
                    }
                }

                items(newCards) { card ->
                    Box {
                        flashCard(cards = card, navController = navController)
                        if (card.isNew) {
                            NewLabel()
                        }
                    }
                }

            }
        }

        // ✅ پاپ‌آپ با لایه محو روی همه‌چی (opacity 50%)
        if (showDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                UnavailableDialog(onDismiss = { showDialog = false })
            }
        }
    }
}
