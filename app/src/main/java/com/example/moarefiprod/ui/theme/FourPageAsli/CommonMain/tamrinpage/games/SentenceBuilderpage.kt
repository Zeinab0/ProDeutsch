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
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game.GrammerGameViewModel
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
    viewModel: BaseGameViewModel
) {
    val grammarViewModel = viewModel as? GrammerGameViewModel ?: return

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val sentenceWords by grammarViewModel.sentenceWords.collectAsState()
    val sentenceAnswer by grammarViewModel.sentenceAnswer.collectAsState()
    val sentenceTitle by grammarViewModel.sentenceTitle.collectAsState()
    var selectedWords by rememberSaveable { mutableStateOf(mutableListOf<String>()) }
    var showResultBox by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var timeInSeconds by remember { mutableStateOf(0) }


    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            timeInSeconds++
        }
    }

    val correctSentence = sentenceAnswer
    val wordList = sentenceWords

    Box(modifier = Modifier.fillMaxSize()) {
        if (wordList.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(screenHeight * 0.1f))

                StepProgressBar(currentStep = gameIndex)

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = sentenceTitle,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.1f))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    selectedWords.forEach { word ->
                        ClickableTextWordBox(
                            word = word,
                            isSelected = true,
                            onClick = {
                                selectedWords = selectedWords.toMutableList().apply { remove(word) }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                                    selectedWords = selectedWords.toMutableList().apply { add(word) }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        val isCurrentCorrect = selectedWords.joinToString(" ") == correctSentence
                        isCorrect = isCurrentCorrect
                        showResultBox = true
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 32.dp)
                ) {
                    Text("تأیید", fontFamily = iranSans)
                }
            }
        } else {
            Text(
                text = "داده‌ای یافت نشد",
                color = Color.Red,
                fontFamily = iranSans,
                modifier = Modifier.align(Alignment.Center)
            )
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
                    .padding(bottom = 24.dp)
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