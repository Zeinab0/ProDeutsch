package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.Review

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans

@Composable
fun ExitReviewDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(260.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(horizontal = 4.dp) // فاصله از کناره‌ها
            ) {
                Column(
                    modifier = Modifier.align(Alignment.CenterEnd), // متن‌ها از راست چیده بشن
                    horizontalAlignment = Alignment.End, // متن‌ها راست‌چین داخل Column
                    verticalArrangement = Arrangement.spacedBy(8.dp) // فاصله بین متن‌ها
                ) {
                    Text(
                        text = "واقعاً داری میری؟",
                        fontFamily = iranSans,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Text(
                        text = "!اگه رفتی باید دوباره از اول شروع کنی",
                        fontFamily = iranSans,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                // ❌ آره
                Button(
                    onClick = { onConfirm() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, // بدون رنگ پس‌زمینه
                        contentColor = Color.Red
                    ),
                    border = BorderStroke(1.dp, Color.Red),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp), // حذف Padding داخلی
                    modifier = Modifier.size(100.dp, 53.dp)
                ) {
                    Text(
                        text = "آره",
                        color = Color.Red,
                        fontFamily = iranSans
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))

                // ✅ نه پشیمون شدم
                Button(
                    onClick = { onConfirm() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7AB2B2),
                        contentColor = Color.White
                    ),

                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp), // حذف Padding داخلی
                    modifier = Modifier.size(100.dp, 53.dp)
                ) {
                    Text(
                        text = "نه پشیمون شدم",
                        color = Color.White,
                        fontFamily = iranSans
                    )
                }

            }
        }
    }
}
