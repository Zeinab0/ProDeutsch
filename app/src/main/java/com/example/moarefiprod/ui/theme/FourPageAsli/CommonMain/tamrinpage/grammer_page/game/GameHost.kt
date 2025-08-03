package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.*
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.ResultDialog
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.GrammarViewModel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.BaseGameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GameHost(
    navController: NavController,
    topicId: String,
    gameIndex: Int,
    grammarViewModel: GrammarViewModel = viewModel(),
    viewModel: BaseGameViewModel = viewModel<GrammerGameViewModel>()
) {
    LaunchedEffect(topicId) {
        grammarViewModel.loadGamesForTopic(topicId)
    }

    val grammarGames by grammarViewModel.games.collectAsState()

    LaunchedEffect(topicId) {
        grammarViewModel.loadGamesForTopic(topicId)
    }

    LaunchedEffect(Unit) {
        grammarViewModel.games.collect {
            if (it.isNotEmpty()) {
                Log.d("GameHost", "📦 Total loaded games: ${it.size}")
                it.forEachIndexed { i, g ->
                    Log.d("GameHost", "Game[$i] = ${g.id}, type=${g.type}, order=${g.order}")
                }

                (viewModel as? GrammerGameViewModel)?.setGameList(it.map { g -> g.id })
                Log.d("GameHost", "✅ Game list set: ${it.map { g -> g.id }}")
            } else {
                Log.d("GameHost", "❌ No games loaded or DB connection failed.")
            }
        }
    }

    if (grammarGames.isEmpty()) {
        Text("هیچ بازی‌ای پیدا نشد یا اتصال به دیتابیس برقرار نیست.", fontFamily = iranSans)
        return
    }

    if (gameIndex >= grammarGames.size) {
        Log.d("GameHost", "Game index $gameIndex out of bounds (${grammarGames.size})")
        ResultDialog(
            navController = navController,
            courseId = topicId,
            lessonId = "",
            contentId = "",
            timeInSeconds = viewModel.totalTimeInSeconds.collectAsState().value,
            onDismiss = {
                navController.navigate("grammar_page")
            }
        )
        return
    }

    val currentGame = grammarGames[gameIndex]
    Log.d("GameHost", "Current game type: ${currentGame.type}, id: ${currentGame.id}")
    Log.d("GameHost", "🔥 Switching to gameIndex = $gameIndex")


    Log.d("GameHost", "Current game type: ${currentGame.type}, id: ${currentGame.id}")

    val grammarViewModelCasted = viewModel as? GrammerGameViewModel ?: return

    when (currentGame.type) {
        "MEMORY_GAME" -> {
            Log.d("GameHost", "Launching MEMORY_GAME: ${currentGame.id}")
            grammarViewModelCasted.loadMemoryGameFromGrammar(topicId, currentGame.id)
            MemoryGamePage(
                navController = navController,
                courseId = topicId,
                lessonId = "",
                contentId = "",
                gameId = currentGame.id,
                gameIndex = gameIndex,
                viewModel = viewModel
            )
        }

        "TEXT_PIC" -> {
            Log.d("GameHost", "Launching TEXT_PIC: ${currentGame.id}")
            grammarViewModelCasted.loadTextPicGameFromGrammar(topicId, currentGame.id)
            TextPicPage(
                navController = navController,
                courseId = topicId,
                lessonId = "",
                contentId = "",
                gameId = currentGame.id,
                gameIndex = gameIndex,
                viewModel = viewModel
            )
        }

        "SENTENCE" -> {
            Log.d("GameHost", "Launching SENTENCE: ${currentGame.id}")
            grammarViewModelCasted.loadSentenceGameFromGrammar(topicId, currentGame.id)
            SentenceBuilderPage(
                navController = navController,
                courseId = topicId,
                lessonId = "",
                contentId = "",
                gameId = currentGame.id,
                gameIndex = gameIndex,
                viewModel = viewModel
            )
        }

        "MULTIPLE_CHOICE" -> {
            Log.d("GameHost", "Launching MULTIPLE_CHOICE: ${currentGame.id}")
            grammarViewModelCasted.loadMultipleChoiceGame(topicId, currentGame.id, gameIndex)
            MultipleChoicePage(
                navController = navController,
                topicId = topicId,
                gameId = currentGame.id,
                gameIndex = gameIndex,
                totalGames = grammarGames.size, // ⬅️ اضافه شده
                viewModel = viewModel
            )

        }


        else -> {
            Log.e("GameHost", "Unknown game type: ${currentGame.type}")
            Text("نوع بازی ناشناخته: ${currentGame.type}", fontFamily = iranSans)
        }
    }
}
