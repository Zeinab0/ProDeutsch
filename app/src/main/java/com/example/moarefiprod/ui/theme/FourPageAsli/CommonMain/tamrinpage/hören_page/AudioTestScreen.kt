package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.evenShadow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AudioTestScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    var showDialog by remember { mutableStateOf(false) }

    var remainingPlays by remember { mutableStateOf(3) }
    var isPlaying by remember { mutableStateOf(false) }
    var shouldPlay by remember { mutableStateOf(false) } // ✅ منتقل‌شده به بالا

    var startPlaybackRequest by remember { mutableStateOf(false) }

    LaunchedEffect(startPlaybackRequest) {
        if (startPlaybackRequest && !isPlaying && remainingPlays > 0) {
            startPlaybackRequest = false  // بلافاصله ریست بشه

            isPlaying = true
            delay(10_000)
            isPlaying = false

            // ❗ بعد از اتمام پخش، مقدار باقی‌مانده کم بشه
            remainingPlays--
        }
    }




    val questions = listOf(
        Question(0, listOf("Ich trinke gerne Kaffee", "Ich esse eine Banane", "Ich gehe heute ins Kino", "Ich habe ein rotes Auto"), 0),
        Question(1, listOf("Ich spiele Fußball", "Ich fahre Rad", "Ich lese ein Buch", "Ich koche Pasta"), 2),
        Question(2, listOf("Er läuft schnell", "Sie tanzt gern", "Wir schwimmen oft", "Du malst schön"), 1),
        Question(3, listOf("Ich lerne Deutsch", "Ich liebe Schokolade", "Ich mag Katzen", "Ich wohne in Berlin"), 0),
        Question(4, listOf("Ich habe Hunger", "Ich bin müde", "Ich gehe schlafen", "Ich esse Pizza"), 3)
    )

    var currentQuestionIndex by remember { mutableStateOf(0) }
    val currentQuestion = questions[currentQuestionIndex]

    val selectedAnswers = remember { mutableStateListOf<Int>() }

    // مقداردهی اولیه (فقط یک بار)
    LaunchedEffect(Unit) {
        selectedAnswers.clear()
        selectedAnswers.addAll(List(questions.size) { -1 })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = screenWidth * 0.05f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = {
                    showDialog = true // 👈 به جای navController.popBackStack()
                },
                modifier = Modifier
                    .padding(top = screenHeight * 0.05f)
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

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            questions.forEachIndexed { index, _ ->
                val isCurrent = index == currentQuestionIndex
                val isAnswered = selectedAnswers.getOrNull(index)?.let { it != -1 } == true

                val bgColor = when {
                    isCurrent ->  Color(0xFFCDE8E5)
                    isAnswered -> Color(0xFF4D869C)
                    else -> Color(0xFFCDE8E5)
                }

                val borderColor = if (isCurrent) Color(0xFF4D869C) else Color.Transparent

                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .width(65.dp)
                        .padding(horizontal = 2.dp)
                        .background(bgColor, RoundedCornerShape(8.dp))
                        .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
                )
            }
        }


        Spacer(modifier = Modifier.height(45.dp))

        Box(
            modifier = Modifier
                .padding(top = 12.dp, start = 16.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 4.dp)
                .align(Alignment.Start)
        ) {
            Text("تکرار مجاز: $remainingPlays", fontSize = 12.sp, fontFamily = iranSans)
        }

        val scope = rememberCoroutineScope()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, top = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.volume),
                contentDescription = "Voice",
                tint = if (remainingPlays == 0) Color.Gray else Color(0xFF4D869C),
                modifier = Modifier
                    .size(40.dp)
                    .clickable(enabled = !isPlaying && remainingPlays > 0) {
                        scope.launch {
                            isPlaying = true
                            delay(10_000)
                            isPlaying = false
                            remainingPlays-- // ✅ بعد از اتمام پخش کم بشه
                        }
                    }
            )

            AudioProgressVisualizer(
                isPlaying = isPlaying,
                isDisabled = remainingPlays == 0
            )
        }


        Spacer(modifier = Modifier.height(85.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.previousbtn),
                contentDescription = "Previous",
                modifier = Modifier
                    .size(36.dp)
                    .clickable(enabled = currentQuestionIndex > 0) {
                        currentQuestionIndex--
                    },
                tint = if (currentQuestionIndex > 0) Color.Black else Color.Gray
            )

            Column(
                modifier = Modifier
                    .width(280.dp)
                    .padding(horizontal = 1.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                currentQuestion.options.forEachIndexed { index, text ->
                    val isSelected = selectedAnswers.getOrNull(currentQuestionIndex) == index
                    val backgroundColor = if (isSelected) Color(0xFF4D869C) else Color(0xFFB7E5E4)
                    val textColor = if (isSelected) Color.White else Color.Black

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(backgroundColor)
                            .clickable {
                                selectedAnswers[currentQuestionIndex] = index
                            }
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "${index + 1}. $text",
                            fontFamily = iranSans,
                            color = textColor
                        )
                    }
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.nextbtn),
                contentDescription = "Next",
                modifier = Modifier
                    .size(36.dp)
                    .clickable(enabled = currentQuestionIndex < questions.lastIndex) {
                        currentQuestionIndex++
                    },
                tint = if (currentQuestionIndex < questions.lastIndex) Color.Black else Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(194.dp))

        Box(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF7AB2B2))
                .clickable { navController.popBackStack() }
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text("پایان آزمون", color = Color.White, fontFamily = iranSans)
        }
    }
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
                        text = "مطمئنی میخوای خارج شی؟",
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
                        text = "اگر بری باید از اول شروع کنی!",
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
                                .clickable {
                                    showDialog = false
                                    navController.popBackStack() // 👈 اینجا خروج واقعی اتفاق میفته
                                },
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
                            Text("انصراف", color = Color.White, fontFamily = iranSans)
                        }
                    }
                }
            }
        }
    }
}

data class Question(
    val id: Int,
    val options: List<String>,
    val correctIndex: Int
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AudioTestScreenPreview() {
    AudioTestScreen(navController = rememberNavController())
}
