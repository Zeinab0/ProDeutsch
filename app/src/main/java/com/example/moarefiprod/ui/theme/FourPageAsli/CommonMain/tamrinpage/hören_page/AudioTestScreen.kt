package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören.evenShadow
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AudioTestScreen(navController: NavController, level: String, exerciseId: String) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val viewModel: AudioTestViewModel = viewModel()
    val questions by viewModel.questions.collectAsState()
    var remainingPlays by remember { mutableStateOf(3) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val selectedAnswers = remember { mutableStateListOf<Int>() }
    val scope = rememberCoroutineScope()

    var showExitDialog by remember { mutableStateOf(false) }
    var showFinishDialog by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadQuestions(level, exerciseId)
    }

    LaunchedEffect(questions) {
        selectedAnswers.clear()
        selectedAnswers.addAll(List(questions.size) { -1 })
    }

    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("در حال بارگذاری سوالات...", fontFamily = iranSans)
        }
        return
    }
    val currentQuestion = questions[currentQuestionIndex]

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
                    showExitDialog = true
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            questions.forEachIndexed { index, _ ->
                val isCurrent = index == currentQuestionIndex
                val isAnswered = selectedAnswers.getOrNull(index)?.let { it != -1 } == true

                val bgColor = when {
                    isCurrent -> Color(0xFFCDE8E5)
                    isAnswered -> Color(0xFF4D869C)
                    else -> Color(0xFFCDE8E5)
                }

                val borderColor = if (isCurrent) Color(0xFF4D869C) else Color.Transparent

                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .weight(1f) // ← مهم‌ترین بخش!
                        .padding(horizontal = 2.dp)
                        .background(bgColor, RoundedCornerShape(8.dp))
                        .border(1.dp, borderColor, RoundedCornerShape(8.dp))
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, top = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current
            val mediaPlayer = remember { MediaPlayer() }
            val audioUrl by viewModel.audioUrl.collectAsState()

            var audioDuration by remember { mutableStateOf(10000) } // پیش‌فرض 10 ثانیه
            var progress by remember { mutableStateOf(0f) }

            Icon(
                painter = painterResource(id = R.drawable.volume),
                contentDescription = "Voice",
                tint = if (remainingPlays == 0) Color.Gray else Color(0xFF4D869C),
                modifier = Modifier
                    .size(40.dp)
                    .clickable(enabled = !isPlaying && remainingPlays > 0 && !audioUrl.isNullOrEmpty()) {
                        scope.launch {
                            try {
                                isPlaying = true
                                mediaPlayer.reset()
                                mediaPlayer.setDataSource(audioUrl)
                                mediaPlayer.setOnPreparedListener {
                                    audioDuration = it.duration
                                    it.start()
                                    isPlaying = true  // ⬅️ بعد از start()
                                    println("✅ صدا پخش شد")
                                }
                                mediaPlayer.setOnCompletionListener {
                                    println("✅ پخش تمام شد")
                                    isPlaying = false
                                    remainingPlays--
                                }
                                mediaPlayer.setOnErrorListener { _, what, extra ->
                                    println("❌ خطا در پخش صدا: what=$what, extra=$extra")
                                    isPlaying = false
                                    true
                                }
                                mediaPlayer.prepareAsync()
                            } catch (e: Exception) {
                                println("❌ خطای کلی در پخش: ${e.message}")
                                isPlaying = false
                            }
                        }
                    }
            )
            LaunchedEffect(isPlaying) {
                while (isPlaying) {
                    if (mediaPlayer.isPlaying && mediaPlayer.duration > 0) {
                        progress = mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration
                    }
                    delay(100L)
                }
                progress = 0f
            }

            // این ویژوالایزر برای نشان دادن حالت در حال پخش
            AudioProgressVisualizerr(
                isPlaying = isPlaying,
                isDisabled = remainingPlays == 0,
                progress = progress
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
                        Text("${index + 1}. $text", fontFamily = iranSans, color = textColor)
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
                .clickable {
                    // ✅ محاسبه نمره
                    val correctCount = questions.zip(selectedAnswers).count { (q, ans) ->
                        ans != -1 && q.correctIndex == ans
                    }
                    val score = ((correctCount.toDouble() / questions.size) * 100).toInt()

                    // ✅ ذخیره در Firestore
                    FirebaseFirestore.getInstance()
                        .collection("hör_levels")
                        .document(level)
                        .collection("exercises")
                        .document(exerciseId)
                        .update("score", score)
                        .addOnSuccessListener {
//                            navController.popBackStack()
                            showFinishDialog = true

                        }
                }
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text("پایان آزمون", color = Color.White, fontFamily = iranSans)
        }
    }
    if (showExitDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = true, onClick = {}),
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
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "آیا مطمئنی می\u200Cخوای آزمون رو ترک کنی؟",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = iranSans,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )


                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "با خروج از آزمون، پیشرفت شما ذخیره نخواهد شد.",
                        fontSize = 12.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.ExtraLight,
                        textAlign = TextAlign.Right,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .evenShadow(radius = 25f, cornerRadius = 20f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .height(45.dp)
                                .clickable { showExitDialog = false
                                    navController.popBackStack()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("خروج", color = Color.Red, fontFamily = iranSans)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .evenShadow(radius = 25f, cornerRadius = 20f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF7AB2B2))
                                .height(45.dp)
                                .clickable {
                                    showExitDialog = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("انصراف", color = Color.White, fontFamily = iranSans)
                        }
                    }
                }
            }
        }
    }

    if (showFinishDialog) {
        val unansweredCount = selectedAnswers.count { it == -1 }
        val allAnswered = unansweredCount == 0
        val message = if (allAnswered) {
            "به همه سوالات پاسخ دادی. تموم کنیم آزمونو؟"
        } else {
            "به $unansweredCount سوال پاسخ ندادی. مطمئنی می‌خوای خارج شی؟"
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = true, onClick = {}),
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
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "پایان آزمون",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = iranSans,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = message,
                        fontSize = 12.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.ExtraLight,
                        textAlign = TextAlign.Right,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .evenShadow(radius = 25f, cornerRadius = 20f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .height(45.dp)
                                .clickable {
                                    showFinishDialog = false
                                    showResultDialog = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("اتمام ازمون", color = Color.Red, fontFamily = iranSans)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .evenShadow(radius = 25f, cornerRadius = 20f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF7AB2B2))
                                .height(45.dp)
                                .clickable {
                                    showFinishDialog = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("ادامه", color = Color.White, fontFamily = iranSans)
                        }
                    }
                }
            }
        }
    }

    if (showResultDialog) {
        val total = questions.size
        val correct = questions.zip(selectedAnswers).count { (q, a) -> a == q.correctIndex }
        val unanswered = selectedAnswers.count { it == -1 }
        val wrong = total - correct - unanswered
        val score = ((correct.toDouble() / total) * 100).toInt()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = true, onClick = {}),
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
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "پایان آزمون",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = iranSans,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("تعداد کل سوالات: $total", fontSize = 12.sp, fontFamily = iranSans, color = Color.Black)
                    Text("تعداد پاسخ صحیح: $correct", fontSize = 12.sp, fontFamily = iranSans, color = Color.Black)
                    Text("تعداد پاسخ غلط: $wrong", fontSize = 12.sp, fontFamily = iranSans, color = Color.Black)
                    Text("پاسخ داده نشده: $unanswered", fontSize = 12.sp, fontFamily = iranSans, color = Color.Black)
                    Text("نمره شما: $score از 100", fontSize = 12.sp, fontFamily = iranSans, fontWeight = FontWeight.SemiBold, color = Color(0xFF4D869C))

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .evenShadow(radius = 25f, cornerRadius = 20f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF7AB2B2))
                                .height(45.dp)
                                .clickable {
                                    showResultDialog = false
                                    FirebaseFirestore.getInstance()
                                        .collection("hör_levels")
                                        .document(level)
                                        .collection("exercises")
                                        .document(exerciseId)
                                        .update("score", score)
                                        .addOnSuccessListener {
                                            navController.popBackStack()
                                        }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("تایید و خروج", color = Color.White, fontFamily = iranSans)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AudioTestScreenPreview() {
    AudioTestScreen(navController = rememberNavController(), level = "A1", exerciseId = "exercise_1")
}