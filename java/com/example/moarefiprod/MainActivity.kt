package com.example.moarefiprod

import RecoverySuccess
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moarefiprod.R
import com.example.moarefiprod.ui.SignUpScreen
import com.example.moarefiprod.ui.theme.Login.LoginScreen
import com.example.moarefiprod.ui.theme.Recoverypass.RecoveryC
import com.example.moarefiprod.ui.theme.Recoverypass.RecoveryChange
import com.example.moarefiprod.ui.theme.Recoverypass.RecoveryE
import com.example.moarefiprod.ui.theme.logofirst.Advertisement
import com.example.moarefiprod.ui.theme.logofirst.Advertisement2
import com.example.moarefiprod.ui.theme.logofirst.Advertisement3
import com.example.moarefiprod.ui.theme.logofirst.Firstlogopage

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
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "firstLogo") {
                composable("firstLogo") {
                    Firstlogopage(
                        onNavigateToLogin = { navController.navigate("advertisement1") }
                    )
                }
                composable("advertisement1") {
                    Advertisement(
                        onNext = { navController.navigate("advertisement2") },
                        onSkip = { navController.navigate("login") }
                    )
                }
                composable("advertisement2") {
                    Advertisement2(
                        onNext = { navController.navigate("advertisement3") },
                        onSkip = { navController.navigate("login") }
                    )
                }
                composable("advertisement3") {
                    Advertisement3(
                        onNext = { navController.navigate("login") },
                        onSkip = { navController.navigate("login") }
                    )
                }
                composable("login") {
                    LoginScreen(
                        onNavigateToRegister = { navController.navigate("register") },
                        onNavigateToRecovery = { navController.navigate("recovery") } // ✅ اضافه کردن مسیر بازیابی
                    )
                }
                composable("register") {
                    SignUpScreen(
                        onNavigateToLogin = { navController.navigate("login") }
                    )
                }
                composable("recovery") {
                    RecoveryE(navController = navController) // ✅ ارسال navController به صفحه بازیابی
                }
                composable("codeScreen") {
                    RecoveryC(navController = navController) // ✅ صفحه جدید برای کد
                }
                composable("changeScreen") {
                    RecoveryChange(navController = navController) // ✅ صفحه جدید برای کد
                }
                composable("changepassecsess") {
                    RecoverySuccess(navController = navController) // ✅ صفحه جدید برای کد
                }


            }
        }
    }
}
