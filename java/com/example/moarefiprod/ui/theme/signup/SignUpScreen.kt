package com.example.moarefiprod.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.ui.theme.signup.EmailValidationTextField
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.signup.ClickableRegisterText
import com.example.moarefiprod.ui.theme.signup.PasswordValidationTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(onNavigateToLogin: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.hamburger),
            contentDescription = "Hamburg",
            modifier = Modifier
                .width(screenWidth * 0.7f)
                .height(screenHeight * 0.3f)
        )

        Spacer(modifier = Modifier.height(screenHeight * 0.02f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .shadow(
                    elevation = 22.dp,
                    shape = RoundedCornerShape(40.dp, 40.dp, 0.dp, 0.dp),
                    ambientColor = Color.Black,
                    spotColor = Color.Black
                )
                .background(Color(0xFF90CECE), shape = RoundedCornerShape(50.dp, 50.dp, 0.dp, 0.dp)),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // **عنوان صفحه**
                Text(
                    text = "ثبت نام",
                    fontSize = (screenWidth * 0.07f).value.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                    color = Color.Black,
                    modifier = Modifier.padding(top = screenHeight * 0.06f)
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.03f))

                Text(
                    text = "ایمیل",
                    fontSize = (screenWidth * 0.04f).value.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.ExtraLight,
                    modifier = Modifier.align(Alignment.Start).padding(start = screenWidth * 0.75f)
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.001f))

                EmailValidationTextField()

                Spacer(modifier = Modifier.height(screenHeight * 0.001f))

                Text(
                    text = "رمز عبور",
                    fontSize = (screenWidth * 0.04f).value.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.ExtraLight,
                    modifier = Modifier.align(Alignment.Start).padding(start = screenWidth * 0.72f)
                )
                Spacer(modifier = Modifier.height(screenHeight * 0.001f))

                PasswordValidationTextField()

                Spacer(modifier = Modifier.height(screenHeight * 0.05f))

                // **دکمه ثبت نام**
                Button(
                    onClick = { /*TODO: ثبت نام*/ },
                    modifier = Modifier
                        .width(screenWidth * 0.73f)
                        .height(screenHeight * 0.07f),
                    colors = ButtonDefaults.buttonColors(Color(0xFF4D869C)),
                    shape = RoundedCornerShape(screenWidth * 0.02f)
                ) {
                    Text(
                        text = "ثبت نام",
                        color = Color.White,
                        fontSize = (screenWidth * 0.045f).value.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.height(screenHeight * 0.01f))

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(height = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

//                    Divider(
//                        color = Color.Gray,
//                        modifier = Modifier
//                            .width(screenWidth * 0.30f)
//                            .height(1.dp)
//                    )
//                    Spacer(modifier = Modifier.width(screenWidth * 0.02f))

                    Text(
                        text = "یا",
                        fontSize = (screenWidth * 0.035f).value.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.ExtraLight,

                    )
//                    Spacer(modifier = Modifier.width(screenWidth * 0.02f))

//                    Divider(
//                        color = Color.Gray,
//                        modifier = Modifier
//                            .width(screenWidth * 0.30f)
//                            .height(1.dp)
//                    )
                }

                Spacer(modifier = Modifier.height(screenHeight * 0.001f))

                ClickableRegisterText(onNavigateToLogin = onNavigateToLogin)

                Spacer(modifier = Modifier.height(screenHeight * 0.028f))

                Text(
                    text = "ProDeutsch",
                    fontSize = (screenWidth * 0.04f).value.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4D869C)
                )
            }
        }
    }
}
