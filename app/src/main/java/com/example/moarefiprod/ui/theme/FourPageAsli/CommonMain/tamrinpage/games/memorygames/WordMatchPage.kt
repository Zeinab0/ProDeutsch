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
import androidx.compose.ui.graphics.Brush
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
import com.google.firebase.auth.FirebaseAuth

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
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"

    LaunchedEffect(Unit) {
        viewModel.loadMemoryGame(gameId)
    }
    var showResultBox by remember { mutableStateOf(false) }  // ✅ اول این باید بیاد
    var timeInSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(showResultBox) {
        if (!showResultBox) {
            while (true) {
                delay(1000)
                timeInSeconds++
            }
        }
    }

    var selectedLeft by remember { mutableStateOf<String?>(null) }
    var selectedRight by remember { mutableStateOf<String?>(null) }

    val correctPairs = remember { mutableStateListOf<Pair<String, String>>() }
    val wrongPairs = remember { mutableStateListOf<Pair<String, String>>() }
    val wrongAttempts = remember { mutableStateMapOf<String, Int>() }
    val permanentlyWrong = remember { mutableStateListOf<String>() }
    val permanentlyWrongMap = remember { mutableStateMapOf<String, String>() } // key: farsi, value: last wrong german

    val allMatched = remember {
        derivedStateOf {
            wordPairs.isNotEmpty() &&
                    (correctPairs.map { it.first }.toSet().size + permanentlyWrong.size) == wordPairs.size
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier.fillMaxWidth()) {
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
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = screenHeight * 0.1f, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                StepProgressBar(currentStep = 0) // یا هر مرحله‌ای
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
                    .padding(end = 10.dp) // 🔹 متن رو یکم بکشه به چپ (نزدیک‌تر به راست صفحه)

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
                        val isWrong = permanentlyWrong.contains(pair.farsiWord)

                        WordItemWithState(
                            word = pair.farsiWord,
                            isSelected = selectedLeft == pair.farsiWord,
                            isMatched = isMatched,
                            isWrong = isWrong,
                            onClick = { if (!isMatched && !isWrong) selectedLeft = pair.farsiWord },
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
                    wordPairs.forEach { pair ->
                        val isMatched = correctPairs.any { it.second == pair.germanWord }
                        val isWrong = permanentlyWrongMap.containsValue(pair.germanWord)

                        WordItemWithState(
                            word = pair.germanWord,
                            isSelected = selectedRight == pair.germanWord,
                            isMatched = isMatched,
                            isWrong = isWrong,
                            onClick = { if (!isMatched && !isWrong) selectedRight = pair.germanWord },
                            screenWidth = screenWidth
                        )
                    }
                }
            }

            LaunchedEffect(allMatched.value) {
                if (allMatched.value && userId != "unknown") {
                    saveGameResultToFirestore(
                        userId = userId,
                        gameId = gameId,
                        correct = correctPairs.count { !permanentlyWrong.contains(it.first) },
                        wrong = permanentlyWrong.size
                    )
                }
            }
        }

        LaunchedEffect(selectedLeft, selectedRight) {
            if (selectedLeft != null && selectedRight != null) {
                val key = selectedLeft!!
                val isCorrect = wordPairs.any {
                    it.farsiWord == selectedLeft && it.germanWord == selectedRight
                }

                if (isCorrect) {
                    correctPairs.add(Pair(selectedLeft!!, selectedRight!!))
                } else {
                    wrongPairs.add(Pair(selectedLeft!!, selectedRight!!))
                    val current = wrongAttempts[key] ?: 0
                    wrongAttempts[key] = current + 1

                    if (wrongAttempts[key] == 3) {
                        permanentlyWrong.add(key)
                        permanentlyWrongMap[key] = selectedRight!!
                    }

                    delay(1000)
                    if (!permanentlyWrong.contains(key)) {
                        wrongPairs.removeAll {
                            it.first == selectedLeft || it.second == selectedRight
                        }
                    }
                }

                if (wordPairs.isNotEmpty() &&
                    (correctPairs.map { it.first }.toSet().size + permanentlyWrong.size) == wordPairs.size
                ) {
                    showResultBox = true
                }

                selectedLeft = null
                selectedRight = null
            }
        }

        if (showResultBox) {

            WinnerBottomBox(
                correct = correctPairs.count { !permanentlyWrong.contains(it.first) },
                wrong = permanentlyWrong.size,
                timeInSeconds = timeInSeconds,
                onNext = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.BottomCenter) // ✅ موقعیت پایین صفحه
                    .padding(bottom = 0.dp)         // ✅ حذف فاصله از پایین
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
fun WinnerBottomBox(
    correct: Int,
    wrong: Int,
    timeInSeconds: Int,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val formattedTime = String.format("%02d:%02d", timeInSeconds / 60, timeInSeconds % 60)
    val allCorrect = wrong == 0

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(screenHeight * 0.18f)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF4DA4A4), Color(0xFFBFEAE8)) // چپ تیره، راست روشن
                ),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // ⏱ زمان بالا سمت چپ (کمی بالاتر و راست‌تر)
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

        // 📄 متن و دکمه سمت راست
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopEnd)
                .offset(y = 12.dp),
            horizontalAlignment = Alignment.End
        ) {
            val message = if (allCorrect)
                "هوراااااااا\nهمرو درست جواب دادی ^_^"
            else
                "اشکالی نداره\n:) دفعه بعد بیشتر تلاش کن"

            // نمایش پیام (همیشه هست)
            Text(
                text = message,
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(6.dp)) // فاصله بین پیام و تعداد درست/غلط

            if (!allCorrect) {
                Text(
                    text = "تعداد درست: $correct     تعداد اشتباه: $wrong",
                    fontFamily = iranSans,
                    color = Color.Black,
                    textAlign = TextAlign.Right
                )
            }

// این Spacer رو از شرط جدا کن تا همیشه فاصله ایجاد بشه
            Spacer(modifier = Modifier.height(24.dp))  // اینجا مقدار دلخواهت رو بذار مثلاً 40.dp

            // 🔵 دکمه بریم بعدی
            Box(
                modifier = Modifier
                    .width(98.dp)
                    .height(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF005B73))
                    .clickable { onNext() }
                    .padding(horizontal = 8.dp),
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

@Preview(showBackground = true)
@Composable
fun WordMatchScreenPreview() {
    WordMatchPage(navController = rememberNavController())
}
