package com.example.moarefiprod.ui.theme.logofirst

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans

@Composable
fun Advertisement2(
    onNext: () -> Unit,
    onSkip: () -> Unit,
    currentPage: Int,
    onPageChange: (Int) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val density = LocalDensity.current

    // انیمیشن برای جابه‌جایی مستطیل آبی
    val indicatorOffset = animateFloatAsState(
        targetValue = if (currentPage == 1) {
            with(density) { screenWidth.value * 0.15f } // فاصله تا دایره دوم
        } else {
            0f
        },
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        Text(
            text = "رد شدن",
            fontSize = (screenWidth * 0.04f).value.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = iranSans,
            color = Color(0xFF5E5E5E),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = screenHeight * 0.05f, end = screenWidth * 0.05f)
                .clickable { onSkip() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ad2),
                contentDescription = "advertisement",
                modifier = Modifier
                    .width(screenWidth * 0.85f)
                    .height(screenHeight * 0.3f)
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.03f))

            Text(
                text = "یادگیری با فلش\u200Cکارت",
                fontSize = (screenWidth * 0.06f).value.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = iranSans,
                color = Color(0xFF000000),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.02f))

            Text(
                text = "هیچ\u200Cکس یه\u200Cشبه زبان یاد نمی\u200Cگیره، اما فلش\u200Cکارت\u200Cها کمک می\u200Cکنن هر روز یه قدم کوچیک برداری. همین قدمای کوچیکه که آخرش باعث می\u200Cشن راحت حرف بزنی",
                fontSize = (screenWidth * 0.04f).value.sp,
                fontWeight = FontWeight.Light,
                fontFamily = iranSans,
                textAlign = TextAlign.Center,
                color = Color(0xFF000000),
                style = TextStyle(textDirection = TextDirection.Rtl),
                modifier = Modifier.width(screenWidth * 0.9f)
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            Box(
                modifier = Modifier
                    .width(screenWidth * 0.3f)
                    .height(screenHeight * 0.010f)
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.circle),
                        contentDescription = "indicator",
                        modifier = Modifier.size(screenWidth * 0.06f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.circle),
                        contentDescription = "indicator",
                        modifier = Modifier.size(screenWidth * 0.10f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.circle),
                        contentDescription = "indicator",
                        modifier = Modifier.size(screenWidth * 0.06f)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = (indicatorOffset.value - (screenWidth.value * 0.033f)).dp) // جابجایی برای مرکز دایره
                        .size(screenWidth * 0.066f, screenHeight * 0.010f)
                        .background(Color(0xFF4D869C), RoundedCornerShape(50))
                )
            }
        }

        Button(
            onClick = {
                onNext()
                onPageChange(2)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = screenHeight * 0.09f)
                .width(screenWidth * 0.7f)
                .height(screenHeight * 0.07f),
            colors = ButtonDefaults.buttonColors(Color(0xFF4D869C)),
            shape = RoundedCornerShape(screenWidth * 0.02f)
        ) {
            Text(
                text = "بعدی",
                color = Color.White,
                fontSize = (screenWidth * 0.05f).value.sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold
            )
        }
    }
}