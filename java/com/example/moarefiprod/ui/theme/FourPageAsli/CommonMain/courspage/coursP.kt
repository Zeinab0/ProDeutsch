package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Cards
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.NewLabel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.flashCard
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.Course
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.CourseCard

@Composable
fun courspage(){
    val allCourses = listOf(
        Course("A1 آموزش آلمانی سطح", "با این دوره، می‌توانید به راحتی آلمانی را یاد بگیرید!", "بدون پیش‌نیاز", "۱۰ ساعت و ۳۰ دقیقه", "۱۲ جلسه + ۲۴ آزمون", 120, R.drawable.cours1,true),
        Course("A2 آموزش آلمانی سطح", "ادامه مسیر یادگیری آلمانی با نکات بیشتر", "نیازمند A1", "۹ ساعت", "۱۰ جلسه + تمرین", 0, R.drawable.cours1, true),

        )

    val allCards = listOf(
        Cards("A1 آموزش آلمانی سطح", "با این دوره، می‌توانید به راحتی آلمانی را یاد بگیرید!",  "۱۰ ساعت و ۳۰ دقیقه", "۱۲ جلسه + ۲۴ آزمون", 120, R.drawable.cours1, true),
        Cards("A2 آموزش آلمانی سطح", "ادامه مسیر یادگیری آلمانی با نکات بیشتر", "۹ ساعت", "۱۰ جلسه + تمرین", 0, R.drawable.cours1, true),
        Cards("B1 آموزش آلمانی سطح", "آمادگی برای مکالمه‌های روزمره و آزمون‌ها",  "۱۱ ساعت", "۱۴ جلسه + پروژه", 200, R.drawable.cours1 ,true),
    )

    val newCourses = allCourses.filter { it.isNew }
    val newCards = allCards.filter { it.isNew }

    ExercisesSection(
        items = exerciseList,
        onItemClick = { selected ->
            // TODO: انتخاب یا رفتن به صفحه مربوطه
        }
    )
    Spacer(modifier = Modifier.height(20.dp)) // فاصله بیشتر بین آیکون و متن

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
        // آیتم‌های جدید از کلاس Course
        items(newCourses) { course ->
            Box {
                CourseCard(course = course)
                if (course.isNew) {
                    NewLabel()
                }
            }
        }

        // آیتم‌های جدید از کلاس Cards
        items(newCards) { card ->
            Box {
                flashCard(cards = card)
                if (card.isNew) {
                    NewLabel()
                }
            }
        }
    }

}