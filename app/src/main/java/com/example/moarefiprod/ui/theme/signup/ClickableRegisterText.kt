package com.example.moarefiprod.ui.theme.signup


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans

@Composable
fun ClickableRegisterText(onNavigateToLogin: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = (screenWidth * 0.035f).value.sp, // ✅ اندازه فونت بر اساس عرض صفحه
                fontFamily = iranSans,
                fontWeight = FontWeight.ExtraLight,
                color = Color(0xFF78746D) // ✅ رنگ کلی متن
            )
        ) {
            append("از قبل حساب کاربری دارید؟ ")
        }

        // ✅ ایجاد بخش قابل کلیک
        pushStringAnnotation(tag = "REGISTER", annotation = "register")
        withStyle(
            style = SpanStyle(
                fontSize = (screenWidth * 0.035f).value.sp, // ✅ اندازه فونت پویا
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold, // ✅ ضخامت بیشتر برای بخش کلیک‌پذیر
                color = Color(0xFF4D869C), // ✅ رنگ لینک
                textDecoration = TextDecoration.Underline // ✅ خط زیر متن
            )
        ) {
            append("اینجا وارد شوید")
        }
        pop()
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center // ✅ وسط‌چین کردن کل متن
    ) {
        ClickableText(
            text = annotatedString,
            style = TextStyle(
                fontSize = (screenWidth * 0.035f).value.sp, // ✅ اندازه فونت بر اساس عرض صفحه
                fontFamily = iranSans,
                fontWeight = FontWeight.ExtraLight,
                color = Color(0xFF78746D)
            ),
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "REGISTER", start = offset, end = offset)
                    .firstOrNull()?.let {
                        onNavigateToLogin() // ✅ مقدار صحیح فراخوانی شد
                    }
            }
        )
    }
}
