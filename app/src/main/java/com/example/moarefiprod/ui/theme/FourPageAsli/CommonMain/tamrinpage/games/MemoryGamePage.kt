package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moarefiprod.repository.saveGameResultToFirestore
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBar
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MemoryGamePage(
    navController: NavController,
    gameId: String = "matchingGame_001",
    viewModel: GameViewModel = viewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val wordPairs = viewModel.wordPairs.collectAsState().value
    val displayGermanWords = remember { mutableStateListOf<String>() }

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"

    LaunchedEffect(Unit) {
        viewModel.loadMemoryGame(gameId)
    }

    LaunchedEffect(wordPairs) {
        if (wordPairs.isNotEmpty()) {
            displayGermanWords.clear()
            displayGermanWords.addAll(wordPairs.map { it.germanWord }.shuffled())
        }
    }

    var showResultBox by remember { mutableStateOf(false) }
    var timeInSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(showResultBox) {
        if (!showResultBox) {
            while (true) {
                delay(1000)
                if (showResultBox) break
                timeInSeconds++
            }
        }
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

    val gameStarted = remember {
        derivedStateOf {
            correctPairs.isNotEmpty() || errorCountMap.isNotEmpty()
        }
    }

    val allUsedUp = remember {
        derivedStateOf {
            wordPairs.isNotEmpty() && usedFarsiWords.value.size == wordPairs.size
        }
    }

    LaunchedEffect(usedFarsiWords.value.size) {
        val total = wordPairs.size
        if (usedFarsiWords.value.size == total && gameStarted.value && !showResultBox) {
            showResultBox = true
            if (userId != "unknown") {
                saveGameResultToFirestore(
                    userId = userId,
                    gameId = gameId,
                    correct = correctPairs.size,
                    wrong = errorCountMap.values.count { it >= 3 }
                )
            }
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
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(start = screenWidth * 0.03f, top = screenHeight * 0.05f)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.backbtn),
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.size(screenWidth * 0.09f)
            )
        }

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                StepProgressBar(currentStep = 0)
            }

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
                        val isWrong = (errorCountMap[pair.farsiWord] ?: 0) >= 3

                        WordItemWithState(
                            word = pair.farsiWord,
                            isSelected = selectedLeft == pair.farsiWord,
                            isMatched = isMatched,
                            isWrong = isWrong,
                            onClick = {
                                if (!isMatched && !isWrong) selectedLeft = pair.farsiWord
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

                        WordItemWithState(
                            word = germanWord,
                            isSelected = selectedRight == germanWord,
                            isMatched = isMatched,
                            isWrong = false,
                            onClick = {
                                if (!isMatched) selectedRight = germanWord
                            },
                            screenWidth = screenWidth
                        )
                    }
                }
            }
        }

        if (showResultBox) {
            Match(
                correct = correctPairs.size,
                wrong = errorCountMap.values.count { it >= 3 },
                timeInSeconds = timeInSeconds,
                onNext = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 0.dp)
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
        isSelected -> Color(0xFF90CECE)
        else -> Color(0xFFF1FFFF)
    }

    val borderColor = if (isWrong) Color.Red else Color(0xFF90CECE)
    val textColor = when {
        isMatched -> Color(0xFF56B096)
        isSelected -> Color.White
        else -> Color.Black
    }

    Box(
        modifier = Modifier
            .width(155.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(10.dp))
            .clickable(enabled = !isMatched) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = word,
            fontSize = (screenWidth * 0.04f).value.sp,
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
                        text = "درست: $correct     اشتباه: $wrong",
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
                        text = "بعدی",
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
