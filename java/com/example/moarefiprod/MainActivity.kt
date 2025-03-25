package com.example.moarefiprod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.HomeScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.WordProgressPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.WordStatus

// ✅ تعریف فونت مستقیم در فایل
val iranSans = FontFamily(
    Font(R.font.iransans_bold, FontWeight.Bold),
    Font(R.font.iransans_light, FontWeight.Light),
    Font(R.font.iransans_medium, FontWeight.Medium)
)
val dummyWords = listOf(
    Word("سیب", "Apfel", WordStatus.CORRECT),
    Word("زود", "Früh", WordStatus.WRONG),
    Word("سلام", "Hallo", WordStatus.IDK),
    Word("خداحافظ", "Tschüss", WordStatus.NEW),
    Word("زینب", "Apfel", WordStatus.CORRECT),
    Word("لپتاپ", "Früh", WordStatus.CORRECT),
    Word("تپه", "Hallo", WordStatus.CORRECT),
    Word("خدا", "Tschüss", WordStatus.NEW),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "progress") {
                composable("progress") {
                    WordProgressPage(words = dummyWords, navController = navController)
                }
            }
            // ✅ نسخه جدید که `HomeScreen()` را نمایش می‌دهد
//            HomeScreen()

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
