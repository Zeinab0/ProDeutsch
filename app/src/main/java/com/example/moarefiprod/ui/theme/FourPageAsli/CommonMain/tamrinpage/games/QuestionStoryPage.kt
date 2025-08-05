package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.R
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBar

@Composable
fun QuestionStoryPage(
    onGameFinished: (isCorrect: Boolean, correctAnswer: String?) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var userInput by remember { mutableStateOf("") }
    var showResultBox by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }

    val correctAnswer = "Der Mann ging über die Brücke."

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {

        // 🔙 دکمه برگشت
        IconButton(
            onClick = { /*TODO: Implement back navigation*/ },
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

        // 📝 نوار پیشرفت مراحل
        StepProgressBar(
            currentStep = 4,
            totalSteps = 6,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.1f)
                .fillMaxWidth(0.8f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.15f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "سوالات مرتبط با داستان:",
                fontFamily = iranSans,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Right,
                style = TextStyle(textDirection = TextDirection.Rtl),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 📝 متن سوال
            Text(
                text = buildAnnotatedString {
                    append("۵. جمله‌ی زیر را به زمان گذشته ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                        append("(Präteritum) ")
                    }
                    append("تبدیل کنید:")
                },
                fontFamily = iranSans,
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Right,
                style = TextStyle(textDirection = TextDirection.Rtl),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 📝 جمله مورد نظر
            Text(
                text = "Der Mann geht über die Brücke.",
                fontFamily = iranSans,
                fontSize = 15.sp,
                color = Color.Black,
                textAlign = TextAlign.Right,
                style = TextStyle(textDirection = TextDirection.Rtl),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

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
                        fontSize = 14.sp,
                        color = Color.Black,
                        // تغییر: راست‌چین کردن متن تایپ شده
                        textAlign = TextAlign.Start,
                        textDirection = TextDirection.Ltr
                    ),
                    modifier = Modifier.fillMaxSize(),
                    readOnly = showResultBox, // غیرفعال کردن TextField وقتی نتیجه نمایش داده می‌شود
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (userInput.isEmpty() && !showResultBox) {
                                Text(
                                    text = "اینجا بنویسید...",
                                    fontFamily = iranSans,
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    // تغییر: راست‌چین کردن placeholder
                                    textAlign = TextAlign.Right,
                                    style = TextStyle(textDirection = TextDirection.Rtl),
                                    modifier = Modifier.fillMaxWidth()
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
//            onClick = {
//                val normalizedUserInput = normalizeText(userInput)
//                val normalizedCorrectAnswer = normalizeText(correctAnswer)
//
//                isCorrect = normalizedUserInput == normalizedCorrectAnswer
//                showResultBox = true
//            }
            onClick = {
                val normalizedUserInput = normalizeText(userInput)
                val normalizedCorrectAnswer = normalizeText(correctAnswer)

                // این خط را اضافه کنید
                println("User Input Normalized: $normalizedUserInput")
                println("Correct Answer Normalized: $normalizedCorrectAnswer")

                isCorrect = normalizedUserInput == normalizedCorrectAnswer
                showResultBox = true
            }
            ,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 40.dp, bottom = 180.dp)
                .width(screenWidth * 0.22f)
                .height(42.dp),
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
                translation = "مرد از روی پل گذشت.",
                onNext = {
                    onGameFinished(isCorrect ?: false, correctAnswer)
                    userInput = ""
                    showResultBox = false
                    isCorrect = null
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 0.dp)
            )
        }
    }
}

@Composable
fun QuestionResultBox(
    isCorrect: Boolean?,
    correctSentence: String,
    userSentence: String,
    translation: String,
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
                    listOf(Color(0xFF4DA4A4), Color(0xFFBFEAE8))
                ),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // ===== جمله اول + ترجمه فارسی روبرویش =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // ✅ جمله اول (درست یا غلط)
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Start
                ) {
                    val iconRes = if (isCorrect == false) R.drawable.cross else R.drawable.tik
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isCorrect == false) userSentence else correctSentence,
                        fontFamily = iranSans,
                        color = Color.Black,
                        fontSize = 14.sp,
                        textAlign = TextAlign.End
                    )
                }

                // 📘 ترجمه فارسی
                Text(
                    text = translation,
                    fontFamily = iranSans,
                    color = Color.DarkGray,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.Top)
                )
            }

            // ✅ اگر غلط بود، جمله درست هم نمایش داده شود
            if (isCorrect == false) {
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.tik),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = correctSentence,
                        fontFamily = iranSans,
                        color = Color.Black,
                        fontSize = 14.sp,
                        textAlign = TextAlign.End
                    )
                }
            }

            // این بخش تغییر کرده است.
            // در هر دو حالت، یک Spacer با ارتفاع ثابت 10.dp قرار داده‌ایم.
            Spacer(modifier = Modifier.height(25.dp))

            // 🟢 دکمه "بریم بعدی"
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .width(98.dp)
                    .height(34.dp)
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

// برای نمایش پیش‌نمایش در محیط توسعه
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QuestionStoryPagePreview() {
    QuestionStoryPage(onGameFinished = { _, _ -> })
}