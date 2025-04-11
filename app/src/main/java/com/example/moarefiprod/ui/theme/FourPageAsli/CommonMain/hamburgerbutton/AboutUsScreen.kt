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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
                .padding(bottom = 130.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = screenWidth * 0.03f,
                        top = screenHeight * 0.05f
                    ),
                contentAlignment = Alignment.TopStart
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.backbtn),
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(screenWidth * 0.09f)
                    )
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                // هاله
                Image(
                    painter = painterResource(id = R.drawable.hale),
                    contentDescription = "Top Halo Image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(500.dp)
                        .height(350.dp)
                        .align(Alignment.Center)
                        .offset(y = (-100).dp)
                )

                // تصویر کاراکتر و متن درباره ما
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp, end = 20.dp)
                        .offset(y = (-40).dp), // ← این رو تغییر بده برای بالا پایین بردن
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.charactersaboutus),
                        contentDescription = "Overlay Image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(155.dp)
                            .offset(y = (-50).dp)
                    )

                    Spacer(modifier = Modifier.width(77.dp)) // ← فاصله رو کمتر کردم

                    Text(
                        text = "درباره ما",
                        fontSize = 18.sp,
                        lineHeight = 30.sp,
                        textAlign = TextAlign.Right,
                        color = Color.DarkGray,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.offset(y = (-47).dp) // ← اینجا متن رو ببر بالا یا پایین
                    )
                }
            }

            // متن اصلی
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
                fontSize = 12.sp,
                lineHeight = 30.sp,
                textAlign = TextAlign.Right,
                color = Color.DarkGray,
                fontFamily = iranSans,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.offset(y = (-105).dp)
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
            )
        }

        // لوگو پایین صفحه
        Box(
            modifier = Modifier.fillMaxSize(),
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
