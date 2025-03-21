package com.example.moarefiprod.ui.theme.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.moarefiprod.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    var searchText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = {
            Text(
                text = ":جستجو دوره",
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth(),
                fontSize = (screenWidth * 0.04f).value.sp, // ✅ اندازه فونت پویا
                color = Color(0xFF90CECE),
                fontFamily = iranSans,
                fontWeight = FontWeight.ExtraLight,
            )
        },
        textStyle = TextStyle(
            fontSize = (screenWidth * 0.045f).value.sp, // ✅ متن کمی بزرگ‌تر
            fontFamily = iranSans,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF4D869C),
            textAlign = TextAlign.Right, // ✅ نوشتن از سمت راست
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, // ✅ برای ورودی فارسی
            autoCorrect = true // ✅ فعال‌سازی تصحیح خودکار
        ),
        shape = RoundedCornerShape(screenWidth * 0.04f),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF4D869C), // ✅ رنگ آبی در حالت فوکوس
            unfocusedBorderColor = Color(0xFF90CECE), // ✅ رنگ خاکستری در حالت غیرفعال
            cursorColor = Color(0xFF4D869C), // ✅ تغییر رنگ نشانگر تایپ
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search),
                tint = Color(0xFF90CECE),
                contentDescription = "search Icon",
                modifier = Modifier.size(screenWidth * 0.055f) // ✅ تنظیم اندازه آیکون نسبت به صفحه
            )
        },
        modifier = Modifier
            .fillMaxWidth(1f) // ✅ عرض متناسب با صفحه و وسط‌چین شده
            .height(screenHeight * 0.065f)
            .padding(horizontal = screenWidth * 0.06f)
            .background(Color(0xffF1FFFF), RoundedCornerShape(screenWidth * 0.04f))
    )
}

