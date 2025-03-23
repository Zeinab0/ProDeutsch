package com.example.moarefiprod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.HomeScreen

// ✅ تعریف فونت مستقیم در فایل
val iranSans = FontFamily(
    Font(R.font.iransans_bold, FontWeight.Bold),
    Font(R.font.iransans_light, FontWeight.Light),
    Font(R.font.iransans_medium, FontWeight.Medium)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ✅ نسخه جدید که `HomeScreen()` را نمایش می‌دهد
            HomeScreen()

            // ✅ نسخه قبلی که شامل سیستم ناوبری بود (کامنت شده)

//            val navController = rememberNavController()
//
//            NavHost(navController = navController, startDestination = "firstLogo") {
//                composable("firstLogo") {
//                    Firstlogopage(
//                        onNavigateToLogin = { navController.navigate("advertisement1") }
//                    )
//                }
//                composable("advertisement1") {
//                    Advertisement(
//                        onNext = { navController.navigate("advertisement2") },
//                        onSkip = { navController.navigate("login") }
//                    )
//                }
//                composable("advertisement2") {
//                    Advertisement2(
//                        onNext = { navController.navigate("advertisement3") },
//                        onSkip = { navController.navigate("login") }
//                    )
//                }
//                composable("advertisement3") {
//                    Advertisement3(
//                        onNext = { navController.navigate("login") },
//                        onSkip = { navController.navigate("login") }
//                    )
//                }
//                composable("login") {
//                    LoginScreen(
//                        onNavigateToRegister = { navController.navigate("register") },
//                        onNavigateToRecovery = { navController.navigate("recovery") } // ✅ اضافه کردن مسیر بازیابی
//                    )
//                }
//                composable("register") {
//                    SignUpScreen(
//                        onNavigateToLogin = { navController.navigate("login") }
//                    )
//                }
//                composable("recovery") {
//                    RecoveryE(navController = navController) // ✅ ارسال navController به صفحه بازیابی
//                }
//                composable("codeScreen") {
//                    RecoveryC(navController = navController) // ✅ صفحه جدید برای کد
//                }
//                composable("changeScreen") {
//                    RecoveryChange(navController = navController) // ✅ صفحه جدید برای کد
//                }
//                composable("changepassecsess") {
//                    RecoverySuccess(navController = navController) // ✅ صفحه جدید برای کد
//                }
//            }
        }
    }
}
