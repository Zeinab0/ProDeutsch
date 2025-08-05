package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören.evenShadow


@Composable
fun StepProgressBarWithExit(
    navController: NavController,
    currentStep: Int,
    totalSteps: Int,
    returnRoute: String, // ✅ مسیر برگشت
    modifier: Modifier = Modifier,
    onRequestExit: () -> Unit
) {
    var showExitDialog by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = screenWidth * 0.03f,
                end = screenWidth * 0.03f,
                top = screenHeight * 0.05f
            )
    ) {
        // نوار پیشرفت (پایین‌تر از دکمه)
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.07f, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(totalSteps) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(
                            if (index <= currentStep) Color(0xFF4D869C)
                            else Color(0xFFE0F2F1)
                        )
                )
            }
        }

        // دکمه برگشت
        IconButton(
            onClick = { onRequestExit() }, // 👈 فقط تریگر کن
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.backbtn),
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.size(screenWidth * 0.09f)
            )
        }
    }

    if (showExitDialog) {
        ExitConfirmationDialog(
            onConfirmExit = {
                navController.popBackStack()
                showExitDialog = false
            },
            onDismiss = {
                showExitDialog = false
            }
        )
    }
}



@Composable
fun ExitConfirmationDialog(
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(enabled = false, onClick = {}),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "آیا مطمئنی می‌خوای آزمون رو ترک کنی؟",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "با خروج از آزمون، پیشرفت شما ذخیره نخواهد شد.",
                    fontSize = 12.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.ExtraLight,
                    textAlign = TextAlign.Right,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .evenShadow(radius = 25f, cornerRadius = 20f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .height(45.dp)
                            .clickable(onClick = onConfirmExit),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("خروج", color = Color.Red, fontFamily = iranSans)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .evenShadow(radius = 25f, cornerRadius = 20f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF7AB2B2))
                            .height(45.dp)
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("انصراف", color = Color.White, fontFamily = iranSans)
                    }
                }
            }
        }
    }
}
