package com.example.moarefiprod.ui.theme.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import java.util.regex.Pattern

@Composable
fun EmailValidationTextField(email: String,
                             onEmailChange: (String) -> Unit)
{
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var isEmailValid by remember { mutableStateOf(true) }

    val emailPattern = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}$"
    )
    val focusedBorder = when {
        email.isEmpty() -> Color(0xFF4D869C)
        isEmailValid -> Color(0xFF4D869C)
        else -> Color.Red
    }

    val unfocusedBorder = when {
        email.isEmpty() -> Color(0xBCBCBCBC)
        isEmailValid -> Color(0xBCBCBCBC)
        else -> Color.Red
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally, // ✅ وسط‌چین کردن فیلد ایمیل
        verticalArrangement = Arrangement.spacedBy(screenHeight * 0.005f)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = {
                onEmailChange(it)
                isEmailValid = it.isEmpty() || emailPattern.matcher(it).matches()
            },
            placeholder = {
                Text(
                    text = "ایمیل خود را وارد کنید",
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = (screenWidth * 0.04f).value.sp, // ✅ اندازه فونت پویا
                    color = Color(0xBCBCBCBC),
                    fontFamily = iranSans,
                    fontWeight = FontWeight.ExtraLight,
                )
            },
            shape = RoundedCornerShape(screenWidth * 0.03f),

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = focusedBorder,
                unfocusedBorderColor = unfocusedBorder,
                cursorColor = Color(0xFF4D869C)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.mail),
                    tint = when {
                        email.isEmpty() -> Color(0xFF4D869C)
                        isEmailValid -> Color(0xFF4D869C)
                        else -> Color.Red
                    },
                    contentDescription = "Email",
                    modifier = Modifier.size(screenWidth * 0.055f) // ✅ تنظیم اندازه آیکون نسبت به صفحه
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.73f) // ✅ عرض متناسب با صفحه و وسط‌چین شده
                .height(screenHeight * 0.07f)
                .background(Color.White, RoundedCornerShape(screenWidth * 0.03f))
//                .align(Alignment.CenterHorizontally) // ✅ وسط‌چین کردن فیلد ایمیل
        )
    }
}
