package com.example.moarefiprod.ui.theme.logofirst

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.moarefiprod.R
import kotlinx.coroutines.delay

@Composable
fun Firstlogopage(onNavigateToLogin: () -> Unit) {
    // ✅ بعد از ۱۰ ثانیه هدایت به صفحه ورود
    LaunchedEffect(Unit) {
        delay(3000L) // ⏳ ۱۰ ثانیه صبر کن
        onNavigateToLogin() // 🚀 انتقال به صفحه ورود
    }

    // ✅ گرفتن اندازه صفحه برای تنظیم مقیاس لوگو
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val logoHeight = (screenHeight * 0.12f) // ✅ کوچک‌تر کردن لوگو (۱۲٪ از ارتفاع صفحه)
    val logoWidth = logoHeight * 1.5f // ✅ نسبت عرض به ارتفاع برای جلوگیری از کشیدگی

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF90CECE)),
        contentAlignment = Alignment.Center // ✅ لوگو را دقیقاً وسط صفحه قرار می‌دهد
    ) {
        Image(
            painter = painterResource(id = R.drawable.logopd),
            contentDescription = "logo prodeutsch",
            modifier = Modifier
                .width(logoWidth) // ✅ تنظیم عرض لوگو
                .height(logoHeight) // ✅ تنظیم ارتفاع لوگو
        )
    }
}
