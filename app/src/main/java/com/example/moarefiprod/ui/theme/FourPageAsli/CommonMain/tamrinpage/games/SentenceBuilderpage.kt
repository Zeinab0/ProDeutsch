package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

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
import androidx.navigation.NavController
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import com.example.moarefiprod.R
import com.example.moarefiprod.data.SentenceGameViewModel
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBar
import kotlinx.coroutines.delay

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
    viewModel: SentenceGameViewModel
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val sentenceState by viewModel.sentenceData.collectAsState()
    val correctSentence = sentenceState?.correctSentence?.joinToString(" ") ?: ""
    var selectedWords by rememberSaveable { mutableStateOf(mutableListOf<String>()) }
    var showResultBox by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var timeInSeconds by remember { mutableStateOf(0) }


    val sentenceViewModel = viewModel as? SentenceGameViewModel ?: return


    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            timeInSeconds++
        }
    }
    LaunchedEffect(gameId) {
        viewModel.loadSentenceGame(courseId, gameId)
    }



    Box(modifier = Modifier.fillMaxSize()) {
        // Header
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {navController.popBackStack()},
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = screenHeight * 0.13f,
                    start = 16.dp, end = 16.dp)
                .align(Alignment.TopCenter)
                .zIndex(3f),
            contentAlignment = Alignment.Center
        ) {
            StepProgressBar(
                currentStep = gameIndex,
                totalSteps = totalGames
            )
        }
        // Header


        when {
            sentenceState == null -> {
                Text(
                    text = "در حال بارگذاری...",
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
                val wordList = state.wordPool
                val correctSentence = state.correctSentence.joinToString(" ")
                val question = state.question

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 30.dp,
                        ),
                ) {

                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            text = question,
                            fontFamily = iranSans,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 16.sp,
                            style = TextStyle(
                                textDirection = TextDirection.Rtl
                            ),
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


                    Spacer(modifier = Modifier.height(screenHeight * 0.05f))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeight * 0.07f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                DottedLine(
                                    width = screenWidth * 0.8f,
                                    modifier = Modifier.align(Alignment.Center)
                                )

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                ) {
                                    selectedWords.forEach { word ->
                                        ClickableTextWordBox(
                                            word = word,
                                            isSelected = true,
                                            onClick = {
                                                selectedWords = selectedWords.toMutableList().apply {
                                                    remove(word)
                                                }
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

                            DottedLine(width = screenWidth * 0.8f)

                            Spacer(modifier = Modifier.height(24.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // اینجا فقط کلماتی که در selectedWords نیستن رو نشون می‌دیم
                                sentenceState?.wordPool?.forEach { word ->
                                    if (!selectedWords.contains(word)) {
                                        ClickableTextWordBox(
                                            word = word,
                                            isSelected = false,
                                            onClick = {
                                                selectedWords = selectedWords.toMutableList().apply {
                                                    add(word)
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(screenHeight * 0.03f))


                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        wordList.forEach { word ->
                            if (!selectedWords.contains(word)) {
                                ClickableTextWordBox(
                                    word = word,
                                    isSelected = false,
                                    onClick = {
                                        selectedWords =
                                            selectedWords.toMutableList().apply { add(word) }
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))


                    if (!showResultBox) {
                        Button(
                            onClick = {
                                val isCurrentSentenceCorrect =
                                    selectedWords.joinToString(" ") == correctSentence
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
                                .padding( bottom = screenHeight * 0.19f)
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
                }
            }
        }

        if (showResultBox) {
            val userSentence = selectedWords.joinToString(" ")

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
                    navController.navigate("GameHost/$courseId/${gameIndex + 1}") {
                        popUpTo("GameHost/$courseId/$gameIndex") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
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
            .background(color = Color(0xFF90CECE),RoundedCornerShape(25.dp))
            .padding(horizontal = 15.dp, vertical = 5.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 8.dp)
        ){
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

            val isLastGame = gameIndex + 1 == totalGames

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End) // ⬅️ دکمه را راست‌چین می‌کند
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