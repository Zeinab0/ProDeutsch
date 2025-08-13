package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans

@Composable
fun MyCoursesScreen(
    navController: NavController,
    onBackClick: () -> Unit = { navController.popBackStack() }
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // 🔎 state جستجو (فعلاً استفاده نمی‌کنیم؛ برای فیلتر بعدی آماده است)
    var query by remember { mutableStateOf("") }

//    val q = remember(query) { query.trim() }
//    val filtered = remember(myCourses, q) {
//        if (q.isEmpty()) myCourses
//        else myCourses.filter { c ->
//            (c.title?.contains(q, ignoreCase = true) == true) ||
//                    (c.description?.contains(q, ignoreCase = true) == true)
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = screenWidth * 0.04f, vertical = screenHeight * 0.02f)
    ) {
        // دکمه برگشت بالا
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = screenWidth * 0.03f,
                    top = screenHeight * 0.05f
                ),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.backbtn),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(screenWidth * 0.09f)
                )
            }
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.015f))

        // ✅ حتماً از SearchBar سفارشی خودت استفاده کن (نه متریال۳)
        com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.SearchBar(
            query = query,
            onQueryChange = { query = it },
            placeholder = ":جستجوی دوره‌های من"
        )

        Spacer(modifier = Modifier.height(screenHeight * 0.03f))

        // 📌 تیتر
        Text(
            text = ":دوره‌های من",
            fontFamily = iranSans,
            fontSize = (screenWidth * 0.045f).value.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = (-5).dp)
        )

        Spacer(modifier = Modifier.height(screenHeight * 0.02f))

        // 🧩 کارت دوره (نمونه‌ی ثابت)
        CourseCardCustom()
    }
}

//val filtered = remember(myCourses, query) {
//    if (query.isBlank()) myCourses
//    else myCourses.filter { c ->
//        c.title.contains(query, ignoreCase = true) ||
//                c.description.contains(query, ignoreCase = true)
//    }
//}

@Composable
fun CourseCardCustom() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val cardWidth = screenWidth * 0.92f
    val imageSize = screenWidth * 0.25f
    val buttonWidth = screenWidth * 0.22f

    Box(
        modifier = Modifier
            .width(cardWidth)
            .height(110.dp)
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
            // 🖼 تصویر دوره
            Image(
                painter = painterResource(id = R.drawable.cours1),
                contentDescription = "Course Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(imageSize)
            )

            Spacer(modifier = Modifier.width(6.dp))

            // 💰 قیمت و دکمه
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxHeight()
                    .padding(vertical = 6.dp)
                    .offset(x = 6.dp, y = 4.dp)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "120 تومان",
                    fontSize = 12.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(3.dp))

                Box(
                    modifier = Modifier
                        .background(Color(0xFF90CECE), RoundedCornerShape(8.dp)) // رنگ جدید برای دکمه
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center // ✅ این اضافه شه
                ) {
                    Text(
                        text = "شروع دوره",
                        fontSize = 9.sp,
                        fontFamily = iranSans,
                        color = Color.White
                    )
                }


            }
            // 📝 متن سمت راست
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyCoursesScreenPreview() {
    val fakeNavController = rememberNavController()
    MyCoursesScreen(navController = fakeNavController)
}
