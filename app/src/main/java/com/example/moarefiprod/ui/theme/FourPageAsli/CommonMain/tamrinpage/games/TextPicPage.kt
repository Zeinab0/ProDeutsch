//package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games
//
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.graphics.Brush
//import androidx.lifecycle.viewmodel.compose.viewModel
//import coil.compose.AsyncImage
//import com.example.moarefiprod.R
//import com.example.moarefiprod.iranSans
//import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBar
//import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören.evenShadow
//import kotlinx.coroutines.delay
//
//@Composable
//fun TextPicPage(
//    navController: NavController,
//    courseId: String,
//    lessonId: String,
//    contentId: String,
//    gameId: String,
//    viewModel: GameViewModel = viewModel()
//) {
//    val configuration = LocalConfiguration.current
//    val screenWidth = configuration.screenWidthDp.dp
//    val screenHeight = configuration.screenHeightDp.dp
//
//    val totalQuestions by viewModel.totalQuestions.collectAsState()
//    val correctCount by viewModel.correctAnswers.collectAsState()
//    val wrongCount by viewModel.wrongAnswers.collectAsState()
//
//    val textPicData by viewModel.textPicData.collectAsState()
//    var selectedWords by remember { mutableStateOf<Set<String>>(emptySet()) }
//    var showResultBox by remember { mutableStateOf(false) }
//    var showFinalResultDialog by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        if (totalQuestions == 0) {
//            viewModel.resetScores()
//            viewModel.initializeTotalQuestions(courseId, lessonId, contentId, gameId)
//            while (viewModel.totalQuestions.value == 0) {
//                delay(100)
//            }
//        }
//        viewModel.loadTextPicGame(courseId, lessonId, contentId, gameId)
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        IconButton(
//            onClick = { navController.popBackStack() },
//            modifier = Modifier
//                .padding(start = screenWidth * 0.03f, top = screenHeight * 0.04f)
//                .align(Alignment.TopStart)
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.backbtn),
//                contentDescription = "Back",
//                tint = Color.Black,
//                modifier = Modifier.size(screenWidth * 0.09f)
//            )
//        }
//
//        textPicData?.let { data ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 40.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Spacer(modifier = Modifier.height(screenHeight * 0.086f))
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 8.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    StepProgressBar(currentStep = 0) // فقط یه مرحله داریم
//                }
//                Spacer(modifier = Modifier.height(20.dp))
//
//                AsyncImage(
//                    model = data.imageUrl,
//                    contentDescription = "Game Image",
//                    modifier = Modifier
//                        .size(screenWidth * 0.8f)
//                        .clip(RoundedCornerShape(10.dp)),
//                    contentScale = ContentScale.Crop
//                )
//                Spacer(modifier = Modifier.height(30.dp))
//
//                Text(
//                    text = "کلمات درست رو انتخاب کن:",
//                    fontSize = 14.sp,
//                    fontFamily = iranSans,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.Center
//                )
//                Spacer(modifier = Modifier.height(20.dp))
//
//                data.words.forEach { word ->
//                    val isSelected = selectedWords.contains(word.word)
//                    val backgroundColor = when {
//                        !showResultBox && isSelected -> Color(0xFF4D869C)
//                        showResultBox && word.isCorrect && isSelected -> Color(0xFF14CB00)
//                        showResultBox && !word.isCorrect && isSelected -> Color(0xFFFF3B3B)
//                        else -> Color(0xFFCDE8E5)
//                    }
//                    val textColor = if (showResultBox && word.isCorrect && isSelected) Color.White else Color.Black
//
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth(0.7f)
//                            .padding(vertical = 4.dp)
//                            .clip(RoundedCornerShape(10.dp))
//                            .background(backgroundColor)
//                            .clickable(enabled = !showResultBox) {
//                                selectedWords = if (isSelected) {
//                                    selectedWords - word.word
//                                } else {
//                                    selectedWords + word.word
//                                }
//                            }
//                            .padding(vertical = 10.dp, horizontal = 14.dp)
//                    ) {
//                        Text(
//                            text = word.word,
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.Medium,
//                            color = textColor
//                        )
//                    }
//                }
//
//                Text(
//                    text = "سوال 1 از 1",
//                    fontSize = 12.sp,
//                    fontFamily = iranSans
//                )
//                Spacer(modifier = Modifier.weight(1f))
//            }
//
//            if (showResultBox) {
//                TextPicResultBox(
//                    correctCount = data.words.count { it.isCorrect && selectedWords.contains(it.word) },
//                    wrongCount = data.words.count { !it.isCorrect && selectedWords.contains(it.word) },
//                    onNext = {
//                        selectedWords = emptySet()
//                        showResultBox = false
//                        showFinalResultDialog = true // مستقیم به نتیجه برو
//                    },
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .padding(bottom = 0.dp)
//                )
//            }
//        }
//
//        Button(
//            onClick = {
//                if (selectedWords.isNotEmpty() && !showResultBox) {
//                    val correct = textPicData?.words?.count { it.isCorrect && selectedWords.contains(it.word) } ?: 0
//                    val wrong = textPicData?.words?.count { !it.isCorrect && selectedWords.contains(it.word) } ?: 0
//                    viewModel.incrementCorrect(correct)
//                    viewModel.incrementWrong(wrong)
//                    showResultBox = true
//                }
//            },
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(end = 30.dp, bottom = 180.dp)
//                .width(screenWidth * 0.20f)
//                .height(40.dp),
//            shape = RoundedCornerShape(10.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFF4D869C),
//                contentColor = Color.White
//            )
//        ) {
//            Text("تایید", fontFamily = iranSans, fontWeight = FontWeight.Bold)
//        }
//    }
//
//    if (showFinalResultDialog) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Black.copy(alpha = 0.5f))
//                .clickable(enabled = true, onClick = {}),
//            contentAlignment = Alignment.Center
//        ) {
//            Surface(
//                shape = RoundedCornerShape(16.dp),
//                color = Color.White,
//                modifier = Modifier
//                    .width(300.dp)
//                    .wrapContentHeight()
//                    .padding(16.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(20.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = ":نتیجه آزمون",
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold,
//                        fontFamily = iranSans,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .wrapContentWidth(Alignment.End),
//                        textAlign = TextAlign.Center
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = "تعداد کل سوالات: 1",
//                        fontSize = 12.sp,
//                        fontFamily = iranSans,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.Black,
//                        textAlign = TextAlign.Right,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .wrapContentWidth(Alignment.End)
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = "تعداد جواب‌های درست: $correctCount",
//                        fontSize = 12.sp,
//                        fontFamily = iranSans,
//                        color = Color.Black,
//                        textAlign = TextAlign.Right,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .wrapContentWidth(Alignment.End)
//                    )
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    Text(
//                        text = "تعداد جواب‌های غلط: $wrongCount",
//                        fontSize = 12.sp,
//                        fontFamily = iranSans,
//                        color = Color.Black,
//                        textAlign = TextAlign.Right,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .wrapContentWidth(Alignment.End)
//                    )
//                    Spacer(modifier = Modifier.height(20.dp))
//
//                    Box(
//                        modifier = Modifier
//                            .padding(4.dp)
//                            .evenShadow(radius = 25f, cornerRadius = 20f)
//                            .clip(RoundedCornerShape(10.dp))
//                            .background(Color(0xFF7AB2B2))
//                            .height(45.dp)
//                            .fillMaxWidth(0.4f)
//                            .clickable {
//                                showFinalResultDialog = false
//                                navController.navigate("grammar_page")
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text("تأیید", color = Color.White, fontFamily = iranSans)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun TextPicResultBox(
//    correctCount: Int,
//    wrongCount: Int,
//    onNext: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
//
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(screenHeight * 0.14f)
//            .background(
//                brush = Brush.horizontalGradient(
//                    colors = listOf(Color(0xFF4D869C), Color(0xFFCDE8E5))
//                ),
//                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
//            )
//            .padding(horizontal = 20.dp, vertical = 10.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .offset(y = 8.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = "درست: $correctCount | غلط: $wrongCount",
//                    fontFamily = iranSans,
//                    fontWeight = FontWeight.Medium,
//                    color = Color.Black,
//                    textAlign = TextAlign.Left
//                )
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Box(
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .offset(y = (-14).dp)
//                    .width(90.dp)
//                    .height(30.dp)
//                    .clip(RoundedCornerShape(10.dp))
//                    .background(Color(0xFF4D869C))
//                    .clickable { onNext() },
//                contentAlignment = Alignment.Center
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = "تأیید",
//                        fontFamily = iranSans,
//                        color = Color.White,
//                        fontSize = 12.sp
//                    )
//                }
//            }
//        }
//    }
//}