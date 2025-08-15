package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ExitConfirmationDialog
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ResultDialog
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBarWithExit
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game.GrammerGameViewModel
import kotlinx.coroutines.delay

@Composable
fun TextPicPage(
    navController: NavController,
    courseId: String,
    lessonId: String,
    contentId: String,
    gameId: String,
    gameIndex: Int,
    totalGames: Int,
    viewModel: BaseGameViewModel,
) {
    val grammarViewModel = viewModel as? GrammerGameViewModel ?: return
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val textPicData by grammarViewModel.textPicData.collectAsState()
    val totalTimeInSeconds by grammarViewModel.totalTimeInSeconds.collectAsState()

    // ❗️ تغییر اصلی: به جای mutableStateOf(mutableListOf()) از mutableStateListOf استفاده می‌کنیم
    val selectedWords = remember { mutableStateListOf<String>() } // ← تغییر

    var correctCount by remember { mutableStateOf(0) }
    var wrongCount by remember { mutableStateOf(0) }
    var timeInSeconds by remember { mutableStateOf(0) }
    var showResultBox by remember { mutableStateOf(false) }
    var showFinalDialog by remember { mutableStateOf(false) }
    val imageSectionHeight = screenHeight * 0.36f
    val overlapHeight = 40.dp
    val resultBoxHeight = screenHeight * 0.19f
    val isLastGame = gameIndex + 1 >= totalGames

    LaunchedEffect(Unit) {
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
        grammarViewModel.loadTextPicGame(
            pathType = pathType,
            courseId = courseId,
            lessonId = lessonId,
            contentId = contentId,
            gameId = gameId
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Header
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
        ){
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
        // Header

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
                        .padding(end = 16.dp)
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.07f))

                if (data.words.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ){
                        data.words.chunked(4).forEach { rowWords ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                rowWords.forEach { word ->
                                    val isSelected = selectedWords.contains(word.word)

                                    // ❗️Brush را فقط وقتی وابستگی‌ها تغییر کنند محاسبه کن
                                    val backgroundBrush = remember(isSelected, showResultBox, word.isCorrect) {
                                        if (showResultBox) {
                                            if (word.isCorrect) {
                                                if (isSelected) Brush.radialGradient(
                                                    colors = listOf(
                                                        Color(0xFF6ABBBB),
                                                        Color(0xFF6ABBBB)
                                                    ),
                                                    radius = 130f
                                                ) else Brush.radialGradient(
                                                    colors = listOf(
                                                        Color(0x1ACDE8E5),
                                                        Color(0xFF6ABBBB)
                                                    ),
                                                    radius = 150f
                                                )
                                            } else {
                                                if (isSelected) Brush.radialGradient(
                                                    colors = listOf(
                                                        Color(0xFF6ABBBB),
                                                        Color(0xFF6ABBBB)
                                                    ),
                                                    radius = 130f
                                                ) else Brush.radialGradient(
                                                    colors = listOf(
                                                        Color(0x1ACDE8E5),
                                                        Color(0xFF6ABBBB)
                                                    ),
                                                    radius = 150f
                                                )
                                            }
                                        } else {
                                            if (isSelected) Brush.radialGradient(
                                                colors = listOf(
                                                    Color(0xFF6ABBBB),
                                                    Color(0xFF6ABBBB)
                                                ),
                                                radius = 130f
                                            ) else Brush.radialGradient(
                                                colors = listOf(
                                                    Color(0x1ACDE8E5),
                                                    Color(0xFF6ABBBB)
                                                ),
                                                radius = 150f
                                            )
                                        }
                                    }

                                    val borderColor = if (showResultBox) {
                                        if (word.isCorrect && isSelected) Color(0xFF14CB00)
                                        else if (!word.isCorrect && isSelected) Color(0xFFD32F2F)
                                        else Color.Transparent
                                    } else Color.Transparent

                                    val borderWidth =
                                        if (showResultBox && isSelected) 3.dp else 0.dp
                                    val textColor =
                                        if (showResultBox && isSelected) Color.White else Color.Black

                                    Box(
                                        modifier = Modifier
                                            .width(74.dp)
                                            .height(74.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(brush = backgroundBrush)
                                            .then(
                                                if (borderWidth > 0.dp)
                                                    Modifier.border(
                                                        BorderStroke(
                                                            borderWidth,
                                                            borderColor
                                                        ), RoundedCornerShape(10.dp)
                                                    )
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

                }
                Spacer(modifier = Modifier.weight(1f))

                if (!showFinalDialog) {
                    Button(
                        onClick = {
                            if (!showResultBox) {
                                correctCount = data.words.count { it.isCorrect && selectedWords.contains(it.word) }
                                wrongCount = selectedWords.count { w ->
                                    data.words.find { it.word == w }?.isCorrect == false
                                }
                                showResultBox = true
                                grammarViewModel.recordMemoryGameResult(correctCount, wrongCount, timeInSeconds)
                            }
                        },
                        enabled = !showResultBox,
                        modifier = Modifier
                            .align(Alignment.End) // مثل بقیه
                            .padding(
                                bottom = if (showResultBox) 0.dp else resultBoxHeight,
                                end = 10.dp // یکسان با دکمه‌های قبلی
                            )
                            .width(screenWidth * 0.20f)
                            .height(40.dp), // یکسان با بقیه
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4D869C),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFF4D869C), // ثابت مثل فعال
                            disabledContentColor = Color.White          // ثابت مثل فعال
                        )
                    ) {
                        Text(
                            text = "تأیید",
                            fontFamily = iranSans,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (showResultBox) {
                TextPicResultBox(
                    visible = showResultBox,
                    screenHeight = screenHeight,
                    isLastGame = isLastGame,
                    correctCount = correctCount,
                    wrongCount = wrongCount,
                    totalWords = data.words.size,
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    // onPrimaryAction
                    if (gameIndex + 1 >= totalGames) {
                        showFinalDialog = true
                    } else {
                        navController.navigate("GameHost/$courseId/$lessonId/$contentId/${gameIndex + 1}") {
                            popUpTo("GameHost/$courseId/$lessonId/$contentId/$gameIndex") { inclusive = true }
                        }
                    }
                    selectedWords.clear()
                    showResultBox = false
                    correctCount = 0
                    wrongCount = 0
                }
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
        } ?: run {
            Text(
                text = "...در حال بارگذاری",
                color = Color.Gray,
                fontFamily = iranSans,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun TextPicResultBox(
    visible: Boolean,
    screenHeight: Dp,
    isLastGame: Boolean,
    correctCount: Int,
    wrongCount: Int,
    totalWords: Int,
    modifier: Modifier = Modifier,
    onPrimaryAction: () -> Unit
) {
    if (!visible) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color(0xFFFFFFFF))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 30.dp)
                .background(color = Color(0xFF90CECE), RoundedCornerShape(25.dp))
                .padding(horizontal = 15.dp, vertical = 12.dp)
                .animateContentSize()
                .heightIn(max = screenHeight * 0.5f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 42.dp)
            ) {
                val total = totalWords
                val unanswered = (total - (correctCount + wrongCount)).coerceAtLeast(0)

                if (wrongCount == 0 && correctCount == total) {
                    Text(
                        text = "آفرین \uD83C\uDF89 همه رو درست انتخاب کردی",
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
                    InfoRow("تعداد نزده", unanswered)
                }
            }

            // دکمه «بریم بعدی» / «تمام»
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .width(90.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF4D869C))
                    .clickable { onPrimaryAction() },
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
                        fontSize = 13.sp
                    )
                    if (!isLastGame) {
                        Spacer(Modifier.width(8.dp))
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
