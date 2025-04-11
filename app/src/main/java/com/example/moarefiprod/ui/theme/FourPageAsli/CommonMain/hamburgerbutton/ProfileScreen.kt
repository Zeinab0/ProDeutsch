//package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton
//
//import android.widget.Toast
//import androidx.activity.compose.BackHandler
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalLayoutDirection
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.unit.LayoutDirection
//import com.example.moarefiprod.R
//
//
//@Composable
//fun CustomField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    label: String,
//    height: Dp,
//    cornerRadius: Dp,
//    isError: Boolean = false,
//    keyboardType: KeyboardType = KeyboardType.Text
//) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = { Text(label, textAlign = TextAlign.Right) },
//        isError = isError,
//        shape = RoundedCornerShape(cornerRadius),
//        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
//        modifier = Modifier
//            .fillMaxWidth(0.53f)
//            .height(height),
//        textStyle = TextStyle(textAlign = TextAlign.Right),
//        colors = OutlinedTextFieldDefaults.colors(
//            focusedBorderColor = Color(0xFF4D869C),
//            unfocusedBorderColor = Color(0xFF4D869C)
//        )
//    )
//}
//
//@Composable
//fun SquareGenderSelector(
//    selected: Boolean,
//    onClick: () -> Unit,
//    label: String
//) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier
//            .clickable { onClick() }
//            .padding(horizontal = 8.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(20.dp)
//                .border(
//                    width = 1.dp,
//                    color = if (selected) Color(0xFF4D869C) else Color.Gray,
//                    shape = RoundedCornerShape(4.dp)
//                )
//                .background(
//                    color = if (selected) Color(0xFF4D869C) else Color.Transparent,
//                    shape = RoundedCornerShape(4.dp)
//                )
//        )
//        Spacer(modifier = Modifier.width(6.dp))
//        Text(label)
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileScreen(onBackPress: () -> Unit) {
//    val context = LocalContext.current
//
//    var fullName by remember { mutableStateOf("") }
//    var username by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var birthDate by remember { mutableStateOf("11 / 12 / 1378") }
//    var gender by remember { mutableStateOf("زن") }
//    var usernameError by remember { mutableStateOf(false) }
//    val configuration = LocalConfiguration.current
//    val screenWidth = configuration.screenWidthDp.dp
//    val screenHeight = configuration.screenHeightDp.dp
//    BackHandler { onBackPress() }
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF90CECE),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
////        Box(
////            modifier = Modifier
////                .fillMaxWidth()
////                .height(screenHeight * 0.3f)
////        ) {
////            Image(
////                painter = painterResource(id = R.drawable.backprofedit),
////                contentDescription = null,
////                contentScale = ContentScale.FillBounds,
////                modifier = Modifier.fillMaxSize()
////            )
////
////            // سایر محتواها اینجا...
////        }
//
//
//
////        Spacer(modifier = Modifier.height(screenHeight * 0.02f))
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(0.7f, fill = true)
//                .shadow(
//                    elevation = 22.dp,
//                    shape = RoundedCornerShape(40.dp, 40.dp, 0.dp, 0.dp),
//                    ambientColor = Color.Black,
//                    spotColor = Color.Black
//                )
//                .background(Color(0xFF90CECE), shape = RoundedCornerShape(50.dp, 50.dp, 0.dp, 0.dp)),
//            contentAlignment = Alignment.TopCenter
//        ) {
//
//        }
//    }
////    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
////        Scaffold(
////            topBar = {
////                SmallTopAppBar(
////                    title = {
////                        Text(
////                            text = "پروفایل",
////                            textAlign = TextAlign.Right,
////                            modifier = Modifier.fillMaxWidth()
////                        )
////                    },
////                    navigationIcon = {
////                        IconButton(onClick = onBackPress) {
////                            Icon(
////                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
////                                contentDescription = "بازگشت"
////                            )
////                        }
////                    }
////                )
////            },
////            content = { padding ->
////                Column(
////                    modifier = Modifier
////                        .fillMaxSize()
////                        .padding(padding)
////                        .padding(16.dp)
////                        .verticalScroll(rememberScrollState()),
////                    horizontalAlignment = Alignment.CenterHorizontally
////                ) {
////                    // پس‌زمینه
////                    Box(
////                        modifier = Modifier
////                            .fillMaxWidth()
////                            .height(180.dp)
////                    ) {
////                        Image(
////                            painter = painterResource(id = R.drawable.paspic),
////                            contentDescription = null,
////                            contentScale = ContentScale.Crop,
////                            modifier = Modifier.fillMaxSize()
////                        )
////                        Box(
////                            modifier = Modifier
////                                .matchParentSize()
////                                .background(Color(0xFF90CECE).copy(alpha = 0.4f))
////                        )
////                    }
////
////                    // پروفایل
////                    Image(
////                        painter = painterResource(id = R.drawable.profile_image),
////                        contentDescription = null,
////                        modifier = Modifier
////                            .size(130.dp)
////                            .background(Color.White, CircleShape)
////                            .padding(4.dp)
////                    )
////
////                    Spacer(modifier = Modifier.height(24.dp))
////
////                    CustomField(
////                        value = fullName,
////                        onValueChange = { fullName = it },
////                        label = "نام و نام خانوادگی",
////                        height = 56.dp,
////                        cornerRadius = 16.dp
////                    )
////
////                    Spacer(modifier = Modifier.height(12.dp))
////
////                    CustomField(
////                        value = username,
////                        onValueChange = {
////                            username = it
////                            usernameError = it == "testuser"
////                        },
////                        label = "نام کاربری",
////                        height = 56.dp,
////                        cornerRadius = 16.dp,
////                        isError = usernameError
////                    )
////
////                    if (usernameError) {
////                        Text(
////                            text = "نام کاربری تکراری می‌باشد!",
////                            color = Color.Red,
////                            fontSize = 12.sp,
////                            modifier = Modifier.fillMaxWidth(0.53f)
////                        )
////                    }
////
////                    Spacer(modifier = Modifier.height(12.dp))
////
////                    CustomField(
////                        value = email,
////                        onValueChange = { email = it },
////                        label = "ایمیل",
////                        height = 56.dp,
////                        cornerRadius = 16.dp,
////                        keyboardType = KeyboardType.Email
////                    )
////
////                    Spacer(modifier = Modifier.height(12.dp))
////
////                    CustomField(
////                        value = birthDate,
////                        onValueChange = { birthDate = it },
////                        label = "تاریخ تولد",
////                        height = 56.dp,
////                        cornerRadius = 16.dp
////                    )
////
////                    Spacer(modifier = Modifier.height(16.dp))
////
////                    // جنسیت
////                    Row(
////                        verticalAlignment = Alignment.CenterVertically,
////                        modifier = Modifier.fillMaxWidth(),
////                        horizontalArrangement = Arrangement.Center
////                    ) {
////                        Text("جنسیت:")
////                        Spacer(modifier = Modifier.width(8.dp))
////                        SquareGenderSelector(
////                            selected = gender == "زن",
////                            onClick = { gender = "زن" },
////                            label = "زن"
////                        )
////                        SquareGenderSelector(
////                            selected = gender == "مرد",
////                            onClick = { gender = "مرد" },
////                            label = "مرد"
////                        )
////                    }
////
////                    Spacer(modifier = Modifier.height(20.dp))
////
////                    // دکمه‌ها
////                    Row(
////                        modifier = Modifier.fillMaxWidth(0.53f),
////                        horizontalArrangement = Arrangement.SpaceBetween
////                    ) {
////                        Button(
////                            onClick = {
////                                Toast.makeText(context, "ذخیره شد!", Toast.LENGTH_SHORT).show()
////                            },
////                            colors = ButtonDefaults.buttonColors(
////                                containerColor = Color(0xFF4D869C)
////                            ),
////                            modifier = Modifier
////                                .weight(1f)
////                                .padding(end = 8.dp)
////                        ) {
////                            Text("ذخیره")
////                        }
////
////                        Button(
////                            onClick = onBackPress,
////                            colors = ButtonDefaults.buttonColors(
////                                containerColor = Color(0xFF90CECE),
////                                contentColor = Color.White
////                            ),
////                            modifier = Modifier
////                                .weight(1f)
////                                .padding(start = 8.dp)
////                        ) {
////                            Text("انصراف")
////                        }
////                    }
////
////                    Spacer(modifier = Modifier.height(24.dp))
////
////                    Image(
////                        painter = painterResource(id = R.drawable.prodeutsch),
////                        contentDescription = null,
////                        modifier = Modifier
////                            .size(160.dp)
////                            .align(Alignment.CenterHorizontally)
////                    )
////                }
////            }
////        )
////    }
//}
////
////import android.widget.Toast
////import androidx.compose.foundation.Image
////import androidx.compose.foundation.background
////import androidx.compose.foundation.layout.*
////import androidx.compose.foundation.shape.RoundedCornerShape
////import androidx.compose.material3.*
////import androidx.compose.runtime.*
////import androidx.compose.ui.Alignment
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.draw.shadow
////import androidx.compose.ui.graphics.Color
////import androidx.compose.ui.platform.LocalConfiguration
////import androidx.compose.ui.platform.LocalContext
////import androidx.compose.ui.res.painterResource
////import androidx.compose.ui.text.font.FontWeight
////import androidx.compose.ui.unit.dp
////import androidx.compose.ui.unit.sp
////import com.example.moarefiprod.R
////import com.example.moarefiprod.iranSans
////import com.example.moarefiprod.ui.theme.signup.ClickableRegisterText
////import com.example.moarefiprod.ui.theme.signup.EmailValidationTextField
////import com.example.moarefiprod.ui.theme.signup.PasswordValidationTextField
////import com.google.firebase.auth.FirebaseAuth
////
////@Composable
////fun editprofile() {
////    val configuration = LocalConfiguration.current
////    val screenWidth = configuration.screenWidthDp.dp
////    val screenHeight = configuration.screenHeightDp.dp
////
////
////
////    Column(
////        modifier = Modifier.fillMaxSize(),
////        horizontalAlignment = Alignment.CenterHorizontally
////    ) {
////        Image(
////            painter = painterResource(id = R.drawable.hamburger),
////            contentDescription = "Hamburg",
////            modifier = Modifier
////                .width(screenWidth * 0.7f)
////                .height(screenHeight * 0.3f)
////                .weight(0.3f, fill = true)
////        )
////
////        Spacer(modifier = Modifier.height(screenHeight * 0.02f))
////
////        Box(
////            modifier = Modifier
////                .fillMaxWidth()
////                .weight(0.7f, fill = true)
////                .shadow(
////                    elevation = 22.dp,
////                    shape = RoundedCornerShape(40.dp, 40.dp, 0.dp, 0.dp),
////                    ambientColor = Color.Black,
////                    spotColor = Color.Black
////                )
////                .background(Color(0xFF90CECE), shape = RoundedCornerShape(50.dp, 50.dp, 0.dp, 0.dp)),
////            contentAlignment = Alignment.TopCenter
////        ) {
////
////        }
////    }
////}
