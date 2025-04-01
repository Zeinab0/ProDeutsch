package com.example.moarefiprod.ui.theme.Login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordValidationTextField() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordValid by remember { mutableStateOf(true) }
    var doPasswordsMatch by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }

    // ✅ الگوی بررسی رمز عبور: حداقل ۶ کاراکتر، شامل حداقل یک حرف و یک عدد
    val passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#\$%^&+=!]{6,}$")

    Column(
        modifier = Modifier.padding(screenWidth * 0.015f),
        verticalArrangement = Arrangement.spacedBy(screenHeight * 0.005f) // فاصله بین فیلدها
    ) {
        // ✅ فیلد رمز عبور
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordValid = it.isEmpty() || passwordPattern.matcher(it).matches()
                doPasswordsMatch = confirmPassword.isEmpty() || confirmPassword == it // بررسی مطابقت رمزها
            },
            placeholder = {
                Text(
                    text = "رمز عبور را وارد کنید",
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = (screenWidth * 0.04f).value.sp, // ✅ اندازه فونت پویا
                    color = Color(0xBCBCBCBC),
                    fontFamily = iranSans,
                    fontWeight = FontWeight.ExtraLight,
                )
            },
            shape = RoundedCornerShape(screenWidth * 0.03f),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = when {
                    password.isEmpty() -> Color(0xFF4D869C)
                    isPasswordValid -> Color(0xFF4D869C)
                    else -> Color.Red
                },
                unfocusedBorderColor = when {
                    password.isEmpty() -> Color(0xBCBCBCBC)
                    isPasswordValid -> Color(0xBCBCBCBC)
                    else -> Color.Red
                },
                cursorColor = Color(0xFF4D869C)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.lock),
                    tint = Color(0xFF4D869C),
                    contentDescription = "Password Icon",
                    modifier = Modifier.size(screenWidth * 0.055f) // ✅ تنظیم اندازه آیکون نسبت به صفحه
                )
            },
            leadingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = if (passwordVisible) R.drawable.eye_open else R.drawable.eye_closed),
                        tint = Color(0xFF4D869C),
                        contentDescription = "Toggle Password Visibility",
                        modifier = Modifier.size(screenWidth * 0.06f) // ✅ تنظیم اندازه آیکون نسبت به صفحه
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.75f) // ✅ تنظیم عرض فیلد متناسب با صفحه
                .height(screenHeight * 0.07f)
                .background(Color.White, RoundedCornerShape(screenWidth * 0.03f))
        )
    }
}
