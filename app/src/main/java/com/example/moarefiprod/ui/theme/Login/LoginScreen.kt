package com.example.moarefiprod.ui.theme.Login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.signup.EmailValidationTextField
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    onNavigateToRegister: () -> Unit,
    onNavigateToRecovery: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (isLoading) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            title = {
                Text(
                    text = "لطفاً صبر کنید",
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF4D869C),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "...در حال ورود",
                        fontFamily = iranSans,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(screenHeight * 0.02f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f, fill = true),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.munich),
                contentDescription = "munich",
                modifier = Modifier
                    .width(screenWidth * 0.7f)
                    .height(screenHeight * 0.3f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f, fill = true)
                .shadow(
                    elevation = 22.dp,
                    shape = RoundedCornerShape(40.dp, 40.dp, 0.dp, 0.dp),
                    ambientColor = Color.Black,
                    spotColor = Color.Black
                )
                .background(Color(0xFF90CECE), shape = RoundedCornerShape(50.dp, 50.dp, 0.dp, 0.dp)),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "ورود",
                    fontSize = (screenWidth * 0.05f).value.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                    color = Color.Black,
                    modifier = Modifier.padding(top = screenHeight * 0.06f)
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.04f))

                Text(
                    text = "ایمیل",
                    fontSize = (screenWidth * 0.04f).value.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.ExtraLight,
                    modifier = Modifier.align(Alignment.Start).padding(start = screenWidth * 0.73f)
                )

                EmailValidationTextField(
                    email = email,
                    onEmailChange = { email = it }
                )

                Text(
                    text = "رمز عبور",
                    fontSize = (screenWidth * 0.04f).value.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.ExtraLight,
                    modifier = Modifier.align(Alignment.Start).padding(start = screenWidth * 0.69f)
                )

                PasswordFieldLogin(
                    password = password,
                    onPasswordChange = { password = it }
                )

                Text(
                    text = "فراموشی رمز عبور؟",
                    fontSize = (screenWidth * 0.03f).value.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4D869C),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.align(Alignment.End).padding(end = screenWidth * 0.18f)
                        .clickable { onNavigateToRecovery() }
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.06f))

                Button(
                    onClick = {
                        isLoading = true
                        FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "با موفقیت وارد شدید! 🎉", Toast.LENGTH_SHORT).show()
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context, "ایمیل یا رمز عبور اشتباه است ❌", Toast.LENGTH_LONG).show()
                                }
                            }
                    },
                    modifier = Modifier
                        .width(screenWidth * 0.73f)
                        .height(screenHeight * 0.07f),
                    colors = ButtonDefaults.buttonColors(Color(0xFF4D869C)),
                    shape = RoundedCornerShape(screenWidth * 0.02f)
                ) {
                    Text(
                        text = "ورود",
                        color = Color.White,
                        fontSize = (screenWidth * 0.045f).value.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.height(screenHeight * 0.01f))

                Text(
                    text = "یا",
                    fontSize = (screenWidth * 0.035f).value.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.ExtraLight,
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.001f))

                ClickableRegisterTextL(onNavigateToRegister = onNavigateToRegister)

                Spacer(modifier = Modifier.height(screenHeight * 0.1f))

                Text(
                    text = "ProDeutsch",
                    fontSize = (screenWidth * 0.035f).value.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4D869C)
                )
            }
        }
    }
}
