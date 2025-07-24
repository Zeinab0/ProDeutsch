package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.data.models.CourseItem
import com.example.moarefiprod.data.models.CourseItemType
import com.example.moarefiprod.data.models.CourseLesson

@Composable
fun DarsDetails(lesson: CourseLesson, navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val imageSectionHeight = screenHeight * 0.32f
    val overlapAmount = 50.dp

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
                            text = lesson.title,
                            fontSize = (screenWidth * 0.04f).value.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = iranSans,
                            color = Color.Black,
                            textAlign = TextAlign.End,
                        )
                        Text(
                            text = "${lesson.duration} دقیقه",
                            fontSize = (screenWidth * 0.03f).value.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = iranSans,
                            color = Color.Gray,
                            textAlign = TextAlign.End,
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Image(
                        painter = painterResource(
                            id = when (lesson.id) {
                                "01" -> R.drawable.num_01
                                "02" -> R.drawable.num_02
                                "03" -> R.drawable.num_03
                                "04" -> R.drawable.num_04
                                "05" -> R.drawable.num_05
                                "06" -> R.drawable.num_06
                                "07" -> R.drawable.num_07
                                else -> R.drawable.num_01
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(screenHeight * 0.015f)
                ) {
                    items(lesson.items) { item ->
                        LessonItemRowUI(item = item)
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
                clip = false
            )
            .clip(cardShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, cardShape)
                .border(1.dp, Color(0xFF90CECE), cardShape)
                .padding(horizontal = 16.dp, vertical = 12.dp),
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
                    )
                    Text(
                        text = "15 دقیقه", // Placeholder duration. Replace with item.duration if available.
                        fontSize = (screenWidth * 0.028f).value.sp,
                        fontFamily = iranSans,
                        color = Color.Gray,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                val iconRes = when (item.type) { // حالا type از نوع CourseItemType هست
                    CourseItemType.VIDEO -> R.drawable.video
                    CourseItemType.DOCUMENT -> R.drawable.document
                    CourseItemType.QUIZ1, CourseItemType.QUIZ2, CourseItemType.QUIZ3, CourseItemType.FINAL_EXAM -> R.drawable.exam
                    CourseItemType.WORDS -> R.drawable.words
                    else -> R.drawable.video
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

@Preview(showBackground = true)
@Composable
fun PreviewDarsDetails() {
    val sampleLesson = CourseLesson(
        id = "01",
        title = "الفبای آلمانی",
        duration = "15",
        items = listOf(
            CourseItem(type = CourseItemType.VIDEO, title = "فیلم آموزشی", isCompleted = false),
            CourseItem(type = CourseItemType.DOCUMENT, title = "جزوه", isCompleted = false)
        ),
        isUnlocked = true,
        isCompleted = false
    )
    val navController = rememberNavController()
    DarsDetails(sampleLesson, navController)
}