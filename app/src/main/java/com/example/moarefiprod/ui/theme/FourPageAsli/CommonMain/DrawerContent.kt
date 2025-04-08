package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans

@Composable
fun DrawerContent(
    navController: NavController,
    onClose: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .width(screenWidth * 0.5f)
            .fillMaxHeight()
            .background(Color(0xFF90CECE))
            .padding(horizontal = screenWidth * 0.04f, vertical = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // 👤 پروفایل
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(screenWidth * 0.15f)
                    .border(
                        width = 2.dp,
                        color = Color(0xff4D869C),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_image),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(screenWidth * 0.12f)
                        .background(Color(0xffDAF8F5), RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Zeinab", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("zizi_germany", fontSize = 12.sp, color = Color.DarkGray)
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(8.dp),
            color = Color.Gray,
            thickness = 1.dp
        )


        // 📋 آیتم‌های منو
        DrawerItem("ویرایش پروفایل") {
            navController.navigate("profile")
            onClose() // تا منو بسته بشه
        }

        DrawerItem("دوره‌های من") { /* TODO */ }
        DrawerItem("اعلان‌ها", hasSwitch = true)
        DrawerItem("تغییر رمز عبور") {
            navController.navigate("change_password")
            onClose()
        }
        DrawerItem("خروج از حساب کاربری") { /* TODO */ }

        DrawerItem("ارتباط با ما") {
            navController.navigate("contact_us")
            onClose()
        }
        DrawerItem("درباره ما") {
            navController.navigate("about_us")
            onClose()
        }



        Spacer(modifier = Modifier.weight(1f))

        // ❗ حذف حساب کاربری
        Text(
            text = "حذف حساب کاربری",
            color = Color(0xFF315664),
            fontFamily = iranSans,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = (screenWidth * 0.034f).value.sp,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
                .padding(bottom = 20.dp)
                .clickable { /* TODO: اقدام حذف */ }
        )
    }
}

@Composable
fun DrawerItem(
    text: String,
    hasSwitch: Boolean = false,
    onClick: () -> Unit = {}
) {
    var switchState by remember { mutableStateOf(true) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .width(220.dp)
                .height(60.dp)
                .padding(vertical = 6.dp)
                .clickable {
                    if (hasSwitch) {
                        switchState = !switchState
                    } else {
                        onClick()
                    }
                }
                .border(2.dp, color = Color(0xFF4D869C), shape = RoundedCornerShape(12.dp))
                .background(Color(0xFF90CECE), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (hasSwitch) {
                Switch(
                    checked = switchState,
                    onCheckedChange = {
                        switchState = it
                    },
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .size(36.dp),
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF4D869C),
                        checkedTrackColor = Color(0xFFB3DDE1),
                        uncheckedThumbColor = Color(0xFFB3DDE1),
                        uncheckedTrackColor = Color(0xFF4D869C)
                    )
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = null,
                    tint = Color(0xFF4D869C),
                    modifier = Modifier.size(screenWidth * 0.05f)
                )
            }

            Text(
                text = text,
                fontSize = (screenWidth * 0.033f).value.sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}