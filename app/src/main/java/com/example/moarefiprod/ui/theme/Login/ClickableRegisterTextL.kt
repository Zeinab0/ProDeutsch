import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
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
fun ClickableRegisterTextL(onNavigateToRegister: () -> Unit) {
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
            append("حساب کاربری ندارید؟ ")
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
            append("اینجا ثبت نام کنید")
        }
        pop()
    }

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
                    onNavigateToRegister() // ✅ مقدار صحیح فراخوانی شد
                }
        }
    )
}
