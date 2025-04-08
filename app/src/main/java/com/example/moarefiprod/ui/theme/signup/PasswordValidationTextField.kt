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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import java.util.regex.Pattern

@Composable
fun PasswordValidationTextField(
    password: String,
    confirmPassword: String,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit
){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var isPasswordValid by remember { mutableStateOf(true) }
    var doPasswordsMatch by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val focusedBorderColor = if (doPasswordsMatch) Color(0xFF4D869C) else Color.Red
    val unfocusedBorderColor = if (doPasswordsMatch) Color(0xBCBCBCBC) else Color.Red

    // ✅ الگوی بررسی رمز عبور: حداقل ۶ کاراکتر، شامل حداقل یک حرف و یک عدد
    val passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#\$%^&+=!]{6,}$")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally, // ✅ وسط‌چین کردن فیلدهای رمز عبور
        verticalArrangement = Arrangement.spacedBy(screenHeight * 0.001f)
    ) {
        // ✅ فیلد رمز عبور
        OutlinedTextField(
            value = password,
            onValueChange = {
                onPasswordChange(it)
                isPasswordValid = it.isEmpty() || passwordPattern.matcher(it).matches()
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
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = focusedBorderColor,
                unfocusedBorderColor = unfocusedBorderColor,
                cursorColor = Color(0xFF4D869C)
            )
            ,
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
                        modifier = Modifier.size(screenWidth * 0.055f) // ✅ تنظیم اندازه آیکون نسبت به صفحه
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.73f) // ✅ عرض متناسب با صفحه و وسط‌چین شده
                .height(screenHeight * 0.07f)
                .background(Color.White, RoundedCornerShape(screenWidth * 0.03f))

        )
        Spacer(modifier = Modifier.height(screenHeight * 0.001f))

        // ✅ متن "تکرار رمز عبور"
        Text(
            text = "تکرار رمز عبور",
            fontSize = (screenWidth * 0.04f).value.sp,
            fontFamily = iranSans,
            fontWeight = FontWeight.ExtraLight,
            modifier = Modifier.align(Alignment.Start).padding(start = screenWidth * 0.64f)
        )

        // ✅ فیلد تأیید رمز عبور
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                onConfirmPasswordChange(it)
                doPasswordsMatch = it.isEmpty() || it == password
            },
            placeholder = {
                Text(
                    text = "رمز عبور را تکرار کنید",
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = (screenWidth * 0.04f).value.sp,
                    color = Color(0xBCBCBCBC),
                    fontFamily = iranSans,
                    fontWeight = FontWeight.ExtraLight,
                )
            },
            shape = RoundedCornerShape(screenWidth * 0.03f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4D869C),
                unfocusedBorderColor = Color(0xFF90CECE),
                cursorColor = Color(0xFF4D869C),
            )
            ,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.lock),
                    tint = Color(0xFF4D869C),
                    contentDescription = "Confirm Password Icon",
                    modifier = Modifier.size(screenWidth * 0.055f) // ✅ تنظیم اندازه آیکون نسبت به صفحه
                )
            },
            leadingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        painter = painterResource(id = if (confirmPasswordVisible) R.drawable.eye_open else R.drawable.eye_closed),
                        tint = Color(0xFF4D869C),
                        contentDescription = "Toggle Confirm Password Visibility",
                        modifier = Modifier.size(screenWidth * 0.055f) // ✅ تنظیم اندازه آیکون نسبت به صفحه
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.73f) // ✅ عرض متناسب با صفحه و وسط‌چین شده
                .height(screenHeight * 0.07f)
                .background(Color.White, RoundedCornerShape(screenWidth * 0.03f))
        )
    }
}
