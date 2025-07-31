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
import com.example.moarefiprod.data.models.Course
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseCard
import java.util.Date

@Composable
fun courspage(onShowDialog: () -> Unit, navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }

    val allCourses = listOf(
        Course(
            "A1 آموزش آلمانی سطح", // id (پیش‌فرض "")
            "با این دوره، می‌توانید به راحتی آلمانی را یاد بگیرید!", // title
            "", // description
            "بدون پیش‌نیاز", // sath
            "۱۰ ساعت و ۳۰ دقیقه", // zaman
            12, // teadad
            120, // price
            "https://example.com/cours1.jpg", // imageUrl (جایگزین R.drawable.cours1)
            true, // isNew
            false, // isFree
            Date(), // publishedAt
            false, // isPurchased
            emptyList() // lessons
        ),
        Course(
            "A2 آموزش آلمانی سطح", // id (پیش‌فرض "")
            "ادامه مسیر یادگیری آلمانی با نکات بیشتر", // title
            "", // description
            "نیازمند A1", // sath
            "۹ ساعت", // zaman
            10, // teadad
            0, // price
            "https://example.com/cours1.jpg", // imageUrl (جایگزین R.drawable.cours1)
            true, // isNew
            true, // isFree
            Date(), // publishedAt
            false, // isPurchased
            emptyList() // lessons
        )
    )

    val allCards = listOf(
        Cards("a1_2", "A1 آموزش آلمانی سطح", "آشنایی با پایه‌ها", 1, "۱۲ جلسه", "cours1"),
        Cards("a2_2", "A2 آموزش آلمانی سطح", "سطح پیشرفته‌تر", 1, "۱۰ جلسه", "cours1"),
        Cards("b1", "B1 آموزش آلمانی سطح", "شروع مکالمات روان", 1, "۱۴ جلسه", "cours1"),
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
                        "مهارت شنیداری" -> navController.navigate("hören_page")
                        "فیلم" -> navController.navigate("MovieScreen")
                        "داستان" -> navController.navigate("StoryScreen")
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

        // پاپ‌آپ با لایه محو روی همه‌چی (opacity 50%)
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