package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.example.moarefiprod.iranSans
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import com.example.moarefiprod.R
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ResultDialog
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBar
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game.GrammerGameViewModel

@Composable
fun MemoryGamePage(
    navController: NavController,
    pathType: GrammerGameViewModel.GamePathType, // ⬅️ اضافه کن
    courseId: String,
    lessonId: String,
    contentId: String,
    gameId: String,
    gameIndex: Int,
    totalGames: Int,
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
    var selectedLeft by remember { mutableStateOf<String?>(null) }
    var selectedRight by remember { mutableStateOf<String?>(null) }
    val correctPairs = remember { mutableStateListOf<Pair<String, String>>() }
    val errorCountMap = remember { mutableStateMapOf<String, Int>() }
    var showError by remember { mutableStateOf(false) }
    var showFinalDialog by remember { mutableStateOf(false) }

    var timeInSeconds by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            timeInSeconds++
        }
    }
    LaunchedEffect(gameId) {
        grammarViewModel.loadMemoryGame(
            pathType = pathType, // یا GRAMMAR_TOPIC بسته به مسیر
            courseId = courseId,
            lessonId = lessonId,
            contentId = contentId,
            gameId = gameId
        )
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
                .padding(
                    top = screenHeight * 0.13f,
                    start = 16.dp, end = 16.dp
                )
                .align(Alignment.TopCenter)
                .zIndex(3f),
            contentAlignment = Alignment.Center
        ) {
            StepProgressBar(
                currentStep = gameIndex,
                totalSteps = totalGames
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = screenHeight * 0.1f,
                    start = 40.dp,
                    end = 40.dp,
                ),
        ){
            Spacer(modifier = Modifier.height(screenHeight * 0.14f))

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

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

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

                    if (gameIndex + 1 < grammarViewModel.gameListSize()) {
                        navController.navigate("GameHost/$courseId/$lessonId/$contentId/${gameIndex + 1}") {
                            popUpTo("GameHost/$courseId/$lessonId/$contentId/$gameIndex") { inclusive = true }
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
                .align(Alignment.BottomEnd)
                .padding(bottom = 66.dp, end = 36.dp)
                .width(screenWidth * 0.20f)
                .height(40.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4D869C),
                contentColor = Color.White
            )
        ) {
            Text("تأیید", fontFamily = iranSans, fontWeight = FontWeight.Bold)
        }

        if (showFinalDialog) {
            val returnRoute = if (lessonId.isNotEmpty() && contentId.isNotEmpty()) {
                "lesson_detail/$courseId/$lessonId" // ✅ مسیر درست
            } else {
                "grammar_page"
            }

            ResultDialog(
                navController = navController,
                courseId = courseId,
                lessonId = lessonId,
                contentId = contentId,
                timeInSeconds = totalTimeInSeconds,
                returnRoute = returnRoute,
                onDismiss = {
                    showFinalDialog = false
                    navController.navigate(returnRoute)
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