package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören.evenShadow

@Composable
fun ResultDialog(
    navController: NavController,
    courseId: String,
    lessonId: String,
    contentId: String,
    timeInSeconds: Int,
    returnRoute: String, // ✅ مسیر داینامیک برای بازگشت
    onDismiss: () -> Unit
) {
    val formattedTime = String.format("%02d:%02d", timeInSeconds / 60, timeInSeconds % 60)
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // بخش اول - تیتر
                Text(
                    text = "^_^ خسته نباشید دوست خوبم",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // بخش دوم - توضیحات
                Text(
                    text = "دوس داری بریم آزمون بعدی؟\nزمان کل بازی‌ها: $formattedTime",
                    fontSize = 12.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.ExtraLight,
                    textAlign = TextAlign.Right,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .evenShadow(radius = 25f, cornerRadius = 20f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .height(45.dp)
                            .clickable {
                                navController.navigate(returnRoute) // ✅ بازگشت به مسیر داده‌شده
                                onDismiss()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("ن", color = Color.Red, fontFamily = iranSans)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .evenShadow(radius = 25f, cornerRadius = 20f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF7AB2B2))
                            .height(45.dp)
                            .clickable {
                                navController.navigate("memoryGame/$courseId/$lessonId/$contentId/memory_game_2?gameIndex=0")
                                onDismiss()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("بله", color = Color.White, fontFamily = iranSans)
                    }
                }
            }
        }
    }
}
