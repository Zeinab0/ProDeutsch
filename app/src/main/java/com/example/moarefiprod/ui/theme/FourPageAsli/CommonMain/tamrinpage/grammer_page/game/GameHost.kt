package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.*
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ResultDialog
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.GrammarViewModel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.BaseGameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun GameHost(
    navController: NavController,
    courseId: String,
    lessonId: String = "",
    contentId: String = "",
    gameIndex: Int,
    viewModel: GrammerGameViewModel = viewModel()
) {
    val pathType = if (lessonId.isNotEmpty() && contentId.isNotEmpty()) {
        GrammerGameViewModel.GamePathType.COURSE
    } else {
        GrammerGameViewModel.GamePathType.GRAMMAR_TOPIC
    }



    // فقط از viewModel استفاده می‌کنیم
    LaunchedEffect(courseId, lessonId, contentId) {
        when (pathType) {
            GrammerGameViewModel.GamePathType.COURSE -> {
                viewModel.loadGamesForCourse(courseId, lessonId, contentId)
            }
            GrammerGameViewModel.GamePathType.GRAMMAR_TOPIC -> {
                viewModel.loadGamesForTopic(courseId)
            }
        }
    }

    val grammarGames by viewModel.games.collectAsState()

    LaunchedEffect(grammarGames) {
        if (grammarGames.isNotEmpty()) {
            Log.d("GameHost", "📦 Total loaded games: ${grammarGames.size}")
            viewModel.setGameList(grammarGames.map { it.id })
        } else {
            Log.d("GameHost", "❌ No games loaded or DB connection failed.")
        }
    }





    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // بعد از 2 ثانیه، بررسی کن هنوز بازی‌ای لود نشده یا نه
        delay(2000)
        isLoading.value = false
    }

    if (isLoading.value && grammarGames.isEmpty()) {
        // نمایش لودینگ در حال بارگذاری
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (!isLoading.value && grammarGames.isEmpty()) {
        // اگر لودینگ تموم شد ولی بازی‌ای لود نشده بود
        Text("هیچ بازی‌ای پیدا نشد یا اتصال به دیتابیس برقرار نیست.", fontFamily = iranSans)
        return
    }

//    if (grammarGames.isEmpty()) {
//        Text("هیچ بازی‌ای پیدا نشد یا اتصال به دیتابیس برقرار نیست.", fontFamily = iranSans)
//        return
//    }

    if (gameIndex >= grammarGames.size) {
        Log.d("GameHost", "Game index $gameIndex out of bounds (${grammarGames.size})")

        val returnRoute = if (pathType == GrammerGameViewModel.GamePathType.COURSE) {
            "lesson_detail/$courseId/$lessonId" // ✅ مسیر درست
        } else {
            "grammar_page"
        }

        ResultDialog(
            navController = navController,
            courseId = courseId,
            lessonId = lessonId,
            contentId = contentId,
            timeInSeconds = viewModel.totalTimeInSeconds.collectAsState().value,
            returnRoute = returnRoute,
            onDismiss = {
                navController.navigate(returnRoute)
            }
        )
        return
    }


    val currentGame = grammarGames[gameIndex]

    when (currentGame.type) {
        "MEMORY_GAME" -> {
            viewModel.loadMemoryGame(pathType, courseId, lessonId, contentId, currentGame.id)

            MemoryGamePage(navController,pathType, courseId, lessonId, contentId, currentGame.id, gameIndex, grammarGames.size, viewModel)
        }

        "TEXT_PIC" -> {
            viewModel.loadTextPicGame(pathType, courseId, lessonId, contentId, currentGame.id)

            TextPicPage(navController, courseId, lessonId, contentId, currentGame.id, gameIndex, grammarGames.size, viewModel)
        }

        "SENTENCE" -> {
            viewModel.loadSentenceGame(pathType, courseId, lessonId, contentId, currentGame.id)

            SentenceBuilderPage(navController, courseId, lessonId, contentId, currentGame.id, gameIndex, grammarGames.size, viewModel)
        }

        "MULTIPLE_CHOICE" -> {
            viewModel.loadMultipleChoiceGame(
                pathType = pathType,
                courseId = courseId,
                gameId = currentGame.id,
                index = gameIndex,
                lessonId = lessonId,
                contentId = contentId
            )

            MultipleChoicePage(
                navController = navController,
                courseId = courseId,
                lessonId = lessonId,
                contentId = contentId,
                gameId = currentGame.id,
                gameIndex = gameIndex,
                totalGames = grammarGames.size,
                viewModel = viewModel
            )
        }


        else -> {
            Log.e("GameHost", "Unknown game type: ${currentGame.type}")
            Text("نوع بازی ناشناخته: ${currentGame.type}", fontFamily = iranSans)
        }
    }
}
