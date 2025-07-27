package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.details

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.data.models.CourseItem
import com.example.moarefiprod.data.models.CourseItemType
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseViewModel

@Composable
fun DarsDetails(
    navController: NavController,
    courseId: String,
    lessonId: String,
    courseViewModel: CourseViewModel = viewModel()
) {
    val course by courseViewModel.selectedCourse.collectAsState()
    val lessons by courseViewModel.selectedCourseLessons.collectAsState()
    val lessonItems by courseViewModel.selectedLessonItems.collectAsState()
    val isLoading by courseViewModel.isLoading.collectAsState()
    val errorMessage by courseViewModel.errorMessage.collectAsState()

    val currentLesson = remember(lessons, lessonId) {
        lessons.find { it.id == lessonId }
    }
    LaunchedEffect(courseId, lessonId) {
        courseViewModel.loadSelectedCourseDetailsAndLessons(courseId)
        courseViewModel.loadLessonItems(courseId, lessonId)
        Log.d("DarsDetails", "lessonItems: $lessonItems")
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val imageSectionHeight = screenHeight * 0.32f
    val overlapAmount = 50.dp

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "خطا: $errorMessage", color = Color.Red, fontFamily = iranSans)
        }
    } else if (currentLesson == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "درس یافت نشد.", fontFamily = iranSans)
        }
    } else {
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dars_pic),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageSectionHeight)
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(start = screenWidth * 0.03f, top = screenHeight * 0.05f)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.backbtn),
                        contentDescription = "Back",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(screenWidth * 0.07f)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = imageSectionHeight - overlapAmount)
                        .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                        .shadow(22.dp, RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                        .background(Color.White)
                        .padding(horizontal = screenWidth * 0.07f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = screenHeight * 0.03f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = currentLesson.title,
                                fontSize = (screenWidth * 0.04f).value.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = iranSans,
                                color = Color.Black,
                                textAlign = TextAlign.End,
                            )
                            Text(
                                text = "${currentLesson.duration}",
                                fontSize = (screenWidth * 0.03f).value.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = iranSans,
                                color = Color.Gray,
                                textAlign = TextAlign.End,
                                style = androidx.compose.ui.text.TextStyle(
                                    textDirection = androidx.compose.ui.text.style.TextDirection.Rtl
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Image(
                            painter = painterResource(
                                id = when (currentLesson.order) {
                                    1 -> R.drawable.num_01
                                    2 -> R.drawable.num_02
                                    3 -> R.drawable.num_03
                                    4 -> R.drawable.num_04
                                    5 -> R.drawable.num_05
                                    6 -> R.drawable.num_06
                                    7 -> R.drawable.num_07
                                    else -> R.drawable.num_01 // پیش‌فرض
                                }
                            ),
                            contentDescription = "Lesson Number",
                            modifier = Modifier
                                .size(screenWidth * 0.07f),
                            colorFilter = ColorFilter.tint(Color(0xFF90CECE))
                        )
                    }

                    Spacer(modifier = Modifier.height(screenHeight * 0.02f))

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(screenHeight * 0.015f),
                        contentPadding = PaddingValues(bottom = screenHeight * 0.02f) // اضافه کردن پدینگ پایین
                    ) {
                        items(lessonItems) { item ->
                            LessonItemRowUI(item = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LessonItemRowUI(item: CourseItem) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardShape = RoundedCornerShape(16.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Handle click to open item content later */ }
            .shadow(
                elevation = 6.dp,
                shape = cardShape,
                clip = true
            )
            .clip(cardShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, cardShape)
                .border(1.dp, Color(0xFF90CECE), cardShape)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = item.title,
                        fontSize = (screenWidth * 0.03f).value.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${item.duration} دقیقه",
                        fontSize = (screenWidth * 0.028f).value.sp,
                        fontFamily = iranSans,
                        color = Color.Gray,
                        textAlign = TextAlign.End,
                        style = androidx.compose.ui.text.TextStyle(
                            textDirection = androidx.compose.ui.text.style.TextDirection.Rtl
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                val iconRes = when (item.type) {
                    CourseItemType.VIDEO -> R.drawable.video
                    CourseItemType.DOCUMENT -> R.drawable.document
                    CourseItemType.QUIZ1, CourseItemType.QUIZ2, CourseItemType.QUIZ3, CourseItemType.FINAL_EXAM -> R.drawable.exam
                    CourseItemType.WORDS -> R.drawable.words
                }
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(screenWidth * 0.07f)
                )
            }
        }
    }
}