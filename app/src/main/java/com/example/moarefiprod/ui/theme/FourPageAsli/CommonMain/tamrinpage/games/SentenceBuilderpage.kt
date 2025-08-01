package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ResultDialog
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBuilderPage(
    navController: NavController,
    courseId: String,
    lessonId: String,
    contentId: String,
    gameId: String,
    gameIndex: Int,
    viewModel: GameViewModel = viewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val sentenceState by viewModel.sentenceData.collectAsState()
    var selectedWords by rememberSaveable { mutableStateOf(mutableListOf<String>()) }
    var showResultBox by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var timeInSeconds by remember { mutableStateOf(0) }

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"
    val totalTimeInSeconds by viewModel.totalTimeInSeconds.collectAsState()

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            timeInSeconds++
        }
    }

    LaunchedEffect(gameId) {
        viewModel.loadSentenceGame(courseId, lessonId, contentId, gameId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        sentenceState?.let { state ->
            val wordList = state.wordPool
            val question = state.question
            val correctSentence = state.correctSentence.joinToString(" ")

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(screenHeight * 0.1f))

                StepProgressBar(currentStep = gameIndex)

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = " $question ",
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = painterResource(id = R.drawable.chat),
                        contentDescription = "chat",
                        modifier = Modifier
                            .size(56.dp)
                            .padding(end = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(screenHeight * 0.12f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            DottedLine(
                                width = screenWidth * 0.76f,
                                modifier = Modifier.align(Alignment.Center)
                            )

                            Image(
                                painter = painterResource(id = R.drawable.pen),
                                contentDescription = "pen",
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.CenterStart)
                                    .absoluteOffset(x = 4.dp, y = (-14).dp)
                            )

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                                verticalArrangement = Arrangement.spacedBy(20.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 40.dp)
                                    .align(Alignment.CenterStart)
                            ) {
                                selectedWords.forEach { word ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFCDE8E5))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                            .clickable {
                                                selectedWords = selectedWords.toMutableList().apply { remove(word) }
                                            }
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
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        DottedLine(width = screenWidth * 0.8f)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    wordList.forEach { word ->
                        ClickableTextWordBox(
                            word = word,
                            isSelected = selectedWords.contains(word),
                            onClick = {
                                if (!selectedWords.contains(word)) {
                                    selectedWords = selectedWords.toMutableList().apply { add(word) }
                                } else {
                                    selectedWords = selectedWords.toMutableList().apply { remove(word) }
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

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
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 30.dp, bottom = screenHeight * 0.15f)
                        .width(screenWidth * 0.20f)
                        .height(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4D869C),
                        contentColor = Color.White
                    )
                ) {
                    Text("تایید", fontFamily = iranSans, fontWeight = FontWeight.Bold)
                }
            }
        } ?: run {
            Text(
                text = "داده‌ها بارگذاری نشد. لطفاً اتصال را بررسی کنید.",
                color = Color.Red,
                fontFamily = iranSans,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        var showFinalDialog by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        if (showResultBox) {
            val correctSentenceText = sentenceState?.correctSentence?.joinToString(" ") ?: ""
            val userSentenceText = selectedWords.joinToString(" ")

            Result(
                correct = if (isCorrect == true) 1 else 0,
                wrong = if (isCorrect == false) 1 else 0,
                timeInSeconds = timeInSeconds,
                showStats = true,
                showTime = false,
                correctSentence = correctSentenceText,
                userSentence = userSentenceText,
                onNext = {
                    val isCurrentSentenceCorrect = selectedWords.joinToString(" ") == correctSentenceText
                    viewModel.recordAnswer(isCurrentSentenceCorrect)
                    selectedWords = mutableListOf()
                    showResultBox = false
                    isCorrect = null
                    val nextGameId = viewModel.getNextGameId(gameIndex + 1)
                    if (nextGameId != null) {
                        scope.launch {
                            navController.navigate("$nextGameId/$courseId/$lessonId/$contentId?gameIndex=${gameIndex + 1}") {
                                popUpTo("$gameId/$courseId/$lessonId/$contentId?gameIndex=$gameIndex") { inclusive = true }
                            }
                        }
                    } else {
                        showFinalDialog = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 0.dp)
            )
        }

        if (showFinalDialog) {
            ResultDialog(
                navController = navController,
                courseId = courseId,
                lessonId = lessonId,
                contentId = contentId,
                timeInSeconds = totalTimeInSeconds,
                onDismiss = {
                    showFinalDialog = false
                    navController.navigate("darsDetails/$courseId/$lessonId") {
                        popUpTo("$gameId/$courseId/$lessonId/$contentId?gameIndex=$gameIndex") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun ClickableTextWordBox(word: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color.Gray else Color(0xFFCDE8E5))
            .clickable(enabled = !isSelected) { onClick() }
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
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val formattedTime = String.format("%02d:%02d", timeInSeconds / 60, timeInSeconds % 60)
    val allCorrect = wrong == 0

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(screenHeight * 0.14f)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF4DA4A4), Color(0xFFBFEAE8))
                ),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        if (showTime) {
            Text(
                text = formattedTime,
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 12.dp, top = 60.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 8.dp),
        ) {
            if (allCorrect) {
                Text(
                    text = "هوراااااااا\n ^_^ همرو درست جواب دادی",
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                )
            } else {
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
                            fontSize = 14.sp,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))

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
                            textAlign = TextAlign.Left,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .width(98.dp)
                    .height(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF4D869C))
                    .clickable { onNext() }
                    .align(Alignment.End),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "بریم بعدی",
                        fontFamily = iranSans,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.nextbtn),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}