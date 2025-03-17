package com.example.moarefiprod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moarefiprod.R
import com.example.moarefiprod.ui.SignUpScreen
import com.example.moarefiprod.ui.theme.Login.LoginScreen
import com.example.moarefiprod.ui.theme.logofirst.Advertisement
import com.example.moarefiprod.ui.theme.logofirst.Advertisement2
import com.example.moarefiprod.ui.theme.logofirst.Advertisement3
import com.example.moarefiprod.ui.theme.logofirst.Firstlogopage

// تعریف فونت مستقیم در فایل
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

            NavHost(navController = navController, startDestination = "firstLogo") { // ✅ اولین صفحه: لوگو
                composable("firstLogo") {
                    Firstlogopage(
                        onNavigateToLogin = { navController.navigate("advertisement1") } // ✅ بعد از نمایش لوگو، به تبلیغات برو
                    )
                }
                composable("advertisement1") {
                    Advertisement(
                        onNext = { navController.navigate("advertisement2") }, // ✅ رفتن به تبلیغ دوم
                        onSkip = { navController.navigate("login") } // ✅ رد کردن و رفتن به ورود
                    )
                }
                composable("advertisement2") {
                    Advertisement2( // ✅ نمایش تبلیغ دوم به جای دوباره `Advertisement`
                        onNext = { navController.navigate("advertisement3") }, // ✅ رفتن به تبلیغ سوم
                        onSkip = { navController.navigate("login") } // ✅ رد کردن و رفتن به ورود
                    )
                }
                composable("advertisement3") {
                    Advertisement3( // ✅ نمایش تبلیغ سوم
                        onNext = { navController.navigate("login") }, // ✅ بعد از تبلیغ سوم، ورود
                        onSkip = { navController.navigate("login") } // ✅ رد کردن و رفتن به ورود
                    )
                }
                composable("login") {
                    LoginScreen(
                        onNavigateToRegister = { navController.navigate("register") }
                    )
                }
                composable("register") {
                    SignUpScreen(
                        onNavigateToLogin = { navController.navigate("login") }
                    )
                }
            }
        }
    }
}
