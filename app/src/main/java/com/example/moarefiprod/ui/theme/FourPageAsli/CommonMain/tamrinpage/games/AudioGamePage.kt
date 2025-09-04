package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ExitConfirmationDialog
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBarWithExit
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game.GrammerGameViewModel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page.AudioProgressVisualizerr
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ResultDialog // ⭐️ اضافه شده

@Composable
fun AudioRecognitionPage(
    navController: NavController,
    courseId: String,
    lessonId: String,
    contentId: String,
    gameId: String,
    gameIndex: Int,
    totalGames: Int,
    viewModel: GrammerGameViewModel,
    onGameFinished: (Boolean, String?) -> Unit
) {
    val pathType = if (lessonId.isNotEmpty() && contentId.isNotEmpty()) {
        GrammerGameViewModel.GamePathType.COURSE
    } else {
        GrammerGameViewModel.GamePathType.GRAMMAR_TOPIC
    }

    val gameState = viewModel.audioRecognitionGameState.collectAsState().value
    var gameTimeInSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(gameId) {
        gameTimeInSeconds = 0 // زمان بازی رو برای بازی جدید ریست کن
        Log.d("GameTimer", "تایمر بازی جدید شروع شد. زمان فعلی: $gameTimeInSeconds")
        while (true) {
            delay(1000L)
            gameTimeInSeconds++
        }
    }

    LaunchedEffect(gameId) {
        viewModel.loadAudioRecognitionGame(pathType, courseId, lessonId, contentId, gameId)
    }


    if (gameState == null) {
        return
    }

    val audioData = viewModel.audioRecognitionGameState.collectAsState().value
    var selectedAnswerIndex by remember { mutableStateOf(-1) }
    var showResultBox by remember { mutableStateOf(false) }
    val audioUrl by viewModel.audioUrl.collectAsState()
    var showFinalDialog by remember { mutableStateOf(false) } // ⭐️ اضافه شده

    val totalTimeInSeconds by viewModel.totalTimeInSeconds.collectAsState() // ⭐️ اضافه شده

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp


    if (audioData == null) {
        return
    }

    val options = audioData.options
    val correctIndex = audioData.correctAnswerIndex


    Box(modifier = Modifier.fillMaxSize()) {
        var showExitDialog by remember { mutableStateOf(false) }

        val returnRoute = if (pathType == GrammerGameViewModel.GamePathType.COURSE) {
            "darsDetails/$courseId/$lessonId"
        } else {
            "grammar_page"
        }

        StepProgressBarWithExit(
            navController = navController,
            currentStep = gameIndex,
            totalSteps = totalGames,
            returnRoute = returnRoute,
            onRequestExit = { showExitDialog = true },
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.15f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(45.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, top = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                val mediaPlayer = remember { MediaPlayer() }

                val audioUrl by viewModel.audioUrl.collectAsState()
                var audioDuration by remember { mutableStateOf(10000) }
                var progress by remember { mutableStateOf(0f) }
                var isPlaying by remember { mutableStateOf(false) }
                var remainingPlays by remember { mutableStateOf(2) }

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
                                        isPlaying = true
                                    }
                                    mediaPlayer.setOnCompletionListener {
                                        isPlaying = false
                                        remainingPlays--
                                    }
                                    mediaPlayer.setOnErrorListener { _, what, extra ->
                                        isPlaying = false
                                        true
                                    }
                                    mediaPlayer.prepareAsync()
                                } catch (e: Exception) {
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
                AudioProgressVisualizerr(
                    isPlaying = isPlaying,
                    isDisabled = remainingPlays == 0,
                    progress = progress
                )
            }
            Spacer(modifier = Modifier.height(85.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                options.forEachIndexed { index, text ->
                    val isSelected = selectedAnswerIndex == index

                    val backgroundColor = when {
                        showResultBox && isSelected && selectedAnswerIndex != correctIndex -> Color.Red
                        showResultBox && index == correctIndex -> Color(0xFF14CB00)
                        isSelected && !showResultBox -> Color(0xFF7AB2B2)
                        else -> Color(0xFFE0F2F1)
                    }

                    val textColor = if (showResultBox && index == correctIndex) Color.White else Color.Black

                    Row(
                        modifier = Modifier
                            .width(280.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(backgroundColor)
                            .clickable(enabled = !showResultBox) {
                                selectedAnswerIndex = index
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}. $text",
                            fontFamily = iranSans,
                            color = textColor
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        if (showResultBox) {
            AudioResultBox(
                isCorrect = selectedAnswerIndex == correctIndex,
                correctSentence = options.getOrNull(correctIndex) ?: "",
                userSentence = options.getOrNull(selectedAnswerIndex) ?: "",
                translation = audioData?.translation ?: "",
                navController = navController,
                courseId = courseId,
                lessonId = lessonId,
                contentId = contentId,
                gameIndex = gameIndex,
                isLastGame = gameIndex == totalGames - 1,
                returnRoute = returnRoute,
                onNext = {
                    val currentRoute = "GameHost/$courseId/$lessonId/$contentId/$gameIndex"
                    val nextRoute = "GameHost/$courseId/$lessonId/$contentId/${gameIndex + 1}"
                    navController.navigate(nextRoute) {
                        popUpTo(currentRoute) { inclusive = true }
                    }
                },
                onFinish = { // ⭐️ اضافه شده
                    showFinalDialog = true
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 0.dp)
            )
        }

        Button(
            onClick = {
                showResultBox = true

                Log.d("GameResult", "⏱ زمان این بازی: $gameTimeInSeconds ثانیه")
                Log.d("GameResult", "🧮 زمان کل (قبل از ثبت): ${viewModel.totalTimeInSeconds.value} ثانیه")

                viewModel.recordAnswer(selectedAnswerIndex == correctIndex)
                viewModel.recordMemoryGameResult(
                    correct = if (selectedAnswerIndex == correctIndex) 1 else 0,
                    wrong = if (selectedAnswerIndex != correctIndex) 1 else 0,
                    //timeInSeconds = totalTimeInSeconds
                    timeInSeconds = gameTimeInSeconds
                )
                Log.d("GameResult", "✅ زمان کل (بعد از ثبت): ${viewModel.totalTimeInSeconds.value} ثانیه")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 30.dp, bottom = 180.dp)
                .width(screenWidth * 0.20f)
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
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        if (showFinalDialog) {
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
                    showFinalDialog = false
                    navController.navigate(returnRouteForDialog)
                }
            )
        }

        if (showExitDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                ExitConfirmationDialog(
                    onConfirmExit = {
                        navController.navigate(returnRoute) {
                            popUpTo("home") { inclusive = false }
                        }
                        showExitDialog = false
                    },
                    onDismiss = {
                        showExitDialog = false
                    }
                )
            }
        }
    }
}


@Composable
fun AudioResultBox(
    isCorrect: Boolean?,
    correctSentence: String,
    userSentence: String,
    translation: String,
    navController: NavController,
    courseId: String,
    lessonId: String,
    contentId: String,
    gameIndex: Int,
    isLastGame: Boolean,
    returnRoute: String,
    onNext: () -> Unit,
    onFinish: () -> Unit, // ⭐️ اضافه شده
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    val iconRes = if (isCorrect == false) R.drawable.cross else R.drawable.tik
                    val iconTint = if (isCorrect == false) Color(0xFFFF3B3B) else Color(0xFF14CB00)

                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isCorrect == false) userSentence else correctSentence,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Left
                    )
                }

                Text(
                    text = translation,
                    fontFamily = iranSans,
                    fontSize = 13.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Right,
                    style = TextStyle(textDirection = TextDirection.Rtl),
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
            }

            if (isCorrect == false) {
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
                        text = correctSentence,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Left
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

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
                        onFinish() // ⭐️ فراخوانی تابع جدید
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
                    fontSize = 12.sp
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