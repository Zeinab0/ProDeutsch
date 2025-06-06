package com.example.moarefiprod

import RecoverySuccess
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moarefiprod.ui.SignUpScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.HomeScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.MyFlashCardScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.Review.ReviewPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.WordProgressPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.WordStatus
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.allcartlist.WordListPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.allcartlist.WordViewType
import com.example.moarefiprod.ui.theme.Login.LoginScreen
import com.example.moarefiprod.ui.theme.Recoverypass.RecoveryC
import com.example.moarefiprod.ui.theme.Recoverypass.RecoveryChange
import com.example.moarefiprod.ui.theme.Recoverypass.RecoveryE
import com.example.moarefiprod.ui.theme.logofirst.Advertisement
import com.example.moarefiprod.ui.theme.logofirst.Advertisement2
import com.example.moarefiprod.ui.theme.logofirst.Advertisement3
import com.example.moarefiprod.ui.theme.logofirst.Firstlogopage

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
            val dummyWords = remember {
                mutableStateListOf(
                    Word("سیب", "Apfel", WordStatus.CORRECT),
                    Word("زود", "Früh", WordStatus.WRONG),
                    Word("سلام", "Hallo", WordStatus.IDK),
                    Word("خداحافظ", "Tschüss", WordStatus.NEW),
                    Word("زینب", "Apfel", WordStatus.CORRECT),
                    Word("لپتاپ", "Früh", WordStatus.CORRECT),
                    Word("تپه", "Hallo", WordStatus.CORRECT),
                    Word("خدا", "Tschüss", WordStatus.NEW),
                )
            }

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
                        onNavigateToRecovery = { navController.navigate("recovery") }
                    )
                }
                composable("register") {
                    SignUpScreen(
                        onNavigateToLogin = { navController.navigate("login") },
                        onSignUpSuccess = { navController.navigate("home") }
                    )
                }
                composable("recovery") {
                    RecoveryE(navController = navController)
                }
                composable("codeScreen") {
                    RecoveryC(navController = navController)
                }
                composable("changeScreen") {
                    RecoveryChange(navController = navController)
                }
                composable("changepassecsess") {
                    RecoverySuccess(navController = navController)
                }
                composable("home") {
                    HomeScreen(navController = navController)
                }
                composable("my_flashcards") {
                    MyFlashCardScreen(navController = navController, words = dummyWords)
                }
                composable("word_progress_page") {
                    val updatedWords = navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<List<Word>>("updated_words")

                    WordProgressPage(
                        words = updatedWords ?: dummyWords,
                        navController = navController
                    )
                }
                composable("word_list_page") {
                    var currentView by remember { mutableStateOf(WordViewType.CARD) }

                    WordListPage(
                        words = dummyWords,
                        selectedView = currentView,
                        onViewChange = { currentView = it },
                        navController = navController
                    )
                }
                composable("review_page") { entry ->
                    val parentEntry = remember(entry) {
                        navController.getBackStackEntry("word_progress_page")
                    }

                    val reviewWords = parentEntry
                        .savedStateHandle
                        .get<List<Word>>("review_words")

                    if (!reviewWords.isNullOrEmpty()) {
                        ReviewPage(
                            words = reviewWords,
                            navController = navController,
                            onReviewFinished = { updatedWords ->
                                for (updated in updatedWords) {
                                    val index = dummyWords.indexOfFirst { it.german == updated.german }
                                    if (index != -1) {
                                        dummyWords[index] = updated
                                    }
                                }
                            }
                        )
                    } else {
                        LaunchedEffect(Unit) {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}
