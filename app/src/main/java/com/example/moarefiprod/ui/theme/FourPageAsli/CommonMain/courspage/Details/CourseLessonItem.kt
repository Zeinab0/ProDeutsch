package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.data.models.CourseItem
import com.example.moarefiprod.data.models.CourseLesson
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Details.DottedLine

@Composable
fun CourseLessonItem(
    lesson: CourseLesson,
    isFreeCourse: Boolean,
    isSelected: Boolean,
    isLessonAccessible: Boolean,
    onLessonClick: (String) -> Unit,
    onCourseItemClick: (CourseLesson, CourseItem) -> Unit
) {
    Log.d("CourseLessonItem", "Rendering lesson: ${lesson.title}, order: ${lesson.order}, isUnlocked: ${lesson.isUnlocked}, isFreeCourse: $isFreeCourse, isLessonAccessible: $isLessonAccessible")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLessonClick(lesson.id) }
            .padding(vertical = 15.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLessonAccessible) {
                Log.d("CourseLessonItem", "Showing play icon for lesson: ${lesson.title}")
                Icon(
                    painter = painterResource(id = R.drawable.play_ic),
                    contentDescription = "Play",
                    modifier = Modifier.size(35.dp),
                    tint = Color.Unspecified
                )
            } else {
                Log.d("CourseLessonItem", "Showing lock icon for lesson: ${lesson.title}")
                Icon(
                    painter = painterResource(id = R.drawable.lock_ic),
                    contentDescription = "Locked",
                    modifier = Modifier.size(35.dp),
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = lesson.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                    color = Color.Black,
                    textAlign = TextAlign.End
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    if (lesson.isCompleted) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_done),
                            contentDescription = "تکمیل شده",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(14.dp)
                        )
                    }

                    Text(
                        text = lesson.duration,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = iranSans,
                        color = Color.Gray,
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            Image(
                painter = painterResource(
                    id = when (lesson.order) {
                        1 -> R.drawable.num_01
                        2 -> R.drawable.num_02
                        3 -> R.drawable.num_03
                        4 -> R.drawable.num_04
                        5 -> R.drawable.num_05
                        6 -> R.drawable.num_06
                        7 -> R.drawable.num_07
                        else -> R.drawable.num_01
                    }
                ),
                contentDescription = "Lesson Number",
                modifier = Modifier.size(30.dp),
                colorFilter = if (isLessonAccessible || isSelected) {
                    ColorFilter.tint(Color(0xFF4D869C))
                } else null
            )
        }

        // نمایش زیربخش‌ها فقط وقتی درس انتخاب شده
        AnimatedVisibility(
            visible = isSelected,
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                if (lesson.items.isEmpty()) {
                    Log.d("CourseLessonItem", "No items for lesson: ${lesson.title}")
                    Text(
                        text = "هیچ زیربخشی موجود نیست",
                        fontSize = 12.sp,
                        fontFamily = iranSans,
                        color = Color.Gray,
                        textAlign = TextAlign.End
                    )
                } else {
                    lesson.items.forEachIndexed { index, item ->
                        Log.d("CourseLessonItem", "Rendering item: ${item.title} for lesson: ${lesson.title}")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    onCourseItemClick(lesson, item)
                                },
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = iranSans,
                                color = when {
                                    item.isCompleted -> Color(0xFF4D869C)
                                    !item.isCompleted && isLessonAccessible -> Color.Black
                                    else -> Color.Gray
                                },
                                textAlign = TextAlign.End
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            val starIconRes = if (item.isCompleted) R.drawable.star else R.drawable.star
                            val starTint = when {
                                item.isCompleted -> Color(0xFFFDD835)
                                !item.isCompleted && isLessonAccessible -> Color(0xFFFDD835).copy(alpha = 0.5f)
                                else -> Color.Gray
                            }

                            Icon(
                                painter = painterResource(id = starIconRes),
                                contentDescription = null,
                                tint = starTint,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        if (index < lesson.items.lastIndex) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                DottedLine(width = 200.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}