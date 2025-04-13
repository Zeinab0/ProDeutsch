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
import androidx.compose.ui.platform.LocalConfiguration
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
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val correctPassword = "123456"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // دکمه برگشت
            Image(
                painter = painterResource(id = R.drawable.backbtn),
                contentDescription = "Back",
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Start)
                    .clickable {
                        // برگشت به صفحه منو زمانی که روی دکمه برگشت کلیک می‌شود
                        navController.navigate("menu_screen") {
                            popUpTo("logout_screen") { inclusive = true }
                        }
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // تیتر
            Text(
                text = "خروج از حساب کاربری",
                fontSize = 20.sp,
                color = Color.Black,
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 15.dp),
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(45.dp))

            // فیلد رمز عبور با placeholder
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    showError = false
                },
                singleLine = true,
                modifier = Modifier
                    .width(280.dp)
                    .height(53.dp),
                textStyle = TextStyle(
                    fontFamily = iranSans,
                    fontSize = 14.sp
                ),
                visualTransformation = PasswordVisualTransformation(),
                isError = showError,
                keyboardOptions = KeyboardOptions.Default,
                shape = RoundedCornerShape(12.dp),
                placeholder = {
                    Text(
                        text = "رمز عبور خود را وارد کنید",
                        fontFamily = iranSans,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
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

            Spacer(modifier = Modifier.height(80.dp))

            // دکمه تایید
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 50.dp), // فاصله از پایین صفحه
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (password == correctPassword) {
                            showSuccessDialog = true
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier
                        .width(129.dp)
                        .height(53.dp),
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
        }

        // لوگو پایین صفحه
        Image(
            painter = painterResource(id = R.drawable.prodeutsch),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(100.dp)
                .padding(bottom = 8.dp)
        )

        // دیالوگ موفقیت
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = {
                    Text(
                        "موفقیت‌آمیز",
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Text(
                        "شما با موفقیت از حساب خود خارج شدید.",
                        fontFamily = iranSans,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                showSuccessDialog = false
                                navController.navigate("login") {
                                    popUpTo("logout_screen") { inclusive = true }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C)),
                            modifier = Modifier
                                .width(120.dp)
                                .height(50.dp)
                        ) {
                            Text(
                                "تایید",
                                fontFamily = iranSans,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                ,
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
