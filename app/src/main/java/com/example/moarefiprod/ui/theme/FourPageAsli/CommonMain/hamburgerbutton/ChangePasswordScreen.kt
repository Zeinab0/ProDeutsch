package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ChangePasswordScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

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
                    text = "تغییر رمز عبور",
                    fontSize = (screenWidth.value * 0.04).sp,
                    color = Color.Black,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 30.dp)
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.05f))


                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = {
                        currentPassword = it
                        showError = false
                    },
                    placeholder = {
                        Text(
                            text = "رمز عبور فعلی",
                            fontFamily = iranSans,
                            fontSize = (screenWidth.value * 0.03).sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    isError = showError,
                    keyboardOptions = KeyboardOptions.Default,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (showError) Color.Red else Color(0xFF4D869C),
                        unfocusedBorderColor = if (showError) Color.Red else Color(0xFF4D869C),
                        cursorColor = Color(0xFF4D869C)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(screenWidth * 0.75f)
                        .height(screenHeight * 0.065f)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.04f))


                OutlinedTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        showError = false
                    },
                    placeholder = {
                        Text(
                            text = "رمز عبور جدید",
                            fontFamily = iranSans,
                            fontSize = (screenWidth.value * 0.03).sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    isError = showError,
                    keyboardOptions = KeyboardOptions.Default,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (showError) Color.Red else Color(0xFF4D869C),
                        unfocusedBorderColor = if (showError) Color.Red else Color(0xFF4D869C),
                        cursorColor = Color(0xFF4D869C)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(screenWidth * 0.75f)
                        .height(screenHeight * 0.065f)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.08f))

                Button(
                    onClick = {
                        if (currentPassword.isBlank() || newPassword.isBlank()) {
                            Toast.makeText(context, "لطفاً تمام فیلدها را پر کنید ⚠️", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (user != null && user.email != null) {
                            isLoading = true
                            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

                            user.reauthenticate(credential).addOnCompleteListener { authTask ->
                                if (authTask.isSuccessful) {
                                    user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                                        isLoading = false
                                        if (updateTask.isSuccessful) {
                                            Toast.makeText(context, "رمز عبور با موفقیت تغییر یافت ✅", Toast.LENGTH_LONG).show()
                                            navController.popBackStack()
                                        } else {
                                            Toast.makeText(context, "خطا در تغییر رمز عبور ❌", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                } else {
                                    isLoading = false
                                    Toast.makeText(context, "رمز فعلی نادرست است ❌", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "کاربر یافت نشد ❌", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier
                        .width(screenWidth * 0.5f)
                        .height(screenHeight * 0.07f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4D869C),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "ثبت",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = iranSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = (screenWidth * 0.036f).value.sp
                            ),
                            textAlign = TextAlign.Center
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
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChangePasswordScreenPreview() {
    ChangePasswordScreen(navController = rememberNavController())
}
