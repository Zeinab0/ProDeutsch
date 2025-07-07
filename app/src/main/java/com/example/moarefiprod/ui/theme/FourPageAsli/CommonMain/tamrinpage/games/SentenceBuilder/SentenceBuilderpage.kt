package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.SentenceBuilder


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
import androidx.navigation.NavController
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.tooling.preview.Preview
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.StepProgressBar
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.WinnerBottomBox
import com.example.moarefiprod.viewmodel.GameViewModel
import com.google.firebase.auth.FirebaseAuth



@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBuilderPage(
    navController: NavController,
    viewModel: GameViewModel = viewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Ensure sentenceData is correctly observed
    val sentenceState by viewModel.sentenceData.collectAsState()

    // Explicitly define type for mutableListOf to avoid ambiguity
    var selectedWords by rememberSaveable { mutableStateOf(mutableListOf<String>()) }
    var showResultBox by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var timeInSeconds by remember { mutableStateOf(0) }

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"

    LaunchedEffect(Unit) {
        viewModel.loadSentenceGame("sentence_01")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Only render the game UI when sentenceState is not null
        sentenceState?.let { state ->
            val wordList = state.wordPool
            val question = state.question
            // Crucial: Ensure correctSentence is aligned with how you want to compare it.
            // If it's stored reversed in DB, you might need to reverse it here:
            // val correctSentence = state.correctSentence.reversed().joinToString(" ")
            // Otherwise, if DB is corrected, use directly:
            val correctSentence = state.correctSentence

            // Back Button - now inside sentenceState.let
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
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(screenHeight * 0.1f))

                StepProgressBar(currentStep = 1) // Assuming this is defined

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = " $question ",
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = painterResource(id = R.drawable.chat),
                        contentDescription = "chat",
                        modifier = Modifier
                            .size(56.dp)
                            .padding(end = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(screenHeight * 0.12f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            DottedLine( // Assuming this is defined
                                width = screenWidth * 0.76f,
                                modifier = Modifier.align(Alignment.Center)
                            )

                            Image(
                                painter = painterResource(id = R.drawable.pen),
                                contentDescription = "pen",
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.CenterStart)
                                    .absoluteOffset(x = 4.dp, y = (-14).dp)
                            )

                            FlowRow( // Ensure ExperimentalLayoutApi if using this from Accompanist
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 40.dp)
                                    .align(Alignment.CenterStart)
                            ) {
                                selectedWords.forEach { word ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFCDE8E5))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                            .clickable {
                                                // Allows removing words by clicking them in the top line
                                                selectedWords = selectedWords.toMutableList().apply { remove(word) }
                                            }
                                    ) {
                                        Text(
                                            text = word,
                                            fontFamily = iranSans,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        DottedLine(width = screenWidth * 0.8f)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Selectable word options at the bottom
                FlowRow( // Ensure ExperimentalLayoutApi if using this from Accompanist
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    wordList.forEach { word ->
                        ClickableTextWordBox( // Assuming this is defined
                            word = word,
                            isSelected = selectedWords.contains(word),
                            onClick = {
                                if (!selectedWords.contains(word)) {
                                    selectedWords = selectedWords.toMutableList().apply { add(word) }
                                } else {
                                    // If word is already selected (in the top line), clicking it here also removes it
                                    // This provides a consistent behavior for toggling selection.
                                    selectedWords = selectedWords.toMutableList().apply { remove(word) }
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Submit Button
                Button(
                    onClick = {
                        val isCurrentSentenceCorrect = selectedWords.joinToString(" ") == correctSentence.joinToString(" ")
                        isCorrect = isCurrentSentenceCorrect
                        showResultBox = true
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 30.dp, bottom = screenHeight * 0.15f)
                        .width(screenWidth * 0.20f)
                        .height(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4D869C),
                        contentColor = Color.White
                    )
                ) {
                    Text("تایید", fontFamily = iranSans, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Result Box (WinnerBottomBox)
        if (showResultBox) {
            // Note: correctSentence from SentenceState is a List<String>. Join it for WinnerBottomBox.
            val correctSentenceText = sentenceState?.correctSentence?.joinToString(" ") ?: ""
            val userSentenceText = selectedWords.joinToString(" ")

            WinnerBottomBox(
                correct = if (isCorrect == true) 1 else 0,
                wrong = if (isCorrect == false) 1 else 0,
                timeInSeconds = timeInSeconds,
                showTime = false,
                showStats = false,
                correctSentence = correctSentenceText,
                userSentence = userSentenceText,
                onNext = {
                    // Reset game state for the next round
                    selectedWords = mutableListOf()
                    showResultBox = false
                    isCorrect = null
                    // You might want to load a new sentence here:
                    // viewModel.loadNextSentence() // Or navigate to next game
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 0.dp)
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
    modifier: Modifier = Modifier, // ✅ اضافه شد
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


@Preview(showBackground = true)
@Composable
fun SentenceBuilderPreview() {
    SentenceBuilderPage(navController = rememberNavController())
}