package com.example.moarefiprod.ui.theme.Recoverypass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.signup.EmailValidationTextField


@Composable
fun RecoveryE(navController: androidx.navigation.NavController){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = screenWidth * 0.03f,  // ✅ فاصله از سمت چپ (5% عرض صفحه)
                    top = screenHeight * 0.05f   // ✅ فاصله از بالا (5% ارتفاع صفحه)
                ),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.backbtn), // ✅ آیکون برگشت
                    contentDescription = "Back",
                    tint = Color.Black, // رنگ آیکون
                    modifier = Modifier.size(screenWidth * 0.09f) // ✅ اندازه آیکون ۸٪ از عرض صفحه
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
        ){

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(
                    text = "بازیابی رمز عبور",
                    fontSize = (screenWidth * 0.05f).value.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                    color = Color.Black,
                    modifier = Modifier.padding(top = screenHeight * 0.07f, start = screenHeight * 0.2f)
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.07f))

                Text(
                    text = "ایمیل",
                    fontSize = (screenWidth * 0.04f).value.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Start).padding(start = screenWidth * 0.75f)
                )

//                EmailValidationTextField()
                Text(
                    text = "حساب کاربری وجود ندارد",
                    color = Color(0xBCFF0000),
                    fontSize = (screenWidth * 0.03f).value.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Start).padding(start = screenWidth * 0.54f)
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.4f))

                Button(
                    onClick = { navController.navigate("codeScreen") },
                    modifier = Modifier
                        .width(screenWidth * 0.7f)
                        .height(screenHeight * 0.07f),
                    colors = ButtonDefaults.buttonColors(Color(0xFF4D869C)),
                    shape = RoundedCornerShape(screenWidth * 0.02f)
                ) {
                    Text(
                        text = "تایید",
                        color = Color.White,
                        fontSize = (screenWidth * 0.045f).value.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                    )
                }

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