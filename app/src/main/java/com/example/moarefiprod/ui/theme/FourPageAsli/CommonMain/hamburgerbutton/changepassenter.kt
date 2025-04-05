package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton


import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ChangePasswordScreen(navController: NavController) {
    val passwordFieldWidth = 280.dp  // عرض فیلد ورودی رمز عبور
    val passwordFieldHeight = 53.dp  // ارتفاع فیلد ورودی رمز عبور
    val buttonWidth = 236.dp  // عرض دکمه ثبت
    val buttonHeight = 53.dp  // ارتفاع دکمه ثبت

    var password by remember { mutableStateOf("") }  // متغیر برای نگهداری مقدار رمز عبور

    val backButtonSize by remember { mutableStateOf(75.dp) } // متغیر برای تنظیم سایز دکمه برگشت
    val iconOffsetX by remember { mutableStateOf(3.dp) } // متغیر برای فاصله افقی آیکون امنیتی از تیتر
    val titleOffsetX by remember { mutableStateOf(83.dp) } // متغیر برای فاصله افقی تیتر از موقعیت پیش‌فرض

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()  // اندازه کامل صفحه برای Column
            .padding(horizontal = 24.dp, vertical = 16.dp),  // فاصله از اطراف صفحه
        verticalArrangement = Arrangement.Top,  // قرار دادن المان‌ها در بالا
        horizontalAlignment = Alignment.CenterHorizontally  // قرار دادن المان‌ها در وسط افقی
    ) {
        // دکمه برگشت (قابل جابه‌جایی)
        IconButton(
            onClick = { navController.popBackStack() },  // عملکرد برگشت به صفحه قبلی
            modifier = Modifier
                .offset(x = (-165).dp, y = 20.dp)  // جابه‌جایی دکمه برگشت به میزان -165dp افقی و 20dp عمودی
                .size(backButtonSize)  // تنظیم سایز دکمه برگشت
        ) {
            Image(
                painter = painterResource(id = R.drawable.backbtn),
                contentDescription = "Back"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))  // فاصله عمودی میان دکمه برگشت و تیتر

        // تیتر + آیکون امنیتی (فقط جابه‌جایی افقی)
        Row(
            verticalAlignment = Alignment.CenterVertically,  // تراز عمودی آیکون و تیتر
            horizontalArrangement = Arrangement.Center,  // تراز افقی آیکون و تیتر
            modifier = Modifier
                .fillMaxWidth()  // عرض کامل صفحه
                .offset(x = titleOffsetX, y = 27.dp)  // جابه‌جایی افقی تیتر
        ) {
            Text(
                text = "تغییر رمز عبور",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = (screenWidth * 0.04f).value.sp  // اندازه مقیاس‌پذیر
                ),
                textAlign = TextAlign.Right,  // تراز متن به سمت راست
                color = Color(0xFF000000)  // رنگ مشکی برای تیتر
            )
            Spacer(modifier = Modifier.width(8.dp))  // فاصله افقی بین تیتر و آیکون
            Image(
                painter = painterResource(id = R.drawable.security),
                contentDescription = "Security Icon",
                modifier = Modifier
                    .size(27.dp)  // اندازه آیکون امنیتی
                    .offset(x = iconOffsetX, y = 3.dp)  // جابه‌جایی افقی آیکون و فاصله عمودی 3dp
            )
        }

        Spacer(modifier = Modifier.height(65.dp))  // فاصله عمودی میان تیتر و فیلد رمز عبور

        // فیلد ورودی رمز عبور (قابل جابه‌جایی + گرد شدن گوشه‌ها)
        OutlinedTextField(
            value = password,  // مقدار وارد شده در فیلد
            onValueChange = { password = it },  // تغییر مقدار ورودی
            label = {
                Text(
                    "رمز عبور جدید خود را وارد کنید",  // متن راهنما در فیلد
                    color = Color(0xFFCFD1D4),  // رنگ خاکستری برای متن راهنما
                    textAlign = TextAlign.Right,  // تراز متن راهنما به سمت راست
                    modifier = Modifier.fillMaxWidth()  // عرض کامل فیلد
                )
            },
            singleLine = true,  // فقط یک خط ورودی
            visualTransformation = PasswordVisualTransformation(),  // مخفی کردن رمز عبور
            keyboardOptions = KeyboardOptions.Default,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4D869C),  // رنگ آبی برای کادر در حالت فوکوس
                unfocusedBorderColor = Color(0xFF4D869C),  // رنگ آبی برای کادر در حالت غیر فوکوس
                cursorColor = Color(0xFF4D869C)  // رنگ مکان‌نما
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .width(passwordFieldWidth)  // عرض فیلد
                .height(passwordFieldHeight)  // ارتفاع فیلد
                .align(Alignment.CenterHorizontally)  // تراز کردن فیلد در وسط صفحه
                .offset(x = 0.dp, y = 20.dp)  // جابه‌جایی عمودی فیلد
        )

        Spacer(modifier = Modifier.height(210.dp))  // فاصله برای قرار دادن دکمه ثبت پایین‌تر

        // دکمه ثبت (قابل جابه‌جایی)
        Button(
            onClick = { /* عملکرد تغییر رمز */ },
            modifier = Modifier
                .width(buttonWidth)  // عرض دکمه ثبت
                .height(buttonHeight)  // ارتفاع دکمه ثبت
                .align(Alignment.CenterHorizontally)  // تراز کردن دکمه در وسط صفحه
                .offset(x = 0.dp, y = 210.dp),  // جابه‌جایی دکمه ثبت به سمت پایین
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4D869C),  // رنگ پس‌زمینه دکمه
                contentColor = Color(0xFFFFFFFF)  // رنگ متن دکمه
            ),
            shape = RoundedCornerShape(12.dp)  // گرد کردن گوشه‌های دکمه
        ) {
            Text(
                text = "ثبت",  // متن دکمه
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = (screenWidth * 0.04f).value.sp  // اندازه مقیاس‌پذیر
                ),
                textAlign = TextAlign.Center  // تراز کردن متن در مرکز دکمه
            )
        }

        Spacer(modifier = Modifier.weight(1f))  // فاصله خالی برای قرار دادن لوگو در پایین صفحه

        // لوگوی پایین صفحه
        Image(
            painter = painterResource(id = R.drawable.prodeutsch),
            contentDescription = "ProDeutsch Logo",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)  // تراز کردن لوگو در وسط صفحه
        )
    }
}
