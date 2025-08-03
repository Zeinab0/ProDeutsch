package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.example.moarefiprod.iranSans
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moarefiprod.R
import com.example.moarefiprod.repository.saveGameResultToFirestore
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ResultDialog
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBar
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game.GrammerGameViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MemoryGamePage(
    navController: NavController,
    courseId: String,
    lessonId: String,
    contentId: String,
    gameId: String,
    gameIndex: Int,
    viewModel: BaseGameViewModel
) {
    val grammarViewModel = viewModel as? GrammerGameViewModel ?: return

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val wordPairs by grammarViewModel.memoryCardPairs.collectAsState()
    val displayGermanWords = remember { mutableStateListOf<String>() }
    var isDataLoaded by remember { mutableStateOf(false) }

    val totalTimeInSeconds by grammarViewModel.totalTimeInSeconds.collectAsState()

    var timeInSeconds by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            timeInSeconds++
        }
    }
    LaunchedEffect(gameId) {
        grammarViewModel.loadMemoryGameFromGrammar(courseId, gameId)
    }


    LaunchedEffect(wordPairs) {
        if (wordPairs.isNotEmpty()) {
            isDataLoaded = true
            displayGermanWords.clear()
            displayGermanWords.addAll(wordPairs.map { it.germanWord }.shuffled())
        } else {
            isDataLoaded = false
        }
    }

    if (!isDataLoaded) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("در حال بارگذاری...", fontFamily = iranSans)
        }
        return
    }

    var selectedLeft by remember { mutableStateOf<String?>(null) }
    var selectedRight by remember { mutableStateOf<String?>(null) }

    val correctPairs = remember { mutableStateListOf<Pair<String, String>>() }
    val errorCountMap = remember { mutableStateMapOf<String, Int>() }

    val usedFarsiWords = remember {
        derivedStateOf {
            val correct = correctPairs.map { it.first }.toSet()
            val wrong = errorCountMap.filter { it.value >= 3 }.keys
            (correct + wrong).toSet()
        }
    }

    val wrongGermanWords = remember {
        derivedStateOf {
            val farsiWordsWrong = errorCountMap.filter { it.value >= 3 }.keys
            wordPairs
                .filter { it.farsiWord in farsiWordsWrong }
                .map { it.germanWord }
                .toSet()
        }
    }

    val allUsedUp = remember {
        derivedStateOf {
            wordPairs.isNotEmpty() && usedFarsiWords.value.size == wordPairs.size
        }
    }

    LaunchedEffect(selectedLeft, selectedRight) {
        if (selectedLeft != null && selectedRight != null) {
            val isCorrect = wordPairs.any {
                it.farsiWord == selectedLeft && it.germanWord == selectedRight
            }

            if (isCorrect) {
                correctPairs.add(Pair(selectedLeft!!, selectedRight!!))
            } else {
                val currentCount = errorCountMap[selectedLeft!!] ?: 0
                errorCountMap[selectedLeft!!] = currentCount + 1
            }

            selectedLeft = null
            selectedRight = null
        }
    }

    var showError by remember { mutableStateOf(false) }
    var showFinalDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = screenHeight * 0.1f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StepProgressBar(currentStep = gameIndex)

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "...بزن بریم\n):باید کلمات رو به معنیشون وصل کنیم ",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = (screenWidth * 0.035f).value.sp
                ),
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp)
            )

            Spacer(modifier = Modifier.height(38.dp))

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    wordPairs.forEach { pair ->
                        val isMatched = correctPairs.any { it.first == pair.farsiWord }
                        val isWrongFarsi = (errorCountMap[pair.farsiWord] ?: 0) >= 3

                        WordItemWithState(
                            word = pair.farsiWord,
                            isSelected = selectedLeft == pair.farsiWord,
                            isMatched = isMatched,
                            isWrong = isWrongFarsi,
                            onClick = {
                                if (!isMatched && !isWrongFarsi) selectedLeft = pair.farsiWord
                            },
                            screenWidth = screenWidth
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    displayGermanWords.forEach { germanWord ->
                        val isMatched = correctPairs.any { it.second == germanWord }
                        val isWrongGerman = wrongGermanWords.value.contains(germanWord)

                        WordItemWithState(
                            word = germanWord,
                            isSelected = selectedRight == germanWord,
                            isMatched = isMatched,
                            isWrong = isWrongGerman,
                            onClick = {
                                if (!isMatched && !isWrongGerman) selectedRight = germanWord
                            },
                            screenWidth = screenWidth
                        )
                    }
                }
            }

            if (showError) {
                Text(
                    text = "لطفا تمام جفت‌ها رو تکمیل کنید و بعد تأیید بزنید",
                    color = Color.Red,
                    fontFamily = iranSans,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
        }

        Button(
            onClick = {
                if (allUsedUp.value) {
                    val correct = correctPairs.size
                    val wrong = errorCountMap.values.count { it >= 3 }

                    grammarViewModel.recordMemoryGameResult(correct, wrong, timeInSeconds)

                    val nextGameId = grammarViewModel.getNextGameId(gameIndex + 1)

                    if (gameIndex + 1 < grammarViewModel.gameListSize()) {
                        navController.navigate("GameHost/$courseId/${gameIndex + 1}") {
                            popUpTo("GameHost/$courseId/$gameIndex") { inclusive = true }
                        }
                    } else {
                        showFinalDialog = true
                    }



                    showError = false
                } else {
                    showError = true
                }
            },
            modifier = Modifier
                .offset(
                    x = screenWidth - (screenWidth * 0.20f) - 30.dp,
                    y = screenHeight * 0.2f
                )
                .width(screenWidth * 0.20f)
                .height(40.dp)
                .zIndex(1f),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4D869C),
                contentColor = Color.White
            )
        ) {
            Text("تأیید", fontFamily = iranSans, fontWeight = FontWeight.Bold)
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
                    navController.navigate("grammar_page")
                }
            )
        }
    }
}


@Composable
fun WordItemWithState(
    word: String,
    isSelected: Boolean,
    isMatched: Boolean,
    isWrong: Boolean,
    onClick: () -> Unit,
    screenWidth: Dp
) {
    val backgroundColor = when {
        isMatched -> Color.White
        isWrong -> Color(0xFF90CECE)
        isSelected -> Color(0xFF90CECE)
        else -> Color(0xFFF1FFFF)
    }

    val borderColor = if (isWrong) Color.Red else Color(0xFF90CECE)
    val textColor = when {
        isMatched -> Color(0xFF56B096)
        isSelected -> Color.White
        isWrong -> Color.White
        else -> Color.Black
    }

    Box(
        modifier = Modifier
            .width(145.dp)
            .height(55.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(10.dp))
            .clickable(enabled = !isMatched) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = word,
            fontSize = (screenWidth * 0.035f).value.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = iranSans,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Match(
    correct: Int = 0,
    wrong: Int = 0,
    timeInSeconds: Int = 0,
    showStats: Boolean = true,
    showTime: Boolean = true,
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
                    text = "عالیییییی\n ^_^ همه جفتا رو پیدا کردی",
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                )
            } else {
                if (showStats) {
                    Text(
                        text = "تعداد درست: $correct    تعداد اشتباه: $wrong",
                        fontFamily = iranSans,
                        color = Color.Black,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )
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
                        painter = painterResource(id = R.drawable.backbtn),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}