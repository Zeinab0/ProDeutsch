package com.example.moarefiprod.ui.theme.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
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
                fontSize = (screenWidth * 0.035f).value.sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.ExtraLight,
                color = Color(0xFF78746D)
            )
        ) {
            append("از قبل حساب کاربری دارید؟ ")
        }

        pushStringAnnotation(tag = "REGISTER", annotation = "register")
        withStyle(
            style = SpanStyle(
                fontSize = (screenWidth * 0.035f).value.sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4D869C),
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("اینجا وارد شوید")
        }
        pop()
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = annotatedString,
            modifier = Modifier.clickable {
                onNavigateToLogin()
            },
            style = TextStyle(
                fontSize = (screenWidth * 0.035f).value.sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.ExtraLight,
                color = Color(0xFF78746D)
            )
        )
    }
}
