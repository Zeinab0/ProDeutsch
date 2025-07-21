// In file: FourPageAsli.CommonMain.courspage/CourseInfoSection.kt

package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Details // مطمئن شوید پکیج درست است

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans // مطمئن شوید این ایمپورت وجود دارد

@Composable
fun CourseInfoSection(
    courseTitle: String,
    coursePrice: Int,
    courseZaman: String,
    courseSath: String,
    showPriceInHeader: Boolean // این پارامتر تعیین می‌کند قیمت نمایش داده شود یا خیر
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // تابع formatPrice را به اینجا منتقل می‌کنیم
    fun formatPrice(price: Int): String {
        return if (price == 0) "رایگان" else "هزار تومان$price "
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = screenHeight * 0.03f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // این بلاک مسئول نمایش قیمت/رایگان است
            if (showPriceInHeader) { // فقط اگر showPriceInHeader true باشد، این Text نمایش داده می‌شود
                Text(
                    text = formatPrice(coursePrice), // اینجا از تابع formatPrice استفاده می‌شود
                    fontSize = (screenWidth * 0.04f).value.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                    color = Color(0xFF4D869C),
                    textAlign = TextAlign.Left,
                    maxLines = 1,
                    softWrap = false,
                )
            }

            Text(
                text = courseTitle,
                fontSize = (screenWidth * 0.05f).value.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = iranSans,
                color = Color.Black,
                textAlign = TextAlign.Right,
                modifier = Modifier.weight(3f)
            )
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.005f))

        Text(
            text = courseZaman,
            fontSize = (screenWidth * 0.032f).value.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = iranSans,
            color = Color.Black.copy(alpha = 0.7f),
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(screenHeight * 0.015f))

        Text(
            text = ":درباره دوره ",
            fontSize = (screenWidth * 0.045f).value.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = iranSans,
            color = Color.Black,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "مدرس: محمد صادق احمدی",
            fontSize = (screenWidth * 0.03f).value.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = iranSans,
            color = Color.Black.copy(alpha = 0.7f),
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "در این دوره شما می‌توانید بدون هیچ پیش‌نیازی زبان آلمانی را یاد بگیرید و آزمون بدهید. در پایان این دوره قادر خواهید بود آزمون کوتاه سطح $courseSath را به خوبی پشت سر بگذارید.",
            fontSize = (screenWidth * 0.03f).value.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = iranSans,
            color = Color.Black.copy(alpha = 0.8f),
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}