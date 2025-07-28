package com.example.moarefiprod

import RecoverySuccess
import UserProfileViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example.moarefiprod.ui.SignUpScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.HomeScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.MyCoursesScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseDetailPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.details.DarsDetails
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.MyFlashCardScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.Review.ReviewPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.WordProgressPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.WordStatus
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.allcartlist.WordListPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.allcartlist.WordViewType
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton.AboutUsScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton.ChangePasswordScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton.ContactUsScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton.LogoutScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton.ProfileScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.courspage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.GrammarPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page.AudioTestScreen
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
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseViewModel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.SentenceBuilderPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie.Movie
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie.MovieDetailScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie.MovieScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

            val viewModel: CourseViewModel = viewModel()
            val userViewModel: UserProfileViewModel = viewModel()

            // لود اولیه داده‌ها
            LaunchedEffect(Unit) {
                viewModel.loadAllCourses()
            }

            // جمع‌آوری StateFlow به‌صورت State
            val allCourses by viewModel.allCourses.collectAsState()


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
                    HomeScreen(navController = navController, userViewModel = userViewModel)
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


                // بخش مربوط به فیلم
                composable("MovieScreen") {
                    MovieScreen(navController = navController)
                }
                composable("movie_detail/{movieId}") { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId") ?: return@composable

                    // فراخوانی دیتای همون فیلم
                    var movie by remember { mutableStateOf<Movie?>(null) }

                    LaunchedEffect(movieId) {
                        FirebaseFirestore.getInstance()
                            .collection("movies")
                            .document(movieId)
                            .get()
                            .addOnSuccessListener {
                                movie = it.toObject(Movie::class.java)
                            }
                    }

                    movie?.let {
                        MovieDetailScreen(
                            title = it.title,
                            level = it.level,
                            price = it.price,
                            description = it.description,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
                // بخش مربوط به فیلم


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
                        ?.get<List<Word>>("review_words")

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
                    courspage(
                        onShowDialog = {},
                        navController = navController
                    )
                }
                composable("profile") {
                    ProfileScreen(navController = navController, userViewModel = userViewModel,)
                }

                composable("change_password") {
                    ChangePasswordScreen(navController = navController)
                }
                composable("logout_screen") {
                    LogoutScreen(navController = navController)
                }
                composable("contact_us") {
                    ContactUsScreen(navController = navController)
                }
                composable("about_us") {
                    AboutUsScreen(navController = navController)
                }
                composable("my_courses_screen") {
                    MyCoursesScreen(navController = navController)
                }

                composable(
                    route = "course_detail/{courseId}",
                    arguments = listOf(navArgument("courseId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getString("courseId")
                    Log.d("CourseDetailPageNav", "Received courseId: $courseId")
                    if (courseId != null) {
                        CourseDetailPage(navController = navController, courseId = courseId)
                    } else {
                        Text(
                            "خطا: شناسه دوره یافت نشد.",
                            color = Color.Red,
                            fontFamily = iranSans
                        )
                        LaunchedEffect(Unit) {
                            navController.popBackStack()
                        }
                    }
                }

                composable(
                    route = "lesson_detail/{courseId}/{lessonId}",
                    arguments = listOf(
                        navArgument("courseId") { type = NavType.StringType },
                        navArgument("lessonId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getString("courseId")
                    val lessonId = backStackEntry.arguments?.getString("lessonId")
                    Log.d("LessonDetailNav", "Received courseId: $courseId, lessonId: $lessonId")
                    if (courseId != null && lessonId != null) {
                        DarsDetails(navController = navController, courseId = courseId, lessonId = lessonId)
                    } else {
                        Text(
                            "خطا: شناسه دوره یا درس در مسیریابی ناقص است.",
                            color = Color.Red,
                            fontFamily = iranSans
                        )
                        LaunchedEffect(Unit) {
                            navController.popBackStack()
                        }
                    }
                }

                // در MainActivity.kt، داخل NavHost
                composable(
                    route = "sentenceBuilder/{courseId}/{lessonId}/{contentId}/{gameId}", // <--- مسیر جدید با چهار آرگومان
                    arguments = listOf( // <--- لیست آرگومان‌های جدید
                        navArgument("courseId") { type = NavType.StringType },
                        navArgument("lessonId") { type = NavType.StringType },
                        navArgument("contentId") { type = NavType.StringType },
                        navArgument("gameId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    // دریافت آرگومان‌ها از مسیر
                    val courseId = backStackEntry.arguments?.getString("courseId")
                    val lessonId = backStackEntry.arguments?.getString("lessonId")
                    val contentId = backStackEntry.arguments?.getString("contentId")
                    val gameId = backStackEntry.arguments?.getString("gameId")

                    Log.d("SentenceBuilderNav", "Received: courseId=$courseId, lessonId=$lessonId, contentId=$contentId, gameId=$gameId")

                    // بررسی اینکه همه آرگومان‌ها معتبر هستند
                    if (courseId != null && lessonId != null && contentId != null && gameId != null) {
                        SentenceBuilderPage(
                            navController = navController,
                            courseId = courseId,    // <--- ارسال courseId
                            lessonId = lessonId,    // <--- ارسال lessonId
                            contentId = contentId,  // <--- ارسال contentId
                            gameId = gameId         // <--- ارسال gameId
                        )
                    } else {
                        // در صورت ناقص بودن آرگومان‌ها، پیام خطا نمایش داده و به عقب برمی‌گردیم
                        Text(
                            "خطا: شناسه های لازم برای بازی یافت نشد.",
                            color = Color.Red,
                            fontFamily = iranSans
                        )
                        LaunchedEffect(Unit) {
                            navController.popBackStack()
                        }
                    }
                }


            }
        }
    }
}