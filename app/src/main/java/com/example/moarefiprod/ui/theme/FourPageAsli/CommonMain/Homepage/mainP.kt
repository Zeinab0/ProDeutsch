package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.Homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans

@Composable
fun mainpage() {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding( horizontal = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)                // ارتفاع کمی بیشتر تا دکمه جا بشه
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {


                // محتوای میانی: مراحل + تصویر
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // --- ستون مراحل (سمت چپ طرح) ---
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // عنوان بالا وسط
                        Text(
                            text = "دوره آموزشی A1.1",
                            fontFamily = iranSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF1B1B1B),
                            style = TextStyle(
                                textDirection = TextDirection.Rtl
                            ),
                            modifier = Modifier.align(Alignment.End)
                        )

                        // نوار پیشرفت زیر عنوان
                        Box(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 6.dp)
                                .height(10.dp)
                                .fillMaxWidth(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE6E6E6))
                        ) {
                            // درصد
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.46f) // 46%
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            listOf(Color(0xFFBFEAF6), Color(0xFF66D0F4))
                                        )
                                    )
                            )
                            Text(
                                text = "46٪",
                                fontFamily = iranSans,
                                fontSize = 10.sp,
                                color = Color(0xFF4D4D4D),
                                style = TextStyle(
                                    textDirection = TextDirection.Rtl
                                ),
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = 6.dp)
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.End
                        ) {
                            // --- آیتم 1 ---
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "معرفی دوره",
                                    fontFamily = iranSans,
                                    fontSize = 10.sp,
                                    color = Color(0xFF000000),
                                    style = TextStyle(textDirection = TextDirection.Rtl),
                                    textAlign = TextAlign.End
                                )
                                Spacer(Modifier.width(5.dp))
                                Text(
                                    text = "01",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4D869C),
                                    style = TextStyle(textDirection = TextDirection.Rtl),
                                    textAlign = TextAlign.End
                                )
                            }

                            Spacer(Modifier.height(3.dp))

                            // --- آیتم 2 (انتخاب شده) ---
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(22.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFE6F7FF))
                                    .padding(horizontal = 10.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "الفبای آلمانی و تلفظ حروف",
                                        fontFamily = iranSans,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B4B5A),
                                        textAlign = TextAlign.End
                                    )
                                    Spacer(Modifier.width(5.dp))
                                    Text(
                                        text = "02",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4D869C),
                                        style = TextStyle(textDirection = TextDirection.Rtl),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }

                            Spacer(Modifier.height(3.dp))

                            // --- آیتم 3 ---
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "آشنایی با حروف صدا دار",
                                    fontFamily = iranSans,
                                    fontSize = 10.sp,
                                    color = Color(0xFF9AA7B3),
                                    textAlign = TextAlign.End
                                )
                                Spacer(Modifier.width(5.dp))
                                Text(
                                    text = "03",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF97AABE),
                                    style = TextStyle(textDirection = TextDirection.Rtl),
                                    textAlign = TextAlign.End
                                )
                            }
                            Spacer(Modifier.height(16.dp))

                            // دکمه پایین
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .height(22.dp)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(Color(0xFF5A8EA3))
                                    .align(alignment = Alignment.End),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "ادامه بدیم",
                                    fontFamily = iranSans,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }




                    }

                    // --- تصویر سمت راست ---
                    Box(
                        modifier = Modifier
                            .weight(0.8f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bb), // تصویرت
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(start = 6.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }
                }


            }
        }


        // --- جمله روز ---
        Text(
            text = "جمله روز :",
            fontFamily = iranSans,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF000000),
            style = TextStyle(
                textDirection = TextDirection.Rtl
            ),
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 4.dp)

        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFF4D869C), // یه آبی ملایم
                    shape = RoundedCornerShape(12.dp) // هم‌شکل با کارت
                ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(color = Color.White)// ارتفاع دلخواه
                    .paint(
                        painter = painterResource(id = com.example.moarefiprod.R.drawable.tagessatz),
                        contentScale = ContentScale.Crop
                    ),
