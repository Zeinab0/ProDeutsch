package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moarefiprod.data.models.Course
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moarefiprod.iranSans
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextOverflow


private fun formatTomanK(price: Int): String {
    if (price <= 0) return "رایگان"
    val k = price / 1000
    return "$k هزار تومان"
}

@Composable
fun CourseCard(
    course: Course,
    navController: NavController,
    viewModel: CourseViewModel
) {
    val myCourses by viewModel.myCourses.collectAsState()
    val purchasedIds by viewModel.purchasedCourseIds.collectAsState()

    val inMyList = myCourses.any { it.id == course.id }
    val purchased = purchasedIds.contains(course.id) || course.isPurchased
    val isFree = course.price == 0

    val purchasedJustNow = remember { mutableStateOf(false) }
    val isContinue = purchased || (isFree && inMyList) || purchasedJustNow.value
    val showPrice = !isContinue

    val ctaLabel = when {
        isContinue -> "ادامه یادگیری"
        isFree -> "اضافه به دوره‌های من"
        else -> "خرید"
    }

    val ctaColor = if (isContinue) Color(0xFF2E7D32) else Color(0xFF4D869C)

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardHeight = screenWidth * 0.3f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // تصویر دوره
            AsyncImage(
                model = course.imageUrl,
                contentDescription = "Course Image",
                modifier = Modifier
                    .fillMaxHeight()
                    .width(cardHeight)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // ستون دکمه‌ها و قیمت
            Column(
                modifier = Modifier
                    .width(95.dp)
                    .align(Alignment.Bottom)
                    .padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (showPrice) {
                    Text(
                        text = if (isFree) "رایگان" else formatTomanK(course.price),
                        fontSize = 10.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                        color = if (isFree) Color(0xFF2E7D32) else Color.Black,
                        textAlign = TextAlign.Right,
                        style = TextStyle(textDirection = TextDirection.Rtl)
                    )
                }

                Button(
                    onClick = {
                        when {
                            isContinue -> {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("course_id", course.id)
                                navController.navigate("course_detail/${course.id}")
                            }
                            isFree -> {
                                viewModel.addCourseToMyList(course)
                                purchasedJustNow.value = true
                            }
                            else -> {
                                viewModel.markCoursePurchased(course.id) {
                                    viewModel.addCourseToMyList(course)
                                    purchasedJustNow.value = true
                                }
                            }
                        }
                    },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ctaColor)
                ) {
                    Text(
                        text = ctaLabel,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        fontSize = 8.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // ستون اطلاعات دوره
            Column(
                modifier = Modifier
                    .padding(0.dp, 10.dp, 10.dp, 10.dp)
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = course.title,
                    fontFamily = iranSans,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Right,
                    style = TextStyle(textDirection = TextDirection.Rtl)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = course.description,
                    fontFamily = iranSans,
                    fontSize = 9.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    style = TextStyle(textDirection = TextDirection.Rtl)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontFamily = iranSans)) {
                            append("تعداد دروس: ")
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal, fontFamily = iranSans)) {
                            append("${course.teadad}")
                        }
                    },
                    fontSize = 10.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}

@Composable
fun NewLabel(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .offset(x = (-10).dp, y = 6.dp)
            .rotate(-40f)
            .background(Color.Red, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = "جدید",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = iranSans,
            style = TextStyle(textDirection = TextDirection.Rtl)
        )
    }
}
