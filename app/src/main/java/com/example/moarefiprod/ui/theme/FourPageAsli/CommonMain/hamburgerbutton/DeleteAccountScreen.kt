package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moarefiprod.iranSans
import kotlinx.coroutines.delay

@Composable
fun DeleteAccountScreen(navController: NavController) {
    var showConfirmDialog by remember { mutableStateOf(true) }
    var showLoadingDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        DeleteAccountDialog(
            onDismiss = {
                showConfirmDialog = false
                navController.navigate("home") {
                    popUpTo("delete_account") { inclusive = true }
                }
            },
            onConfirmDelete = {
                showConfirmDialog = false
                showLoadingDialog = true
            }
        )
    }

    if (showLoadingDialog) {
        DeletingAccountDialog()

        LaunchedEffect(Unit) {
            delay(2000L)
            showLoadingDialog = false
            showSuccessDialog = true
        }
    }

    if (showSuccessDialog) {
        SuccessDeleteDialog {
            showSuccessDialog = false
            navController.navigate("register") {
                popUpTo(0)
            }
        }
    }
}

@Composable
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.width(320.dp) // 👈 بزرگ‌ترش کردیم
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "آیا مایل به حذف حساب خود هستید؟",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "در صورت حذف، تمامی دوره‌های خریداری‌شده حذف خواهند شد.",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontFamily = iranSans,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // 🔄 جای دکمه‌ها عوض شده
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    Button(
                        onClick = onConfirmDelete,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .border(1.dp, Color.LightGray, shape = RoundedCornerShape(12.dp)), // 👈 قابل تنظیم
                        shape = RoundedCornerShape(12.dp), // 👈 حتما اضافه کن!
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("حذف حساب", color = Color.Red, fontFamily = iranSans)
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp), // فقط اگه ارور نده
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7AB2B2))
                    ) {
                        Text("لغو", color = Color.White, fontFamily = iranSans)
                    }


                }
            }
        }
    }
}

@Composable
fun DeletingAccountDialog() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.width(300.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "لطفاً صبر کنید... در حال حذف حساب کاربری شما هستیم",
                    fontSize = 14.sp,
                    fontFamily = iranSans,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(3) { index ->
                        val visible = remember { mutableStateOf(true) }
                        LaunchedEffect(Unit) {
                            delay(index * 150L)
                            while (true) {
                                visible.value = !visible.value
                                delay(300L)
                            }
                        }
                        AnimatedVisibility(visible.value) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color.Gray, shape = RoundedCornerShape(50))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessDeleteDialog(onConfirm: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.width(300.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "حساب شما با موفقیت حذف شد",
                    fontSize = 14.sp,
                    fontFamily = iranSans,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C))
                ) {
                    Text("تأیید", color = Color.White, fontFamily = iranSans)
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DeleteAccountScreenPreview() {
    DeleteAccountScreen(navController = rememberNavController())
}

