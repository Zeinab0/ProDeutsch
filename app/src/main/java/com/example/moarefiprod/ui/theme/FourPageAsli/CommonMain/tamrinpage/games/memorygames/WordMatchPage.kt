package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.memorygames


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx .compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.StepProgressBar
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moarefiprod.viewmodel.GameViewModel
import com.example.moarefiprod.repository.saveGameResultToFirestore
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.WinnerBottomBox
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material3.Text

@Composable
fun WordMatchPage(
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

    // بررسی پایان بازی و ذخیره نتیجه
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

    // بررسی انتخاب کاربر
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
                // لیست فارسی
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

                // لیست آلمانی
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
            WinnerBottomBox(
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

@Preview(showBackground = true)
@Composable
fun WordMatchScreenPreview() {
    WordMatchPage(navController = rememberNavController())
}
