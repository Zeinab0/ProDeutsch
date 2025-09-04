package com.example.moarefiprod

import RecoverySuccess
import UserProfileViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.ui.SignUpScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.HomeScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.MyCoursesScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseDetailPage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Dars.DarsDetails
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
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Dars.Jozve
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Dars.VideoScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Dars.WordsScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.MyFlashCardScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.Review.updateWordStatusInFirestore
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game.GameHost
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game.GrammerGameViewModel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie.Movie
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie.MovieDetailScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie.MovieScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music.Famous
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music.MusicDetailScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music.MusicScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music.Singer
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music.SingerDetailScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music.faveriteScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story.Story
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story.StoryDetailScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story.StoryReadingScreen
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story.StoryScreen
import com.example.moarefiprod.ui.theme.logofirst.OnboardingViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.net.URLDecoder

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

        try {
            val pm = packageManager
            val pi = if (android.os.Build.VERSION.SDK_INT >= 28) {
                pm.getPackageInfo(packageName, android.content.pm.PackageManager.GET_SIGNING_CERTIFICATES)
            } else {
                @Suppress("DEPRECATION")
                pm.getPackageInfo(packageName, android.content.pm.PackageManager.GET_SIGNATURES)
            }

            val certBytes: ByteArray = if (android.os.Build.VERSION.SDK_INT >= 28) {
                val signingInfo = pi.signingInfo
                if (signingInfo != null && signingInfo.apkContentsSigners.isNotEmpty()) {
                    signingInfo.apkContentsSigners[0].toByteArray()
                } else {
                    throw Exception("No signing certificate found (API 28+)")
                }
            } else {
                @Suppress("DEPRECATION")
                val sigs = pi.signatures
                if (sigs != null && sigs.isNotEmpty()) {
                    sigs[0].toByteArray()
                } else {
                    throw Exception("No signing certificate found (API <28)")
                }
            }

            val md = java.security.MessageDigest.getInstance("SHA1")
            val digest = md.digest(certBytes)
            val sha1 = digest.joinToString(":") { "%02X".format(it) }
            Log.e("FBCHK", "runtimeSHA1=$sha1")

        } catch (e: Exception) {
            Log.e("FBCHK", "Error reading SHA1", e)
        }


        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                val navController = rememberNavController()
                val viewModel: CourseViewModel = viewModel()
                val userViewModel: UserProfileViewModel = viewModel()
                val onboardingViewModel: OnboardingViewModel = viewModel()
                LaunchedEffect(Unit) {
                    viewModel.loadAllCourses()
                }

                LaunchedEffect(Unit) {
                    viewModel.loadAllCourses()
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
                            onSkip = { navController.navigate("login") },
                            currentPage = onboardingViewModel.currentPage, // پاس دادن currentPage
                            onPageChange = { onboardingViewModel.currentPage = it } // برای به‌روزرسانی
                        )
                    }
                    composable("advertisement2") {
                        Advertisement2(
                            onNext = { navController.navigate("advertisement3") },
                            onSkip = { navController.navigate("login") },
                            currentPage = onboardingViewModel.currentPage,
                            onPageChange = { onboardingViewModel.currentPage = it }
                        )
                    }
                    composable("advertisement3") {
                        Advertisement3(
                            onNext = { navController.navigate("login") },
                            onSkip = { navController.navigate("login") },
                            currentPage = onboardingViewModel.currentPage,
                            onPageChange = { onboardingViewModel.currentPage = it }
                        )
                    }
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            onNavigateToRegister = { navController.navigate("register") },
                            onNavigateToRecovery = { navController.navigate("recovery") },
                            userViewModel = userViewModel
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

                    composable("my_flashcards") {
                        MyFlashCardScreen(navController = navController) // فعلاً لیست خالی
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
                        var loading by remember { mutableStateOf(true) }

                        // فقط برای نمایش، از خود داکیومنت می‌خونیم
                        LaunchedEffect(movieId) {
                            FirebaseFirestore.getInstance()
                                .collection("movies")
                                .document(movieId)
                                .get()
                                .addOnSuccessListener { snap ->
                                    movie = snap.toObject(Movie::class.java)
                                    loading = false
                                }
                                .addOnFailureListener { _ ->
                                    loading = false
                                }
                        }

                        when {
                            loading -> {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            }
                            movie != null -> {
                                MovieDetailScreen(
                                    movieId   = movieId,           // 👈 مهم: از route پاس بده، نه it.id
                                    title     = movie!!.title,
                                    description = movie!!.description,
                                    level     = movie!!.level,
                                    price     = movie!!.price,
                                    videoUrl  = movie!!.videoUrl,
                                    imageUrl  = movie!!.imageUrl,
                                    onBack    = { navController.popBackStack() }
                                )
                            }
                            else -> {
                                // هندل خطا/عدم وجود
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("فیلم پیدا نشد", fontWeight = FontWeight.Bold)
                                }
                            }
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
                    composable("MusicScreen") {
                        MusicScreen(navController = navController)
                    }

                    composable("detail/{songId}") { backStackEntry ->
                        val songId = backStackEntry.arguments?.getString("songId") ?: ""
                        MusicDetailScreen(songId = songId, navController = navController)
                    }
                    composable("favorites") {
                        faveriteScreen(navController = navController)
                    }
                    composable("famous_singers") {
                        Famous(navController)
                    }
                    composable(
                        route = "singerDetail/{name}/{imageUrl}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("imageUrl") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val encodedUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
                        val imageUrl = URLDecoder.decode(encodedUrl, "UTF-8")

                        val singer = Singer(id = "", name = name, imageUrl = imageUrl)

                        SingerDetailScreen(
                            singer = singer,
                            navController = navController
                        )
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
                        route = "darsDetails/{courseId}/{lessonId}",
                        arguments = listOf(
                            navArgument("courseId") { type = NavType.StringType },
                            navArgument("lessonId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                        val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""

                        DarsDetails(navController = navController, courseId = courseId, lessonId = lessonId)
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

//                composable(
//                    route = "sentenceBuilder/{courseId}/{lessonId}/{contentId}/{gameId}?gameIndex={gameIndex}", // اضافه کردن gameIndex
//                    arguments = listOf(
//                        navArgument("courseId") { type = NavType.StringType },
//                        navArgument("lessonId") { type = NavType.StringType },
//                        navArgument("contentId") { type = NavType.StringType },
//                        navArgument("gameId") { type = NavType.StringType },
//                        navArgument("gameIndex") { type = NavType.IntType; defaultValue = 0 } // مقدار پیش‌فرض
//                    )
//                ) { backStackEntry ->
//                    val courseId = backStackEntry.arguments?.getString("courseId")
//                    val lessonId = backStackEntry.arguments?.getString("lessonId")
//                    val contentId = backStackEntry.arguments?.getString("contentId")
//                    val gameId = backStackEntry.arguments?.getString("gameId")
//                    val gameIndex = backStackEntry.arguments?.getInt("gameIndex") ?: 0 // دریافت gameIndex
//
//                    Log.d("SentenceBuilderNav", "Received: courseId=$courseId, lessonId=$lessonId, contentId=$contentId, gameId=$gameId, gameIndex=$gameIndex")
//
//                    if (courseId != null && lessonId != null && contentId != null && gameId != null) {
//                        SentenceBuilderPage(
//                            navController = navController,
//                            courseId = courseId,
//                            lessonId = lessonId,
//                            contentId = contentId,
//                            gameId = gameId,
//                            gameIndex = gameIndex // پاس دادن gameIndex
//                        )
//                    } else {
//                        Text(
//                            "خطا: شناسه های لازم برای بازی یافت نشد.",
//                            color = Color.Red,
//                            fontFamily = iranSans
//                        )
//                        LaunchedEffect(Unit) {
//                            navController.popBackStack()
//                        }
//                    }
//                }

                    composable(
                        route = "memoryGame/{courseId}/{lessonId}/{contentId}/{gameId}?gameIndex={gameIndex}", // اضافه کردن gameIndex
                        arguments = listOf(
                            navArgument("courseId") { type = NavType.StringType },
                            navArgument("lessonId") { type = NavType.StringType },
                            navArgument("contentId") { type = NavType.StringType },
                            navArgument("gameId") { type = NavType.StringType },
                            navArgument("gameIndex") { type = NavType.IntType; defaultValue = 0 } // مقدار پیش‌فرض
                        )
                    ) { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId")
                        val lessonId = backStackEntry.arguments?.getString("lessonId")
                        val contentId = backStackEntry.arguments?.getString("contentId")
                        val gameId = backStackEntry.arguments?.getString("gameId")
                        val gameIndex = backStackEntry.arguments?.getInt("gameIndex") ?: 0 // دریافت gameIndex

                        Log.d("MemoryGameNav", "Received: courseId=$courseId, lessonId=$lessonId, contentId=$contentId, gameId=$gameId, gameIndex=$gameIndex")
                    }
                    //بازی های مربوط به گرامر

                    composable(
                        route = "GameHost/{courseId}/{lessonId}/{contentId}/{gameIndex}",
                        arguments = listOf(
                            navArgument("courseId") { type = NavType.StringType },
                            navArgument("lessonId") { type = NavType.StringType },
                            navArgument("contentId") { type = NavType.StringType },
                            navArgument("gameIndex") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                        val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                        val contentId = backStackEntry.arguments?.getString("contentId") ?: ""
                        val gameIndex = backStackEntry.arguments?.getInt("gameIndex") ?: 0

                        // ✅ گرفتن parentEntry برای share کردن ViewModel
                        val parentEntry = remember(backStackEntry) {
                            navController.getBackStackEntry("GameHost/{courseId}/{lessonId}/{contentId}/{gameIndex}")
                        }
                        val viewModel: GrammerGameViewModel = viewModel(parentEntry)

                        GameHost(
                            navController = navController,
                            courseId = courseId,
                            lessonId = lessonId,
                            contentId = contentId,
                            gameIndex = gameIndex,
                            viewModel = viewModel // ✅ به صورت share شده
                        )
                    }

                    composable(
                        route = "GameHostGrammar/{courseId}/{gameId}",
                        arguments = listOf(
                            navArgument("courseId") { type = NavType.StringType },
                            navArgument("gameId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                        val gameId = backStackEntry.arguments?.getString("gameId") ?: ""

//                    GameHostGrammar(
//                        courseId = courseId,
//                        gameId = gameId
//                    )
                    }


                    //بازی های مربوط به گرامر
                    //دکمه های برگشت

                    composable(
                        route = "course_detail/{courseId}?imageUrl={imageUrl}",
                        arguments = listOf(
                            navArgument("courseId") { type = NavType.StringType },
                            navArgument("imageUrl") { type = NavType.StringType; defaultValue = "" }
                        )
                    ) { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                        val headerImageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
                        CourseDetailPage(
                            navController = navController,
                            courseId = courseId,
                            headerImageUrl = headerImageUrl
                        )
                    }
//                composable(
//                    route = "courseDetail/{courseId}",
//                    arguments = listOf(navArgument("courseId") { type = NavType.StringType })
//                ) {
//                    val courseId = it.arguments?.getString("courseId") ?: ""
//                    CourseDetailPage(navController = navController, courseId = courseId)
//                }

                    composable("jozve_page/{courseId}/{lessonId}/{contentId}") { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                        val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                        val contentId = backStackEntry.arguments?.getString("contentId") ?: ""

                        Log.d("NavGraph", "🧾 Navigating to Jozve -- courseId=$courseId | lessonId=$lessonId | contentId=$contentId")

                        Jozve(
                            courseId = courseId,
                            lessonId = lessonId,
                            contentId = contentId,
                            navController = navController
                        )
                    }

                    composable("video_page/{courseId}/{lessonId}/{contentId}") { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                        val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                        val contentId = backStackEntry.arguments?.getString("contentId") ?: ""

                        Log.d("NavGraph", " Navigating to Video -- courseId=$courseId | lessonId=$lessonId | contentId=$contentId")

                        VideoScreen(
                            courseId = courseId,
                            lessonId = lessonId,
                            contentId = contentId,
                            navController = navController
                        )
                    }

                    composable("words_page/{courseId}/{lessonId}/{contentId}") { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                        val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                        val contentId = backStackEntry.arguments?.getString("contentId") ?: ""

                        WordsScreen(
                            courseId = courseId,
                            lessonId = lessonId,
                            contentId = contentId,
                            navController = navController
                        )
                    }

                }
            }
        }
    }

}


fun logFirebaseAuthError(tag: String, e: Exception, context: android.content.Context) {
    val msg = e.message ?: "no message"
    when (e) {
        is FirebaseAuthException -> {
            // کد خطای FirebaseAuth مثل ERROR_INVALID_API_KEY, ERROR_USER_NOT_FOUND, ...
            Log.e(tag, "FirebaseAuthException code=${e.errorCode}, msg=$msg", e)
            Toast.makeText(context, "FirebaseAuth error: ${e.errorCode}", Toast.LENGTH_LONG).show()
        }
        else -> {
            Log.e(tag, "Other Auth error: $msg", e)
            Toast.makeText(context, "Auth error: $msg", Toast.LENGTH_LONG).show()
        }
    }
}
