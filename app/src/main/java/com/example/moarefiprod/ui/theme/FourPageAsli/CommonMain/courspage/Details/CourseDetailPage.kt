package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.data.models.Course
import com.example.moarefiprod.data.models.CourseItem
import com.example.moarefiprod.data.models.CourseLesson
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Details.CourseHeaderSection
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Details.CourseInfoSection

@Composable
fun CourseDetailPage(
    navController: NavController,
    courseId: String,
    courseViewModel: CourseViewModel = viewModel()
) {
    val courseState by courseViewModel.selectedCourse.collectAsState()
    val lessons by courseViewModel.selectedCourseLessons.collectAsState()
    val isLoading by courseViewModel.isLoading.collectAsState()
    val errorMessage by courseViewModel.errorMessage.collectAsState()

    LaunchedEffect(courseId) {
        if (courseId.isNotEmpty()) {
            courseViewModel.loadSelectedCourseDetailsAndLessons(courseId)
            Log.d("CourseDetailPage", "Loaded lessons: ${lessons.map { "${it.title} (order: ${it.order}, items: ${it.items.size})" }}")
        } else {
            Log.e("CourseDetailPage", "courseId is empty")
        }
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val imageSectionHeight = screenHeight * 0.3f
    val overlapHeight = 32.dp

    val isFreeCourse = courseState?.price == 0
    var showPurchaseButton by remember { mutableStateOf(false) }
    var showPriceInHeader by remember { mutableStateOf(true) }
    var isPurchasedSimulated by remember { mutableStateOf(false) }

    LaunchedEffect(courseState) {
        courseState?.let {
            showPurchaseButton = it.price > 0 && !isPurchasedSimulated
            showPriceInHeader = showPurchaseButton
            Log.d("CourseDetailPage", "Course: ${it.title}, isFree: ${it.isFree}, isPurchasedSimulated: $isPurchasedSimulated")
        } ?: run {
            showPurchaseButton = false
            showPriceInHeader = true
        }
    }

    var selectedLessonId by remember { mutableStateOf<String?>(null) }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "خطا: $errorMessage", color = Color.Red, fontFamily = iranSans)
        }
    } else if (courseState == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "دوره یافت نشد.", fontFamily = iranSans)
        }
    } else {
        val course = courseState!!
        Scaffold(
            bottomBar = {
                AnimatedVisibility(visible = showPurchaseButton) {
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
                                isPurchasedSimulated = true
                                showPurchaseButton = false
                                showPriceInHeader = false
                                Log.d("CourseDetailPage", "Purchase simulated for course: ${course.title}")
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
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                        .shadow(22.dp, RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                        .background(Color.White)
                        .padding(horizontal = screenWidth * 0.07f)
                ) {
                    CourseInfoSection(
                        courseTitle = course.title,
                        coursePrice = if (showPriceInHeader) course.price else 0,
                        courseZaman = course.zaman,
                        courseSath = course.sath,
                        showPriceInHeader = showPriceInHeader
                    )

                    Spacer(modifier = Modifier.height(screenHeight * 0.010f))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), // Takes remaining space for scrolling
                        verticalArrangement = Arrangement.spacedBy(screenHeight * 0.015f),
                        contentPadding = PaddingValues(bottom = 80.dp, top = 8.dp) // Matches tamrinpage padding
                    ) {
                        items(lessons) { lesson ->
                            val isLessonAccessible = isFreeCourse || (lesson.order == 1 && !isPurchasedSimulated) || (isPurchasedSimulated && !lesson.isUnlocked)
                            Log.d("CourseDetailPage", "Lesson: ${lesson.title}, order: ${lesson.order}, isAccessible: $isLessonAccessible, isUnlocked: ${lesson.isUnlocked}, isPurchasedSimulated: $isPurchasedSimulated")
                            CourseLessonItem(
                                lesson = lesson,
                                isFreeCourse = isFreeCourse,
                                isSelected = selectedLessonId == lesson.id,
                                isLessonAccessible = isLessonAccessible,
                                onLessonClick = { clickedId ->
                                    Log.d("CourseDetailPage", "Clicked lesson: courseId=$courseId, lessonId=$clickedId, isAccessible=$isLessonAccessible")
                                    if (isLessonAccessible || lesson.isUnlocked) {
                                        if (selectedLessonId == clickedId) {
                                            // کلیک دوباره روی درس باز: برو به صفحه جزئیات
                                            navController.navigate("lesson_detail/$courseId/$clickedId") {
                                                launchSingleTop = true
                                            }
                                            selectedLessonId = null // بستن زیربخش
                                        } else {
                                            // انتخاب درس جدید: زیربخش باز بشه
                                            selectedLessonId = clickedId
                                        }
                                    } else {
                                        // اگه درس قفل باشه، فقط باز و بسته کن زیربخش
                                        selectedLessonId = if (selectedLessonId == clickedId) null else clickedId
                                    }
                                },
                                onCourseItemClick = { clickedLesson, clickedItem ->
                                    val canAccessLessonContent = isFreeCourse || clickedLesson.isUnlocked
                                    if (canAccessLessonContent && courseId.isNotEmpty()) {
                                        navController.navigate("lesson_detail/$courseId/${clickedLesson.id}") {
                                            launchSingleTop = true
                                        }
                                    } else {
                                        Log.d("CourseDetailPage", "Access denied to lesson content: ${clickedLesson.title}")
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