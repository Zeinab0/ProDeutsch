package com.example.moarefiprod.ui.theme.Login
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
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
fun ClickableRegisterTextL(onNavigateToRegister: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val annotatedString: AnnotatedString = buildAnnotatedString {
        append("حساب کاربری ندارید؟ ")
        pushStringAnnotation(tag = "REGISTER", annotation = "register")
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4D869C),
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("اینجا ثبت نام کنید")
        }
        pop()
    }

    Text(
        text = annotatedString,
        modifier = Modifier.clickable { onNavigateToRegister() },
        style = TextStyle(
            fontSize = (screenWidth * 0.035f).value.sp,
            fontFamily = iranSans,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF78746D)
        )
    )
}
