package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans

@Composable
fun LogoutScreen(navController: NavController) {
    val passwordFieldWidth = 280.dp
    val passwordFieldHeight = 53.dp
    val buttonWidth = 129.dp
    val buttonHeight = 53.dp
    val horizontalPadding = 24.dp
    val titleTopSpacing = 16.dp
    val underTitleSpacing = 8.dp
    val labelSpacing = 45.dp
    val confirmButtonSpacing = 80.dp
    val logoBottomPadding = 8.dp

    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val correctPassword = "123456"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = horizontalPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // دکمه برگشت با عکس دلخواه
            Image(
                painter = painterResource(id = R.drawable.backbtn),
                contentDescription = "Back",
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Start)
                    .clickable { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(titleTopSpacing))

            // تیتر
            Text(
                text = "خروج از حساب کاربری",
                fontSize = 20.sp,
                color = Color.Black,
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp),
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(underTitleSpacing))

            // متن زیر تیتر
            Text(
                text = "برای خروج، رمز عبور خود را وارد کنید.",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = iranSans,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp),
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(labelSpacing))

            // برچسب فیلد رمز عبور
            Text(
                text = "رمز عبور",
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = iranSans,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(8.dp))

            // فیلد رمز عبور
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    showError = false
                },
                singleLine = true,
                modifier = Modifier
                    .width(passwordFieldWidth)
                    .height(passwordFieldHeight),
                textStyle = TextStyle(
                    fontFamily = iranSans,
                    fontSize = 14.sp
                ),
                visualTransformation = PasswordVisualTransformation(),
                isError = showError,
                keyboardOptions = KeyboardOptions.Default,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (showError) Color.Red else Color(0xFF4D869C),
                    unfocusedBorderColor = if (showError) Color.Red else Color(0xFF4D869C),
                    cursorColor = Color(0xFF4D869C)
                )
            )

            if (showError) {
                Text(
                    "رمز عبور را اشتباه وارد کردید.",
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontFamily = iranSans,
                    modifier = Modifier
                        .padding(top = 4.dp, end = 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
            }

            Spacer(modifier = Modifier.height(confirmButtonSpacing))

            // دکمه تایید
            Button(
                onClick = {
                    if (password == correctPassword) {
                        showSuccessDialog = true
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier
                    .width(buttonWidth)
                    .height(buttonHeight),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C))
            ) {
                Text(
                    "تأیید",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // لوگو پایین صفحه
        Image(
            painter = painterResource(id = R.drawable.prodeutsch),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(100.dp)
                .padding(bottom = logoBottomPadding)
        )

        // دیالوگ موفقیت
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "موفقیت‌آمیز",
                            fontFamily = iranSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                text = {
                    Text(
                        "شما با موفقیت از حساب خود خارج شدید.",
                        fontFamily = iranSans,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                showSuccessDialog = false
                                navController.navigate("login") {
                                    popUpTo("logout") { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .width(120.dp)
                                .height(45.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "تأیید",
                                fontFamily = iranSans,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LogoutScreenPreview() {
    LogoutScreen(navController = rememberNavController())
}
