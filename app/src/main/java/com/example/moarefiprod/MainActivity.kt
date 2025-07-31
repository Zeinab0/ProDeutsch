package com.example.moarefiprod

import RecoverySuccess
import UserProfileViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.ui.SignUpScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.HomeScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.MyCoursesScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseDetailPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.details.DarsDetails
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.MyFlashCardScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.Review.ReviewPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.WordProgressPage
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
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.Review.updateWordStatusInFirestore
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.GameViewModel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.MemoryGamePage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.MultipleChoicePage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.SentenceBuilderPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.TextPicPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie.Movie
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie.MovieDetailScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie.MovieScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story.Story
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story.StoryDetailScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story.StoryReadingScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story.StoryScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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
//            val dummyWords = remember {
//                mutableStateListOf(
//                    Word("سیب", "Apfel", WordStatus.CORRECT),
//                    Word("زود", "Früh", WordStatus.WRONG),
//                    Word("سلام", "Hallo", WordStatus.IDK),
//                    Word("خداحافظ", "Tschüss", WordStatus.NEW),
//                    Word("زینب", "Apfel", WordStatus.CORRECT),
//                    Word("لپتاپ", "Früh", WordStatus.CORRECT),
//                    Word("تپه", "Hallo", WordStatus.CORRECT),
//                    Word("خدا", "Tschüss", WordStatus.NEW),
//                )
//            }

            val viewModel: CourseViewModel = viewModel()
            val userViewModel: UserProfileViewModel = viewModel()
            val gameViewModel: GameViewModel = viewModel()
            LaunchedEffect(Unit) {
                viewModel.loadAllCourses()
            }

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

                composable("MovieScreen") {
                    MovieScreen(navController = navController)
                }
                composable("movie_detail/{movieId}") { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId") ?: return@composable

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
                            videoUrl = it.videoUrl,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                // بخش مربوط به داستان
                composable("StoryScreen") {
                    StoryScreen(navController = navController)
                }
                composable("story_detail/{storyId}") { backStackEntry ->
                    val storyId = backStackEntry.arguments?.getString("storyId") ?: return@composable

                    var story by remember { mutableStateOf<Story?>(null) }

                    LaunchedEffect(storyId) {
                        FirebaseFirestore.getInstance()
                            .collection("stories") // اسم کالکشن درست باشه
                            .document(storyId)
                            .get()
                            .addOnSuccessListener {
                                story = it.toObject(Story::class.java)?.copy(id = it.id)
                            }

                    }

                    story?.let {
                        StoryDetailScreen(
                            id = it.id,
                            title = it.title,
                            author = it.author,
                            price = it.price,
                            summary = it.summary,
                            imageUrl = it.imageUrl,
                            duration = it.duration,
                            onBack = { navController.popBackStack() },
                            navController = navController
                        )
                    } ?: Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                composable(
                    route = "reading_screen/{storyId}?isPurchased={isPurchased}",
                    arguments = listOf(
                        navArgument("storyId") { type = NavType.StringType },
                        navArgument("isPurchased") { type = NavType.BoolType; defaultValue = false }
                    )
                ) {  backStackEntry ->
                    val storyId = backStackEntry.arguments?.getString("storyId") ?: return@composable
                    val isPurchased = backStackEntry.arguments?.getBoolean("isPurchased") ?: false
                    val user = FirebaseAuth.getInstance().currentUser
                    val userId = user?.uid ?: return@composable

                    var story by remember { mutableStateOf<Story?>(null) }
                    var scrollOffset by remember { mutableStateOf(0) }
                    var scrollIndex by remember { mutableStateOf(0) }

                    LaunchedEffect(storyId) {
                        val db = FirebaseFirestore.getInstance()

                        // 📚 دریافت داستان
                        val doc = db.collection("stories").document(storyId).get().await()
                        story = doc.toObject(Story::class.java)?.copy(id = storyId)

                        // 📌 دریافت اطلاعات مطالعه قبلی
                        val progressDoc = db.collection("users")
                            .document(userId)
                            .collection("reading_progress")
                            .document(storyId)
                            .get()
                            .await()

                        scrollIndex = progressDoc.getLong("scrollIndex")?.toInt() ?: 0
                        scrollOffset = progressDoc.getLong("scrollOffset")?.toInt() ?: 0
                    }

                    when {
                        story != null && !story!!.content.isNullOrBlank() -> {
                            StoryReadingScreen(
                                title = story!!.title,
                                content = story!!.content,
                                isPurchased = isPurchased,
                                storyId = storyId,
                                userId = userId,
                                initialScrollOffset = scrollOffset,
                                onBack = { navController.popBackStack() }
                            )

                        }
                        story != null -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "❗ محتوای داستان موجود نیست.",
                                    color = Color.Red,
                                    fontSize = 16.sp,
                                    fontFamily = iranSans
                                )
                            }
                        }
                        else -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }

                // بخش مربوط به داستان


