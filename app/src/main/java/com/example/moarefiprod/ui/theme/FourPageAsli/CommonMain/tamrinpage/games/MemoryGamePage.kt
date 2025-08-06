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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.Dp
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
fun MemoryGamePage(
    navController: NavController,
    pathType: GrammerGameViewModel.GamePathType,
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
    var showResultBox by remember { mutableStateOf(false) } // ✅ اضافه شده

    var timeInSeconds by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            timeInSeconds++
        }
    }
    LaunchedEffect(gameId) {
        grammarViewModel.loadMemoryGame(
            pathType = pathType,
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
        var showExitDialog by remember { mutableStateOf(false) }

        val returnRoute = if (pathType == GrammerGameViewModel.GamePathType.COURSE) {
            "darsDetails/$courseId/$lessonId"
        } else {
            "grammar_page"
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .zIndex(3f),
        ) {
            StepProgressBarWithExit(
                navController = navController,
                currentStep = gameIndex,
                totalSteps = totalGames,
                returnRoute = returnRoute,
                modifier = Modifier.fillMaxSize(),
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = screenHeight * 0.1f,
                    start = 40.dp,
                    end = 40.dp,
                ),
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.10f))

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
                    showResultBox = true // ✅ متغیر نمایش باکس را true می‌کند
                    showError = false
                } else {
                    showError = true
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = screenHeight * 0.19f, end = screenWidth * 0.06f)
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

        if (showResultBox) { // ✅ باکس نتایج فقط در صورت true بودن این متغیر نمایش داده می‌شود
            val correctCount = correctPairs.size
            val wrongCount = errorCountMap.values.count { it >= 3 }
            val totalCount = wordPairs.size

            MemoryGameResultBox(
                correctCount = correctCount,
                wrongCount = wrongCount,
                total = totalCount,
                onNext = {
                    if (gameIndex + 1 < grammarViewModel.gameListSize()) {
                        navController.navigate("GameHost/$courseId/$lessonId/$contentId/${gameIndex + 1}") {
                            popUpTo("GameHost/$courseId/$lessonId/$contentId/$gameIndex") { inclusive = true }
                        }
                    } else {
                        showFinalDialog = true
                    }
                },
                isLastGame = gameIndex + 1 >= grammarViewModel.gameListSize(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = screenHeight * 0.03f)
            )
        }

        if (showFinalDialog) {
            val returnRoute = if (lessonId.isNotEmpty() && contentId.isNotEmpty()) {
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

@Composable
fun MemoryGameResultBox(
    correctCount: Int,
    wrongCount: Int,
    total: Int,
    onNext: () -> Unit,
    isLastGame: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .offset(y = 8.dp)
            .height(160.dp)
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF90CECE)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            // ✅ Arrangement را به Top تغییر دهید تا کنترل کامل روی فواصل داشته باشید
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End
        ) {
            // متن‌ها
            if (wrongCount == 0) {
                Text(
                    text = "آفرین 🎉 همه رو درست زدی!",
                    fontFamily = iranSans,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    style = TextStyle(textDirection = TextDirection.Rtl),
                    textAlign = TextAlign.Right,
                )
            } else {
                InfoRowMemory("تعداد کلمات درست", correctCount)
                Spacer(modifier = Modifier.height(6.dp))
                InfoRowMemory("تعداد کلمات غلط", wrongCount)
            }

            // ✅ یک Spacer با ارتفاع ثابت بین متن و دکمه قرار دهید
            Spacer(modifier = Modifier.height(20.dp))

            // دکمه "بریم بعدی"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
                    // ✅ offset دکمه را کمتر کنید تا کمتر به پایین حرکت کند
                    .offset(y = (-5).dp)
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

@Composable
fun InfoRowMemory(label: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = count.toString(),
            fontFamily = iranSans,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = ": $label",
            fontFamily = iranSans,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}
