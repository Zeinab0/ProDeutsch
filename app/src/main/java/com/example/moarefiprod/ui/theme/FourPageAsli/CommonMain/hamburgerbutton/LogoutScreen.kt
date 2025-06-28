package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

@Composable

fun LogoutScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }


    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = screenWidth * 0.05f, vertical = screenHeight * 0.02f)
    ) {
        // دکمه برگشت بالا
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = screenWidth * 0.03f,
                    top = screenHeight * 0.05f
                ),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.backbtn),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(screenWidth * 0.09f)
                )
            }
        }

        // بخش وسط صفحه
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "خروج از حساب کاربری",
                    fontSize = (screenWidth.value * 0.05).sp,
                    color = Color.Black,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.04f))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        showError = false
                    },
                    singleLine = true,
                    modifier = Modifier
                        .width(screenWidth * 0.75f)
                        .height(screenHeight * 0.065f),
                    textStyle = TextStyle(
                        fontFamily = iranSans,
                        fontSize = (screenWidth.value * 0.035).sp
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = showError,
                    keyboardOptions = KeyboardOptions.Default,
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text(
                            text = "رمز عبور خود را وارد کنید",
                            fontFamily = iranSans,
                            fontSize = (screenWidth.value * 0.03).sp,
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
                    Spacer(modifier = Modifier.height(screenHeight * 0.005f))
                    Text(
                        "رمز عبور را اشتباه وارد کردید.",
                        color = Color.Red,
                        fontSize = (screenWidth.value * 0.03).sp,
                        fontFamily = iranSans,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(screenHeight * 0.08f))

                Button(
                    onClick = {
                        isLoading = true
                        val auth = FirebaseAuth.getInstance()
                        val user = auth.currentUser

                        if (user != null && user.email != null) {
                            val credential = EmailAuthProvider.getCredential(user.email!!, password)

                            user.reauthenticate(credential).addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    auth.signOut()
                                    Toast.makeText(context, "با موفقیت خارج شدید ✅", Toast.LENGTH_SHORT).show()
                                    navController.navigate("login") {
                                        popUpTo("logout_screen") { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context, "رمز عبور اشتباه است ❌", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            isLoading = false
                            Toast.makeText(context, "کاربر یافت نشد ❌", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier
                        .width(screenWidth * 0.5f)
                        .height(screenHeight * 0.07f),
                    colors = ButtonDefaults.buttonColors(Color(0xFF4D869C)),
                    shape = RoundedCornerShape(screenWidth * 0.03f),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(screenHeight * 0.035f)
                        )
                    } else {
                        Text(
                            text = "تأیید خروج",
                            color = Color.White,
                            fontSize = (screenWidth * 0.045f).value.sp,
                            fontFamily = iranSans,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

            }
        }

        // لوگو پایین صفحه
        Image(
            painter = painterResource(id = R.drawable.prodeutsch),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(screenWidth * 0.25f, screenHeight * 0.05f)
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
                        fontSize = (screenWidth.value * 0.045).sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Text(
                        "شما با موفقیت از حساب خود خارج شدید.",
                        fontFamily = iranSans,
                        textAlign = TextAlign.Center,
                        fontSize = (screenWidth.value * 0.035).sp,
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
                                .width(screenWidth * 0.4f)
                                .height(screenHeight * 0.065f)
                        ) {
                            Text(
                                "تایید",
                                fontFamily = iranSans,
                                fontSize = (screenWidth.value * 0.035).sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
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
