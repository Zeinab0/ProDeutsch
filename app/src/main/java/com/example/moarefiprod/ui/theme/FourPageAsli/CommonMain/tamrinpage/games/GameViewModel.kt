package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class GameViewModel : ViewModel() {

    init {
        Log.d("GameViewModel", "New GameViewModel instance created: ${this.hashCode()}")
    }

    private val db = FirebaseFirestore.getInstance()


    private val _totalGames = MutableStateFlow(0)

    private val _gameIds = MutableStateFlow<List<String>>(emptyList())
    val gameIds: StateFlow<List<String>> = _gameIds.asStateFlow()


    private var loadedCourseId: String? = null
    private var loadedLessonId: String? = null
    private var loadedContentId: String? = null


    suspend fun initializeGames(courseId: String, lessonId: String, contentId: String) {
        if (loadedCourseId == courseId && loadedLessonId == lessonId && loadedContentId == contentId && _totalGames.value > 0) {
            Log.d("GameViewModel", "Using cached games: ${_totalGames.value}, IDs: ${_gameIds.value}")
            return
        }

        try {
            Log.d("GameViewModel", "Fetching games for courseId=$courseId, lessonId=$lessonId, contentId=$contentId")
            val documents = db.collection("Courses")
                .document(courseId)
                .collection("Lessons")
                .document(lessonId)
                .collection("Contents")
                .document(contentId)
                .collection("games")
                .get().await()
            val gameIds = documents.documents.map { it.id }
            Log.d("GameViewModel", "Raw documents: ${documents.documents.map { it.id to it.data }}")
            _gameIds.value = gameIds
            _totalGames.value = gameIds.size
            loadedCourseId = courseId
            loadedLessonId = lessonId
            loadedContentId = contentId
            Log.d("GameViewModel", "Total games loaded: ${gameIds.size}, IDs: $gameIds")
        } catch (e: Exception) {
            _totalGames.value = 0
            _gameIds.value = emptyList()
            Log.e("GameViewModel", "Error loading games: ${e.message}")
            // برای تست موقت، لیست gameIds رو دستی پر می‌کنیم
            _gameIds.value = listOf("memory_game_2", "sentence_builder_1", "text_pic_3")
            _totalGames.value = _gameIds.value.size
            Log.d("GameViewModel", "Using fallback gameIds: ${_gameIds.value}")
        }
    }


    fun getNextGameId(index: Int): String? {
        val ids = _gameIds.value
        Log.d("BaseGameViewModel", "👉 Requested index=$index, gameIdList=$ids")

        return if (index in ids.indices) ids[index] else null
    }

}