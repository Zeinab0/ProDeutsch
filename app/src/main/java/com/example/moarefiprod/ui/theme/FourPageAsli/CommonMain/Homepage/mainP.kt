package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.Homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans

@Composable
fun mainpage(query: String = "") {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val q = remember(query) { query.trim() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = screenWidth * 0.05f, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (q.isEmpty())
                "به‌زودی این بخش تکمیل می‌شود"
            else
                "نتیجه‌ای برای «$q» در صفحهٔ اصلی نداریم",
            fontFamily = iranSans,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF4D869C)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "tip: از نوار جستجو در تب‌های دیگر (دوره‌ها، فلش‌کارت‌ها…) استفاده کن",
            fontFamily = iranSans,
            fontSize = 10.sp,
            color = Color(0xFF90CECE)
        )
    }
}


