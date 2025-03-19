package com.example.moarefiprod.ui.theme.Recoverypass

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.shape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryC(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val focusRequesters = List(4) { FocusRequester() } // ✅ ایجاد ۴ کنترل فوکوس جداگانه
    var otpCode by remember { mutableStateOf(List(4) { "" }) }
    var focusState by remember { mutableStateOf(List(4) { false }) } // ✅ وضعیت فوکوس هر فیلد

    var remainingTime by remember { mutableStateOf(60) } // ⏳ مقدار اولیه: ۶۰ ثانیه
    var isCounting by remember { mutableStateOf(true) } // ✅ آیا تایمر فعال است؟

    // ✅ اجرای شمارنده معکوس
    LaunchedEffect(isCounting) {
        if (isCounting) {
            while (remainingTime > 0) {
                delay(1000L) // ⏳ ۱ ثانیه صبر کن
                remainingTime--
            }
            isCounting = false // ✅ بعد از اتمام زمان، غیرفعال شود
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
                .fillMaxWidth(),
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "بازیابی رمز عبور",
                    fontSize = (screenWidth * 0.05f).value.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                    color = Color.Black,
                    modifier = Modifier.padding(
                        top = screenHeight * 0.07f,
                        start = screenHeight * 0.2f
                    )
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.14f))

                Text(
                    text = "لطفا کد ارسال شده به ایمیل خود را وارد کنید.",
                    fontSize = (screenWidth * 0.035f).value.sp,
                    fontFamily = iranSans,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.03f))

                // ✅ نمایش فیلدهای ورود کد تایید
                Row(
                    horizontalArrangement = Arrangement.spacedBy(screenWidth * 0.03f),
                ) {
                    otpCode.forEachIndexed { index, value ->
                        OutlinedTextField(
                            value = value,
                            onValueChange = { newValue ->
                                if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                    val newCode = otpCode.toMutableList()
                                    newCode[index] = newValue
                                    otpCode = newCode

                                    // ✅ انتقال خودکار به فیلد بعدی
                                    if (newValue.isNotEmpty() && index < 3) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            visualTransformation = VisualTransformation.None,
                            textStyle = TextStyle(
                                fontSize = (screenWidth * 0.07f).value.sp, // ✅ اندازه بزرگ‌تر
                                fontFamily = iranSans,
                                fontWeight = FontWeight.Bold
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(screenWidth * 0.03f),
                            modifier = Modifier
                                .size(screenWidth * 0.14f) // ✅ اندازه مربع
                                .background(
                                    Color(0xFFCDE8E5),
                                    RoundedCornerShape(screenWidth * 0.03f)
                                ) // ✅ پس‌زمینه آبی کم‌رنگ
                                .border(
                                    width = 2.dp,
                                    color = if (focusState[index]) Color(0xFF4D869C) else Color.Transparent, // ✅ تغییر رنگ کادر هنگام انتخاب
                                    RoundedCornerShape(screenWidth * 0.03f)
                                )
                                .focusRequester(focusRequesters[index]) // ✅ فوکوس اختصاصی برای هر فیلد
                                .onFocusChanged { isFocused ->
                                    val newFocusState = focusState.toMutableList()
                                    newFocusState[index] = isFocused.isFocused
                                    focusState = newFocusState
                                }
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            end = screenWidth * 0.15f,  // ✅ فاصله از سمت چپ (5% عرض صفحه)
                        ),
                    contentAlignment = Alignment.TopEnd,
                ){
                    TextButton(
                        onClick = {
                            if (!isCounting) {
                                remainingTime = 60 // 🔄 راه‌اندازی مجدد تایمر
                                isCounting = true
                                // TODO: ارسال مجدد کد تایید به کاربر
                            }
                        },
                        enabled = !isCounting // ❌ دکمه غیرفعال است تا زمانی که شمارش معکوس تمام شود
                    ) {
                        Text(
                            text = if (isCounting) "ارسال مجدد کد: $remainingTime ثانیه دیگر"
                            else "ارسال مجدد کد", // ✅ تغییر متن بر اساس زمان
                            fontSize = (screenWidth * 0.035f).value.sp,
                            fontFamily = iranSans,
                            color = if (isCounting) Color.Gray else Color(0xFF4D869C),
                            modifier = Modifier.padding(
                                end = screenHeight * 0.008f
                            )
                        )
                    }
                }


                Spacer(modifier = Modifier.height(screenHeight * 0.28f))

                Button(
                   onClick = {navController.navigate("changeScreen")},
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