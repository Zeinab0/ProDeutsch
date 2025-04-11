package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.moarefiprod.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans


@Composable
fun HeaderSection(onMenuClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = screenWidth * 0.06f, vertical = screenHeight * 0.015f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // ✅ کادر پروفایل
            Box(
                modifier = Modifier
                    .size(screenWidth * 0.13f) // ✅ اندازه پویا برای تطابق با دستگاه‌های مختلف
                    .border(
                        width = screenWidth * 0.004f, // ✅ عرض بوردر بر اساس سایز صفحه
                        color = Color(0xff4D869C),
                        shape = RoundedCornerShape(screenWidth * 0.03f) // ✅ گوشه‌های گرد متناسب با صفحه
                    )
                    .clip(RoundedCornerShape(screenWidth * 0.03f)), // ✅ گرد کردن گوشه‌ها
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_image), // ✅ عکس پروفایل
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(screenWidth * 0.1f) // ✅ اندازه عکس پویا
                        .background(Color(0xffDAF8F5), RoundedCornerShape(screenWidth * 0.02f))
                )
            }

            Spacer(modifier = Modifier.width(screenWidth * 0.02f)) // ✅ فاصله متناسب با عرض صفحه

            // ✅ نام کاربر
            Column {
                Text(
                    text = "Hallo",
                    fontSize = (screenWidth * 0.04f).value.sp, // ✅ متن مقیاس‌پذیر
                    color = Color.Black,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.ExtraLight,
                )
                Text(
                    text = "liebe Zeinab",
                    fontSize = (screenWidth * 0.04f).value.sp, // ✅ متن مقیاس‌پذیر
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                )
            }
        }

        // ✅ دکمه منو
        IconButton(
            onClick =  onMenuClick ,
            modifier = Modifier.size(screenWidth * 0.1f) // ✅ اندازه متناسب با صفحه
        ) {
            Icon(
                painter = painterResource(id = R.drawable.menu_button),
                contentDescription = "Menu",
                modifier = Modifier.size(screenWidth * 0.1f) // ✅ مقیاس متناسب برای دکمه منو
            )
        }
    }
}