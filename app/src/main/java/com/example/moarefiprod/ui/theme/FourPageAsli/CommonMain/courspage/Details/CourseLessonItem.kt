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
    onLessonClick: (String) -> Unit, // این برای Expand/Collapse کردن است
    onCourseItemClick: (CourseLesson, CourseItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLessonClick(lesson.id) } // این فقط برای Expand/Collapse شدن درس است
            .padding(vertical = 15.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isFreeCourse || lesson.isUnlocked) {
                Icon(
                    painter = painterResource(id = R.drawable.play_ic),
                    contentDescription = "Play",
                    modifier = Modifier.size(35.dp),
                    tint = Color.Unspecified
                )
            } else {
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
                modifier = Modifier.size(30.dp),
                colorFilter = if (lesson.isCompleted || isSelected) {
                    ColorFilter.tint(Color(0xFF4D869C))
                } else null
            )
        }

        // Animated visibility for lesson items
//        AnimatedVisibility(
//            visible = isSelected,
//            enter = expandVertically(expandFrom = Alignment.Top),
//            exit = shrinkVertically(shrinkTowards = Alignment.Top)
//        )
        // تو CourseLessonItem.kt، به صورت موقت این خط رو تغییر بده:
        AnimatedVisibility(
            visible = true, // به جای isSelected
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top)
        )

        {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                lesson.items.forEachIndexed { index, item ->
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
                            // 🔴🔴🔴 رنگ متن آیتم را هم بر اساس وضعیت تکمیل شدن تغییر می‌دهیم 🔴🔴🔴
                            color = when {
                                item.isCompleted -> Color(0xFF4D869C) // اگر تکمیل شده، رنگ سبز آبی
                                // اگر در حال انجام باشد (فرض می‌کنیم اگر تکمیل نشده و isLocked هم نیست، در حال انجام است)
                                !item.isCompleted && (lesson.isUnlocked || isFreeCourse) -> Color.Black // یا رنگی دیگر برای در حال انجام
                                else -> Color.Gray // اگر قفل یا انجام نشده
                            },
                            textAlign = TextAlign.End
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        // 🔴🔴🔴 تغییر در اینجا: استفاده از آیکون ستاره و منطق رنگ‌آمیزی 🔴🔴🔴
                        val starIconRes = if (item.isCompleted) R.drawable.star else R.drawable.star // ⬅️ اینجا آیکون ها را انتخاب می کنیم
                        val starTint = when {
                            item.isCompleted -> Color(0xFFFDD835) // ستاره زرد کامل برای تکمیل شده
                            // اگر درس اصلی باز یا رایگان است و این آیتم تکمیل نشده، یعنی در حال انجام است
                            !item.isCompleted && (lesson.isUnlocked || isFreeCourse) -> Color(0xFFFDD835).copy(alpha = 0.5f) // زرد نیمه شفاف برای در حال انجام (می‌تواند فقط دور ستاره باشد)
                            else -> Color.Gray // طوسی برای انجام نشده / قفل
                        }

                        Icon(
                            painter = painterResource(id = starIconRes),
                            contentDescription = null,
                            tint = starTint,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    lesson.items.forEachIndexed { index, item ->
                        Log.d("CourseLessonItem", "Item $index: ${item.title}")
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