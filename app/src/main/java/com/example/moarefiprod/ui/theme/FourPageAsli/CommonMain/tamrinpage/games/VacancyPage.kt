package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBarWithExit
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ExitConfirmationDialog
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game.GrammerGameViewModel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ResultDialog // ⭐️ اضافه شده
import kotlinx.coroutines.delay

@Composable
fun VacancyPage(
    navController: NavController,
    courseId: String,
    lessonId: String,
    contentId: String,
    gameId: String,
    gameIndex: Int,
    totalGames: Int,
    viewModel: BaseGameViewModel,
    onGameFinished: (Boolean, String?) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var gameTimeInSeconds by remember { mutableStateOf(0) }
    var showResultBox by remember { mutableStateOf(false) }
    var isTimerRunning by remember { mutableStateOf(true) }

    val grammarViewModel = viewModel as? GrammerGameViewModel ?: return
    val pathType = if (lessonId.isNotEmpty() && contentId.isNotEmpty()) {
        GrammerGameViewModel.GamePathType.COURSE
    } else {
        GrammerGameViewModel.GamePathType.GRAMMAR_TOPIC
    }

    LaunchedEffect(gameId) {
        grammarViewModel.loadVacancyGame(pathType, courseId, lessonId, contentId, gameId)
    }
    // تایمر برای اندازه گیری زمان این بازی خاص
    LaunchedEffect(gameId, isTimerRunning) {
        gameTimeInSeconds = 0
        while (isTimerRunning) {
            delay(1000L)
            gameTimeInSeconds++
            Log.d("VacancyPage", "⏱ زمان همین بازی: $gameTimeInSeconds ثانیه")
        }
    }

    val vacancyData = grammarViewModel.vacancyGameState.collectAsState().value
    val sentence = vacancyData?.sentence ?: ""
    val correctAnswer = vacancyData?.correctAnswer ?: ""
    val translation = vacancyData?.translation ?: ""
    val totalTimeInSeconds by grammarViewModel.totalTimeInSeconds.collectAsState() // ⭐️ اضافه شده

    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var showCompletedSentence by remember { mutableStateOf(false) }
    var showFinalDialog by remember { mutableStateOf(false) } // ⭐️ اضافه شده
    val sentenceParts = sentence.split("____")

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

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.24f, start = 32.dp, end = 32.dp)
                .fillMaxWidth()
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height =  screenHeight * 1f)
            ){
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pen),
                        contentDescription = "pen",
                        modifier = Modifier
                            .size(26.dp)
                            .align(Alignment.Top)
                            .offset(y = (20).dp)
                    )
                }

                Text(
                    text = if (showCompletedSentence) {
                        buildAnnotatedString {
                            append(sentenceParts.getOrNull(0) ?: "")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = if (isCorrect == true) Color.Green else Color.Red)) {
                                append(userInput.text)
                            }
                            append(sentenceParts.getOrNull(1) ?: "")
                        }
                    } else {
                        buildAnnotatedString {
                            append(sentenceParts.getOrNull(0) ?: "")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Gray)) {
                                append("____")
                            }
                            append(sentenceParts.getOrNull(1) ?: "")
                        }
                    },
                    fontSize = 16.sp,
                    fontFamily = iranSans,
                    softWrap = true,
                    maxLines = Int.MAX_VALUE,
                    minLines = 3,
                    modifier = Modifier
                        .padding(start = 36.dp)
                )
                Spacer(modifier = Modifier.height(screenHeight * 0.02f))

                Text(
                    text = translation,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontSize = 14.sp,
                    minLines = 3,
                    maxLines = 3,
                    textAlign = TextAlign.Right,
                    style = TextStyle(textDirection = TextDirection.Rtl),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = screenHeight * 0.66f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!showCompletedSentence) {
                BasicTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .fillMaxWidth()
                        .background(Color(0xFFCDE8E5), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (userInput.text.isEmpty()) {
                                Text(
                                    text = "پاسخ را اینجا وارد کنید",
                                    fontSize = 14.sp,
                                    fontFamily = iranSans,
                                    color = Color.White
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }

        if (showResultBox) {
            WinnerBoxVacancy(
                isCorrect = isCorrect,
                correctSentence = correctAnswer,
                userSentence = userInput.text,
                translation = vacancyData?.translation ?: "",
                navController = navController,
                isLastGame = gameIndex == totalGames - 1,
                returnRoute = if (pathType == GrammerGameViewModel.GamePathType.COURSE)
                    "darsDetails/$courseId/$lessonId"
                else
                    "grammar_page",
                onNext = {
                    onGameFinished(isCorrect == true, correctAnswer)
                    navController.navigate("GameHost/$courseId/$lessonId/$contentId/${gameIndex + 1}") {
                        popUpTo("GameHost/$courseId/$lessonId/$contentId/$gameIndex") { inclusive = true }
                    }
                },
                onFinish = { // ⭐️ اضافه شده
                    showFinalDialog = true
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )

        }
        Button(
            onClick = {
                isCorrect = userInput.text.trim().equals(correctAnswer, ignoreCase = true)
                showResultBox = true
                showCompletedSentence = true
                isTimerRunning = false // تایمر را متوقف کن

                Log.d("GameResult", "⏱ زمان همین بازی: $gameTimeInSeconds ثانیه")

                // ثبت نتیجه و زمان این بازی خاص
                viewModel.recordAnswer(isCorrect == true)
                viewModel.recordMemoryGameResult(
                    correct = if (isCorrect == true) 1 else 0,
                    wrong = if (isCorrect == false) 1 else 0,
                    timeInSeconds = gameTimeInSeconds // 👈 این زمان همین بازی است
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
            ),
        ) {
            Text(
                text = "تایید",
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        // ⭐️ اضافه شده
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

@Composable
fun WinnerBoxVacancy(
    isCorrect: Boolean?,
    correctSentence: String,
    userSentence: String,
    translation: String = "من آلمانی یاد می‌گیرم چون زبان زیبایی است.",
    navController: NavController,
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
            if (isCorrect == false) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cross),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = userSentence,
                        fontFamily = iranSans,
                        color = Color.Black,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.tik),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = correctSentence,
                        fontFamily = iranSans,
                        color = Color.Black,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }

            } else if (isCorrect == true) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.tik),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = correctSentence,
                        fontFamily = iranSans,
                        color = Color.Black,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(screenHeight * 0.018f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
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
}