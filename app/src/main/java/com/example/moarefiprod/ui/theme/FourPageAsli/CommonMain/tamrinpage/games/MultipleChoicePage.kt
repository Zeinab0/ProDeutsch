package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ExitConfirmationDialog
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ResultDialog
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBarWithExit
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game.GrammerGameViewModel
import kotlinx.coroutines.delay

@Composable
fun MultipleChoicePage(
    navController: NavController,
    courseId: String,
    lessonId: String = "",
    contentId: String = "",
    gameId: String,
    gameIndex: Int,
    totalGames: Int,
    viewModel: GrammerGameViewModel
) {
    val grammarViewModel = viewModel as? GrammerGameViewModel ?: return

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    var gameTimeInSeconds by remember { mutableStateOf(0) }

    val mcqData by grammarViewModel.selectOption.collectAsState()
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var showResultBox by remember { mutableStateOf(false) }
    var showFinalResultDialog by remember { mutableStateOf(false) }

    val pathType = if (lessonId.isNotEmpty() && contentId.isNotEmpty()) {
        GrammerGameViewModel.GamePathType.COURSE
    } else {
        GrammerGameViewModel.GamePathType.GRAMMAR_TOPIC
    }

    val totalTimeInSeconds by grammarViewModel.totalTimeInSeconds.collectAsState()

    LaunchedEffect(gameIndex) {
        viewModel.initializeTotalQuestions(
            pathType = pathType,
            courseId = courseId,
            lessonId = lessonId,
            contentId = contentId
        )
        gameTimeInSeconds = 0 // ✅ ریست کردن تایمر در هر بازی جدید

        while (viewModel.totalQuestions.value == 0) {
            delay(100)
        }

        if (gameIndex == 0) {
            viewModel.resetScores()
        }

        viewModel.loadMultipleChoiceGame(
            pathType = pathType,
            courseId = courseId,
            lessonId = lessonId,
            contentId = contentId,
            gameId = gameId,
            index = gameIndex
        )
        Log.d("GameTimer", "تایمر بازی جدید شروع شد. زمان فعلی: $gameTimeInSeconds")
        // ✅ شروع شمارش زمان برای این بازی
        while (true) {
            delay(1000L)
            gameTimeInSeconds++
            Log.d("MultipleChoicePage", "زمان بازی: $gameTimeInSeconds ثانیه")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        var showExitDialog by remember { mutableStateOf(false) }

        val returnRoute = if (pathType == GrammerGameViewModel.GamePathType.COURSE) {
            "darsDetails/$courseId/$lessonId"
        } else {
            "grammar_page"
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .zIndex(3f)
        ) {
            StepProgressBarWithExit(
                navController = navController,
                currentStep = gameIndex,
                totalSteps = totalGames,
                returnRoute = returnRoute,
                modifier = Modifier.fillMaxWidth(),
                onRequestExit = { showExitDialog = true }
            )

            if (showExitDialog) {
                ExitConfirmationDialog(
                    onConfirmExit = {
                        navController.navigate(returnRoute) {
                            popUpTo("home") { inclusive = false }
                        }
                        showExitDialog = false
                    },
                    onDismiss = { showExitDialog = false }
                )
            }
        }

        mcqData?.let { data ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = screenHeight * 0.1f,
                        start = 40.dp,
                        end = 40.dp
                    )
            ) {
                Spacer(modifier = Modifier.height(screenHeight * 0.14f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.pen),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(screenHeight * 0.01f))
                    Text(
                        text = data.questionText,
                        fontSize = 16.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(screenHeight * 0.1f))

                data.options.forEachIndexed { index, option ->
                    val backgroundColor = when {
                        !showResultBox && selectedIndex == index -> Color(0xFF4D869C)
                        showResultBox && index == data.correctAnswerIndex -> Color(0xFF14CB00)
                        showResultBox && index == selectedIndex && selectedIndex != data.correctAnswerIndex -> Color(0xFFFF3B3B)
                        else -> Color(0xFFCDE8E5)
                    }
                    val textColor = if (showResultBox && index == data.correctAnswerIndex) Color.White else Color.Black

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(backgroundColor)
                            .clickable(enabled = !showResultBox) { selectedIndex = index }
                            .padding(vertical = 15.dp, horizontal = 14.dp)
                    ) {
                        Text(
                            text = "${index + 1}. $option",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }

            if (showResultBox) {
                val userWord = selectedIndex?.let { data.options.getOrNull(it) } ?: ""
                val correctWord = data.options.getOrNull(data.correctAnswerIndex) ?: ""
                ChoiceResultBox(
                    correct = if (selectedIndex == data.correctAnswerIndex) 1 else 0,
                    wrong = if (selectedIndex != data.correctAnswerIndex) 1 else 0,
                    correctSentence = data.questionText.replace("________", correctWord),
                    userSentence = if (selectedIndex != data.correctAnswerIndex) {
                        data.questionText.replace("________", userWord)
                    } else {
                        ""
                    },
                    translation = data.translation,
                    gameIndex = gameIndex,
                    totalGames = totalGames,
                    onNext = {
                        selectedIndex = null
                        showResultBox = false
                        if (gameIndex + 1 < totalGames) {
                            navController.navigate("GameHost/$courseId/$lessonId/$contentId/${gameIndex + 1}")
                        } else {
                            showFinalResultDialog = true
                        }
                    },
                    onFinish = {
                        showFinalResultDialog = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 0.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = screenWidth * 0.06f,
                    bottom = screenHeight * 0.19f
                )
                .width(screenWidth * 0.20f)
                .height(40.dp)
                .zIndex(2f)
        ) {
            Button(
                onClick = {
                    if (selectedIndex != null) {
                        val isCorrect = mcqData?.correctAnswerIndex == selectedIndex
                        grammarViewModel.recordAnswer(isCorrect)
                        showResultBox = true

                        // ✅ فراخوانی صحیح تابع ثبت زمان
                        grammarViewModel.recordMemoryGameResult(
                            correct = if (isCorrect) 1 else 0,
                            wrong = if (!isCorrect) 1 else 0,
                            timeInSeconds = gameTimeInSeconds
                        )
                        Log.d("MultipleChoicePage", "⏱ زمان این بازی: $gameTimeInSeconds ثانیه | 🧮 مجموع کل تا الان: ${grammarViewModel.totalTimeInSeconds.value} ثانیه")
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4D869C),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "تأیید",
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (showFinalResultDialog) {
            val returnRouteForDialog = if (lessonId.isNotEmpty() && contentId.isNotEmpty()) {
                "lesson_detail/$courseId/$lessonId"
            } else {
                "grammar_page"
            }
            ResultDialog(
                navController = navController,
                courseId = courseId,
                lessonId = lessonId,
                contentId = contentId,
                timeInSeconds = totalTimeInSeconds,
                returnRoute = returnRouteForDialog,
                onDismiss = {
                    showFinalResultDialog = false
                    navController.navigate(returnRouteForDialog)
                }
            )
        }
    }
}

@Composable
fun ChoiceResultBox(
    correct: Int = 0,
    wrong: Int = 0,
    correctSentence: String? = null,
    userSentence: String? = null,
    translation: String? = null,
    gameIndex: Int,
    totalGames: Int,
    onNext: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(screenHeight * 0.19f)
            .padding(horizontal = 20.dp, vertical = 30.dp)
            .background(color = Color(0xFF90CECE), RoundedCornerShape(25.dp))
            .padding(horizontal = 15.dp, vertical = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 8.dp)
        ) {
            if (correct == 1 || wrong == 1) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = if (correct == 1) R.drawable.tik else R.drawable.cross),
                            contentDescription = null,
                            tint = if (correct == 1) Color(0xFF14CB00) else Color(0xFFFF3B3B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (correct == 1) correctSentence ?: "" else userSentence ?: "",
                            fontFamily = iranSans,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Left
                        )
                    }

                    if (!translation.isNullOrEmpty()) {
                        Text(
                            text = translation,
                            fontFamily = iranSans,
                            fontSize = 13.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Right,
                            style = TextStyle(textDirection = TextDirection.Rtl),
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }

                if (wrong == 1) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.tik),
                            contentDescription = null,
                            tint = Color(0xFF14CB00),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = correctSentence ?: "",
                            fontFamily = iranSans,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Left
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        val isLastGame = gameIndex + 1 == totalGames

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = (-14).dp)
                .width(90.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF4D869C))
                .clickable {
                    if (isLastGame) {
                        onFinish()
                    } else {
                        onNext()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isLastGame) "تمام" else "بریم بعدی",
                    fontFamily = iranSans,
                    color = Color.White,
                    fontSize = 12.sp,
                )

                if (!isLastGame) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.nextbtn),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}