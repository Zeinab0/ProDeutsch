package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans

@Composable
fun DatePickerField(
    label: String,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(selectedItem) }

    Box {
        OutlinedTextField(
            value = textValue,
            onValueChange = { newValue ->
                textValue = newValue
                expanded = newValue.isNotEmpty() // If there is input, show dropdown
            },
            modifier = modifier
                .clickable { expanded = true } // کلیک برای باز شدن
                .background(Color.White),  // رنگ پس‌زمینه سفید
            readOnly = false,  // تایپ دستی فعال است
            shape = RoundedCornerShape(8.dp),
            textStyle = TextStyle(textAlign = TextAlign.Center, fontFamily = iranSans),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF4D869C),
                unfocusedIndicatorColor = Color(0xFF4D869C)
            )
        )

        // نمایش لیست زمانی که فیلد کلیک میشه
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .heightIn(max = 200.dp)
                .width(80.dp)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item, fontFamily = iranSans, fontSize = 14.sp) },
                    onClick = {
                        onItemSelected(item)
                        textValue = item  // انتخاب شده در فیلد
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val iranSansFont = iranSans

    val fieldWidth = 280.dp
    val fieldHeight = 53.dp
    val cornerRadius = 8.dp

    val dateFieldWidth = 38.dp
    val dateFieldHeight = 26.dp
    val dateFieldSpacing = 10.dp
    val checkboxSize = 18.dp

    val selectedGender = remember { mutableStateOf("female") } // یا "male"
    val borderColorDefault = Color(0xFF4D869C) // رنگ دور در حالت غیرفعال
    val selectedColor = Color(0xFF4D869C) // رنگ داخل در حالت انتخاب شده
    val borderColor = Color(0xFF4D869C)

    Box(modifier = Modifier.fillMaxSize()) {
        // 🔹 پس‌زمینه‌ی بالا
        Image(
            painter = painterResource(id = R.drawable.backprofedit),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(screenHeight * 0.4f)
                .fillMaxWidth()
        )

        // 🔙 دکمه برگشت
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(start = screenWidth * 0.03f, top = screenHeight * 0.05f)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.backbtn),
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.size(screenWidth * 0.09f)
            )
        }

        // 🧍 آواتار
        Image(
            painter = painterResource(id = R.drawable.prof_with_square),
            contentDescription = "Profile Icon",
            modifier = Modifier
                .size(screenWidth * 0.28f)
                .align(Alignment.TopCenter)
                .offset(y = screenHeight * 0.14f)
                .zIndex(1f)
        )

        // 🧾 فرم اصلی
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.90f)
                .offset(y = screenHeight * 0.20f)
                .shadow(22.dp, RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(Color.White)
                .align(Alignment.TopCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp)
                    .padding(top = screenHeight * 0.10f, bottom = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text("نام و نام خانوادگی", fontFamily = iranSansFont, textAlign = TextAlign.Right)
                    OutlinedTextField(
                        value = "", onValueChange = {},
                        modifier = Modifier.width(fieldWidth).height(fieldHeight),
                        shape = RoundedCornerShape(cornerRadius),
                        textStyle = TextStyle(textAlign = TextAlign.Right, fontFamily = iranSansFont),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = borderColor,
                            unfocusedIndicatorColor = borderColor
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(horizontalAlignment = Alignment.End) {
                    Text("نام کاربری", fontFamily = iranSansFont, textAlign = TextAlign.Right)
                    OutlinedTextField(
                        value = "", onValueChange = {},
                        isError = true,
                        modifier = Modifier.width(fieldWidth).height(fieldHeight),
                        shape = RoundedCornerShape(cornerRadius),
                        textStyle = TextStyle(textAlign = TextAlign.Right, fontFamily = iranSansFont, color = Color.Black),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent,
                            focusedIndicatorColor = borderColor,
                            unfocusedIndicatorColor = borderColor,
                            errorIndicatorColor = borderColor,
                            cursorColor = Color.Black,
                            errorCursorColor = Color.Black,
                            errorTextColor = Color.Black
                        )
                    )
                    Text(
                        "نام کاربری تکراری می‌باشد!",
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontFamily = iranSansFont,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(horizontalAlignment = Alignment.End) {
                    Text("ایمیل", fontFamily = iranSansFont, textAlign = TextAlign.Right)
                    OutlinedTextField(
                        value = "", onValueChange = {},
                        modifier = Modifier.width(fieldWidth).height(fieldHeight),
                        shape = RoundedCornerShape(cornerRadius),
                        textStyle = TextStyle(textAlign = TextAlign.Right, fontFamily = iranSansFont),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = borderColor,
                            unfocusedIndicatorColor = borderColor
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(horizontalAlignment = Alignment.End) {
                    Text("تاریخ تولد", fontFamily = iranSansFont, textAlign = TextAlign.Right)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.width(fieldWidth),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // روز
                        DatePickerField(
                            label = "روز",
                            items = (1..31).map { it.toString().padStart(2, '0') },
                            selectedItem = "01",  // مقدار اولیه
                            onItemSelected = { /* به روز رسانی مقدار */ },
                            modifier = Modifier.width(dateFieldWidth).height(dateFieldHeight)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("/", fontSize = 16.sp, fontFamily = iranSans)

                        Spacer(modifier = Modifier.width(4.dp))

                        // ماه
                        DatePickerField(
                            label = "ماه",
                            items = listOf("فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"),
                            selectedItem = "فروردین",  // مقدار اولیه
                            onItemSelected = { /* به روز رسانی مقدار */ },
                            modifier = Modifier.width(dateFieldWidth).height(dateFieldHeight)
                        )

                        Spacer(modifier = Modifier.width(4.dp))
                        Text("/", fontSize = 16.sp, fontFamily = iranSans)

                        Spacer(modifier = Modifier.width(4.dp))

                        // سال
                        DatePickerField(
                            label = "سال",
                            items = (1370..1412).map { it.toString() },
                            selectedItem = "1400",  // مقدار اولیه
                            onItemSelected = { /* به روز رسانی مقدار */ },
                            modifier = Modifier.width(dateFieldWidth).height(dateFieldHeight)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 👇 اینو بذار به‌جای بخش جنسیت قبلی
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.width(fieldWidth)) {
                        Text("جنسیت", fontFamily = iranSansFont, textAlign = TextAlign.Right)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // 🔷 گزینه زن
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { selectedGender.value = "female" }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(checkboxSize)
                                        .background(
                                            if (selectedGender.value == "female") selectedColor else Color.Transparent,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = borderColorDefault,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("زن", fontFamily = iranSansFont)
                            }

                            Spacer(modifier = Modifier.width(35.dp))

                            // 🔷 گزینه مرد
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { selectedGender.value = "male" }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(checkboxSize)
                                        .background(
                                            if (selectedGender.value == "male") selectedColor else Color.Transparent,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = borderColorDefault,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("مرد", fontFamily = iranSansFont)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(60.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .width(129.dp)
                            .height(53.dp)
                            .background(Color(0xFF5B8EA3), RoundedCornerShape(cornerRadius)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("ذخیره", color = Color.White, fontFamily = iranSansFont)
                    }
                    Box(
                        modifier = Modifier
                            .width(129.dp)
                            .height(53.dp)
                            .background(Color(0xFFD1F1EF), RoundedCornerShape(cornerRadius)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("انصراف", color = Color.Black, fontFamily = iranSansFont)
                    }
                }
            }
        }

        // 🖼 لوگو پایین صفحه - بدون هیچ پس‌زمینه‌ای
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.prodeutsch),
                contentDescription = "Logo",
                modifier = Modifier.size(110.dp)
                    .offset(y = 16.dp)
            )
        }
    }
}
