package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
//import com.google.android.play.integrity.internal.y

@Composable
fun ContactUsScreen(navController: NavController, contentOffset: Dp = 60.dp) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    // ✅ متغیر برای جابجایی عنوان "ارتباط با ما"
    val headerOffset = 60.dp

    // ✅ متغیر برای جابجایی محتوای اصلی (کارت‌ها)
    val contentOffset = 80.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(
                            start = LocalConfiguration.current.screenWidthDp.dp * 0.03f,
                            top = LocalConfiguration.current.screenHeightDp.dp * 0.05f
                        )
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.backbtn),
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(LocalConfiguration.current.screenWidthDp.dp * 0.09f)
                    )
                }
            }

            // 🔻 فاصله قابل تنظیم برای تیتر
            Spacer(modifier = Modifier.height(headerOffset))

            // 🔻 عنوان و آیکون "ارتباط با ما"
            // 🔻 عنوان و آیکون "ارتباط با ما" (راست‌چین + آیکون سمت راست)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 19.dp), // 👉 این باعث میشه یه کوچولو بیشتر به سمت راست بره
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End // 🟢 راست‌چین کل Row
            ) {
                Text(
                    text = "ارتباط با ما",
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.connectwithus),
                    contentDescription = "Contact Icon",
                    modifier = Modifier.size(36.dp)
                )
            }


            // 🔻 فاصله قابل تنظیم برای پایین بردن لیست افراد
            Spacer(modifier = Modifier.height(contentOffset))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ContactItem(
                    imageRes = R.drawable.mrahmadi,
                    name = "محمد صادق احمدی",
                    role = "مدرس زبان آلمانی",
                    email = "ahmadisadeq39@gmail.com",
                    imageSize = 110.dp,
                    imageTextSpacing = (-55).dp
                )

                Spacer(modifier = Modifier.height(26.dp))

                ContactItem(
                    imageRes = R.drawable.mrsahmadi,
                    name = "زینب احمدی",
                    role = "توسعه‌گر اپلیکیشن",
                    email = "zeinabahmadi499@gmail.com",
                    imageSize = 110.dp,
                    imageTextSpacing = (-55).dp
                )

                Spacer(modifier = Modifier.height(26.dp))

                ContactItem(
                    imageRes = R.drawable.meme,
                    name = "فرشته جعفری",
                    role = "توسعه‌گر اپلیکیشن",
                    email = "fereshteJafari3059@gmail.com",
                    imageSize = 110.dp,
                    imageTextSpacing = (-55).dp
                )
            }
        }

        // لوگو پایین
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.prodeutsch),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(110.dp)
                    .offset(y = 8.dp)
            )
        }
    }
}

@Composable
fun ContactItem(
    imageRes: Int,
    name: String,
    role: String,
    email: String,
    imageSize: Dp = 110.dp,               // سایز عکس
    imageTextSpacing: Dp = (-55).dp // فاصله بین عکس و متن (کم شده)

) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp), // حاشیه‌ها برای زیبایی
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 🔹 عکس (چپ‌چین، وسط عمودی)
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = name,
            modifier = Modifier
                .size(imageSize)
        )

        Spacer(modifier = Modifier.width(imageTextSpacing))

        // 🔹 متن‌ها (راست‌چین، وسط عمودی، کنار عکس)
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = name,
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Black
            )
            Text(
                text = role,
                fontFamily = iranSans,
                fontSize = 13.sp,
                color = Color.Gray
            )
            Text(
                text = email,
                fontFamily = iranSans,
                fontSize = 13.sp,
                color = Color(0xFF4D869C)
            )
        }
    }
}