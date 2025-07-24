package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain

import UserProfileViewModel
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DrawerContent(
    navController: NavController,
    onClose: () -> Unit,
    userViewModel: UserProfileViewModel
) {
    val userName by userViewModel.firstName
    val email by userViewModel.email
    val profileImage by userViewModel.profileImage

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val fontScale = screenWidth.value / 400f // مقیاس نسبی برای فونت


    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    var enteredPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }


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
            // ✅ کادر پروفایل
            Box(
                modifier = Modifier
                    .size(screenWidth * 0.16f) // ✅ اندازه پویا برای تطابق با دستگاه‌های مختلف
                    .border(
                        width = screenWidth * 0.004f, // ✅ عرض بوردر بر اساس سایز صفحه
                        color = Color(0xff4D869C),
                        shape = RoundedCornerShape(screenWidth * 0.03f) // ✅ گوشه‌های گرد متناسب با صفحه
                    )
                    .clip(RoundedCornerShape(screenWidth * 0.03f)), // ✅ گرد کردن گوشه‌ها
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(screenWidth * 0.13f)
                        .background(Color(0xffDAF8F5), RoundedCornerShape(screenWidth * 0.02f))
                        .clip(RoundedCornerShape(screenWidth * 0.02f))
                ) {
                    when (profileImage) {
                        "profm" -> {
                            Image(
                                painter = painterResource(id = R.drawable.profm),
                                contentDescription = "مرد",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        "profw" -> {
                            Image(
                                painter = painterResource(id = R.drawable.profw),
                                contentDescription = "زن",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                    }
                }

            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(userName, fontWeight = FontWeight.Bold, fontSize = (fontScale * 16).sp,)
                Text(email, fontWeight = FontWeight.Medium, fontSize = (fontScale * 12).sp,)
            }
        }

        Divider(
            color = Color(0xFF4D869C),
            thickness = 2.dp,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        // 📋 آیتم‌های منو
        DrawerItem("ویرایش پروفایل") {
            navController.navigate("profile")
            onClose() // تا منو بسته بشه
        }

        DrawerItem("دوره‌های من") {
            navController.navigate("my_courses_screen")
            onClose() // تا منو بسته بشه}
            }
        DrawerItem("اعلان‌ها", hasSwitch = true)
        DrawerItem("تغییر رمز عبور") {
            navController.navigate("change_password")
            onClose()
        }
        DrawerItem("خروج از حساب کاربری") {
            navController.navigate("logout_screen")
            onClose()
        }
        DrawerItem("ارتباط با ما") { navController.navigate("contact_us")
            onClose()}
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
                .clickable {  showDeleteDialog = true}

        )
    }
    if (showDeleteDialog) {
        Dialog(
            onDismissRequest = { showDeleteDialog = false }
        ) {
            Surface(
                shape = RoundedCornerShape(screenWidth * 0.03f),
                color = Color.White,
                modifier = Modifier
                    .width(screenWidth * 0.85f)
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.padding(screenWidth * 0.05f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "آیا مایل به حذف حساب خود هستید؟",
                        fontSize = (fontScale * 16).sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = iranSans,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(screenHeight * 0.01f))

                    Text(
                        ".برای ادامه، رمز عبور خود را وارد کنید",
                        fontSize = (fontScale * 13).sp,
                        color = Color.Gray,
                        fontFamily = iranSans,
                        textAlign = TextAlign.Right
                    )

                    Spacer(modifier = Modifier.height(screenHeight * 0.015f))

                    OutlinedTextField(
                        value = enteredPassword,
                        onValueChange = {
                            enteredPassword = it
                            passwordError = false
                        },
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "رمز عبور را وارد کنید",
                                textAlign = TextAlign.Right,
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = (screenWidth * 0.04f).value.sp, // ✅ اندازه فونت پویا
                                color = Color(0xBCBCBCBC),
                                fontFamily = iranSans,
                                fontWeight = FontWeight.ExtraLight,
                            )
                        },
                        shape = RoundedCornerShape(screenWidth * 0.03f),
                        isError = passwordError,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeight * 0.065f)
                            .background(Color.White, RoundedCornerShape(screenWidth * 0.025f)),

                                colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (passwordError) Color.Red else Color(0xFF4D869C),
                            unfocusedBorderColor = if (passwordError) Color.Red else Color(0xFF4D869C),
                            cursorColor = Color(0xFF4D869C)
                        ),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(id = if (passwordVisible) R.drawable.eye_open else R.drawable.eye_closed),
                                    tint = Color(0xFF4D869C),
                                    contentDescription = "Toggle Password Visibility",
                                    modifier = Modifier.size(screenWidth * 0.06f)
                                )
                            }
                        },
                    )

                    if (passwordError) {
                        Spacer(modifier = Modifier.height(screenHeight * 0.005f))
                        Text(
                            "❌رمز عبور اشتباه است",
                            color = Color.Red,
                            fontSize = (fontScale * 11).sp,
                            fontFamily = iranSans,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }

                    Spacer(modifier = Modifier.height(screenHeight * 0.03f))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (enteredPassword.isBlank()) {
                                    passwordError = true
                                    return@Button
                                }

                                isLoading = true
                                val auth = FirebaseAuth.getInstance()
                                val user = auth.currentUser
                                val email = user?.email

                                if (user != null && email != null) {
                                    val credential = EmailAuthProvider.getCredential(email, enteredPassword)

                                    user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                                        if (reauthTask.isSuccessful) {
                                            // اول سند Firestore رو پیدا و حذف کن
                                            val db = FirebaseFirestore.getInstance()
                                            db.collection("users")
                                                .whereEqualTo("email", email)
                                                .get()
                                                .addOnSuccessListener { querySnapshot ->
                                                    val doc = querySnapshot.documents.firstOrNull()
                                                    doc?.reference?.delete()?.addOnSuccessListener {
                                                        // بعد از حذف Firestore، از Authentication حذف کن
                                                        user.delete().addOnCompleteListener { deleteTask ->
                                                            isLoading = false
                                                            showDeleteDialog = false
                                                            if (deleteTask.isSuccessful) {
                                                                Toast.makeText(context, "حساب شما حذف شد ✅", Toast.LENGTH_LONG).show()
                                                                navController.navigate("register") {
                                                                    popUpTo(0)
                                                                }
                                                            } else {
                                                                Toast.makeText(context, "❌ خطا در حذف حساب از احراز هویت", Toast.LENGTH_LONG).show()
                                                            }
                                                        }
                                                    } ?: run {
                                                        // اگر سندی پیدا نشد، فقط Authentication رو حذف کن
                                                        user.delete().addOnCompleteListener { deleteTask ->
                                                            isLoading = false
                                                            showDeleteDialog = false
                                                            if (deleteTask.isSuccessful) {
                                                                Toast.makeText(context, "حساب شما حذف شد (بدون اطلاعات در دیتابیس)", Toast.LENGTH_LONG).show()
                                                                navController.navigate("register") {
                                                                    popUpTo(0)
                                                                }
                                                            } else {
                                                                Toast.makeText(context, "❌ خطا در حذف حساب", Toast.LENGTH_LONG).show()
                                                            }
                                                        }
                                                    }
                                                }
                                                .addOnFailureListener {
                                                    isLoading = false
                                                    Toast.makeText(context, "❌ خطا در حذف اطلاعات از پایگاه داده", Toast.LENGTH_LONG).show()
                                                }

                                        } else {
                                            isLoading = false
                                            passwordError = true
                                        }
                                    }
                                } else {
                                    isLoading = false
                                    Toast.makeText(context, "❌ کاربر یافت نشد", Toast.LENGTH_LONG).show()
                                }

                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(screenHeight * 0.065f),
                            shape = RoundedCornerShape(screenWidth * 0.025f),
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(screenWidth * 0.045f),
                                    strokeWidth = 2.dp,
                                    color = Color.Red
                                )
                            } else {
                                Text("حذف حساب", color = Color.Red, fontFamily = iranSans, fontSize = (fontScale * 14).sp,)
                            }
                        }

                        Button(
                            onClick = {
                                showDeleteDialog = false
                                enteredPassword = ""
                                passwordError = false
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(screenHeight * 0.065f),
                            shape = RoundedCornerShape(screenWidth * 0.025f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7AB2B2))
                        ) {
                            Text("لغو", color = Color.White, fontFamily = iranSans, fontSize = (fontScale * 14).sp,)
                        }
                    }
                }
            }
        }
    }


}
@Composable
fun DrawerItem(
    text: String,
    hasSwitch: Boolean = false,
    onClick: () -> Unit = {}
) {
    var switchState by remember { mutableStateOf(true) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val fontScale = configuration.screenWidthDp / 400f

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .width(screenWidth * 0.55f)
                .height(screenHeight * 0.075f)
                .padding(vertical = screenHeight * 0.008f)
                .clickable {
                    if (hasSwitch) {
                        switchState = !switchState
                    } else {
                        onClick()
                    }
                }
                .border(
                    width = screenWidth * 0.005f,
                    color = Color(0xFF4D869C),
                    shape = RoundedCornerShape(screenWidth * 0.03f)
                )
                .background(
                    Color(0xFF90CECE),
                    RoundedCornerShape(screenWidth * 0.03f)
                )
                .padding(
                    horizontal = screenWidth * 0.03f,
                    vertical = screenHeight * 0.015f
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (hasSwitch) {
                Switch(
                    checked = switchState,
                    onCheckedChange = { switchState = it },
                    modifier = Modifier.size(screenWidth * 0.09f),
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
                fontSize = (fontScale * 14).sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
