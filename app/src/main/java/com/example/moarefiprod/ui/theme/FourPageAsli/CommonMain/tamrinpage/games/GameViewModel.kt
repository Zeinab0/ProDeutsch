package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GameViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _totalGames = MutableStateFlow(0)
    private val _gameIds = MutableStateFlow<List<String>>(emptyList())
    val gameIds: StateFlow<List<String>> = _gameIds.asStateFlow()

    private val _sentenceData = MutableStateFlow<SentenceState?>(null)
    val sentenceData: StateFlow<SentenceState?> = _sentenceData.asStateFlow()

    private val _totalTimeInSeconds = MutableStateFlow(0)
    val totalTimeInSeconds: StateFlow<Int> = _totalTimeInSeconds.asStateFlow()

    private val _correctAnswers = MutableStateFlow(0)
    val correctAnswers: StateFlow<Int> = _correctAnswers.asStateFlow()

    private val _wrongAnswers = MutableStateFlow(0)
    val wrongAnswers: StateFlow<Int> = _wrongAnswers.asStateFlow()

    private var loadedCourseId: String? = null
    private var loadedLessonId: String? = null
    private var loadedContentId: String? = null

    fun incrementCorrect(count: Int) {
        _correctAnswers.value += count
    }

    fun incrementWrong(count: Int) {
        _wrongAnswers.value += count
    }

    fun recordMemoryGameResult(correct: Int, wrong: Int, timeInSeconds: Int) {
        _correctAnswers.value += correct
        _wrongAnswers.value += wrong
        _totalTimeInSeconds.value += timeInSeconds
    }

    fun recordAnswer(isCorrect: Boolean) {
        if (isCorrect) _correctAnswers.value++
        else _wrongAnswers.value++
    }

    suspend fun initializeGames(courseId: String, lessonId: String, contentId: String) {
        // همون کد قبلی برای لود بازی‌ها
    }

    fun getNextGameId(index: Int): String? {
        val ids = _gameIds.value
        return if (index in ids.indices) ids[index] else null
    }

    fun loadSentenceGame(courseId: String, lessonId: String, contentId: String, gameId: String) {
        viewModelScope.launch {
            try {
                val gameRef = db.collection("Courses")
                    .document(courseId)
                    .collection("Lessons")
                    .document(lessonId)
                    .collection("Contents")
                    .document(contentId)
                    .collection("games")
                    .document(gameId)

                val snapshot = gameRef.get().await()
                val question = snapshot.getString("title") ?: ""
                val correct = snapshot.get("answer") as? List<String> ?: emptyList()
                val words = snapshot.get("words") as? List<String> ?: emptyList()

                _sentenceData.value = SentenceState(
                    question = question,
                    correctSentence = correct,
                    wordPool = words.shuffled()
                )
            } catch (e: Exception) {
                Log.e("GameViewModel", "Error loading sentence game: ${e.message}")
                _sentenceData.value = null
            }
        }
    }

}
data class SentenceState(
    val question: String,
    val correctSentence: List<String>,
    val wordPool: List<String>
)
