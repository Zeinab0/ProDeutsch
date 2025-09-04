package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ExitConfirmationDialog
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBarWithExit
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game.GrammerGameViewModel
import kotlinx.coroutines.delay
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ResultDialog // ⭐️ اضافه شده

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBuilderPage(
    navController: NavController,
    courseId: String,
    lessonId: String,
    contentId: String,
    gameId: String,
    gameIndex: Int,
    totalGames: Int,
    viewModel: BaseGameViewModel
) {
    val grammarViewModel = viewModel as? GrammerGameViewModel ?: return
    val sentenceState by grammarViewModel.sentenceData.collectAsState()
    val totalTimeInSeconds by grammarViewModel.totalTimeInSeconds.collectAsState() // ⭐️ اضافه شده

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val correctSentence = sentenceState?.correctSentence?.joinToString(" ") ?: ""
    var selectedWords by rememberSaveable { mutableStateOf(mutableListOf<String>()) }
    var showResultBox by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var timeInSeconds by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    var showFinalDialog by remember { mutableStateOf(false) } // ⭐️ اضافه شده
    var gameTimeInSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        Log.d("GameTimer", "تایمر بازی جدید شروع شد. زمان فعلی: $gameTimeInSeconds")
        while (true) {
            delay(1000L)
            timeInSeconds++
        }
    }
    val pathType = if (lessonId.isNotEmpty() && contentId.isNotEmpty()) {
        GrammerGameViewModel.GamePathType.COURSE
    } else {
        GrammerGameViewModel.GamePathType.GRAMMAR_TOPIC
    }
    LaunchedEffect(gameId) {
        grammarViewModel.loadSentenceGame(
            pathType = pathType,
            courseId = courseId,
            lessonId = lessonId,
            contentId = contentId,
            gameId = gameId
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        var showExitDialog by remember { mutableStateOf(false) }

        val returnRoute = if (pathType == GrammerGameViewModel.GamePathType.COURSE) {
            "darsDetails/$courseId/$lessonId"
        } else {
            "grammar_page"
        }

        // ProgressBar and Exit button at the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(3f)
        ) {
            StepProgressBarWithExit(
                navController = navController,
                currentStep = gameIndex,
                totalSteps = totalGames,
                returnRoute = returnRoute,
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

        when {
            sentenceState == null -> {
                Text(
                    text = "...در حال بارگذاری",
                    color = Color.Gray,
                    fontFamily = iranSans,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            sentenceState?.wordPool.isNullOrEmpty() -> {
                Text(
                    text = "داده‌ای یافت نشد",
                    color = Color.Red,
                    fontFamily = iranSans,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                val state = sentenceState!!
                val correctSentence = state.correctSentence.joinToString(" ")
                val question = state.question

                // Fixed sentence and chat icon
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = screenHeight * 0.18f)
                        .zIndex(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = question,
                            fontFamily = iranSans,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 16.sp,
                            style = TextStyle(textDirection = TextDirection.Rtl),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Image(
                            painter = painterResource(id = R.drawable.chat),
                            contentDescription = "chat",
                            modifier = Modifier
                                .size(36.dp)
                                .padding(end = 12.dp)
                        )
                    }
                }

                // Scrollable main content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp)
                        .padding(top = screenHeight * 0.3f)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp)
                                    .verticalScroll(rememberScrollState())
                                    .clipToBounds()
                            ) {
                                FlowRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                        .padding(start = 30.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    selectedWords.forEach { word ->
                                        ClickableTextWordBox(
                                            word = word,
                                            isSelected = true,
                                            enabled = !showResultBox,
                                            onClick = {
                                                if (!showResultBox) {
                                                    selectedWords = selectedWords.toMutableList().apply {
                                                        remove(word)
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                            }

                            // Fixed dashed lines and pencil icon
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .zIndex(1f)
                                    .padding(horizontal = 5.dp)
                            ) {
                                Row(
                                    modifier = Modifier.offset(y = 15.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.pen),
                                        contentDescription = "Pencil Icon",
                                        modifier = Modifier.size(26.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    DottedLine(
                                        width = screenWidth * 0.9f - 18.dp,
                                        modifier = Modifier.weight(1.2f)
                                    )
                                }

                                DottedLine(
                                    width = screenWidth * 0.96f,
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .offset(y = 76.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            sentenceState?.wordPool?.forEach { word ->
                                if (!selectedWords.contains(word)) {
                                    ClickableTextWordBox(
                                        word = word,
                                        isSelected = false,
                                        enabled = !showResultBox,
                                        onClick = {
                                            if (!showResultBox) {
                                                selectedWords = selectedWords.toMutableList().apply {
                                                    add(word)
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        // Fixed Confirm Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = screenHeight * 0.19f, end = screenWidth * 0.06f)
                .width(screenWidth * 0.20f)
                .height(40.dp)
                .zIndex(2f)
        ) {
            Button(
                onClick = {
                    val isCurrentSentenceCorrect = selectedWords.joinToString(" ") == correctSentence
                    isCorrect = isCurrentSentenceCorrect
                    showResultBox = true
                    if (isCurrentSentenceCorrect) {
                        viewModel.incrementCorrect(1)
                    } else {
                        viewModel.incrementWrong(1)
                    }
                    viewModel.recordMemoryGameResult(
                        correct = if (isCurrentSentenceCorrect) 1 else 0,
                        wrong = if (!isCurrentSentenceCorrect) 1 else 0,
                        timeInSeconds = timeInSeconds
                    )
                },
                enabled = !showResultBox,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4D869C),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF4D869C),
                    disabledContentColor = Color.White
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                Text("تایید", fontFamily = iranSans, fontWeight = FontWeight.Bold)
            }
        }

        // Result Box
        if (showResultBox) {
            val userSentence = selectedWords.joinToString(" ")
            val isLastGame = gameIndex + 1 >= totalGames // ⭐️ اینجا هم تغییر داده شد

            Result(
                correct = if (isCorrect == true) 1 else 0,
                wrong = if (isCorrect == false) 1 else 0,
                timeInSeconds = timeInSeconds,
                showStats = true,
                showTime = true,
                correctSentence = correctSentence,
                userSentence = userSentence,
                gameIndex = gameIndex,
                totalGames = totalGames,
                onNext = {
                    selectedWords = mutableListOf()
                    showResultBox = false
                    isCorrect = null
                    // ⭐️ تغییر: اگر آخرین بازی نیست، به بازی بعدی برو
                    if (!isLastGame) {
                        navController.navigate("GameHost/$courseId/$lessonId/$contentId/${gameIndex + 1}") {
                            popUpTo("GameHost/$courseId/$lessonId/$contentId/$gameIndex") { inclusive = true }
                        }
                    } else {
                        // ⭐️ تغییر: در غیر این صورت، دیالوگ نهایی را نمایش بده
                        showFinalDialog = true
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        // ⭐️ اضافه شده: ResultDialog
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
    }
}

// نسخه جدید کلیک‌پذیر با کنترل فعال/غیرفعال
@Composable
fun ClickableTextWordBox(
    word: String,
    isSelected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) Color.Gray
                else if (enabled) Color(0xFFCDE8E5)
                else Color(0xFFE6E6E6)
            )
            .clickable(enabled = enabled) { onClick() }
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

@Composable
fun DottedLine(
    width: Dp,
    modifier: Modifier = Modifier,
    color: Color = Color.Gray.copy(alpha = 0.4f)
) {
    Box(
        modifier = modifier
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

@Composable
fun Result(
    correct: Int = 0,
    wrong: Int = 0,
    timeInSeconds: Int = 0,
    showStats: Boolean = true,
    showTime: Boolean = true,
    correctSentence: String? = null,
    userSentence: String? = null,
    onNext: () -> Unit,
    gameIndex: Int,
    totalGames: Int,
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val allCorrect = wrong == 0

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
            if (allCorrect) {
                Text(
                    text = "آفرین \uD83C\uDF89درست انتخاب کردی",
                    fontFamily = iranSans,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    textAlign = TextAlign.Right,
                    style = TextStyle(textDirection = TextDirection.Rtl),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                )
            } else {

                if (userSentence != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.cross),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = userSentence,
                            fontFamily = iranSans,
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                if (correctSentence != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.tik),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = correctSentence,
                            fontFamily = iranSans,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(25.dp))

            val isLastGame = gameIndex + 1 == totalGames

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
                    .offset(y = (-14).dp)
                    .width(90.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF4D869C))
                    .clickable { onNext() },
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