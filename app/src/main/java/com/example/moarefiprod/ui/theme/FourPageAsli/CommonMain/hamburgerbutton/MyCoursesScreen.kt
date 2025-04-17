package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans

@Composable
fun MyCoursesPage(onBackClick: () -> Unit = {}) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // 🔙 دکمه برگشت
        Icon(
            painter = painterResource(id = R.drawable.backbtn), // آیکن دلخواهت
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier
                .size(28.dp)
                .clickable { onBackClick() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🔎 سرچ‌بار
        SearchBar()

        Spacer(modifier = Modifier.height(20.dp))

        // 📌 تیتر
        Text(
            text = ":دوره‌های من",
            fontFamily = iranSans,
            fontSize = (screenWidth * 0.045f).value.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Right,
            modifier = Modifier.fillMaxWidth()
                .offset(x=(-5).dp)
        )



        Spacer(modifier = Modifier.height(16.dp))

        // 🧩 کارت دوره
        CourseCardCustom()
    }
}

@Composable
fun CourseCardCustom() {
    val boxWidth = 348.dp
    val boxHeight = 110.dp

    Box(
        modifier = Modifier
            .width(boxWidth)
            .height(boxHeight)
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🖼 تصویر دوره (سمت چپ)
            Image(
                painter = painterResource(id = R.drawable.cours1),
                contentDescription = "Course Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            // 💰 قیمت و دکمه
            Column(
                verticalArrangement = Arrangement.SpaceBetween, // از اینجا استفاده می‌کنیم تا فاصله‌ها تنظیم بشه
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxHeight()
                    .padding(vertical = 6.dp) // فاصله‌های عمودی کم شده
                    .offset(x=6.dp,y=4.dp)
            ) {
                Spacer(modifier = Modifier.height(30.dp)) // فاصله بیشتر برای پایین آوردن قیمت

                Text(
                    text = " 120 تومان",
                    fontSize = 12.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(3.dp)) // فاصله بیشتر بین قیمت و دکمه

                Box(
                    modifier = Modifier
                        .width(66.dp)  // تنظیم عرض دکمه
                        .background(Color(0xFF90CECE), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .clickable { /* انجام عملیاتی مثل شروع دوره */ }
                ) {
                    Text(
                        text = "شروع دوره",
                        fontSize = 10.sp,
                        fontFamily = iranSans,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)  // وسط چین کردن متن
                    )
                }
            }

            // 📝 متن‌ها سمت راست با راست‌چین شدن
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "آموزش کلمات مهم آلمانی",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                    color = Color.Black
                )
                Text(
                    text = "با این دوره سریع کلمات را یاد بگیر!",
                    fontSize = 11.sp,
                    fontFamily = iranSans,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "سطح دوره: بدون پیش‌نیاز",
                    fontSize = 10.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "مدت زمان: ۱۰ ساعت",
                    fontSize = 10.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "تعداد جلسات: ۱۲ جلسه",
                    fontSize = 10.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MyCoursesPagePreview() {
    MyCoursesPage()
}
