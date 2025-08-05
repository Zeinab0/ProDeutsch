package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBarWithExit
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp



@Composable
fun VacancyPage(
    onGameFinished: (isCorrect: Boolean, correctAnswer: String?) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    var showResultBox by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var showCompletedSentence by remember { mutableStateOf(false) }

    val sentence = "Ich lerne ____ ,weil es eine schöne Sprache ist."
    val correctAnswer = "Deutsch"
    val sentenceParts = sentence.split("____")

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔙 دکمه برگشت
        IconButton(
            onClick = { /*TODO*/ },
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

        // 📝 StepProgressBar
//        StepProgressBar(currentStep = 3, totalSteps = 6, modifier = Modifier
//            .align(Alignment.TopCenter)
//            .padding(top = screenHeight * 0.1f))

        // 📝 Row حاوی آیکون مداد و جمله
        // این Row در بالای صفحه ثابت می‌ماند
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.22f, start = 24.dp, end = 24.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pen),
                    contentDescription = "pen",
                    modifier = Modifier
                        .size(26.dp)
                        .align(Alignment.Top)
                    // .offset(y = (-10).dp)
                )
            }

            Text(
                text = if (showCompletedSentence) {
                    buildAnnotatedString {
                        append(sentenceParts.getOrNull(0) ?: "")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = if (isCorrect == true) Color.Green else Color.Red)) {
                            append(userInput.text)
                        }
                        append(sentenceParts.getOrNull(1) ?: "")
                    }
                } else {
                    buildAnnotatedString {
                        append(sentenceParts.getOrNull(0) ?: "")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Gray)) {
                            append("____")
                        }
                        append(sentenceParts.getOrNull(1) ?: "")
                    }
                },
                fontSize = 15.sp,
                fontFamily = iranSans,
                softWrap = true,
                maxLines = Int.MAX_VALUE,
                modifier = Modifier
                    .padding(start = 36.dp) // برای اینکه با آیکون تداخل نداشته باشه
                    .align(Alignment.TopStart)
            )
        }


        // 📝 Column برای فیلد ورودی و دکمه تایید
        // این Column در بخش میانی و پایین صفحه قرار می‌گیرد
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = screenHeight * 0.45f, bottom = 100.dp), // تنظیم موقعیت پایین‌تر
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!showCompletedSentence) {
                BasicTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    modifier = Modifier
                        .padding(horizontal = 40.dp)
                        .fillMaxWidth()
                        .background(Color(0xFFEFEFEF), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (userInput.text.isEmpty()) {
                                Text(
                                    text = "کلمه را اینجا وارد کنید",
                                    fontSize = 15.sp,
                                    fontFamily = iranSans,
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // 🖼️ باکس نتیجه در پایین صفحه
        if (showResultBox) {
            WinnerBoxVacancy(
                isCorrect = isCorrect,
                correctSentence = (sentenceParts.getOrNull(0) ?: "") + " " + correctAnswer + " " + (sentenceParts.getOrNull(1) ?: ""),
                userSentence = (sentenceParts.getOrNull(0) ?: "") + " " + userInput.text + " " + (sentenceParts.getOrNull(1) ?: ""),
                onNext = {
                    onGameFinished(isCorrect ?: false, correctAnswer)
                    userInput = TextFieldValue("")
                    showResultBox = false
                    isCorrect = null
                    showCompletedSentence = false
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 0.dp)
            )
        }

        // --- دکمه تایید ---
        val buttonWidth = (screenWidth * 0.22f)
        val buttonHeight = 42.dp
        val paddingRight = 44.dp
        val paddingBottom = if (showResultBox) (screenHeight * 0.14f) + 18.dp else 180.dp
        val calculatedOffsetX = screenWidth - buttonWidth - paddingRight
        val calculatedOffsetY = screenHeight - buttonHeight - paddingBottom

        if (!showCompletedSentence) {
            ConfirmButton(
                onClick = {
                    isCorrect = userInput.text.trim().equals(correctAnswer, ignoreCase = true)
                    showResultBox = true
                    showCompletedSentence = true
                },
                xOffset = calculatedOffsetX,
                yOffset = calculatedOffsetY
            )
        }
        // --- پایان بخش دکمه تایید ---
    }
}

@Composable
fun WinnerBoxVacancy(
    isCorrect: Boolean?,
    correctSentence: String,
    userSentence: String,
    translation: String = "من آلمانی یاد می‌گیرم چون زبان زیبایی است.",
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(screenHeight * if (isCorrect == true) 0.14f else 0.22f)
            .background(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFF4DA4A4), Color(0xFFBFEAE8))
                ),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            if (isCorrect == false) {
                // 🔴 جمله غلط
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cross),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = userSentence,
                        fontFamily = iranSans,
                        color = Color.Black,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // ✅ جمله درست
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
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
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

            } else if (isCorrect == true) {
                // ✅ فقط جمله درست
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
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
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            // 📘 ترجمه جمله
            Text(
                text = translation,
                fontFamily = iranSans,
                color = Color.DarkGray,
                fontSize = 13.sp,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🟢 دکمه بریم بعدی
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

@Composable
fun ConfirmButton(
    onClick: () -> Unit,
    xOffset: Dp,
    yOffset: Dp
) {
    Box(
        modifier = Modifier
            .offset(x = xOffset, y = yOffset)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF4D869C))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = "تایید",
            fontFamily = iranSans,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}
