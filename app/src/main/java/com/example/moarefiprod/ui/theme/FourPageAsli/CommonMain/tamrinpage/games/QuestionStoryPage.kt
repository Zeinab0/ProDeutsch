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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.R
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ExitConfirmationDialog
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBarWithExit
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game.GrammerGameViewModel
import kotlinx.coroutines.delay


@Composable
fun QuestionStoryPage(
    navController: NavController,
    courseId: String,
    lessonId: String,
    contentId: String,
    gameId: String,
    gameIndex: Int,
    totalGames: Int,
    viewModel: BaseGameViewModel,
    onGameFinished: (isCorrect: Boolean, correctAnswer: String?) -> Unit
){
    val grammarViewModel = viewModel as? GrammerGameViewModel ?: return
    val pathType = if (lessonId.isNotEmpty() && contentId.isNotEmpty()) {
        GrammerGameViewModel.GamePathType.COURSE
    } else {
        GrammerGameViewModel.GamePathType.GRAMMAR_TOPIC
    }

    LaunchedEffect(gameId) {
        grammarViewModel.loadQuestionStoryGame(
            pathType = pathType,
            courseId = courseId,
            lessonId = lessonId,
            contentId = contentId,
            gameId = gameId
        )
    }

    val timeInSeconds = viewModel.totalTimeInSeconds.collectAsState().value
    val questionStoryData = (viewModel as GrammerGameViewModel).questionStoryGameState.collectAsState().value
    val questionText = questionStoryData?.questionText ?: ""
    val correctAnswer = questionStoryData?.correctAnswer ?: ""
    val translation = questionStoryData?.translation ?: ""

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    var userInput by remember { mutableStateOf("") }
    var showResultBox by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        var showExitDialog by remember { mutableStateOf(false) }

        val returnRoute = if (pathType == GrammerGameViewModel.GamePathType.COURSE) {
            "darsDetails/$courseId/$lessonId"
        } else {
            "grammar_page"
        }

        StepProgressBarWithExit(
            navController = navController,
            currentStep = gameIndex,
            totalSteps = totalGames,
            returnRoute = returnRoute,
            onRequestExit = { showExitDialog = true },
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.15f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.07f))

            // 📝 متن سوال
            Text(
                text = buildAnnotatedString {
                    append("جمله‌ی زیر را به زمان گذشته ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                        append("(Präteritum) ")
                    }
                    append("تبدیل کنید:")
                },
                fontFamily = iranSans,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Right,
                style = TextStyle(textDirection = TextDirection.Rtl),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.012f))

            // 📝 جمله مورد نظر
            Text(
                text = questionText,
                fontFamily = iranSans,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.07f))

            // 📝 کادر ورودی متن
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.2f)
                    .shadow(8.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFCDE8E5))
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                // اینجا از BasicTextField استفاده می‌شود که خودش placeholder را نمایش می‌دهد.
                BasicTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    textStyle = TextStyle(
                        fontFamily = iranSans,
                        fontSize = 15.sp,
                        color = Color(0xFF4D869C),
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.fillMaxSize(),
                    readOnly = showResultBox, // غیرفعال کردن TextField وقتی نتیجه نمایش داده می‌شود
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (userInput.isEmpty() && !showResultBox) {
                                Text(
                                    text = "اینجا بنویسید",
                                    fontFamily = iranSans,
                                    fontSize = 14.sp,
                                    color = Color(0xFF4D869C),
                                    // تغییر: راست‌چین کردن placeholder
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Right,
                                    style = TextStyle(textDirection = TextDirection.Rtl),
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }

        // --- دکمه تایید ---
            Button(
                onClick = {
                    val normalizedUserInput = normalizeText(userInput)
                    val normalizedCorrectAnswer = normalizeText(correctAnswer)

                    println("User Input Normalized: $normalizedUserInput")
                    println("Correct Answer Normalized: $normalizedCorrectAnswer")

                    isCorrect = normalizedUserInput == normalizedCorrectAnswer
                    showResultBox = true

                    viewModel.recordAnswer(isCorrect == true)
                    viewModel.recordMemoryGameResult(
                        correct = if (isCorrect == true) 1 else 0,
                        wrong = if (isCorrect == false) 1 else 0,
                        timeInSeconds = timeInSeconds
                    )
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
                    text = "تایید",
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

        // 🖼️ باکس نتیجه در پایین صفحه
        if (showResultBox) {
            QuestionResultBox(
                isCorrect = isCorrect,
                correctSentence = correctAnswer,
                userSentence = userInput,
                translation = questionStoryData?.translation ?: "",
                navController = navController,
                courseId = courseId,
                lessonId = lessonId,
                contentId = contentId,
                gameIndex = gameIndex,
                isLastGame = gameIndex == totalGames - 1,
                returnRoute = returnRoute, // ✅ مسیر برگشت
                onNext = {
                    val currentRoute = "GameHost/$courseId/$lessonId/$contentId/$gameIndex"
                    val nextRoute = "GameHost/$courseId/$lessonId/$contentId/${gameIndex + 1}"
                    navController.navigate(nextRoute) {
                        popUpTo(currentRoute) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 0.dp)
            )


        }


        if (showExitDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)) // سایه کامل
                    .zIndex(999f), // حتماً اضافه بشه
                contentAlignment = Alignment.Center
            ) {
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
    }
}

@Composable
fun QuestionResultBox(
    isCorrect: Boolean?,
    correctSentence: String,
    userSentence: String,
    translation: String,
    navController: NavController,
    courseId: String,
    lessonId: String,
    contentId: String,
    gameIndex: Int,
    isLastGame: Boolean,
    returnRoute: String,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(screenHeight * 0.19f)
            .padding(horizontal = 20.dp, vertical = 30.dp)
            .background(color = Color(0xFF90CECE), RoundedCornerShape(25.dp))
            .padding(horizontal = 15.dp, vertical = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 8.dp)
        ) {
            // 📘 ردیف بالا: ترجمه و جمله (user/correct)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // ✅ جمله کاربر یا جمله صحیح (بسته به درست بودن)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    val iconRes = if (isCorrect == false) R.drawable.cross else R.drawable.tik
                    val iconTint = if (isCorrect == false) Color(0xFFFF3B3B) else Color(0xFF14CB00)

                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isCorrect == false) userSentence else correctSentence,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Left
                    )
                }

                // ✅ ترجمه فارسی
                Text(
                    text = translation,
                    fontFamily = iranSans,
                    fontSize = 13.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
            }

            // ✅ اگر کاربر اشتباه کرده بود، جمله صحیح رو هم نشون بده
            if (isCorrect == false) {
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
                        text = correctSentence,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Left
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // ✅ دکمه "بریم بعدی / تمام" پایین باکس سمت راست
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = (-14).dp)
                .width(90.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF4D869C))
                .clickable {
                    if (isLastGame) {
                        navController.navigate(returnRoute) {
                            popUpTo("home") { inclusive = false }
                        }
                    } else {
                        onNext()
                    }
                },
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

