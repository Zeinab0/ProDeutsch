package com.example.moarefiprod.ui.theme.Login

import ClickableRegisterTextL
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.signup.EmailValidationTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(screenHeight * 0.02f))

        Image(
            painter = painterResource(id = R.drawable.munich),
            contentDescription = "munich",
            modifier = Modifier
                .width(screenWidth * 0.7f)
                .height(screenHeight * 0.3f)

        )


        Box(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight()
                .shadow(
                    elevation = 22.dp,
                    shape = RoundedCornerShape(40.dp,40.dp,0.dp,0.dp),
                    ambientColor = Color.Black,
                    spotColor = Color.Black
                )
                .background(Color(0xFF90CECE), shape = RoundedCornerShape(50.dp, 50.dp,0.dp,0.dp)),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ورود",
                    fontSize = (screenWidth * 0.07f).value.sp,
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

                Spacer(modifier = Modifier.height(screenHeight * 0.0f))

                EmailValidationTextField()

                Spacer(modifier = Modifier.height(screenHeight * 0f))

                Text(
                    text = "رمز عبور",
                    fontSize = (screenWidth * 0.04f).value.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.ExtraLight,
                    modifier = Modifier.align(Alignment.Start).padding(start = screenWidth * 0.69f)
                )

                com.example.moarefiprod.ui.theme.Login.PasswordValidationTextField()

                Spacer(modifier = Modifier.height(screenHeight * 0.0f))

                Text(
                    text = "فراموشی رمز عبور؟",
                    fontSize = (screenWidth * 0.03f).value.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4D869C),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.align(Alignment.End).padding(end = screenWidth * 0.18f)
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.06f))

                Button(
                    onClick = { /*TODO: ورود*/ },
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
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

                ClickableRegisterTextL(onNavigateToRegister = onNavigateToRegister)

                Spacer(modifier = Modifier.height(screenHeight * 0.076f))

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
