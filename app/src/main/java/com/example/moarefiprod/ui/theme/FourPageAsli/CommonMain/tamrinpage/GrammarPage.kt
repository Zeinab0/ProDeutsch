package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas

@Composable
fun GrammarPage(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var showDialog by remember { mutableStateOf(false) }

    val grammarTopics = listOf(
        "آرتیکل (der, die, das)",
        "صرف فعل",
        "صرف صفت",
        "ضمائر شخصی",
        "ضمائر ملکی",
        "افعال دو بخشی",
        "آکوزاتیو",
        "داتیو",
        "گنتیو",
        "افعال modal",
        "جملات دستوری",
        "افعال دو بخشی",
        "آکوزاتیو",
        "داتیو",
        "گنتیو",
        "افعال modal",
        "جملات دستوری"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(
                        start = screenWidth * 0.03f,
                        top = screenHeight * 0.05f
                    )
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.backbtn),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(screenWidth * 0.09f)
                )
            }
        }

        Text(
            text = "گرامر",
            fontSize = (screenWidth * 0.045f).value.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = iranSans,
            color = Color.Black,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .padding(horizontal = 30.dp)
                .padding(top = 10.dp)
        )

        // سوال و آیکن
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "چه مبحثی رو دوسداری تمرین کنیم؟",
                fontSize = (screenWidth * 0.04f).value.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = iranSans,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                painter = painterResource(id = R.drawable.grammer),
                contentDescription = "Grammer",
                tint = Color.Black,
                modifier = Modifier.size(screenWidth * 0.08f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // لیست مباحث گرامری
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(1.dp)) // مقدار پیشنهادی برای دیدن سایه بالا
            }
            items(grammarTopics) { topic ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White, // 👈 باکس سفید
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(53.dp) // 👈 ارتفاع ثابت
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(12.dp),

                            ambientColor = Color(0xFF00FFFF), // رنگ سایه
                            spotColor = Color(0xFF00A4A4)     // برای Android 12+
                        )
                        .clip(RoundedCornerShape(12.dp)) // برای گوشه گرد بعد از سایه
                        .clickable {
                            showDialog = true
                        }
                ) {
                    Text(
                        text = topic,
                        fontSize = 16.sp,
                        fontFamily = iranSans,
                        color = Color(0xFF4D869C),
                        modifier = Modifier.fillMaxWidth()
                            .padding(end = 18.dp, top = 15.dp),
                        textAlign = TextAlign.Right
                    )
                }


            }
        }
    }
    // ✅ پاپ‌آپ
    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = true, onClick = {}), // ✅ جلوگیری از کلیک روی عناصر پشت
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "شروع کنیم؟",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = iranSans,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End) // ✅ راست‌چین
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "سوالات به صورت تصادفی می‌باشند و هر آزمون شامل ۱۰ سوال است.",
                        fontSize = 12.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.ExtraLight,
                        textAlign = TextAlign.Right,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End) // ✅ راست‌چین
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp) // ✅ فضای بیرونی برای نمایش سایه
                                .evenShadow(radius = 25f, cornerRadius = 20f) // ✅ سایه نرم و متقارن
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .height(45.dp)
                                .clickable { showDialog = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("خروج", color = Color.Red, fontFamily = iranSans)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp) // ✅ فضای بیرونی برای نمایش سایه
                                .evenShadow(radius = 25f, cornerRadius = 20f) // ✅ سایه نرم و متقارن
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .height(45.dp)
                                .background(Color(0xFF7AB2B2))
                                .clickable { showDialog = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("شروع", color = Color.White, fontFamily = iranSans)
                        }
                    }
                }
            }
        }
    }
}

fun Modifier.evenShadow(
    color: Color = Color.Black,
    radius: Float = 20f,
    cornerRadius: Float = 20f
): Modifier = this.then(
    Modifier.drawBehind {
        val shadowColor = color.copy(alpha = 0.3f).toArgb()
        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            this.color = android.graphics.Color.TRANSPARENT
            setShadowLayer(radius, 0f, 0f, shadowColor)
        }
        drawIntoCanvas {
            it.nativeCanvas.drawRoundRect(
                0f,
                0f,
                size.width,
                size.height,
                cornerRadius,
                cornerRadius,
                paint
            )
        }
    }
)
