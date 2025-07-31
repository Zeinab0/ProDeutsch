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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBar
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören.evenShadow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TextPicPage(
    navController: NavController,
    courseId: String,
    lessonId: String,
    contentId: String,
    gameId: String,
    viewModel: GameViewModel = viewModel() // حالا از GameViewModel مشترک استفاده می‌کنیم
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val textPicData by viewModel.textPicData.collectAsState()
    val selectedWords by viewModel.textPicSelectedWords.collectAsState() // کلمات انتخاب شده از GameViewModel
    val correctCount by viewModel.textPicCorrectCount.collectAsState() // تعداد درست برای TextPic از GameViewModel
    val wrongCount by viewModel.textPicWrongCount.collectAsState()   // تعداد غلط برای TextPic از GameViewModel

    var showResultBox by remember { mutableStateOf(false) } // وضعیت نمایش جعبه نتیجه
    val scope = rememberCoroutineScope()

    LaunchedEffect(gameId) {
        Log.d("TextPicPage", "Loading game with courseId=$courseId, lessonId=$lessonId, contentId=$contentId, gameId=$gameId")
        viewModel.loadTextPicGame(courseId, lessonId, contentId, gameId) // بارگذاری بازی TextPic
        // ViewModel خودش در loadTextPicGame وضعیت TextPic را ریست می‌کند.
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

        textPicData?.let { data ->
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
                    StepProgressBar(currentStep = 2) // گام 2 برای TextPic
                }
                Spacer(modifier = Modifier.height(20.dp))

                AsyncImage(
                    model = data.imageUrl,
                    contentDescription = "Game Image",
                    modifier = Modifier
                        .size(screenWidth * 0.8f)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "کلمات درست رو انتخاب کن:",
                    fontSize = 14.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    data.words.forEach { word ->
                        val isSelected = selectedWords.contains(word.word) // از selectedWords ViewModel استفاده کنید
                        val backgroundColor = when {
                            showResultBox -> {
                                if (word.isCorrect) {
                                    if (isSelected) Color(0xFF56B096) // درست و انتخاب شده
                                    else Color(0xFFCDE8E5) // درست و انتخاب نشده
                                } else {
                                    if (isSelected) Color(0xFFFF3B3B) // غلط و انتخاب شده
                                    else Color(0xFFCDE8E5) // غلط و انتخاب نشده
                                }
                            }
                            isSelected -> Color(0xFF4D869C) // فقط انتخاب شده قبل از تایید
                            else -> Color(0xFFCDE8E5) // حالت عادی
                        }
                        val textColor = when {
                            showResultBox && (word.isCorrect || !word.isCorrect) && isSelected -> Color.White
                            isSelected -> Color.White
                            else -> Color.Black
                        }

                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(backgroundColor)
                                .clickable(enabled = !showResultBox) {
                                    viewModel.toggleTextPicWordSelection(word.word) // استفاده از متد ViewModel
                                }
                                .padding(vertical = 10.dp, horizontal = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = word.word,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = textColor,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            if (!showResultBox) {
                Button(
                    onClick = {
                        if (selectedWords.isNotEmpty()) {
                            viewModel.checkTextPicAnswers() // محاسبه امتیازات در ViewModel
                            showResultBox = true // نمایش جعبه نتیجه
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 30.dp, bottom = 30.dp)
                        .width(screenWidth * 0.20f)
                        .height(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4D869C),
                        contentColor = Color.White
                    ),
                    enabled = selectedWords.isNotEmpty()
                ) {
                    Text("تایید", fontFamily = iranSans, fontWeight = FontWeight.Bold)
                }
            }

            if (showResultBox) {
                TextPicResultBox(
                    correctCount = correctCount, // استفاده از correctCount از GameViewModel
                    wrongCount = wrongCount,     // استفاده از wrongCount از GameViewModel
                    onNext = {
                        scope.launch {
                            Log.d("TextPicNav", "TextPic game finished. Navigating to grammar_page.")
                            navController.popBackStack("grammar_page", inclusive = false)
                            viewModel.resetTextPicGame() // ریست کردن وضعیت TextPic در ViewModel
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 0.dp)
                )
            }
        } ?: run {
            Text(
                text = "داده‌ها بارگذاری نشد. لطفاً اتصال را بررسی کنید.",
                color = Color.Red,
                fontFamily = iranSans,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

// TextPicResultBox بدون تغییرات عمده، همچنان پارامترها را دریافت می‌کند
@Composable
fun TextPicResultBox(
    correctCount: Int,
    wrongCount: Int,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val allCorrect = wrongCount == 0

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(screenHeight * 0.14f)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF4D869C), Color(0xFFBFEAE8))
                ),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 8.dp),
        ) {
            if (allCorrect && correctCount > 0) {
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
                Text(
                    text = "تعداد درست: $correctCount    تعداد اشتباه: $wrongCount",
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                )
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