fun normalizeText(input: String): String {
    return input
        .trim()
        .lowercase()
        .replace("ä", "a")
        .replace("ö", "o")
        .replace("ü", "u")
        .replace("ß", "ss")
        .replace(Regex("\\s+"), " ")
        // این خط جدید است: حذف علائم نگارشی از انتهای رشته با Regex
        .replace(Regex("[\\p{Punct}]*$"), "") // این الگو تمام علائم نگارشی در انتهای رشته را حذف می‌کند.
}

            // این بخش تغییر کرده است.
            // در هر دو حالت، یک Spacer با ارتفاع ثابت 10.dp قرار داده‌ایم.
//            Spacer(modifier = Modifier.height(25.dp))
//
//            if (courseId.isNotBlank() && lessonId.isNotBlank() && contentId.isNotBlank()) {
//                Box(
//                    modifier = Modifier
//                        .align(Alignment.End)
//                        .width(98.dp)
//                        .height(34.dp)
//                        .clip(RoundedCornerShape(10.dp))
//                        .background(Color(0xFF4D869C))
//                        .clickable {
//                            val currentRoute = "GameHost/$courseId/$lessonId/$contentId/$gameIndex"
//                            val nextRoute = "GameHost/$courseId/$lessonId/$contentId/${gameIndex + 1}"
//
//                            navController.navigate(nextRoute) {
//                                popUpTo(currentRoute) { inclusive = true }
//                            }
//                        },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Center
//                    ) {
////                        Text(
////                            text = "بریم بعدی",
////                            fontFamily = iranSans,
////                            color = Color.White,
////                            fontSize = 14.sp
////                        )
////                        Spacer(modifier = Modifier.width(4.dp))
////                        Icon(
////                            painter = painterResource(id = R.drawable.nextbtn),
////                            contentDescription = null,
////                            tint = Color.White,
////                            modifier = Modifier.size(16.dp)
////                        )
//                    }
//                }
//            } else {
//                // اختیاری: نمایش پیام خطا یا غیرفعال کردن دکمه
//                Log.e("Navigation", "❌ مسیر ناوبری ناقص است: courseId or lessonId or contentId is empty.")
//            }
//
//        }
//    }
//}

