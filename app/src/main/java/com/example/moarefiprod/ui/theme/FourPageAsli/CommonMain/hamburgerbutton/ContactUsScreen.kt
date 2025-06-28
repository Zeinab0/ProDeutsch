package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans

@Composable
fun ContactUsScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val scrollState = rememberScrollState()

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
                    text = "ارتباط با ما",
                    fontSize = (screenWidth.value * 0.045f).sp,
                    color = Color.Black,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenWidth * 0.07f)
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.05f))

                ContactItem(
                    imageRes = R.drawable.mrahmadi,
                    name = "محمد صادق احمدی",
                    role = "مدرس زبان آلمانی",
                    email = "ahmadisadeq39@gmail.com",
                    screenWidth = screenWidth
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.04f))

                ContactItem(
                    imageRes = R.drawable.mrsahmadi,
                    name = "زینب احمدی",
                    role = "توسعه‌گر اپلیکیشن",
                    email = "zeinabahmadi499@gmail.com",
                    screenWidth = screenWidth
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.04f))

                ContactItem(
                    imageRes = R.drawable.meme,
                    name = "فرشته جعفری",
                    role = "توسعه‌گر اپلیکیشن",
                    email = "fereshteJafari3059@gmail.com",
                    screenWidth = screenWidth
                )
            }

        }

        // لوگو پایین
        Image(
            painter = painterResource(id = R.drawable.prodeutsch),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(screenWidth * 0.25f, screenHeight * 0.05f)
        )
    }
}

@Composable
fun ContactItem(
    imageRes: Int,
    name: String,
    role: String,
    email: String,
    screenWidth: Dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = screenWidth * 0.07f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = name,
            modifier = Modifier
                .size(screenWidth * 0.25f)
        )

        Spacer(modifier = Modifier.width(screenWidth * 0.04f))

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = name,
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold,
                fontSize = (screenWidth.value * 0.038f).sp,
                color = Color.Black
            )
            Text(
                text = role,
                fontFamily = iranSans,
                fontSize = (screenWidth.value * 0.033f).sp,
                color = Color.Gray
            )
            Text(
                text = email,
                fontFamily = iranSans,
                fontSize = (screenWidth.value * 0.033f).sp,
                color = Color(0xFF4D869C)
            )
        }
    }
}
