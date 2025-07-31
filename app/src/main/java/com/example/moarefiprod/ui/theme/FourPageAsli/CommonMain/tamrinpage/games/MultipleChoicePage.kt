package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBar
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören.evenShadow
import kotlinx.coroutines.delay

@Composable
fun MultipleChoicePage(
    navController: NavController,
    topicId: String,
    gameIndex: Int,
    viewModel: GameViewModel = viewModel(key = "GameViewModel_$topicId")
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val totalQuestions by viewModel.totalQuestions.collectAsState()
    val correctCount by viewModel.correctAnswers.collectAsState()
    val wrongCount by viewModel.wrongAnswers.collectAsState()
    val mcqData by viewModel.selectOption.collectAsState()
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var showResultBox by remember { mutableStateOf(false) }
    var showFinalResultDialog by remember { mutableStateOf(false) }

    val gameId = "multiple_choice_quiz_article_1"

    LaunchedEffect(gameIndex) {
        Log.d("MultipleChoicePage", "LaunchedEffect triggered for gameIndex: $gameIndex, ViewModel: ${viewModel.hashCode()}")
        viewModel.initializeTotalQuestions(topicId, gameId)

        while (viewModel.totalQuestions.value == 0) {
            delay(100)
            Log.d("MultipleChoicePage", "Waiting for totalQuestions to load: ${viewModel.totalQuestions.value}")
        }

        if (gameIndex == 0) {
            Log.d("MultipleChoicePage", "Resetting scores for first question")
            viewModel.resetScores()
        }

        Log.d("MultipleChoicePage", "Loading multiple choice game for index: $gameIndex")
        viewModel.loadMultipleChoiceGame(topicId, gameId, gameIndex)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(start = screenWidth * 0.03f, top = screenHeight * 0.04f)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.backbtn),
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.size(screenWidth * 0.09f)
            )
        }

        mcqData?.let { data ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(screenHeight * 0.086f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    StepProgressBar(currentStep = gameIndex)
                }
                Spacer(modifier = Modifier.height(80.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.pen),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = data.question,
                        fontSize = 14.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(70.dp))

                data.options.forEachIndexed { index, option ->
                    val backgroundColor = when {
                        !showResultBox && selectedIndex == index -> Color(0xFF4D869C)
                        showResultBox && index == data.correctIndex -> Color(0xFF14CB00)
                        showResultBox && index == selectedIndex && selectedIndex != data.correctIndex -> Color(0xFFFF3B3B)
                        else -> Color(0xFFCDE8E5)
                    }
                    val textColor = if (showResultBox && index == data.correctIndex) Color.White else Color.Black

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(backgroundColor)
                            .clickable(enabled = !showResultBox) { selectedIndex = index }
                            .padding(vertical = 15.dp, horizontal = 14.dp)
                    ) {
                        Text(
                            text = "${index + 1}. $option",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor
                        )
                    }
                }

                Text(
                    text = "سوال ${gameIndex + 1} از $totalQuestions",
                    fontSize = 12.sp,
                    fontFamily = iranSans
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            if (showResultBox) {
                ChoiceResultBox(
                    correct = if (selectedIndex == data.correctIndex) 1 else 0,
                    wrong = if (selectedIndex != data.correctIndex) 1 else 0,
                    correctSentence = data.question.replace("________", data.options[data.correctIndex]),
                    userSentence = if (selectedIndex != data.correctIndex) data.question.replace("________", data.options[selectedIndex ?: -1]) else "",
                    translation = data.translation,
                    gameIndex = gameIndex,
                    totalQuestions = totalQuestions,
                    onNext = {
                        Log.d("MultipleChoicePage", "Moving to next question. Current correct: ${viewModel.correctAnswers.value}, wrong: ${viewModel.wrongAnswers.value}")
                        selectedIndex = null
                        showResultBox = false

                        if (gameIndex + 1 < totalQuestions) {
                            Log.d("MultipleChoicePage", "Navigating to next question: ${gameIndex + 1}")
                            navController.navigate("multipleChoice/$topicId/${gameIndex + 1}") {
                                popUpTo("multipleChoice/$topicId/$gameIndex") { inclusive = true }
                            }
                        } else {
                            Log.d("MultipleChoicePage", "Showing final result dialog. Final correct: ${viewModel.correctAnswers.value}, wrong: ${viewModel.wrongAnswers.value}")
                            showFinalResultDialog = true
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 0.dp)
                )
            }
        }

        Button(
            onClick = {
                if (selectedIndex != null && !showResultBox) {
                    val isCorrect = mcqData?.correctIndex == selectedIndex
                    viewModel.recordAnswer(isCorrect)
                    Log.d("MultipleChoicePage", "Answer recorded. Correct: ${viewModel.correctAnswers.value}, Wrong: ${viewModel.wrongAnswers.value}")
                    showResultBox = true
                } else {
                    Log.d("MultipleChoicePage", "Confirm button clicked but no option selected or result box already shown")
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 30.dp, bottom = 180.dp)
                .width(screenWidth * 0.20f)
                .height(40.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4D869C),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "تأیید",
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold
            )
        }
    }

    if (showFinalResultDialog) {
        Log.d("MultipleChoicePage", "Displaying final result dialog. Total questions: $totalQuestions, Correct: $correctCount, Wrong: $wrongCount")
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = true, onClick = {}),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = ":نتیجه آزمون",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = iranSans,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "تعداد کل سوالات: $totalQuestions",
                        fontSize = 12.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "تعداد جواب‌های درست: $correctCount",
                        fontSize = 12.sp,
                        fontFamily = iranSans,
                        color = Color.Black,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "تعداد جواب‌های غلط: $wrongCount",
                        fontSize = 12.sp,
                        fontFamily = iranSans,
                        color = Color.Black,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .evenShadow(radius = 25f, cornerRadius = 20f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF7AB2B2))
                            .height(45.dp)
                            .fillMaxWidth(0.4f)
                            .clickable {
                                Log.d("MultipleChoicePage", "Final dialog confirmed. Navigating to grammar_page")
                                showFinalResultDialog = false
                                navController.navigate("grammar_page")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("تأیید", color = Color.White, fontFamily = iranSans)
                    }
                }
            }
        }
    }
}

@Composable
fun ChoiceResultBox(
    correct: Int = 0,
    wrong: Int = 0,
    correctSentence: String? = null,
    userSentence: String? = null,
    translation: String? = null,
    gameIndex: Int,
    totalQuestions: Int,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(screenHeight * 0.14f)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF4D869C), Color(0xFFCDE8E5))
                ),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 8.dp)
        ) {
            if (correct == 1 || wrong == 1) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = if (correct == 1) R.drawable.tik else R.drawable.cross),
                            contentDescription = null,
                            tint = if (correct == 1) Color(0xFF14CB00) else Color(0xFFFF3B3B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (correct == 1) correctSentence ?: "" else userSentence ?: "",
                            fontFamily = iranSans,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            textAlign = TextAlign.Left
                        )
                    }

                    if (!translation.isNullOrEmpty()) {
                        Text(
                            text = translation,
                            fontFamily = iranSans,
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Right,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }

                if (wrong == 1) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.tik),
                            contentDescription = null,
                            tint = Color(0xFF14CB00),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = correctSentence ?: "",
                            fontFamily = iranSans,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            textAlign = TextAlign.Left
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
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
                    text = if (gameIndex + 1 == totalQuestions) "تمام" else "بریم بعدی",
                    fontFamily = iranSans,
                    color = Color.White,
                    fontSize = 12.sp
                )

                if (gameIndex + 1 != totalQuestions) {
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