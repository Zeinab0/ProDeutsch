package com.example.moarefiprod.ui.theme.logofirst

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans

@Composable
fun Advertisement2(onNext: () -> Unit, onSkip: () -> Unit) {
    // ✅ گرفتن اندازه صفحه برای مقیاس‌بندی بهتر
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        // ✅ دکمه "رد شدن" در گوشه بالا سمت راست
        Text(
            text = "رد شدن",
            fontSize = (screenWidth * 0.04f).value.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = iranSans,
            color = Color(0xFF5E5E5E),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = screenHeight * 0.05f, end = screenWidth * 0.05f)
                .clickable { onSkip() }
        )

        // ✅ ستون وسط صفحه که شامل تصویر و متن‌ها است
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center, // ✅ وسط‌چین کردن عمودی محتوا
            horizontalAlignment = Alignment.CenterHorizontally // ✅ وسط‌چین کردن افقی محتوا
        ) {
            // ✅ تصویر تبلیغاتی
            Image(
                painter = painterResource(id = R.drawable.ad1),
                contentDescription = "advertisement",
                modifier = Modifier
                    .width(screenWidth * 0.85f)
                    .height(screenHeight * 0.3f)
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.03f))

            // ✅ متن عنوان
            Text(
                text = "تمرین مکالمه واقعی",
                fontSize = (screenWidth * 0.06f).value.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = iranSans,
                color = Color(0xFF000000),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.02f))

            // ✅ متن توضیحاتی
            Text(
                text = "با تمرین\u200Cهای مکالمه و تلفظ دقیق، مهارت گفتاری خود را تقویت کنید. جملات را بشنوید، تکرار کنید و به\u200Cآرامی مثل یک آلمانی صحبت کنید! ",
                fontSize = (screenWidth * 0.04f).value.sp,
                fontWeight = FontWeight.Light,
                fontFamily = iranSans,
                textAlign = TextAlign.Center,
                color = Color(0xFF000000),
                modifier = Modifier.width(screenWidth * 0.9f)
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            // ✅ نشانگر صفحات تبلیغاتی (ریسپانسیو)
            Row(
                modifier = Modifier.width(screenWidth * 0.3f),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.twocircle),
                    contentDescription = "indicator",
                    modifier = Modifier.size(screenWidth * 0.066f, screenHeight * 0.014f)
                )
                Spacer(modifier = Modifier.width(screenWidth * 0.02f))

                Image(
                    painter = painterResource(id = R.drawable.circle),
                    contentDescription = "indicator",
                    modifier = Modifier.size(screenWidth * 0.03f)
                )
                Spacer(modifier = Modifier.width(screenWidth * 0.02f))

                Image(
                    painter = painterResource(id = R.drawable.circle),
                    contentDescription = "indicator",
                    modifier = Modifier.size(screenWidth * 0.03f)
                )
            }
        }

        // ✅ دکمه "بعدی" ریسپانسیو که از `Column` جدا شده و پایین صفحه قرار گرفته است
        Button(
            onClick = { onNext() },
            modifier = Modifier
                .align(Alignment.BottomCenter) // ✅ دکمه را در پایین صفحه قرار می‌دهد
                .padding(bottom = screenHeight * 0.09f)
                .width(screenWidth * 0.7f)
                .height(screenHeight * 0.07f),
            colors = ButtonDefaults.buttonColors(Color(0xFF4D869C)),
            shape = RoundedCornerShape(screenWidth * 0.02f)
        ) {
            Text(
                text = "بعدی",
                color = Color.White,
                fontSize = (screenWidth * 0.05f).value.sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