//                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Übung macht den Meister",
                    fontFamily = iranSans,
                    fontSize = 14.sp,
                    color = Color.Black, // بهتره سفید باشه روی بک‌گراند تصویری
                    fontWeight = FontWeight.Bold
                )
            }
        }


        // --- کلمات حفظ شده و چالش‌ها ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                // لایه‌ی پس‌زمینه‌ی تصویری
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)) // تا تصویر هم مثل کارت گرد شود
                        .paint(
                            painter = painterResource(id = com.example.moarefiprod.R.drawable.banerflash),
                            contentScale = ContentScale.Crop
                        )
                ) {
                    // پنل سمت راست که نصف عرض کارت را می‌گیرد
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.48f)          // نصف عرض کارت
                            .align(Alignment.CenterEnd),  // سمت راست کارت
//                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "کلمات حفظ شده",
                            fontFamily = iranSans,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF424242),
                        )
                        Spacer(modifier = Modifier.height(screenHeight * 0.01f))

                        Text(
                            text = "230",
                            fontFamily = iranSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color(0xFF4D869C)
                        )
                    }
                }
            }


            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0FF)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .paint(
                            painter = painterResource(id = com.example.moarefiprod.R.drawable.chalange),
                            contentScale = ContentScale.Crop
                        )
                        .padding(top = 5.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "چالش‌ آرتیکل",
                        fontFamily = iranSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // --- دوره‌های فعال ---
        Text(
            text = "دوره‌های فعال :",
            fontFamily = iranSans,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF4D869C),
            style = TextStyle(
                textDirection = TextDirection.Rtl
            ),
            modifier = Modifier.align(Alignment.End)

        )

        // یک نمونه کارت دوره
        // لیست نمونه (می‌تونی از ViewModel بیاری)
        val activeCourses = listOf(
            Triple("آموزش آلمانی", "سطح A1", 15) to 0.5f,
            Triple("آموزش آلمانی", "سطح A2", 10) to 0.2f,
            Triple("واژگان روزمره", "سطح A1", 8)  to 0.7f,
            Triple("گرامر پایه", "سطح A2", 12)    to 0.35f,
            Triple("مکالمه مقدماتی", "سطح A1", 9) to 0.1f,
        )

// باکس اسکرول‌دار فقط برای دوره‌های فعال
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = screenHeight * 0.32f) // حدوداً یک سومیِ ارتفاع؛ هرچقدر خواستی تغییر بده
                .padding(top = 6.dp),
            contentPadding = PaddingValues(vertical = 2.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(activeCourses) { (info, progress) ->
                val (title, level, lessons) = info
                CourseItem(
                    title = title,
                    level = level,
                    lessons = lessons,
                    progress = progress
                )
            }
        }

    }
}

@Composable
fun CourseItem(title: String, level: String, lessons: Int, progress: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
// Progress fake bar
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(8.dp)
                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .background(Color(0xFF4D869C), shape = RoundedCornerShape(4.dp))
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontFamily = iranSans,
                    fontSize = 14.sp,
                    style = TextStyle(
                        textDirection = TextDirection.Rtl
                    ),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.End)

                )
                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // فاصله بین آیتم‌ها
                ) {
                    // سطح
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = level, // مثلا "سطح A1"
                            fontFamily = iranSans,
                            fontSize = 12.sp,
                            style = TextStyle(
                                textDirection = TextDirection.Rtl
                            ),
                            color = Color.Gray
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.words), // آیکون سطح
                            contentDescription = "سطح",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )

                    }

                    // تعداد درس‌ها
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$lessons درس",
                            fontFamily = iranSans,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            style = TextStyle(
                                textDirection = TextDirection.Rtl
                            )
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.tamrin), // آیکون درس
                            contentDescription = "تعداد درس‌ها",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )

                    }
                }

            }
            Spacer(Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp)
                    .clip(RoundedCornerShape(6.dp)) // گوشه‌ها گرد بشن
                    .paint(
                        painter = painterResource(id = R.drawable.profw), // تصویر دلخواه
                        contentScale = ContentScale.Crop                 // عکس رو فیت کنه
                    )
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMiddleSection() {
    mainpage()
}
