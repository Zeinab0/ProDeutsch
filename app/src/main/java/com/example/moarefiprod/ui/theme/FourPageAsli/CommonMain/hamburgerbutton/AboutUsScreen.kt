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

@Composable
fun AboutUsScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 🔹 دکمه برگشت
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(
                        start = screenWidth * 0.03f,
                        top = screenHeight * 0.05f
                    )
                    .size(screenWidth * 0.09f)
                    .align(Alignment.Start)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.backbtn),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // 🔹 بخش بالایی: تصویر و تیتر
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.3f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hale),
                    contentDescription = "Top Halo Image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(screenWidth * 1.2f)
                        .height(screenHeight * 0.45f)
                        .align(Alignment.Center)
                        .offset(y = screenHeight * -0.05f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = screenWidth * 0.05f)
                        .offset(y = screenHeight * -0.05f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.charactersaboutus),
                        contentDescription = "Characters",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(screenWidth * 0.35f)
                            .offset(y = screenHeight * -0.01f)
                    )

                    Spacer(modifier = Modifier.width(screenWidth * 0.1f))

                    Text(
                        text = "درباره ما",
                        fontSize = (screenWidth.value * 0.045f).sp,
                        lineHeight = (screenWidth.value * 0.07f).sp,
                        textAlign = TextAlign.Right,
                        color = Color.DarkGray,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            // 🔹 متن اصلی
            Text(
                text = """
                    ما یک تیم کوچک با تخصص‌های متنوع هستیم که هدفمان تلفیق دانش آموزشی و فناوری برای ارائه خدمات برتر است.
                    آقای محمدصادق احمدی، معلم با تجربه در زمینه آموزش زبان آلمانی، سابقه‌ای برجسته در تدریس دارد.
                    ایشان به مدت ۹ سال در سوییس زندگی و کار کرده و پیش از آن در افغانستان به عنوان معلم فعالیت داشته‌اند.
                    در حال حاضر، در سوییس به عنوان مربی کودکان درحال فعالیت هستند همچنین کلاس‌های حضوری زبان آلمانی برگزار می‌کنند.
                    علاوه بر این، آقای احمدی دارای مدرک زبان C1 بوده و به زبان انگلیسی کاملاً مسلط هستند؛ همچنین در حال یادگیری زبان فرانسوی می‌باشند.
                    در کنار ایشان، زینب احمدی و فرشته جعفری، توسعه‌دهندگان اپلیکیشن و طراحان برنامه و دانشجویان مهندسی کامپیوتر دانشگاه اصفهان،
                    به بخش فناوری تیم ما رونق می‌بخشند. ترکیب این تخصص‌ها زمینه‌ساز ایجاد رویکردهای نوآورانه در حوزه‌های آموزش و فناوری شده است.
                    ما باور داریم با بهره‌گیری از روش‌های نوین آموزشی و آخرین دستاوردهای فنی، می‌توانیم تجربه‌ای منحصربه‌فرد و مفید
                    برای کاربران و دانشجویان فراهم آوریم. از شما دعوت می‌کنیم تا در این مسیر همراه ما باشید و از خدمات و تجربیات ما بهره‌مند شوید.
                """.trimIndent(),
                fontSize = (screenWidth.value * 0.033f).sp,
                lineHeight = (screenWidth.value * 0.06f).sp,
                textAlign = TextAlign.Right,
                color = Color.DarkGray,
                fontFamily = iranSans,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .offset(y = screenHeight * -0.12f)
                    .fillMaxWidth()
                    .padding(horizontal = screenWidth * 0.06f)
            )
        }

        // 🔹 لوگو پایین
        Image(
            painter = painterResource(id = R.drawable.prodeutsch),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(screenWidth * 0.25f, screenHeight * 0.05f)
                .padding(bottom = screenHeight * 0.015f)
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AboutUsScreenPreview() {
    AboutUsScreen(navController = rememberNavController())
}