//                composable("my_flashcards") {
//                    MyFlashCardScreen(navController = navController, words = dummyWords)
//                }
                composable("word_progress_page") {
                    WordProgressPage(navController = navController)
                }

                composable("word_list_page") {
                    var currentView by remember { mutableStateOf(WordViewType.CARD) }

                    val parentEntry = remember(it) {
                        navController.getBackStackEntry("word_progress_page")
                    }

                    val allWords = parentEntry.savedStateHandle.get<List<Word>>("all_words") ?: emptyList()

                    WordListPage(
                        words = allWords,
                        selectedView = currentView,
                        onViewChange = { currentView = it },
                        navController = navController
                    )
                }

                composable("review_page") { entry ->
                    val parentEntry = remember(entry) {
                        navController.getBackStackEntry("word_progress_page")
                    }

                    val reviewWords = parentEntry.savedStateHandle.get<List<Word>>("review_words")
                    val cardId = parentEntry.savedStateHandle.get<String>("review_card_id")

                    if (!reviewWords.isNullOrEmpty() && !cardId.isNullOrEmpty()) {
                        ReviewPage(
                            words = reviewWords,
                            navController = navController,
                            onReviewFinished = { updatedWords ->
                                for (updated in updatedWords) {
                                    updateWordStatusInFirestore(cardId, updated)
                                }
                            }
                        )
                    } else {
                        // اگر چیزی نبود، برگرد به صفحه قبل
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
                    ProfileScreen(navController = navController, userViewModel = userViewModel)
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

                composable(
                    route = "sentenceBuilder/{courseId}/{lessonId}/{contentId}/{gameId}",
                    arguments = listOf(
                        navArgument("courseId") { type = NavType.StringType },
                        navArgument("lessonId") { type = NavType.StringType },
                        navArgument("contentId") { type = NavType.StringType },
                        navArgument("gameId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getString("courseId")
                    val lessonId = backStackEntry.arguments?.getString("lessonId")
                    val contentId = backStackEntry.arguments?.getString("contentId")
                    val gameId = backStackEntry.arguments?.getString("gameId")

                    Log.d("SentenceBuilderNav", "Received: courseId=$courseId, lessonId=$lessonId, contentId=$contentId, gameId=$gameId")

                    if (courseId != null && lessonId != null && contentId != null && gameId != null) {
                        SentenceBuilderPage(
                            navController = navController,
                            courseId = courseId,
                            lessonId = lessonId,
                            contentId = contentId,
                            gameId = gameId
                        )
                    } else {
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

                composable(
                    route = "memoryGame/{courseId}/{lessonId}/{contentId}/{gameId}",
                    arguments = listOf(
                        navArgument("courseId") { type = NavType.StringType },
                        navArgument("lessonId") { type = NavType.StringType },
                        navArgument("contentId") { type = NavType.StringType },
                        navArgument("gameId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getString("courseId")
                    val lessonId = backStackEntry.arguments?.getString("lessonId")
                    val contentId = backStackEntry.arguments?.getString("contentId")
                    val gameId = backStackEntry.arguments?.getString("gameId")

                    Log.d("MemoryGameNav", "Received: courseId=$courseId, lessonId=$lessonId, contentId=$contentId, gameId=$gameId")

                    if (courseId != null && lessonId != null && contentId != null && gameId != null) {
                        MemoryGamePage(
                            navController = navController,
                            courseId = courseId,
                            lessonId = lessonId,
                            contentId = contentId,
                            gameId = gameId,
                            viewModel = viewModel()
                        )
                    } else {
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
                composable(
                    route = "multipleChoice/{topicId}/{gameIndex}",
                    arguments = listOf(
                        navArgument("topicId") { type = NavType.StringType },
                        navArgument("gameIndex") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val topicId = backStackEntry.arguments?.getString("topicId")
                    val gameIndex = backStackEntry.arguments?.getInt("gameIndex") ?: 0

                    Log.d("MultipleChoiceNav", "Received: topicId=$topicId, gameIndex=$gameIndex")

                    if (topicId != null) {
                        MultipleChoicePage(
                            navController = navController,
                            topicId = topicId,
                            gameIndex = gameIndex,
                            viewModel = gameViewModel // انتقال ViewModel از NavHost
                        )
                    } else {
                        Text(
                            "خطا: شناسه مبحث گرامر یافت نشد.",
                            color = Color.Red,
                            fontFamily = iranSans
                        )
                        LaunchedEffect(Unit) {
                            navController.popBackStack()
                        }
                    }
                }
                composable(
                    route = "textPic/{courseId}/{lessonId}/{contentId}/{gameId}", // حذف gameIndex از مسیر
                    arguments = listOf(
                        navArgument("courseId") { type = NavType.StringType },
                        navArgument("lessonId") { type = NavType.StringType },
                        navArgument("contentId") { type = NavType.StringType },
                        navArgument("gameId") { type = NavType.StringType }
                        // navArgument("gameIndex") { type = NavType.IntType; defaultValue = 0 } // حذف این خط
                    )
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                    val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                    val contentId = backStackEntry.arguments?.getString("contentId") ?: ""
                    val gameId = backStackEntry.arguments?.getString("gameId") ?: ""
                    // val gameIndex = backStackEntry.arguments?.getInt("gameIndex") ?: 0 // حذف این خط

                    Log.d("TextPicNav", "Received: courseId=$courseId, lessonId=$lessonId, contentId=$contentId, gameId=$gameId")
                    // Log.d("TextPicNav", "Received: gameIndex=$gameIndex") // حذف این خط

                    if (courseId.isNotEmpty() && lessonId.isNotEmpty() && contentId.isNotEmpty() && gameId.isNotEmpty()) {
                        TextPicPage(
                            navController = navController,
                            courseId = courseId,
                            lessonId = lessonId,
                            contentId = contentId,
                            gameId = gameId,
                            viewModel = gameViewModel // پاس دادن GameViewModel مشترک
                            // gameIndex = gameIndex // حذف این خط
                        )
                    } else {
                        Text(
                            "خطا: شناسه‌های لازم برای بازی یافت نشد.",
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