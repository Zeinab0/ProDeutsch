package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.data.models.Course
import com.example.moarefiprod.data.models.CourseItem
import com.example.moarefiprod.data.models.CourseItemType
import com.example.moarefiprod.data.models.CourseLesson
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Details.CourseHeaderSection
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Details.CourseInfoSection

@Composable
fun CourseDetailPage(navController: NavController, course: Course) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val imageSectionHeight = screenHeight * 0.3f
    val overlapHeight = 32.dp
    val isFreeCourse = course.price == 0

    var showPriceInHeader by remember { mutableStateOf(true) }
    var showPurchaseButton by remember { mutableStateOf(course.price > 0 && !course.isPurchased) }

    val actualLessons = course.lessons
    var selectedLessonId by remember { mutableStateOf<String?>(null) }
    var lastClickedLessonId by remember { mutableStateOf<String?>(null) }

    // دیباگ ساده برای چک کردن تعداد درس‌ها
    println("Number of lessons: ${actualLessons.size}")

    Scaffold(
        bottomBar = {
            if (showPurchaseButton) {
                val bottomBarShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.09f)
                        .shadow(20.dp, bottomBarShape)
                        .background(Color.White, bottomBarShape),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Button(
                        onClick = {
                            showPurchaseButton = false
                            showPriceInHeader = false
                            // TODO: Navigate to payment screen
                        },
                        modifier = Modifier
                            .padding(start = screenWidth * 0.07f)
                            .width(screenWidth * 0.2f)
                            .height(screenHeight * 0.04f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C)),
                        shape = RoundedCornerShape(screenWidth * 0.02f)
                    ) {
                        Text(
                            "خرید",
                            color = Color.White,
                            fontSize = (screenWidth * 0.035f).value.sp,
                            fontFamily = iranSans,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .background(Color.White)
        ) {
            CourseHeaderSection(navController = navController, courseSath = course.sath)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = imageSectionHeight - overlapHeight)
                    .fillMaxHeight() // استفاده از fillMaxHeight
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .shadow(22.dp, RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(Color.White)
                    .padding(horizontal = screenWidth * 0.07f)
            ) {
                CourseInfoSection(
                    courseTitle = course.title,
                    coursePrice = course.price,
                    courseZaman = course.zaman,
                    courseSath = course.sath,
                    showPriceInHeader = showPriceInHeader
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.010f))

                Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(screenHeight * 0.015f)
                    ) {
                        items(actualLessons) { lesson ->
                            CourseLessonItem(
                                lesson = lesson,
                                isFreeCourse = isFreeCourse,
                                isSelected = selectedLessonId == lesson.id,
                                onLessonClick = { clickedId ->
                                    if (lesson.isUnlocked || isFreeCourse) {
                                        if (selectedLessonId == clickedId) {
                                            if (lastClickedLessonId == clickedId) {
                                                navController.navigate("lesson_detail/${course.title}/$clickedId")
                                                lastClickedLessonId = null
                                            } else {
                                                selectedLessonId = null
                                                lastClickedLessonId = clickedId
                                            }
                                        } else {
                                            selectedLessonId = clickedId
                                            lastClickedLessonId = clickedId
                                        }
                                    } else {
                                        selectedLessonId = if (selectedLessonId == clickedId) null else clickedId
                                        lastClickedLessonId = null
                                    }
                                },
                                onCourseItemClick = { clickedLesson, clickedItem ->
                                    if (clickedLesson.isUnlocked || isFreeCourse) {
                                        navController.navigate("lesson_detail/${course.title}/${clickedLesson.id}")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCourseDetailPageFull() {
    val sampleCourse = Course(
        title = "آموزش زبان آلمانی سطح A1",
        description = "این توضیحات دیگر استفاده نمی‌شود.",
        sath = "A1",
        zaman = "۱۲ ساعت",
        teadad = 14,
        price = 120000,
        imageUrl = "https://example.com/course_pic.jpg",
        isNew = false,
        isFree = false,
        isPurchased = false,
        lessons = listOf(
            CourseLesson(id = "01", title = "الفبای آلمانی", duration = "15 دقیقه", isUnlocked = true, isCompleted = false, items = listOf(
                CourseItem(type = CourseItemType.VIDEO, title = "فیلم آموزشی", isCompleted = false),
                CourseItem(type = CourseItemType.DOCUMENT, title = "جزوه", isCompleted = false)
            )),
            CourseLesson(id = "02", title = "گرامر پایه", duration = "10 دقیقه", isUnlocked = true, isCompleted = false, items = listOf(
                CourseItem(type = CourseItemType.VIDEO, title = "فیلم آموزشی", isCompleted = false)
            )),
            CourseLesson(id = "03", title = "مکالمه ساده", duration = "10 دقیقه", isUnlocked = false, isCompleted = false, items = listOf(
                CourseItem(type = CourseItemType.VIDEO, title = "فیلم آموزشی", isCompleted = false)
            )),
            CourseLesson(id = "04", title = "آزمون کوتاه", duration = "10 دقیقه", isUnlocked = false, isCompleted = false, items = listOf(
                CourseItem(type = CourseItemType.QUIZ1, title = "آزمون", isCompleted = false)
            ))
        )
    )
    val navController = rememberNavController()
    MaterialTheme {
        CourseDetailPage(navController, sampleCourse)
    }
}