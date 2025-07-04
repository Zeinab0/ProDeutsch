package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.SentenceBuilder

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.tooling.preview.Preview
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.StepProgressBar
import androidx.compose.ui.geometry.Offset
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBuilderPage(navController: NavController) {

    val wordList = listOf("Ich", "mit", "ins Kino", "gehe", "Freunden", "meinen")

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔙 دکمه برگشت
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .size(36.dp)
                .align(Alignment.TopStart)
        ) {
            Image(
                painter = painterResource(id = R.drawable.backbtn),
                contentDescription = "Back"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {

            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp

            Spacer(modifier = Modifier.height(40.dp))

            // ⬜ نوار مرحله‌ای
            StepProgressBar(currentStep = 1)

            Spacer(modifier = Modifier.height(50.dp))

            // جمله فارسی + آیکون
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ".من با دوستانم به سینما می‌روم",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.course),
                    contentDescription = "chat",
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(120.dp))

            // ✏️ آیکون مداد + خطوط نقطه‌چین با موقعیت دقیق
            Box(modifier = Modifier.fillMaxWidth()) {

                // آیکون مداد بالا و چپ با موقعیت مستقل
                Image(
                    painter = painterResource(id = R.drawable.mail),
                    contentDescription = "pen",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 8.dp, top = 0.dp)  // فقط برای مداد
                        .size(25.dp)
                )

                // خطوط نقطه‌چین با padding جداگانه
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 40.dp, top = 25.dp) // مستقل از مداد
                ) {
                    DottedLine(width = 300.dp)
                    Spacer(modifier = Modifier.height(40.dp))
                    DottedLine(width = 300.dp)
                }
            }



            Spacer(modifier = Modifier.height(40.dp))

            // 🔤 کلمات آلمانی
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                wordList.forEach { word ->
                    Box(
                        modifier = Modifier

                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFCDE8E5))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = word,
                            fontFamily = iranSans,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 🔘 دکمه تایید
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 30.dp, bottom = 140.dp)
                    .width(93.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4D869C),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "تایید",
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DottedLine(width: Dp, color: Color = Color.Gray.copy(alpha = 0.4f)) {
    Box(
        modifier = Modifier
            .width(width)
            .height(1.dp)
            .drawBehind {
                val dotSpacing = 5.dp.toPx()
                val dotRadius = 1.5.dp.toPx()
                var x = 0f
                while (x < size.width) {
                    drawCircle(
                        color = color,
                        radius = dotRadius,
                        center = Offset(x, size.height / 2)
                    )
                    x += dotSpacing
                }
            }
    )
}


@Preview(showBackground = true)
@Composable
fun SentenceBuilderPreview() {
    // استفاده از NavHostController خالی برای پیش‌نمایش
    SentenceBuilderPage(navController = rememberNavController())
}
