package com.example.moarefiprod.ui.theme.Login

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.example.moarefiprod.iranSans

@Composable
fun ForgotPasswordText(onForgotPasswordClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "FORGOT_PASSWORD", annotation = "forgot_password")
        withStyle(
            style = SpanStyle(
                fontSize = (screenWidth * 0.035f).value.sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4D869C),
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("فراموشی رمز عبور؟")
        }
        pop()
    }

    Text(
        text = annotatedString,
        modifier = Modifier.clickable {
            onForgotPasswordClick()
        },
        style = TextStyle(
            fontSize = (screenWidth * 0.035f).value.sp,
            fontFamily = iranSans,
            fontWeight = FontWeight.ExtraLight,
            color = Color(0xFF78746D),
            textAlign = TextAlign.Start
        )
    )
}
