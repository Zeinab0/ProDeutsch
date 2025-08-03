package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ResultDialog
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBar
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game.GrammerGameViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@Composable
//fun TextPicPage(
//    navController: NavController,
//    courseId: String,
//    lessonId: String,
//    contentId: String,
//    gameId: String,
//    gameIndex: Int,
//    viewModel: BaseGameViewModel,
//) {
//    val grammarViewModel = viewModel as? GrammerGameViewModel ?: return
//
//    val configuration = LocalConfiguration.current
//    val screenWidth = configuration.screenWidthDp.dp
//    val screenHeight = configuration.screenHeightDp.dp
//
//    val textPicData by grammarViewModel.textPicData.collectAsState()
//    var selectedWords by remember { mutableStateOf(mutableListOf<String>()) }
//    var correctCount by remember { mutableStateOf(0) }
//    var wrongCount by remember { mutableStateOf(0) }
//    var timeInSeconds by remember { mutableStateOf(0) }
//    val totalTimeInSeconds by grammarViewModel.totalTimeInSeconds.collectAsState()
//
//    val scope = rememberCoroutineScope()
//    var showResultBox by remember { mutableStateOf(false) }
//    var showFinalDialog by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(1000L)
//            timeInSeconds++
//        }
//    }
//
//    LaunchedEffect(gameId) {
//        grammarViewModel.loadTextPicGameFromGrammar(courseId, gameId)
//    }
//
//    textPicData?.let { data ->
//        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//            StepProgressBar(currentStep = gameIndex)
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = data.title,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold,
//                fontFamily = iranSans
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            data.words.chunked(2).forEach { row ->
//                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
//                    row.forEach { word ->
//                        val isSelected = selectedWords.contains(word.word)
//                        Box(
//                            modifier = Modifier
//                                .padding(8.dp)
//                                .clickable(enabled = !showResultBox) {
//                                    if (isSelected) selectedWords.remove(word.word)
//                                    else selectedWords.add(word.word)
//                                }
//                                .background(
//                                    color = if (isSelected) Color(0xFF7AB2B2) else Color.LightGray,
//                                    shape = RoundedCornerShape(8.dp)
//                                )
//                                .padding(12.dp)
//                        ) {
//                            Text(word.word, fontFamily = iranSans)
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            if (!showResultBox) {
//                Button(onClick = {
//                    showResultBox = true
//                    correctCount = data.words.count { it.isCorrect && selectedWords.contains(it.word) }
//                    wrongCount = selectedWords.count { word ->
//                        data.words.find { it.word == word }?.isCorrect == false
//                    }
//                    grammarViewModel.recordMemoryGameResult(correctCount, wrongCount, timeInSeconds)
//                }) {
//                    Text("تأیید", fontFamily = iranSans)
//                }
//            }
//
//            if (showResultBox) {
//                Spacer(modifier = Modifier.height(16.dp))
//                Text("تعداد درست: $correctCount", fontFamily = iranSans)
//                Text("تعداد غلط: $wrongCount", fontFamily = iranSans)
//
//                Spacer(modifier = Modifier.height(16.dp))
//                Button(onClick = {
//                    val nextGameId = "game_${gameIndex + 1}"
//                    navController.navigate("GameHost/$courseId/${gameIndex + 1}") {
//                        popUpTo("GameHost/$courseId/$gameIndex") { inclusive = true }
//                    }
//                }) {
//                    Text("برو به مرحله بعد", fontFamily = iranSans)
//                }
//            }
//        }
//    } ?: run {
//        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            Text("در حال بارگذاری...", fontFamily = iranSans)
//        }
//    }
//
//    if (showFinalDialog) {
//        ResultDialog(
//            navController = navController,
//            courseId = courseId,
//            lessonId = lessonId,
//            contentId = contentId,
//            timeInSeconds = totalTimeInSeconds,
//            onDismiss = {
//                showFinalDialog = false
//                navController.navigate("grammar_page")
//            }
//        )
//    }
//}


@Composable
fun TextPicPage(
    navController: NavController,
    courseId: String,
    lessonId: String,
    contentId: String,
    gameId: String,
    gameIndex: Int,
    viewModel: BaseGameViewModel,
) {
    val grammarViewModel = viewModel as? GrammerGameViewModel ?: return

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val textPicData by grammarViewModel.textPicData.collectAsState()
    val totalTimeInSeconds by grammarViewModel.totalTimeInSeconds.collectAsState()

    var selectedWords by remember { mutableStateOf(mutableListOf<String>()) }
    var correctCount by remember { mutableStateOf(0) }
    var wrongCount by remember { mutableStateOf(0) }
    var timeInSeconds by remember { mutableStateOf(0) }

    var showResultBox by remember { mutableStateOf(false) }
    var showFinalDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            timeInSeconds++
        }
    }
    LaunchedEffect(gameId) {
        grammarViewModel.loadTextPicGameFromGrammar(courseId, gameId)
    }


    val imageSectionHeight = screenHeight * 0.36f
    val overlapHeight = 40.dp
    val resultBoxHeight = screenHeight * 0.14f

    Box(modifier = Modifier.fillMaxSize()) {
        // Step Progress
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = screenHeight * 0.08f)
                .align(Alignment.TopCenter)
                .zIndex(3f),
            contentAlignment = Alignment.Center
        ) {
            StepProgressBar(currentStep = gameIndex)
        }

        textPicData?.let { data ->
            Image(
                painter = painterResource(id = R.drawable.kitchen),
                contentDescription = "Game Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageSectionHeight)
                    .align(Alignment.TopCenter)
                    .zIndex(1f)
            )

            val whiteBoxTopOffset = imageSectionHeight - overlapHeight
            val whiteBoxDynamicHeight = screenHeight - whiteBoxTopOffset - (if (showResultBox) resultBoxHeight else 0.dp)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = whiteBoxTopOffset)
                    .height(whiteBoxDynamicHeight)
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .shadow(22.dp, RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(Color.White)
                    .zIndex(2f)
                    .padding(horizontal = screenWidth * 0.07f)
                    .padding(top = 40.dp)
            ) {
                Text(
                    text = data.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                    color = Color.Black,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 34.dp)
                )

                Spacer(modifier = Modifier.height(55.dp))

                if (data.words.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        data.words.chunked(4).forEach { rowWords ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                rowWords.forEach { word ->
                                    val isSelected = selectedWords.contains(word.word)

                                    val backgroundBrush = if (showResultBox) {
                                        if (word.isCorrect) {
                                            if (isSelected) Brush.radialGradient(
                                                colors = listOf(Color(0xCC7ABDBB), Color(0xFFB0E1DF)),
                                                radius = 130f
                                            ) else Brush.radialGradient(
                                                colors = listOf(Color(0x1ACDE8E5), Color(0xFF6ABBBB)),
                                                radius = 150f
                                            )
                                        } else {
                                            if (isSelected) Brush.radialGradient(
                                                colors = listOf(Color(0xCC6ABBBB), Color(0xFFB0E1DF)),
                                                radius = 130f
                                            ) else Brush.radialGradient(
                                                colors = listOf(Color(0x1ACDE8E5), Color(0xFF6ABBBB)),
                                                radius = 150f
                                            )
                                        }
                                    } else {
                                        if (isSelected) Brush.radialGradient(
                                            colors = listOf(Color(0xFF6ABBBB), Color(0xFFB0E1DF)),
                                            radius = 130f
                                        ) else Brush.radialGradient(
                                            colors = listOf(Color(0x1ACDE8E5), Color(0xFF6ABBBB)),
                                            radius = 150f
                                        )
                                    }

                                    val borderColor = if (showResultBox) {
                                        if (word.isCorrect && isSelected) Color(0xFF14CB00)
                                        else if (!word.isCorrect && isSelected) Color(0xFFD32F2F)
                                        else Color.Transparent
                                    } else Color.Transparent

                                    val borderWidth = if (showResultBox && isSelected) 3.dp else 0.dp
                                    val textColor = if (showResultBox && isSelected) Color.White else Color.Black

                                    Box(
                                        modifier = Modifier
                                            .width(74.dp)
                                            .height(74.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(brush = backgroundBrush)
                                            .then(
                                                if (borderWidth > 0.dp)
                                                    Modifier.border(BorderStroke(borderWidth, borderColor), RoundedCornerShape(10.dp))
                                                else Modifier
                                            )
                                            .clickable(enabled = !showResultBox) {
                                                if (isSelected) selectedWords.remove(word.word)
                                                else selectedWords.add(word.word)
                                            }
                                            .padding(4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = word.word,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = textColor,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 22.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))

            if (!showResultBox) {
                Button(onClick = {
                    showResultBox = true
                    correctCount = data.words.count { it.isCorrect && selectedWords.contains(it.word) }
                    wrongCount = selectedWords.count { word ->
                        data.words.find { it.word == word }?.isCorrect == false
                    }
                    grammarViewModel.recordMemoryGameResult(correctCount, wrongCount, timeInSeconds)
                }) {
                    Text("تأیید", fontFamily = iranSans)
                }
            }


                }
            }
//            Spacer(modifier = Modifier.weight(1f))

//            if (!showResultBox) {
//                val buttonWidth = screenWidth * 0.22f
//                val buttonHeight = 42.dp
//                val paddingRight = 44.dp
//                val paddingBottom = 54.dp
//                val calculatedOffsetX = screenWidth - buttonWidth - paddingRight
//                val calculatedOffsetY = screenHeight - buttonHeight - paddingBottom
//
//                Button(
//                    onClick = {
//                        showResultBox = true
//                        correctCount = data.words.count { it.isCorrect && selectedWords.contains(it.word) }
//                        wrongCount = selectedWords.count { word ->
//                            data.words.find { it.word == word }?.isCorrect == false
//                        }
//                        grammarViewModel.recordMemoryGameResult(correctCount, wrongCount, timeInSeconds)
//                    },
//                    modifier = Modifier
//                        .offset(x = calculatedOffsetX, y = calculatedOffsetY)
//                        .width(buttonWidth)
//                        .height(buttonHeight),
//                    shape = RoundedCornerShape(10.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF4D869C),
//                        contentColor = Color.White
//                    ),
//                    enabled = selectedWords.isNotEmpty()
//                ) {
//                    Text("تأیید", fontFamily = iranSans, fontWeight = FontWeight.Bold)
//                }
//            }

            if (!showResultBox) {
                val buttonWidth = screenWidth * 0.22f
                val buttonHeight = 42.dp
                val paddingRight = 44.dp
                val paddingBottom = 54.dp
                val calculatedOffsetX = screenWidth - buttonWidth - paddingRight
                val calculatedOffsetY = screenHeight - buttonHeight - paddingBottom

                Button(
                    onClick = {
                        showResultBox = true
                        correctCount = data.words.count { it.isCorrect && selectedWords.contains(it.word) }
                        wrongCount = selectedWords.count { word ->
                            data.words.find { it.word == word }?.isCorrect == false
                        }
                        grammarViewModel.recordMemoryGameResult(correctCount, wrongCount, timeInSeconds)
                    },
                    modifier = Modifier
                        .offset(x = calculatedOffsetX, y = calculatedOffsetY)
                        .width(buttonWidth)
                        .height(buttonHeight),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4D869C),
                        contentColor = Color.White
                    ),
                    enabled = selectedWords.isNotEmpty()
                ) {
                    Text("تأیید", fontFamily = iranSans, fontWeight = FontWeight.Bold)
                }
            }


            if (showResultBox) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(resultBoxHeight)
                        .align(Alignment.BottomCenter)
                        .zIndex(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(resultBoxHeight)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF4DA4A4), Color(0xFFBFEAE8))
                                ),
                                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                            )
                            .padding(horizontal = 22.dp, vertical = 15.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.7f),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (wrongCount == 0 && correctCount > 0) {
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
                                InfoRow("تعداد درست", correctCount)
                                InfoRow("تعداد اشتباه", wrongCount)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(y = (-13).dp)
                                .width(98.dp)
                                .height(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF4D869C))
                                .clickable {
                                    val nextGameId = "game_${gameIndex + 1}"

                                    // اگر به پایان رسیده:
                                    if (nextGameId == "game_END") {
                                        showFinalDialog = true
                                    } else {
                                        navController.navigate("GameHost/$courseId/${gameIndex + 1}") {
                                            popUpTo("GameHost/$courseId/$gameIndex") { inclusive = true }
                                        }
                                    }

                                    // ریست برای مرحله بعدی
                                    selectedWords.clear()
                                    showResultBox = false
                                    correctCount = 0
                                    wrongCount = 0
                                },
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
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
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
        } ?: run {
            Text(
                text = "داده‌ها بارگذاری نشد.",
                color = Color.Red,
                fontFamily = iranSans,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun InfoRow(label: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp, horizontal = 0.dp),
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