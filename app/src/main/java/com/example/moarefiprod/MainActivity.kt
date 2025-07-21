package com.example.moarefiprod

import RecoverySuccess
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moarefiprod.data.allAppCourses
import com.example.moarefiprod.ui.SignUpScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.HomeScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.MyCoursesScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Course
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseDetailPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.details.DarsDetails
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.MyFlashCardScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.Review.ReviewPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.WordProgressPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.WordStatus
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.allcartlist.WordListPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.allcartlist.WordViewType
//import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton.AboutUsScreen
//import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton.AboutUsScreen
//import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton.ProfileScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton.ChangePasswordScreen
// ادامه‌ی سایر importها به همان شکل قبل
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton.AboutUsScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton.ContactUsScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton.LogoutScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton.ProfileScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.SentenceBuilder.SentenceBuilderPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.memorygames.WordMatchPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.GrammarPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page.HörenPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören.HörenLevelDetailPage
import com.example.moarefiprod.ui.theme.Login.LoginScreen
import com.example.moarefiprod.ui.theme.Recoverypass.RecoveryC
import com.example.moarefiprod.ui.theme.Recoverypass.RecoveryChange
import com.example.moarefiprod.ui.theme.Recoverypass.RecoveryE
import com.example.moarefiprod.ui.theme.logofirst.Advertisement
import com.example.moarefiprod.ui.theme.logofirst.Advertisement2
import com.example.moarefiprod.ui.theme.logofirst.Advertisement3
import com.example.moarefiprod.ui.theme.logofirst.Firstlogopage
import com.google.firebase.auth.FirebaseAuth
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page.AudioTestScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.tamrinpage


val iranSans = FontFamily(
    Font(R.font.iransans_bold, FontWeight.Bold),
    Font(R.font.iransans_light, FontWeight.Light),
    Font(R.font.iransans_medium, FontWeight.Medium)
)

class MainActivity : ComponentActivity() {
    @SuppressLint("ComposableDestinationInComposeScope")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null

        setContent {
            //زینب خانم، خانمم خواستی بازی اولی رو ببینی فقط همین یه خط رو فعال کن بقیه رو غیر فعال
//             WordMatchPage(navController = rememberNavController())
//
//            SentenceBuilderPage(navController = rememberNavController())

            //اینا رو غیر فعال کن تا بسته شدن پرانتز ست کانتنت

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
                        isLoggedIn = isUserLoggedIn,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                popUpTo("firstLogo") { inclusive = true }
                            }
                        }
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
                        navController = navController,
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
                composable("grammar_page") {
                    GrammarPage(navController = navController)
                }
                composable("hören_page") {
                    HörenPage(navController = navController)
                }
                composable("hören_level/{level}") { backStackEntry ->
                    val level = backStackEntry.arguments?.getString("level") ?: "A1"
                    HörenLevelDetailPage(navController = navController, level = level)
                }

                composable(
                    "audio_test/{level}/{exerciseId}",
                    arguments = listOf(
                        navArgument("level") { type = NavType.StringType },
                        navArgument("exerciseId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val level = backStackEntry.arguments?.getString("level") ?: ""
                    val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: ""

                    AudioTestScreen(navController, level = level, exerciseId = exerciseId)
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
                composable("tamrin_page_route") {
                    tamrinpage(navController = navController)
                }
                composable("profile") {
                    ProfileScreen(navController = navController)
                }

                // ✅ مسیر جدید برای صفحه تغییر رمز عبور
                composable("change_password") {
                    ChangePasswordScreen(navController = navController)
                }
                composable("logout_screen") {
                    LogoutScreen(navController = navController)
                }
// داخل NavHost
                composable("contact_us") {
                    ContactUsScreen(navController = navController)
                }
                composable("about_us") {
                    AboutUsScreen(navController = navController)
                }
                composable("my_courses_screen") {
                    MyCoursesScreen(navController = navController)
                }
                // حالا courseTitle را به عنوان NavArgument دریافت می‌کنیم.
                composable(
                    route = "course_detail/{courseTitle}", // ⬅️ مسیر را تغییر دادیم
                    arguments = listOf(navArgument("courseTitle") { type = NavType.StringType })
                ) { backStackEntry ->
                    val courseTitle = backStackEntry.arguments?.getString("courseTitle")

                    // Course را از allAppCourses پیدا می‌کنیم
                    val course = remember(courseTitle) {
                        allAppCourses.find { it.title == courseTitle }
                    }

                    if (course != null) {
                        CourseDetailPage(navController = navController, course = course) // ⬅️ Course را به CourseDetailPage پاس می‌دهیم
                    } else {
                        // در صورتی که دوره پیدا نشد، پیام خطا می‌دهیم.
                        // این به جای خطای اولیه "اطلاعات دوره پیدا نشد" در CourseDetailPage نمایش داده می‌شود.
                        Text("خطا: اطلاعات دوره با عنوان '$courseTitle' پیدا نشد.",
                            color = Color.Red, // از Color.Red استفاده کنید
                            fontFamily = iranSans)
                        LaunchedEffect(Unit) {
                            // می‌توانید به صفحه قبل برگردید
                            // navController.popBackStack()
                        }
                    }
                }

                // 🔴🔴🔴 این Composable برای lesson_detail کاملا صحیح است و نیازی به تغییر ندارد 🔴🔴🔴
                composable(
                    route = "lesson_detail/{courseTitle}/{lessonId}",
                    arguments = listOf(
                        navArgument("courseTitle") { type = NavType.StringType },
                        navArgument("lessonId") { type = NavType.StringType } // این آرگومان "navArgument" نیست، بلکه "lessonId" است.
                    )
                ) { backStackEntry ->
                    val courseTitle = backStackEntry.arguments?.getString("courseTitle")
                    val lessonId = backStackEntry.arguments?.getString("lessonId")

                    val parentCourse = remember(courseTitle) {
                        allAppCourses.find { it.title == courseTitle }
                    }

                    if (parentCourse != null && lessonId != null) {
                        val lesson = remember(parentCourse, lessonId) {
                            parentCourse.lessons.find { it.id == lessonId }
                        }
                        if (lesson != null) {
                            DarsDetails(lesson = lesson, navController = navController)
                        } else {
                            Text("درس با شناسه '$lessonId' در دوره '$courseTitle' پیدا نشد.",
                                color = Color.Red,
                                fontFamily = iranSans)
                        }
                    } else {
                        Text("اطلاعات دوره یا درس در مسیریابی ناقص است.",
                            color = Color.Red,
                            fontFamily = iranSans)
                    }
                }
            }
        }
    }
}
//                composable(
//                    route = "lesson_detail/{lessonId}",
//                    arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
//                ) { backStackEntry ->
//                    val lessonId = backStackEntry.arguments?.getString("lessonId")
//                    val course = navController.previousBackStackEntry?.savedStateHandle?.get<Course>("course")
//                    val lesson = course?.lessons?.find { it.id == lessonId }
//
//                    lesson?.let {
//                        DarsDetails(lesson = it, navController = navController)
//                    }
//                }
//            }
//        }
//    }
//}
