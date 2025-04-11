package com.example.moarefiprod.ui.theme.Login

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans

@Composable
fun ForgotPasswordText(onForgotPasswordClick: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "FORGOT_PASSWORD", annotation = "forgot_password")
        withStyle(
            style = SpanStyle(
                color = Color.Blue, // ✅ رنگ آبی برای لینک
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline // ✅ اضافه کردن زیرخط
            )
        ) {
            append("فراموشی رمز عبور؟")
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = iranSans,
            fontWeight = FontWeight.ExtraLight,
            color = Color(0xFF78746D)
        ),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "FORGOT_PASSWORD", start = offset, end = offset)
                .firstOrNull()?.let {
                    onForgotPasswordClick() // ✅ اجرای عملیات بازنشانی رمز عبور
                }
        }
    )
}
